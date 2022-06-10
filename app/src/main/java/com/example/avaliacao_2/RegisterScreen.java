package com.example.avaliacao_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterScreen extends AppCompatActivity {

    EditText emailSignUp_, passwordSignUp_, confirmPasswordSingUp_, nameSignUp_;
    Button signUpBtn;

    private FirebaseAuth mAuth;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        nameSignUp_ = findViewById(R.id.nameSignUpField);
        emailSignUp_ = findViewById(R.id.emailRegisterField);
        passwordSignUp_ = findViewById(R.id.passwdRegisterField);
        confirmPasswordSingUp_ = findViewById(R.id.confirmPasswdRegisterField);
        signUpBtn = findViewById(R.id.buttonRecoveryPasswd);
        mAuth = FirebaseAuth.getInstance();

    }
    private boolean validateFields(){
        if(emailSignUp_.getText().toString().isEmpty()){
            emailSignUp_.setError("O campo e-mail não pode estar vazio!");
            emailSignUp_.requestFocus();
            return false;
        }if(passwordSignUp_.getText().toString().isEmpty()){
            passwordSignUp_.setError("A senha não pode estar vazia!");
            passwordSignUp_.requestFocus();
            return false;
        }if(confirmPasswordSingUp_.getText().toString().isEmpty()){
            confirmPasswordSingUp_.setError("A confirmação de senha não pode estar vazia!");
            confirmPasswordSingUp_.requestFocus();
            return false;
        }if(nameSignUp_.getText().toString().isEmpty()){
            nameSignUp_.setError("O nome não pode estar vazio!");
            nameSignUp_.requestFocus();
            return false;
        }if(!passwordSignUp_.getText().toString().equals(confirmPasswordSingUp_.getText().toString())){
            confirmPasswordSingUp_.setError("A confirmação de senha precisa ser igual a senha!");
            confirmPasswordSingUp_.requestFocus();
            return false;
        }if(passwordSignUp_.getText().toString().length() < 6){
            passwordSignUp_.setError("A senha não pode ser menor que 6 carácteres");
            passwordSignUp_.requestFocus();
            return false;
        }if(!Patterns.EMAIL_ADDRESS.matcher(emailSignUp_.getText().toString()).matches()){
            emailSignUp_.setError("Preencha o email corretamente");
            emailSignUp_.requestFocus();
            return false;
        }else{
            return true;
        }
    }
    public void signUpUser(View v){
        if(!validateFields()){
            return;
        }
        mAuth.createUserWithEmailAndPassword(emailSignUp_.getText().toString(), passwordSignUp_.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("CREATE_USER", "createUserWithEmail:success");
                    Toast.makeText(RegisterScreen.this, "Usuario criado com sucesso!", Toast.LENGTH_SHORT).show();
                    i = new Intent(RegisterScreen.this, MainActivity.class);
                    startActivity(i);
                }else{
                    Log.w("CREATE_USER", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RegisterScreen.this, "Falha ao criar usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}