package com.csis.drivedoctor.postAuth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csis.drivedoctor.R;
import com.csis.drivedoctor.model.CarsModel;

import java.util.List;

public class MyCarsAdapter extends RecyclerView.Adapter<MyCarsAdapter.CarViewHolder> {

    private List<CarsModel> carsModelList;
    Context contxt;
    CarItemClickLister clickLister;


    public MyCarsAdapter(Context cntxt, List<CarsModel> carsModelList, CarItemClickLister clickLister) {
        this.contxt = cntxt;
        this.carsModelList = carsModelList;
        this.clickLister = clickLister;
    }

    @NonNull
    @Override
    public MyCarsAdapter.CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_carlist,parent,false);
        MyCarsAdapter.CarViewHolder viewHolder = new CarViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCarsAdapter.CarViewHolder holder, int position) {

        CarsModel carData = carsModelList.get(position);
        holder.textViewCarMake.setText(carData.getCompany());
        holder.textViewCarModel.setText(carData.getCarName() + " " + carData.getModelyear());
        holder.textViewcarNo.setText(carData.getCarNo());
        holder.imageViewcarImage.setImageBitmap(carData.getCarImage(contxt));
    }

    @Override
    public int getItemCount() {
        return carsModelList.size();
    }

    public class CarViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCarMake;
        TextView textViewcarNo;
        TextView textViewCarModel;
        ImageView imageViewcarImage;
        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewCarMake = itemView.findViewById(R.id.id_carslist_comapny_textView);
            this.textViewcarNo = itemView.findViewById(R.id.id_carslist_numberPlate_textView);
            this.textViewCarModel = itemView.findViewById(R.id.id_carslist_name_textView);
            this.imageViewcarImage = itemView.findViewById(R.id.idimageViewcarImage);
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

    interface CarItemClickLister {
        void clickListner(View view, int position);
    }
}
