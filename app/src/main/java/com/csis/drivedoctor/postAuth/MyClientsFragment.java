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
import com.csis.drivedoctor.model.MyUserData;
import com.csis.drivedoctor.preauthDatabase.DBCompletionHandler;
import com.csis.drivedoctor.preauthDatabase.DatabaseHelper;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class MyClientsFragment extends Fragment implements ServiceProviderListAdapter.ServiceProvidersItemClickLister {

    TextView noclientsssavailable ;
    RecyclerView clients_recyclerView ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listen();
        getNewData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myclients, container, false);

        clients_recyclerView = view.findViewById(R.id.id_Clients_recyclerView);
        noclientsssavailable = view.findViewById(R.id.id_carsList_noClients_textView) ;


        if(DatabaseHelper.getInstance().clientsList.size() > 0) {
            refreshView(DatabaseHelper.getInstance().clientsList);
        } else {
            getNewData();
        }

        return view;
    }


    void refreshView(ArrayList<MyUserData> myList) {
        clients_recyclerView.setVisibility(View.VISIBLE);
        noclientsssavailable.setVisibility(View.INVISIBLE);
        ServiceProviderListAdapter clientsListAdapter = new ServiceProviderListAdapter(getActivity(), myList, this);
        clients_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        clients_recyclerView.setItemAnimator(new DefaultItemAnimator());
        clients_recyclerView.setAdapter(clientsListAdapter);
    }

    void getNewData() {
        Loader.show(getActivity());
        DatabaseHelper.getInstance().getClients(new DBCompletionHandler<MyUserData>() {
            @Override
            public void handle(ArrayList<MyUserData> myList, boolean isSuccess) {
                clients_recyclerView.setVisibility(View.INVISIBLE);
                noclientsssavailable.setVisibility(View.VISIBLE);
                Loader.dismiss();
                if (isSuccess) {
                    refreshView(myList);
                }
            }
        });
    }

    @Override
    public void onItemClick(View v, int position) {
        Intent obj = new Intent(getActivity(), EditProfileActivity.class);
        obj.putExtra("user", DatabaseHelper.getInstance().clientsList.get(position));
        startActivity(obj);
    }

    private void listen() {
        DatabaseHelper.getInstance().db.collection("user")
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

