package com.csis.drivedoctor.postAuth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csis.drivedoctor.Helpers.Loader;
import com.csis.drivedoctor.Helpers.NotificationHelper;
import com.csis.drivedoctor.R;
import com.csis.drivedoctor.model.MyAppointmentsModel;
import com.csis.drivedoctor.preauthDatabase.DBCompletionHandler;
import com.csis.drivedoctor.preauthDatabase.DatabaseHelper;
import com.csis.drivedoctor.Helpers.ModelHelper;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyAppointmentsFragment extends Fragment implements MyAppointmentsAdapter.MyAppointmentItemClickLister {

    TextView noappointmentsavailable;
    RecyclerView appointment_recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationHelper.getInstance().registerForNotification(getActivity());
        listen();
        getNewData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_appointments, container, false);
        noappointmentsavailable = view.findViewById(R.id.id_noAppointments_textView);
        appointment_recyclerView = view.findViewById(R.id.id_myappointments_recyclerView);

        refreshView(DatabaseHelper.getInstance().appointmentsList);
        return view;
    }

    void refreshView(ArrayList<MyAppointmentsModel> myList) {
        appointment_recyclerView.setVisibility(View.VISIBLE);
        noappointmentsavailable.setVisibility(View.INVISIBLE);
        MyAppointmentsAdapter appointmentListAdapter = new MyAppointmentsAdapter(getContext(), myList, this);
        appointment_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        appointment_recyclerView.setItemAnimator(new DefaultItemAnimator());
        appointment_recyclerView.setAdapter(appointmentListAdapter);
    }

    void getNewData() {
        Loader.show(getActivity());
        DatabaseHelper.getInstance().getAppointments(new DBCompletionHandler<MyAppointmentsModel>() {
            @Override
            public void handle(ArrayList<MyAppointmentsModel> myList, boolean isSuccess) {
                appointment_recyclerView.setVisibility(View.INVISIBLE);
                noappointmentsavailable.setVisibility(View.VISIBLE);
                Loader.dismiss();
                if (isSuccess) {
                    refreshView(myList);
                }
            }
        });
    }

    @Override
    public void clickListner(View view, int position) {

        if(ModelHelper.getInstance().getUser().isServiceProvider()) {
            Intent appointmentIntent = new Intent(getActivity(),BookAppointmentActivity.class);
            appointmentIntent.putExtra("appointment",DatabaseHelper.getInstance().appointmentsList.get(position));
            startActivity(appointmentIntent);
        }
    }

    private void listen() {
        DatabaseHelper.getInstance().db.collection("appointments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("My Cars", "listen:error", e);
                            return;
                        }
                        //Auto Listner
                        getNewData();
                    }
                });
    }

}