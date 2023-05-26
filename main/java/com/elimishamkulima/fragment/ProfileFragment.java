package com.elimishamkulima.fragment;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elimishamkulima.R;
import com.elimishamkulima.authentication.LoginActivity;
import com.elimishamkulima.dashboard.HomeActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    Context context;
    View view;
    DatabaseReference databaseReference;
    TextView name, location, phone, id, activities, email;
    ImageView profile;
    String userid;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    public ProfileFragment(Context context) {
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        userid = firebaseUser.getUid();
        name = view.findViewById(R.id.full_name);
        location = view.findViewById(R.id.location);
        id = view.findViewById(R.id.ID_number);
        phone = view.findViewById(R.id.phone_number);
        activities = view.findViewById(R.id.activities);
        email = view.findViewById(R.id.email_address);
        profile = view.findViewById(R.id.profile_image);


        getProfile();
        view.findViewById(R.id.logout).setOnClickListener(v->{
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            assert firebaseUser != null;
            firebaseAuth.signOut();
            startActivity(new Intent(context, LoginActivity.class));
            getActivity().finish();

        });


        view.findViewById(R.id.pass_change).setOnClickListener(v-> {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            View layoutDialog = LayoutInflater.from(context).inflate(R.layout.change_password, null);
            alertDialog.setView(layoutDialog);

            Button ok = layoutDialog.findViewById(R.id.change_pass_ok);

            AlertDialog dialog = alertDialog.create();
            dialog.show();
            dialog.setCancelable(false);

            dialog.getWindow().setGravity(Gravity.CENTER);

            ok.setOnClickListener(var -> {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                EditText pass1 = layoutDialog.findViewById(R.id.new_pass1);
                EditText pass2 = layoutDialog.findViewById(R.id.new_pass2);
                EditText current = layoutDialog.findViewById(R.id.old_pass);
                if(pass1.getText().toString().equals(pass2.getText().toString()) && pass2.getText().toString().length()>6) {
                    String newPassword = pass1.getText().toString();

                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), current.getText().toString());

                    user.reauthenticate(credential)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    user.updatePassword(newPassword)
                                            .addOnCompleteListener(updateTask -> {
                                                if (updateTask.isSuccessful()) {
                                                    // Password updated successfully
                                                    Log.d(TAG, "Password updated successfully");
                                                    Snackbar.make(getView(), "Password changed successfully", Snackbar.LENGTH_SHORT).show();
                                                } else {
                                                    // Failed to update password
                                                    Log.e(TAG, "Failed to update password", updateTask.getException());
                                                }
                                                dialog.dismiss();
                                            });
                                } else {
                                    // Failed to reauthenticate
                                    Toast.makeText(context, "Failed to verify your password "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "Failed to reauthenticate", task.getException());
                                    dialog.dismiss();
                                }
                            });

                }else{
                    Toast.makeText(context, "Please set valid password", Toast.LENGTH_SHORT).show();
                    pass1.setError("Passwords did not match");
                }
                });
        });

        return view;
    }

    private void getProfile() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Farmers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(userid)) {
                    email.setText(snapshot.child(userid).child("email").getValue(String.class));
                    phone.setText(snapshot.child(userid).child("phone_number").getValue(String.class));
                    name.setText(snapshot.child(userid).child("full_name").getValue(String.class));
                    id.setText(snapshot.child(userid).child("id_number").getValue(String.class));
                    activities.setText(snapshot.child(userid).child("farm_activities").getValue(String.class));
                    location.setText(snapshot.child(userid).child("location").getValue(String.class));

                    Picasso.get().load(snapshot.child(userid).child("profile").getValue(String.class)).fit().centerCrop().into(profile);

                } else {
                    Toast.makeText(context, "Please confirm that you are logged in to continue.", Toast.LENGTH_SHORT).show();
                }
            }

            public void onCancelled(DatabaseError error) {
                Toast.makeText(context, "Failed to load data to your profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}