package com.elimishamkulima.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.elimishamkulima.R;
import com.elimishamkulima.adapter.ConnectsAdapter;
import com.elimishamkulima.adapter.EventsAdapter;
import com.elimishamkulima.adapter.FarmAdapter;
import com.elimishamkulima.model.Farm;
import com.elimishamkulima.model.Events;
import com.elimishamkulima.model.Farmers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    Context context;
    View view;
    ArrayList<Farm> farms;
    FarmAdapter farmAdapter;
    EventsAdapter eventsAdapter;
    ArrayList<Events> events;
    ArrayList<Farmers> farmers;
    RecyclerView farmersRecyclerView;
    ConnectsAdapter farmersAdapter;
    RecyclerView recyclerView1, recyclerView2;
    public HomeFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_home, container, false);

        farms = new ArrayList<>();
        farmAdapter = new FarmAdapter(context, farms);
        recyclerView1 = view.findViewById(R.id.farm_updates);

        farmers = new ArrayList<>();
        farmersAdapter = new ConnectsAdapter(context, farmers);
        farmersRecyclerView = view.findViewById(R.id.connect);

        events = new ArrayList<>();
        eventsAdapter = new EventsAdapter(context, events);
        recyclerView2 = view.findViewById(R.id.event_updates);
        recyclerView2.setAdapter(eventsAdapter);

        fetchFarmUpdates();
        fetchEventUpdates();
        fetchContacts();

        return  view;
    }

    private void fetchContacts() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Farmers");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        farmers.add((Farmers) dataSnapshot1.getValue(Farmers.class));
                        farmersAdapter = new ConnectsAdapter(context, farmers);
                        farmersRecyclerView.setAdapter(farmersAdapter);
                    }
                }else{
                    Toast.makeText(context, "No Farmers were found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed to load farmers", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchFarmUpdates() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Farm");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        farms.add((Farm) dataSnapshot1.getValue(Farm.class));
                        farmAdapter = new FarmAdapter(context, farms);
                        recyclerView1.setAdapter(farmAdapter);
                    }
                }else {
                    Toast.makeText(context, "There are no farm updates at the moment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed to load Farm updates", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fetchEventUpdates() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Events");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        events.add((Events) dataSnapshot1.getValue(Events.class));
                        eventsAdapter = new EventsAdapter(context, events);
                        recyclerView2.setAdapter(eventsAdapter);
                    }
                } else {
                    Toast.makeText(context, "No event update at the moment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        });
    }

}