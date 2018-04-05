package com.example.user.surokkha.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.surokkha.R;
import com.example.user.surokkha.model.DoctorData;
import com.example.user.surokkha.model.HospitalData;

import java.util.ArrayList;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {
    Context contex;
    ArrayList<HospitalData> data;

    public HospitalAdapter(Context contex, ArrayList<HospitalData> data) {
        this.contex = contex;
        this.data = data;
    }


    @Override
    public HospitalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(contex);
        View view = inflater.inflate(R.layout.hospital_card_layout, parent, false);
        return new HospitalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HospitalAdapter.ViewHolder holder, int position) {
        final HospitalData obj = data.get(position);

        holder.hospitalName.setText(obj.getHospitalName());
        //String location=obj.getLocation()+", "+obj.getDistrict();
        holder.location.setText(obj.getLocation());
        //holder.iv.setImageResource(R.drawable.image);

        /*
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Checking: "+obj.getCode()+" "+obj.getDoctorName()+" "+obj.getDate()+" "+obj.getTime());

                Intent intent=new Intent(contex, EditAppointment.class);
                intent.putExtra("code",obj.getCode());
                intent.putExtra("doctorName",obj.getDoctorName());
                intent.putExtra("location",obj.getLocation());
                intent.putExtra("date",obj.getDate());
                intent.putExtra("time",obj.getTime());
                intent.putExtra("note",obj.getNote());
                intent.putExtra("active",obj.getActive());
                contex.startActivity(intent);
                Log.d(TAG, obj.getCode()+" "+obj.getDoctorName());
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView hospitalName;
        TextView location;
        //ImageView iv;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            hospitalName = itemView.findViewById(R.id.tvHospitalName);
            location = itemView.findViewById(R.id.tvHospitalLocation);
            //iv = itemView.findViewById(R.id.ivT1);
            card=itemView.findViewById(R.id.card_hospital);
        }
    }
}

