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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText loginEmailField_, loginPasswdField_;
    TextView forgotPasswrd_, signUpBtn_;
    Button buttonLogin;
    Intent i;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        forgotPasswrd_ = findViewById(R.id.forgotPasswrd);
        signUpBtn_ = findViewById(R.id.signUpBtn);
        buttonLogin = findViewById(R.id.buttonLogin);
        loginEmailField_ = findViewById(R.id.loginEmailField);
        loginPasswdField_ = findViewById(R.id.loginPasswdField);
        progressBar = findViewById(R.id.progressBarLogin);

        mAuth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.GONE);

        signUpBtn_.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                i = new Intent(MainActivity.this, RegisterScreen.class);
                startActivity(i);
            }
        });

        forgotPasswrd_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(MainActivity.this, ForgottenPassword.class);
                startActivity(i);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(!validateFields()){
//                    return;
//                }else{
//                    progressBar.setVisibility(View.VISIBLE);
//                    mAuth.signInWithEmailAndPassword(loginEmailField_.getText().toString(), loginPasswdField_.getText().toString())
//                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if(task.isSuccessful()){
//                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                                        if(user.isEmailVerified()){
//                                            Toast.makeText(MainActivity.this, "Usuario logado com sucesso!", Toast.LENGTH_LONG).show();
//
//                                        }else{
//                                            Toast.makeText(MainActivity.this, "Verifique a conta via email.", Toast.LENGTH_LONG).show();
//                                            user.sendEmailVerification();
//                                        }
//                                    }else{
//                                        Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
//                                    }
//                                    progressBar.setVisibility(View.GONE);
//                                }
//                            });
//                }
                i = new Intent(MainActivity.this, SelectItems.class);
                startActivity(i);
            }
        });
    }

    private boolean validateFields(){
        if(loginEmailField_.getText().toString().isEmpty()){
            loginEmailField_.setError("O campo e-mail não pode estar vazio!");
            loginEmailField_.requestFocus();
            return false;
        }if(!Patterns.EMAIL_ADDRESS.matcher(loginEmailField_.getText().toString()).matches()){
            loginEmailField_.setError("Preencha corretamente!");
            loginEmailField_.requestFocus();
            return false;
        }if(loginPasswdField_.getText().toString().isEmpty()){
            loginPasswdField_.setError("A senha não pode estar vazia!");
            loginPasswdField_.requestFocus();
            return false;
        }if(loginPasswdField_.getText().toString().length() < 6){
            loginPasswdField_.setError("A senha não pode ser menor que 6 carácteres");
            loginPasswdField_.requestFocus();
            return false;
        }else{
            return true;
        }
    }
}