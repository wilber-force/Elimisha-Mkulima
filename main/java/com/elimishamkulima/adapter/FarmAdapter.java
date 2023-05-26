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
import com.elimishamkulima.model.Farm;

import java.util.ArrayList;

public class FarmAdapter extends RecyclerView.Adapter<FarmAdapter.ViewHolder> {
    ArrayList<Farm> dataList;
    Context context;
    public FarmAdapter(Context context, ArrayList<Farm> dataList) {
        this.dataList = dataList;
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
        holder.title.setText(dataList.get(position).getTitle());
        holder.date.setText("Published on: "+dataList.get(position).getPublish_date());
        holder.itemView.setOnClickListener(v->{
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            View layoutDialog = LayoutInflater.from(context).inflate(R.layout.dialog_view_update, null);
            alertDialog.setView(layoutDialog);

            TextView txt = layoutDialog.findViewById(R.id.title_update);
            TextView txt1 = layoutDialog.findViewById(R.id.posted_on_update);
            TextView txt2 = layoutDialog.findViewById(R.id.description_update);
            TextView txt3 = layoutDialog.findViewById(R.id.start_update);
            TextView txt4 = layoutDialog.findViewById(R.id.end_update);

            txt.setText(dataList.get(position).getTitle());
            txt1.setText(dataList.get(position).getPublish_date());
            txt2.setText(dataList.get(position).getDescription());
            txt3.setVisibility(View.GONE);
            txt4.setVisibility(View.GONE);

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
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, date;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.topic);
            date = itemView.findViewById(R.id.post_date);
            TextView txt1 = itemView.findViewById(R.id.begin_date);
            txt1.setVisibility(View.GONE);
            TextView txt2 = itemView.findViewById(R.id.final_date);
            txt2.setVisibility(View.GONE);
        }
    }

}

