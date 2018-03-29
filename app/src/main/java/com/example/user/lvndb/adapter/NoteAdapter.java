package com.example.user.lvndb.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.lvndb.R;
import com.example.user.lvndb.activities.AddNote;
import com.example.user.lvndb.model.NoteData;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 3/23/2018.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    Context contex;
    ArrayList<NoteData> data;

    public NoteAdapter(Context contex, ArrayList<NoteData> data) {
        this.contex = contex;
        this.data = data;
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(contex);
        View view = inflater.inflate(R.layout.note_card_layout, parent, false);
        return new NoteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteAdapter.ViewHolder holder, int position) {
        final NoteData obj = data.get(position);

        holder.title.setText(obj.getTitle());
        holder.note.setText(obj.getNote());
        holder.date.setText(obj.getDate());
        holder.time.setText(obj.getTime());
        //holder.iv.setImageResource(R.drawable.image);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(contex, AddNote.class);
                intent.putExtra("id",obj.getId());
                intent.putExtra("title",obj.getTitle());
                intent.putExtra("note",obj.getNote());
                intent.putExtra("date",obj.getDate());
                intent.putExtra("time",obj.getTime());
                contex.startActivity(intent);
                Log.d(TAG, "Checking: "+obj.getId()+" "+obj.getTitle()+" "+obj.getDate()+" "+obj.getTime());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView note;
        TextView date;
        TextView time;
        //ImageView iv;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvNoteTitle);
            note = itemView.findViewById(R.id.tvNoteBody);
            date = itemView.findViewById(R.id.tvDate_Note);
            time = itemView.findViewById(R.id.tvTime_Note);
           // iv = itemView.findViewById(R.id.ivT1);
            card=itemView.findViewById(R.id.card_note);
        }
    }
}
