package com.example.user.lvndb.adapter;

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

import com.example.user.lvndb.R;
import com.example.user.lvndb.activities.EditAlarm;
import com.example.user.lvndb.model.DataSchedule;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 3/18/2018.
 */

public class PillAdapter extends RecyclerView.Adapter<PillAdapter.ViewHolder> {
    Context contex;
    ArrayList<DataSchedule> data;

    public PillAdapter(Context contex, ArrayList<DataSchedule> data) {
        this.contex = contex;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //contex=parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(contex);
        View view = inflater.inflate(R.layout.pill_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DataSchedule obj = data.get(position);

        holder.pill.setText(obj.getPillName());
        holder.qty.setText(String.valueOf(obj.getQty()));
        holder.unit.setText(obj.getUnit());
        holder.day.setText(obj.getDay());
        holder.time.setText(obj.getTime());
        holder.iv.setImageResource(R.drawable.image);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Checking: "+obj.getCode()+" "+obj.getPillName());
                Log.d(TAG, "Checking: "+contex);
                Intent intent=new Intent(contex, EditAlarm.class);
                intent.putExtra("pillName",obj.getPillName());
                intent.putExtra("qty",obj.getQty());
                intent.putExtra("unit",obj.getUnit());
                intent.putExtra("day",obj.getDay());
                intent.putExtra("time",obj.getTime());
                intent.putExtra("date",obj.getDate());
                intent.putExtra("duration",obj.getDuration());
                intent.putExtra("active",obj.getActive());
                intent.putExtra("repeatNo",obj.getRepeatNo());
                contex.startActivity(intent);
                Log.d(TAG, obj.getCode()+" "+obj.getPillName());
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView pill;
        TextView qty;
        TextView unit;
        TextView day;
        TextView time;
        ImageView iv;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            pill = itemView.findViewById(R.id.tvPillName);
            qty = itemView.findViewById(R.id.tvQty);
            unit = itemView.findViewById(R.id.tvUnit);
            day = itemView.findViewById(R.id.tvDay);
            time = itemView.findViewById(R.id.tvTime);
            iv = itemView.findViewById(R.id.ivT1);
            card=itemView.findViewById(R.id.card);
        }
    }

}
