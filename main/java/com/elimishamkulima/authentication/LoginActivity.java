package com.elimishamkulima.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.elimishamkulima.SplashActivity;
import com.elimishamkulima.dashboard.HomeActivity;
import com.elimishamkulima.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ActivityLoginBinding binding;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        
        if (firebaseUser != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show();
            finish();
        }

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = binding.signInEmailEditText.getText().toString().trim();
                String passwd = binding.signInPasswordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(binding.signInEmailEditText.getText().toString())) {
                    binding.signInEmailEditText.setError("Field can't be empty!!");
                    return;
                } else if (TextUtils.isEmpty(binding.signInPasswordEditText.getText().toString())) {
                    binding.signInPasswordEditText.setError("Field can't be empty!!");
                    return;
                } else {
                    binding.signInProgressBar.setVisibility(View.VISIBLE);
                }

                firebaseAuth.signInWithEmailAndPassword(mail, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_LONG).show();

                        } else {
                            binding.signInProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.textViewDontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}