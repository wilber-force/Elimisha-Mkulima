package com.elimishamkulima.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elimishamkulima.R;
import com.elimishamkulima.dashboard.MessagesActivity;
import com.elimishamkulima.model.Events;
import com.elimishamkulima.model.Farmers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ConnectsAdapter extends RecyclerView.Adapter<ConnectsAdapter.ViewHolder> {
    ArrayList<Farmers> farmers;
    Context context;
    public ConnectsAdapter(Context context, ArrayList<Farmers> farmers) {
        this.farmers = farmers;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile_connect, parent, false);
        return new ViewHolder(view);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String currentUserUID = FirebaseAuth.getInstance().getUid();
        holder.name.setText(farmers.get(position).getFull_name());
        holder.farm_activities.setText(farmers.get(position).getFarm_activities());
        Picasso.get().load(farmers.get(position).getProfile()).fit().centerCrop().into(holder.image);
        holder.btn.setOnClickListener(v->{
            // Get the receiver's UID
            String receiverUID = farmers.get(position).getUid();

            // Create a chat room ID using the sender and receiver UID
            String chatRoomID = generateChatRoomID(currentUserUID, receiverUID);

            // Start the ChatRoomActivity
            Intent intent = new Intent(context, MessagesActivity.class);
            intent.putExtra("chatRoomID", chatRoomID);
            intent.putExtra("receiverUID", receiverUID);
            context.startActivity(intent);
        });
    }

    private String generateChatRoomID(String senderUID, String receiverUID) {
        // Generate a unique chat room ID using the sender and receiver UID
        // You can use any logic you prefer to create a unique ID
        return senderUID + "/" + receiverUID;
    }
    @Override
    public int getItemCount() {
        return farmers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, farm_activities;
        ImageView image;
        Button btn;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.connect_name);
            image = itemView.findViewById(R.id.connect_profile);
            farm_activities = itemView.findViewById(R.id.connect_activities);
            btn = itemView.findViewById(R.id.connect_message);
        }
    }

}
