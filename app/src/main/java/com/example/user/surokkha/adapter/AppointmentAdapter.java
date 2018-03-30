package com.example.user.surokkha.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.surokkha.R;
import com.example.user.surokkha.activities.EditAppointment;
import com.example.user.surokkha.model.AppointmentData;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 3/21/2018.
 */

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    Context contex;
    ArrayList<AppointmentData> data;

    public AppointmentAdapter(Context contex, ArrayList<AppointmentData> data) {
        this.contex = contex;
        this.data = data;
    }


    @Override
    public AppointmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(contex);
        View view = inflater.inflate(R.layout.appointment_card_layout, parent, false);
        return new AppointmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppointmentAdapter.ViewHolder holder, int position) {
        final AppointmentData obj = data.get(position);

        holder.doctorName.setText(obj.getDoctorName());
        holder.location.setText(obj.getLocation());
        holder.date.setText(obj.getDate());
        holder.time.setText(obj.getTime());
        holder.iv.setImageResource(R.drawable.image);

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

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView doctorName;
        TextView location;
        TextView date;
        TextView time;
        ImageView iv;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.tvDoctorNAme_appointment);
            location = itemView.findViewById(R.id.tvLocation_appointment);
            date = itemView.findViewById(R.id.tvDate_appointment);
            time = itemView.findViewById(R.id.tvTime_appointment);
            iv = itemView.findViewById(R.id.ivT1);
            card=itemView.findViewById(R.id.card_appointment);
        }
    }
}
