package com.example.avaliacao_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgottenPassword extends AppCompatActivity {

    EditText emailForgottenField_;
    Button buttonRecoveryPasswd_;
    TextView homeButton_;
    Intent i;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);
        emailForgottenField_ = findViewById(R.id.emailForgottenField);
        buttonRecoveryPasswd_ = findViewById(R.id.buttonRecoveryPasswd);
        homeButton_ = findViewById(R.id.homeButton);
        progressBar = findViewById(R.id.progressBarForgottenPasswd);
        mAuth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.GONE);

        homeButton_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(ForgottenPassword.this, MainActivity.class);
                startActivity(i);
            }
        });

        buttonRecoveryPasswd_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(emailForgottenField_.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailForgottenField_.getText().toString()).matches()){
                    emailForgottenField_.setError("Preencha corretamente!");
                    emailForgottenField_.requestFocus();
                    return;
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.sendPasswordResetEmail(emailForgottenField_.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgottenPassword.this, "Senha resetada. Verifique seu e-mail", Toast.LENGTH_LONG).show();
                                i = new Intent(ForgottenPassword.this, MainActivity.class);
                                startActivity(i);
                            }else{
                                Toast.makeText(ForgottenPassword.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}