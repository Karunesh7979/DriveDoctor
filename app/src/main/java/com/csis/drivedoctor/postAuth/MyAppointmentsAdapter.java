package com.csis.drivedoctor.postAuth;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csis.drivedoctor.Helpers.NotificationHelper;
import com.csis.drivedoctor.R;
import com.csis.drivedoctor.model.CarsModel;
import com.csis.drivedoctor.model.MyAppointmentsModel;
import com.csis.drivedoctor.Helpers.ModelHelper;

import java.util.ArrayList;

public class MyAppointmentsAdapter extends RecyclerView.Adapter {

    Context cntxt;
    ArrayList<MyAppointmentsModel> appointmentsList;
    MyAppointmentItemClickLister clickLister;


    public MyAppointmentsAdapter(Context cntxt, ArrayList<MyAppointmentsModel> dataArray, MyAppointmentItemClickLister lis) {
        clickLister = lis;
        this.cntxt = cntxt;
        appointmentsList = dataArray;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(cntxt).inflate(R.layout.myappointmentslist_layout ,parent, false);

        MyAppointmentListCardView cardView = new MyAppointmentListCardView(view);
        return cardView;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyAppointmentsModel appoinmnt = appointmentsList.get(position);


        String name = ModelHelper.getInstance().getUser().isServiceProvider() ? appoinmnt.getUserName() : appoinmnt.getProviderName();
        ((MyAppointmentListCardView)holder).providerName. setText(name);
        String cardetais = appoinmnt.getCarCompany() + " " + appoinmnt.getCarmodel();
        ((MyAppointmentListCardView)holder).carmodel. setText(cardetais);
        ((MyAppointmentListCardView)holder).carNameplate. setText(appoinmnt.getCarNamePlate());
        ((MyAppointmentListCardView)holder).serviceDateTime. setText(appoinmnt.getDate());

        if(appoinmnt.isServiceCompleted()) {
            ((MyAppointmentListCardView)holder).serviceStatus.setTextColor(Color.GREEN);
            ((MyAppointmentListCardView)holder).serviceStatus. setText("Completed");
            ((MyAppointmentListCardView)holder).serviceAmount. setText(Double.toString(appoinmnt.getAmount()) + " $");

        } else {
            ((MyAppointmentListCardView)holder).serviceStatus.setTextColor(Color.RED);
            ((MyAppointmentListCardView)holder).serviceAmount. setText("");
            ((MyAppointmentListCardView)holder).serviceStatus. setText("Pending");
        }

        CarsModel car = new CarsModel();
        car.setCompany(appoinmnt.getCarCompany());
        ((MyAppointmentListCardView)holder).appointmentCarImage.setImageBitmap(car.getCarImage(cntxt));
    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }


    class MyAppointmentListCardView extends RecyclerView.ViewHolder {

        TextView providerName;
        TextView carmodel;
        TextView carNameplate;
        TextView serviceAmount;
        TextView serviceStatus;
        TextView serviceDateTime;
        ImageView appointmentCarImage;

        public MyAppointmentListCardView(@NonNull View itemView) {
            super(itemView);
            this.providerName = itemView.findViewById(R.id.id_myappointments_name_textView);
            this.carmodel = itemView.findViewById(R.id.id_myappointments_carDetails_textView);
            this.carNameplate = itemView.findViewById(R.id.id_myappointments_platenumber_textView);
            this.serviceAmount = itemView.findViewById(R.id.id_myappointments_amount_textView);
            this.serviceStatus = itemView.findViewById(R.id.id_myappointments_status_textView);
            this.serviceDateTime = itemView.findViewById(R.id.id_myappointments_datetime_textView);
            this.appointmentCarImage = itemView.findViewById(R.id.id_myappointmentImageView);

            this.appointmentCarImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NotificationHelper.getInstance().sendAppointmentNotification();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickLister != null) {
                        clickLister.clickListner(v,getAdapterPosition());
                    }
                }
            });
        }
    }
    interface MyAppointmentItemClickLister {
        void clickListner(View view, int position);
    }
}
