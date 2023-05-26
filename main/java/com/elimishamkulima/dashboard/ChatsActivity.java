package com.elimishamkulima.dashboard;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.elimishamkulima.adapter.AllChatsAdapter;
import com.elimishamkulima.databinding.ActivityChatsBinding;
import com.elimishamkulima.model.Chat;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ChatsActivity extends AppCompatActivity {

    private ActivityChatsBinding binding;
    private AllChatsAdapter adapter;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserID = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        DatabaseReference mConvDatabase = databaseReference.child("chats").child(currentUserID);

        Query query = mConvDatabase.orderByChild("time_stamp");

        FirebaseRecyclerOptions<Chat> options = new FirebaseRecyclerOptions.Builder<Chat>()
                .setQuery(query, Chat.class)
                .build();

        adapter = new AllChatsAdapter(options);
        binding.chatsRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        finishAndRemoveTask();
    }
}
