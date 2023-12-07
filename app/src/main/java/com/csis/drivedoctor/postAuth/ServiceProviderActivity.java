package com.csis.drivedoctor.postAuth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csis.drivedoctor.Helpers.Loader;
import com.csis.drivedoctor.R;
import com.csis.drivedoctor.model.MyUserData;
import com.csis.drivedoctor.preauthDatabase.DBCompletionHandler;
import com.csis.drivedoctor.preauthDatabase.DatabaseHelper;

import java.util.ArrayList;

public class ServiceProviderActivity extends AppCompatActivity implements ServiceProviderListAdapter.ServiceProvidersItemClickLister{

    TextView noserviceproviderssavailable ;
    RecyclerView service_provider_recyclerView ;
    EditText searchCityTextField ;
    Button searchButton ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serviceprovider);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        service_provider_recyclerView = findViewById(R.id.id_serviceProvider_RecyclerView);
        noserviceproviderssavailable = findViewById(R.id.id_providerList_noServiceProviders_textView) ;
        searchCityTextField = findViewById(R.id.idEditSeachWithCityEditText) ;
        searchButton = findViewById(R.id.idFilterWithCityButton) ;

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchCityTextField.getText().toString().trim().equals("")) {
                        getNewData();
                } else {
                    getNewDataWithFilter();
                }

            }
        });

        if(DatabaseHelper.getInstance().providersList.size() > 0) {
            refreshView(DatabaseHelper.getInstance().providersList);
        } else {
            getNewData();
        }
    }

    void refreshView(ArrayList<MyUserData> myList) {
        service_provider_recyclerView.setVisibility(View.VISIBLE);
        noserviceproviderssavailable.setVisibility(View.INVISIBLE);
        ServiceProviderListAdapter providerslistAdapter = new ServiceProviderListAdapter(this, myList, this);
        service_provider_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        service_provider_recyclerView.setItemAnimator(new DefaultItemAnimator());
        service_provider_recyclerView.setAdapter(providerslistAdapter);
    }

    void getNewData() {
        Loader.show(this);
        DatabaseHelper.getInstance().getProviders(new DBCompletionHandler<MyUserData>() {
            @Override
            public void handle(ArrayList<MyUserData> myList, boolean isSuccess) {
                service_provider_recyclerView.setVisibility(View.INVISIBLE);
                noserviceproviderssavailable.setVisibility(View.VISIBLE);
                Loader.dismiss();
                if (isSuccess) {
                    refreshView(myList);
                }
            }
        });
    }
    void getNewDataWithFilter() {
        Loader.show(this);
        DatabaseHelper.getInstance().getProvidersWithFilter(searchCityTextField.getText().toString().trim(),new DBCompletionHandler<MyUserData>() {
            @Override
            public void handle(ArrayList<MyUserData> myList, boolean isSuccess) {
                service_provider_recyclerView.setVisibility(View.INVISIBLE);
                noserviceproviderssavailable.setVisibility(View.VISIBLE);
                Loader.dismiss();
                if (isSuccess) {
                    refreshView(myList);
                }
            }
        });
    }

    @Override
    public void onItemClick(View v, int position) {
        finish();
        Intent obj = new Intent(ServiceProviderActivity.this, BookAppointmentActivity.class);
        obj.putExtra("car", getIntent().getSerializableExtra("car"));
        obj.putExtra("serviceprovider", DatabaseHelper.getInstance().providersList.get(position));
        startActivity(obj);
    }
    }


