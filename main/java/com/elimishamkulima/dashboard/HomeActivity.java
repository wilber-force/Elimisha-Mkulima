package com.elimishamkulima.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.elimishamkulima.R;
import com.elimishamkulima.fragment.HomeFragment;
import com.elimishamkulima.fragment.ProfileFragment;
import com.elimishamkulima.fragment.TrainingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {
    FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Farmers");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(user.getUid()).child("admin").getValue(Boolean.class)){
                    findViewById(R.id.add_items).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        replaceFragment(new HomeFragment(HomeActivity.this));

        fab = findViewById(R.id.chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.add_items).setOnClickListener(v->{startActivity(new Intent(HomeActivity.this, UpdatesActivity.class));});

        fab.setOnClickListener(v->{startActivity(new Intent(HomeActivity.this, ChatsActivity.class));});
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(nav);
        bottomNavigationView.setSelectedItemId(R.id.home_menu);
    }




    private BottomNavigationView.OnNavigationItemSelectedListener nav = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.home_menu) {
                replaceFragment(new HomeFragment(HomeActivity.this));
                return true;
            } else if (itemId == R.id.profile_menu) {
                replaceFragment(new ProfileFragment(HomeActivity.this));
                return true;
            } else if (itemId == R.id.training_menu) {
                replaceFragment(new TrainingFragment(HomeActivity.this));
                return true;
            } else {
                return false;
            }
        }
    };

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = HomeActivity.this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        finishAndRemoveTask();
    }
}