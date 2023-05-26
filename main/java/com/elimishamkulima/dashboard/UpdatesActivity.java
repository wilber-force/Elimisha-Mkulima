package com.elimishamkulima.dashboard;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.util.Base64;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.elimishamkulima.R;
import com.elimishamkulima.model.Farmers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdatesActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "MainActivity";
    ArrayList<String> phone_numbers;
    private static final String ACCOUNT_SID = "ACeb2bc54af4c5d5ea2e8441093ac50c36";
    private static final String AUTH_TOKEN = "dfa2c38964b7cc33cc3e3dd36830d1c1";
    private static final String FROM_PHONE_NUMBER = "+12524944550";
    private static final String TO_PHONE_NUMBER = "+254748385222";
    private static final String MESSAGE_BODY = "Hey";
    Spinner spinner;
    EditText title, start_date, end_date, details;
    DatabaseReference reference;
    ArrayList<Farmers> farmers;
    Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updates);

        spinner = findViewById(R.id.spinner_add);
        title = findViewById(R.id.title_add);
        start_date = findViewById(R.id.start_add);
        end_date = findViewById(R.id.end_add);
        details = findViewById(R.id.details_add);
        add = findViewById(R.id.btn_add);

        farmers = new ArrayList<>();

        phone_numbers = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
        }
        reference = FirebaseDatabase.getInstance().getReference();

        loadFarmers();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item from the Spinner
                String selectedValue = parent.getItemAtPosition(position).toString();
                if(selectedValue.equalsIgnoreCase("Add a farm update")){
                    start_date.setVisibility(View.GONE);
                    end_date.setVisibility(View.GONE);
                }else{
                    start_date.setVisibility(View.VISIBLE);
                    end_date.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        add.setOnClickListener(v->{
            String type, topic, start, end, desc;
            type = spinner.getSelectedItem().toString().trim();
            topic = title.getText().toString().trim();
            start = start_date.getText().toString().trim();
            end = end_date.getText().toString().trim();
            desc = details.getText().toString().trim();
            HashMap<String, String> hashMap = new HashMap<>();

            if(type.equalsIgnoreCase("Add a farm update")){
                if(topic.isEmpty()){
                    title.setError("Please type a title");
                }
                if(desc.isEmpty()){
                    details.setError("Field cannot be blank");
                }else {
                    hashMap.put("title", topic);
                    hashMap.put("description", desc);
                    hashMap.put("publish_date", new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(new Date()));

                    reference = FirebaseDatabase.getInstance().getReference("Farm");
                    reference.child(topic).setValue(hashMap);
                    Toast.makeText(this, "The details have been saved successfully", Toast.LENGTH_SHORT).show();
                    for (String phone_number : phone_numbers) {
                        sendSms(formatPhoneNumber(phone_number), topic+"\n"+desc);
                        Log.e("SMS send to: ", phone_number);
                    }
                    title.setText("");
                    details.setText("");
                    start_date.setText("");
                    end_date.setText("");
                }
            } else if(type.equalsIgnoreCase("Add an Event")){
                if(topic.isEmpty()){
                    title.setError("Please type a title");
                }
                if(desc.isEmpty()){
                    details.setError("Field cannot be blank");
                }
                if(start.isEmpty()){
                    start_date.setError("Field cannot be blank");
                }
                if(end.isEmpty()){
                    end_date.setError("Field cannot be blank");
                }else {
                    hashMap.put("title", topic);
                    hashMap.put("description", desc);
                    hashMap.put("start_date", start);
                    hashMap.put("end_date", end);
                    hashMap.put("published_on", new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(new Date()));

                    reference = FirebaseDatabase.getInstance().getReference("Events");
                    //reference.child(topic).setValue(hashMap);
                    Toast.makeText(this, "The details have been saved successfully", Toast.LENGTH_SHORT).show();
                    for (String phone_number : phone_numbers) {
                        sendSms(formatPhoneNumber(phone_number), topic+"\n"+desc);
                        Log.e("SMS send to: ", phone_number);
                    }
                    title.setText("");
                    details.setText("");
                    start_date.setText("");
                    end_date.setText("");
                }
            } else if (type.equalsIgnoreCase("Add a training forum")) {
                if(topic.isEmpty()){
                    title.setError("Please type a title");
                }
                if(desc.isEmpty()){
                    details.setError("Field cannot be blank");
                }
                if(start.isEmpty()){
                    start_date.setError("Field cannot be blank");
                }
                if(end.isEmpty()){
                    end_date.setError("Field cannot be blank");
                }else {
                    hashMap.put("title", topic);
                    hashMap.put("description", desc);
                    hashMap.put("start_date", start);
                    hashMap.put("end_date", end);
                    hashMap.put("published_on", new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(new Date()));

                    reference = FirebaseDatabase.getInstance().getReference("Trainings");
                    //reference.child(topic).setValue(hashMap);
                    Toast.makeText(this, "The details have been saved successfully", Toast.LENGTH_SHORT).show();
                    for (String phone_number : phone_numbers) {
                        sendSms(formatPhoneNumber(phone_number), topic+"\n"+desc);
                        Log.e("SMS send to: ", phone_number);
                    }
                    title.setText("");
                    details.setText("");
                    start_date.setText("");
                    end_date.setText("");
                }
            }else{
                Toast.makeText(this, "Please select a valid option from the dropdown", Toast.LENGTH_SHORT).show();
            }

        });


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                for (String phone_number : phone_numbers) {
                    sendSms(phone_number, "");
                }
            } else {
                Log.e(TAG, "SMS permission denied");
            }
        }
    }

    private void sendSms(String phoneNumber, String message) {
        new SendSmsTask().execute(phoneNumber, message);
    }

    private class SendSmsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String phoneNumber = params[0];
            String message = params[1];

            try {
                OkHttpClient client = new OkHttpClient();

                String url = "https://api.twilio.com/2010-04-01/Accounts/" + ACCOUNT_SID + "/Messages.json";

                RequestBody body = new FormBody.Builder()
                        .add("From", FROM_PHONE_NUMBER)
                        .add("To", phoneNumber)
                        .add("Body", message)
                        .build();

                String credentials = ACCOUNT_SID + ":" + AUTH_TOKEN;
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                String authHeader = "Basic " + base64EncodedCredentials;

                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .header("Authorization", authHeader)
                        .build();

                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();

                return responseBody;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String responseBody) {
            if (responseBody != null) {
                Log.d(TAG, "SMS sent: " + responseBody);
                // Handle the response as needed
            } else {
                Log.e(TAG, "Failed to send SMS");
            }
        }

    }



    public String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber.startsWith("+254")) {
            return phoneNumber; // Phone number already begins with "+254"
        } else if (phoneNumber.startsWith("0")) {
            return "+254" + phoneNumber.substring(1); // Remove leading "0" and prepend "+254"
        } else {
            return "+254" + phoneNumber; // Prepend "+254" to the phone number
        }
    }


    private void loadFarmers() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Farmers");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        farmers.add((Farmers) dataSnapshot1.getValue(Farmers.class));
                        phone_numbers.add(dataSnapshot1.getValue(Farmers.class).getPhone_number());
                    }
                }else{
                    Toast.makeText(UpdatesActivity.this, "No Farmers were found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdatesActivity.this, "Failed to load farmers", Toast.LENGTH_SHORT).show();
            }
        });
    }
}