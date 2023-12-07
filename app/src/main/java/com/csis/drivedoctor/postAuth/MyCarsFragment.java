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
import com.csis.drivedoctor.R;
import com.csis.drivedoctor.model.CarsModel;
import com.csis.drivedoctor.preauthDatabase.DBCompletionHandler;
import com.csis.drivedoctor.preauthDatabase.DatabaseHelper;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyCarsFragment extends Fragment implements MyCarsAdapter.CarItemClickLister {

    TextView nocarsavailable;
    RecyclerView cars_recyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listen();
        getNewData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_cars, container, false);

        cars_recyclerView = view.findViewById(R.id.id_carslist_RecyclerView);
        nocarsavailable = view.findViewById(R.id.id_carsList_noCarsMessage_textView);

        refreshView(DatabaseHelper.getInstance().carsList);
        return view;
    }

    void refreshView(ArrayList<CarsModel> myList) {
        cars_recyclerView.setVisibility(View.VISIBLE);
        nocarsavailable.setVisibility(View.INVISIBLE);
        MyCarsAdapter carlistAdapter = new MyCarsAdapter(getActivity(), myList, this);
        cars_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cars_recyclerView.setItemAnimator(new DefaultItemAnimator());
        cars_recyclerView.setAdapter(carlistAdapter);
    }

    void getNewData() {
        Loader.show(getActivity());
        DatabaseHelper.getInstance().getCars(new DBCompletionHandler<CarsModel>() {
            @Override
            public void handle(ArrayList<CarsModel> myList, boolean isSuccess) {
                cars_recyclerView.setVisibility(View.INVISIBLE);
                nocarsavailable.setVisibility(View.VISIBLE);
                Loader.dismiss();
                if (isSuccess) {
                    refreshView(myList);
                }
            }
        });
    }

    @Override
    public void clickListner(View view, int position) {
        Intent carIntent = new Intent(getActivity(),ServiceProviderActivity.class);
        carIntent.putExtra("car",DatabaseHelper.getInstance().carsList.get(position));
        startActivity(carIntent);
        //go to book appointment
        //DatabaseHelper.getInstance().carsList.get(position);
    }

    private void listen() {
        DatabaseHelper.getInstance().db.collection("mycars")
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