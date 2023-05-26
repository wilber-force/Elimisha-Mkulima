package com.elimishamkulima.adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.elimishamkulima.R;
import com.elimishamkulima.dashboard.MessagesActivity;
import com.elimishamkulima.model.Chat;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AllChatsAdapter extends FirebaseRecyclerAdapter<Chat, AllChatsAdapter.MessagesViewHolder> {

    private static final String TAG = "AllChatsAdapter";

    public AllChatsAdapter(@NonNull FirebaseRecyclerOptions<Chat> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessagesViewHolder holder, int position, @NonNull Chat model) {

        DatabaseReference mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DatabaseReference mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Farmers");

        final String list_user_id = getRef(position).getKey();
        Query lastMessageQuery = mMessageDatabase.child(list_user_id).limitToLast(1);

        Log.d(TAG, "onBindViewHolder: user_key " + list_user_id);

        //---IT WORKS WHENEVER CHILD OF mMessageDatabase IS CHANGED---
        lastMessageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String data = dataSnapshot.child("chatMessage").getValue().toString();
                String time = dataSnapshot.child("chatTime").getValue().toString();
                holder.setMessage(data);
                holder.setTime(time);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String userName = dataSnapshot.child("full_name").getValue().toString();
                holder.setName(userName);


                holder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String receiverUID = getRef(holder.getAdapterPosition()).getKey(); // Get the receiver's UID
                        String currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get the current user's UID

                        String chatRoomID = generateChatRoomID(currentUserUID, receiverUID); // Create chat room ID

                        // Start the MessagesActivity and pass the chat room ID and receiver UID as extras
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MessagesActivity.class);
                        intent.putExtra("chatRoomID", chatRoomID);
                        intent.putExtra("receiverUID", receiverUID);
                        context.startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private String generateChatRoomID(String senderUID, String receiverUID) {
        // Generate a unique chat room ID using the sender and receiver UID
        // You can use any logic you prefer to create a unique ID
        return senderUID + "_" + receiverUID;
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row, parent, false);
        return new MessagesViewHolder(view);
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView name;
        TextView time;
        CardView card;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.textViewLastMessage);
            name = itemView.findViewById(R.id.textViewSendeeName);
            time = itemView.findViewById(R.id.textViewLastMessageTime);
            card = itemView.findViewById(R.id.card);
        }

        public void setTime(String tme) {
            time.setText(tme);
        }

        public void setMessage(String data) {
            message.setText(data);
        }

        public void setName(String userName) {
            name.setText(userName);
        }
    }
}