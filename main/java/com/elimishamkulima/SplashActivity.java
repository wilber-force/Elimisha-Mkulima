package com.elimishamkulima;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import com.elimishamkulima.authentication.LoginActivity;
import com.elimishamkulima.dashboard.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    int progress = 0;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressBar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
                startApp();
                finish();

            }
        }).start();

    }
    public void startApp(){
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }
    public void doWork(){
        for(progress = 0; progress <= 100;progress +=20){
            try {
                progressBar.setProgress(progress);
                Thread.sleep(500);
            } catch (Exception e){
                e.printStackTrace();
                Log.e("Error", e.getMessage());
            }
        }
    }
}