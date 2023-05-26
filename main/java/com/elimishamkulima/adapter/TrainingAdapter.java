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
import com.elimishamkulima.model.Trainings;

import java.util.ArrayList;

public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.ViewHolder>{
    ArrayList<Trainings> trainingsArrayList;
    Context context;

    public TrainingAdapter(ArrayList<Trainings> trainingsArrayList, Context context) {
        this.trainingsArrayList = trainingsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public TrainingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.training_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingAdapter.ViewHolder holder, int position) {
        holder.txt_title.setText(trainingsArrayList.get(position).getTitle());
        holder.txt_pubdate.setText(trainingsArrayList.get(position).getPublished_on());
        holder.txt_start.setText(trainingsArrayList.get(position).getStart_date());
        holder.txt_end.setText(trainingsArrayList.get(position).getEnd_date());
        holder.itemView.setOnClickListener(v->{
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            View layoutDialog = LayoutInflater.from(context).inflate(R.layout.dialog_view_update, null);
            alertDialog.setView(layoutDialog);

            TextView txt = layoutDialog.findViewById(R.id.title_update);
            TextView txt1 = layoutDialog.findViewById(R.id.posted_on_update);
            TextView txt2 = layoutDialog.findViewById(R.id.description_update);
            TextView txt3 = layoutDialog.findViewById(R.id.start_update);
            TextView txt4 = layoutDialog.findViewById(R.id.end_update);

            txt.setText(trainingsArrayList.get(position).getTitle());
            txt1.setText(trainingsArrayList.get(position).getPublished_on());
            txt2.setText(trainingsArrayList.get(position).getDescription());
            txt3.setText(trainingsArrayList.get(position).getStart_date());
            txt4.setText(trainingsArrayList.get(position).getEnd_date());

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
        return trainingsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_title,txt_pubdate, txt_start, txt_end;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.topic);
            txt_pubdate = itemView.findViewById(R.id.post_date);
            txt_start = itemView.findViewById(R.id.begin_date);
            txt_end = itemView.findViewById(R.id.final_date);
        }
    }
}
