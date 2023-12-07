package com.csis.drivedoctor.postAuth;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.csis.drivedoctor.Helpers.ModelHelper;
import com.csis.drivedoctor.Helpers.UsersDBHelper;
import com.csis.drivedoctor.R;
import com.csis.drivedoctor.model.MyUserData;
import com.csis.drivedoctor.preauthDatabase.DatabaseHelper;
import com.csis.drivedoctor.preauthDatabase.TaskCompletion;

import java.util.List;

public class ServiceProviderListAdapter extends RecyclerView.Adapter {

    private List<MyUserData> providersModelList;
    Context contxt;
    private ServiceProvidersItemClickLister providerItemClickLitsner;
    UsersDBHelper userDB;


    public ServiceProviderListAdapter(Context cntxt, List<MyUserData> providersModelList, ServiceProvidersItemClickLister clickLister) {
        this.contxt = cntxt;
        this.providersModelList = providersModelList;
        this.providerItemClickLitsner = clickLister;
        userDB = new UsersDBHelper(cntxt);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.contxt).inflate(R.layout.serviceproviderlist_layout, parent, false);
        ProvidersViewHolder viewHolder = new ProvidersViewHolder(view);

        if(!ModelHelper.getInstance().getUser().isServiceProvider()) {
            viewHolder.deleteUser.setVisibility(View.GONE);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyUserData usr = providersModelList.get(position);
        ((ProvidersViewHolder) holder).providerNameTextView.setText(usr.getFirstName() + " "+usr.getLastName());
        ((ProvidersViewHolder) holder).providerEmailTexView.setText(usr.getEmail());
        ((ProvidersViewHolder) holder).providerPhoneTextView.setText(usr.getPhone());
        ((ProvidersViewHolder) holder).providerCityTextView.setText(usr.getCity());
        ((ProvidersViewHolder) holder).providerAddressTextView.setText(usr.getAddress());


        if (!usr.getProfileURL().equals("")) {
            Glide.with(contxt).load(usr.getProfileURL()).into(((ProvidersViewHolder) holder).providerImageView );
        } else {
            ((ProvidersViewHolder) holder).providerImageView.setImageResource(R.drawable.appicon);
        }
    }

    @Override
    public int getItemCount() {
        return providersModelList.size();
    }

    public class ProvidersViewHolder extends RecyclerView.ViewHolder {
        ImageView providerImageView;
        TextView providerNameTextView;
        TextView providerEmailTexView;
        TextView providerAddressTextView;
        TextView providerCityTextView;
        TextView providerPhoneTextView;
        ImageView deleteUser;

        public ProvidersViewHolder(@NonNull View itemView) {
            super(itemView);

            deleteUser = itemView.findViewById(R.id.id_Myclients_userDeleteIcon);
            providerImageView = itemView.findViewById(R.id.idserviceproviderimage);
            providerEmailTexView = itemView.findViewById(R.id.idServiceProviderEmail);
            providerNameTextView = itemView.findViewById(R.id.idServiceProviderName);
            providerAddressTextView = itemView.findViewById(R.id.idServiceProviderAddress);
            providerPhoneTextView = itemView.findViewById(R.id.idServiceProviderPhoneNumber);
            providerCityTextView = itemView.findViewById(R.id.idServiceProviderCity);

            deleteUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyUserData usr = providersModelList.get(getAdapterPosition());
                    DatabaseHelper.getInstance().deleteUser(usr.getDocumentId(), new TaskCompletion() {
                        @Override
                        public void taskCompletion(boolean isSuccess) {
                            userDB.deleteRec(usr.getUserId());
                        }
                    });

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (providerItemClickLitsner != null) {
                        providerItemClickLitsner.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface ServiceProvidersItemClickLister {
        void onItemClick(View v, int position);
    }
}


