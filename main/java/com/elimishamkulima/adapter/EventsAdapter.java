package com.elimishamkulima.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elimishamkulima.R;
import com.elimishamkulima.model.Events;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    ArrayList<Events> events;
    Context context;
    public EventsAdapter(Context context, ArrayList<Events> events) {
        this.events = events;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(events.get(position).getTitle());
        holder.start_date.setText("Starts on: "+events.get(position).getStart_date());
        holder.publish_date.setText("Published on: "+events.get(position).getPublished_on());
        holder.end_date.setText("Ends on: "+events.get(position).getEnd_date());
        holder.itemView.setOnClickListener(v->{
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            View layoutDialog = LayoutInflater.from(context).inflate(R.layout.dialog_view_update, null);
            alertDialog.setView(layoutDialog);

            TextView txt = layoutDialog.findViewById(R.id.title_update);
            TextView txt1 = layoutDialog.findViewById(R.id.posted_on_update);
            TextView txt2 = layoutDialog.findViewById(R.id.description_update);
            TextView txt3 = layoutDialog.findViewById(R.id.start_update);
            TextView txt4 = layoutDialog.findViewById(R.id.end_update);

            txt.setText(events.get(position).getTitle());
            txt1.setText(events.get(position).getPublished_on());
            txt2.setText(events.get(position).getDescription());
            txt3.setText(events.get(position).getStart_date());
            txt4.setText(events.get(position).getEnd_date());

            Button retry = layoutDialog.findViewById(R.id.dismiss);

            AlertDialog dialog = alertDialog.create();
            dialog.show();
            dialog.setCancelable(true);

            dialog.getWindow().setGravity(Gravity.CENTER);
            retry.setOnClickListener(view->{dialog.dismiss();});

        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, publish_date, start_date, end_date;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.topic);
            publish_date = itemView.findViewById(R.id.post_date);
            start_date = itemView.findViewById(R.id.begin_date);
            end_date = itemView.findViewById(R.id.final_date);
        }
    }

}

