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
import com.elimishamkulima.adapter.TrainingAdapter;
import com.elimishamkulima.model.Trainings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class TrainingFragment extends Fragment {
    Context context;
    View view;
    ArrayList<Trainings> trainings;
    TrainingAdapter trainingAdapter;

    RecyclerView recyclerView;
    public TrainingFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_training, container, false);
        recyclerView = view.findViewById(R.id.training);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        trainings = new ArrayList<>();
        trainingAdapter = new TrainingAdapter(trainings, context);
        recyclerView.setAdapter(trainingAdapter);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Trainings");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        trainings.add(snapshot1.getValue(Trainings.class));
                        trainingAdapter = new TrainingAdapter(trainings, context);
                        recyclerView.setAdapter(trainingAdapter);
                    }
                }else{
                    Toast.makeText(context, "There are no trainings at the moment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed to connect", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}