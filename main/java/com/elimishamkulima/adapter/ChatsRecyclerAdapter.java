package com.elimishamkulima.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elimishamkulima.R;
import com.elimishamkulima.model.ChatMessage;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class ChatsRecyclerAdapter extends FirebaseRecyclerAdapter<ChatMessage, ChatsRecyclerAdapter.MessagesViewHolder> {

    private static final String TAG = "ChatsRecyler";

    String currentUserName;

    public ChatsRecyclerAdapter(@NonNull FirebaseRecyclerOptions<ChatMessage> options, String currentUserName) {
        super(options);
        this.currentUserName = currentUserName;
    }

    @Override
    protected void onBindViewHolder(@NonNull MessagesViewHolder holder, int position, @NonNull ChatMessage model) {
        ChatMessage message = model;
        Log.d(TAG, "onBindViewHolder: chats"+message.getChatMessage());
        holder.bind(message);
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row, parent, false);
        return new MessagesViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView name;
        TextView time;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.messageTextView);
            name = itemView.findViewById(R.id.messengerTextView);
            time = itemView.findViewById(R.id.textViewTimeDate);
        }

        public void bind(ChatMessage messge) {
            message.setText(messge.getChatMessage());
            setTextColor(messge.getChatSender(), message);
            name.setText(messge.getChatSender());
            time.setText(messge.getChatTime());
        }

        private void setTextColor(String userName, TextView textView) {

            Log.d(TAG, "setTextColor: CurrentUserName"+currentUserName+ " Username"+userName);
            if (currentUserName.equals(userName)) {
                textView.setBackgroundResource(R.drawable.message_blue);
                textView.setTextColor(Color.WHITE);
            } else {
                textView.setBackgroundResource(R.drawable.message_gray);
                textView.setTextColor(Color.BLACK);
            }
        }
    }
}