package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText emailEditText,passwordEditText;
    Button loginButton;
    FirebaseAuth mAuth;
    TextView signUpTextView,otpTextView;


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.signUpTextView){
            startActivity(new Intent(getApplicationContext(),MainActivity.class)); //sending to SignUp Page
        } else if(v.getId() == R.id.otpTextView){
            startActivity(new Intent(getApplicationContext(),OTPActivity.class));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);


        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        mAuth = FirebaseAuth.getInstance();
        signUpTextView = findViewById(R.id.signUpTextView);
        signUpTextView.setOnClickListener(this);
        otpTextView = findViewById(R.id.otpTextView);
        otpTextView.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            //Send the User to Notes Activity
            startActivity(new Intent(getApplicationContext(),NotesActivity.class));
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    passwordEditText.setError("Password is Required");
                    return;
                }

                //Authenticate the User
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "User has been Logged In!", Toast.LENGTH_SHORT).show();
                            //Send User to Notes Page
                            startActivity(new Intent(getApplicationContext(), NotesActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
        });
    }
}