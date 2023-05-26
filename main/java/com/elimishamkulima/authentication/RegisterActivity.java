package com.elimishamkulima.authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.elimishamkulima.R;
import com.elimishamkulima.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    Uri image = null;
    int profile = 1;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ActivityRegisterBinding binding;
    ImageView profilephoto;
    String displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Farmers");
        profilephoto = findViewById(R.id.profile_picture_upload);

        binding.buttonCreateAccount.setOnClickListener(v -> {
            String mail = binding.signUpEmailEditText.getText().toString().trim();
            String pass = binding.signUpPasswordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(binding.signUpEmailEditText.getText().toString())) {
                binding.signUpEmailEditText.setError("Field can't be empty!");
            }
            else if (TextUtils.isEmpty(binding.signUpPasswordEditText.getText().toString())) {
                binding.signUpPasswordEditText.setError("Field can't be empty!");
            }
            else if (TextUtils.isEmpty(binding.signUpIdNumber.getText().toString())) {
                binding.signUpIdNumber.setError("Field can't be empty!");
            }
            else if (binding.signUpIdNumber.getText().toString().length() < 8) {
                binding.signUpIdNumber.setError("Please enter a valid ID number!");
            }
            else if (TextUtils.isEmpty(binding.userFullName.getText().toString())) {
                binding.userFullName.setError("Field can't be empty!");
            }
            else if (TextUtils.isEmpty(binding.editTextPhone2.getText().toString())) {
                binding.editTextPhone2.setError("Field can't be empty!");
            }
            else if (binding.residenceCounty.getSelectedItem().toString().equalsIgnoreCase("Please select Your county")) {
                Toast.makeText(RegisterActivity.this, "Please select an option!", Toast.LENGTH_SHORT).show();
            }
            else if (binding.editTextPhone2.getText().toString().length() != 10 && ! binding.editTextPhone2.getText().toString().startsWith("0")) {
                binding.editTextPhone2.setError("Incorrect length of phone number");
            }
            else if (binding.signUpPasswordEditText.length() < 6) {
                binding.signUpPasswordEditText.setError("Password should be 6 characters or more!");
            }
            else if (!(binding.signUpPasswordEditText.getText().toString().equals(binding.signUpRetypePasswordEditText.getText().toString()))) {
                binding.signUpRetypePasswordEditText.setError("The passwords did not match!");
            }
            else if (!(isValidMail(binding.signUpEmailEditText.getText().toString())  && binding.signUpEmailEditText.getText().toString().contains("@"))) {
                binding.signUpEmailEditText.setError("Invalid Email!");
            } else {
                binding.signUpProgressBar.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            assert firebaseUser != null;
                            //Save the details to Firebase
                            HashMap<String, Object> hashmap = new HashMap<>();
                            hashmap.put("full_name", binding.userFullName.getText().toString().trim());
                            hashmap.put("phone_number", binding.editTextPhone2.getText().toString().trim());
                            hashmap.put("id_number", binding.signUpIdNumber.getText().toString().trim());
                            hashmap.put("email", binding.signUpEmailEditText.getText().toString().trim());
                            hashmap.put("location", binding.residenceCounty.getSelectedItem().toString());
                            hashmap.put("uid", firebaseUser.getUid());
                            hashmap.put("admin", false);

                            databaseReference.child(firebaseUser.getUid()).setValue(hashmap);

                            displayName = binding.userFullName.getText().toString();
                            binding.signUpProgressBar.setVisibility(View.GONE);
                            findViewById(R.id.signup_layout).setVisibility(View.GONE);
                            findViewById(R.id.farm_activities_layout).setVisibility(View.VISIBLE);
                            selectFarmActivities();
                        } else {
                            Toast.makeText(RegisterActivity.this, "" + task.getException(), Toast.LENGTH_LONG).show();
                            binding.signUpProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });

        binding.textViewHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void selectProfilePhoto() {
        Button save = findViewById(R.id.image_upload_btn);
        profilephoto.setOnClickListener(v->{
            selectProfile();
        });
        save.setOnClickListener(v -> {
            findViewById(R.id.upload_progress).setVisibility(View.VISIBLE);

            if(image == null){
                Map<String, Object> map = new HashMap<>();
                map.put("profile", "https://firebasestorage.googleapis.com/v0/b/elimisha-mkulima-e1d36.appspot.com/o/profile.png?alt=media&token=0e2fe03f-7734-4961-87d8-44d207459ce8");

                databaseReference.child(firebaseUser.getUid()).updateChildren(map);

                Toast.makeText(this, "Default profile picture applied", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();

            }else {
                StorageReference storage = FirebaseStorage.getInstance().getReference("images").child(firebaseUser.getUid()).child(image.getLastPathSegment());
                storage.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(displayName)
                                        .setPhotoUri(uri)
                                        .build();

                                Map<String, Object> map = new HashMap<>();
                                map.put("profile", String.valueOf(uri));

                                databaseReference.child(firebaseUser.getUid()).updateChildren(map);
                                firebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Profile Photo updated successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Failed to Upload your profile photo. " + task.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    private void selectFarmActivities() {
        Button save = findViewById(R.id.done);

        save.setOnClickListener(v -> {
            findViewById(R.id.upload_progress_farm).setVisibility(View.VISIBLE);
            List<String> selectedItems = new ArrayList<>();

            CheckBox beefFarmingCheckBox = findViewById(R.id.beef_farming);
            CheckBox dairyFarmingCheckBox = findViewById(R.id.dairy_farming);
            CheckBox poultryFarmingCheckBox = findViewById(R.id.poultry_farming);
            CheckBox beeFarmingCheckBox = findViewById(R.id.bee_keeping);
            CheckBox cropFarmingCheckBox = findViewById(R.id.crop_farming);

            if (beefFarmingCheckBox.isChecked()) {
                selectedItems.add(beefFarmingCheckBox.getText().toString());
            }

            if (dairyFarmingCheckBox.isChecked()) {
                selectedItems.add(dairyFarmingCheckBox.getText().toString());
            }

            if (poultryFarmingCheckBox.isChecked()) {
                selectedItems.add(poultryFarmingCheckBox.getText().toString());
            }
            if (beeFarmingCheckBox.isChecked()) {
                selectedItems.add(beefFarmingCheckBox.getText().toString());
            }

            if (cropFarmingCheckBox.isChecked()) {
                selectedItems.add(dairyFarmingCheckBox.getText().toString());
            }
            if(selectedItems.size() != 0){
                firebaseUser = firebaseAuth.getCurrentUser();
                String activity = selectedItems.get(0);
                int i = 1;
                while (selectedItems.size() > i){
                    activity = activity+", "+selectedItems.get(i);
                    i++;
                }
                assert firebaseUser != null;

                Map<String, Object> map = new HashMap<>();
                map.put("farm_activities", activity);

                databaseReference.child(firebaseUser.getUid()).updateChildren(map);

                findViewById(R.id.farm_activities_layout).setVisibility(View.GONE);
                findViewById(R.id.upload_progress_farm).setVisibility(View.GONE);
                findViewById(R.id.profile_layout).setVisibility(View.VISIBLE);
                selectProfilePhoto();

            } else{
                Toast.makeText(this, "Please select the farming activities you participate in", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidMail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }
    public void selectProfile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, profile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == profile){
            image = data.getData();
            if(image == null){
                Toast.makeText(this, "Could not get the image you selected", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "The image was loaded, Please proceed to upload it", Toast.LENGTH_SHORT).show();
                Picasso.get().load(new File(getPathFromUri(image))).fit().centerCrop().into(profilephoto);
            }
        }
    }
}