package com.elimishamkulima.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.elimishamkulima.R;
import com.elimishamkulima.adapter.ChatsRecyclerAdapter;
import com.elimishamkulima.databinding.ActivityMessagesBinding;
import com.elimishamkulima.model.ChatMessage;
import com.elimishamkulima.observers.ButtonObserver;
import com.elimishamkulima.observers.ScrollToBottomObserver;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MessagesActivity extends AppCompatActivity {
    private static final String TAG = "MessagesActivity";

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private ChatsRecyclerAdapter adapter;
    private LinearLayoutManager manager;

    private String currentUserID;
    private String otherUserID;

    private ActivityMessagesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
//            chatRoomID = extras.getString("chatRoomID");
            otherUserID = extras.getString("receiverUID");
        }

        databaseReference.child("chats").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(otherUserID)) {
                    Map<String, Object> chatAddMap = new HashMap<>();
                    chatAddMap.put("seen", false);
                    chatAddMap.put("time_stamp", ServerValue.TIMESTAMP);

                    Map<String, Object> chatUserMap = new HashMap<>();
                    chatUserMap.put("chats/" + otherUserID + "/" + currentUserID, chatAddMap);
                    chatUserMap.put("chats/" + currentUserID + "/" + otherUserID, chatAddMap);

                    databaseReference.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null) {
                                //Toast.makeText(MessagesActivity.this, "Successfully added chats feature", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MessagesActivity.this, "Cannot add chats feature", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MessagesActivity.this, "Slow internet connections", Toast.LENGTH_SHORT).show();
            }
        });

        displayMessages();

        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        binding.messageEditText.addTextChangedListener(new ButtonObserver(binding.sendButton));
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
       finish();
    }

    private void sendMessage() {

        if (!TextUtils.isEmpty(binding.messageEditText.getText().toString())) {
            String currentUserReference = "messages/" + currentUserID + "/" + otherUserID;
            String otherUserReference = "messages/" + otherUserID + "/" + currentUserID;

            DatabaseReference userMessagePush = databaseReference.child("messages")
                    .child(currentUserID).child(otherUserID).push();

            String userMessagePushID = userMessagePush.getKey();

            Log.d(TAG, "sendMessage: created key" + userMessagePushID);

            ChatMessage message = new ChatMessage(
                    binding.messageEditText.getText().toString(),
                    firebaseUser.getDisplayName(),
                    new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH).format(new Date())
            );

            Log.d(TAG, "sendMessage: chatMessage" + message);

            Map<String, Object> messageUserMap = new HashMap<>();
            messageUserMap.put(currentUserReference + "/" + userMessagePushID, message);
            messageUserMap.put(otherUserReference + "/" + userMessagePushID, message);

            databaseReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error != null) {
                        Log.d(TAG, "onComplete: Chat message cannot be added to the database" + error.getMessage());
                    } else {
                        Log.d(TAG, "onComplete: Completed" + ref.getKey());
                        Toast.makeText(MessagesActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        binding.messageEditText.setText("");
                    }
                }
            });
        }
    }

    private void displayMessages() {
        DatabaseReference messagesReference = databaseReference.child("messages")
                .child(currentUserID).child(otherUserID);

        Query query = messagesReference.limitToLast(50);

        FirebaseRecyclerOptions<ChatMessage> options = new FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .build();

        adapter = new ChatsRecyclerAdapter(options, firebaseUser.getDisplayName());

        manager = new LinearLayoutManager(getApplicationContext());
        manager.setStackFromEnd(true);
        binding.messageRecyclerView.setLayoutManager(manager);
        binding.messageRecyclerView.setAdapter(adapter);

        adapter.registerAdapterDataObserver(
                new ScrollToBottomObserver(binding.messageRecyclerView, adapter, manager)
        );
    }

}
