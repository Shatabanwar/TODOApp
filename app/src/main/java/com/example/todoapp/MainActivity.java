package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{

    EditText birthdayEditText,emailEditText,phoneEditText,passwordEditText,fullNameEditText;
    int year,month,day;
    String DOB;
    FirebaseAuth mAuth;
    Button signUpButton;
    TextView loginTextView;




    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.loginTextView){

            startActivity(new Intent(getApplicationContext(),LoginActivity.class));   //Sending to Login Activity
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        signUpButton = findViewById(R.id.signUpButton);
        loginTextView = findViewById(R.id.loginTextView);
        loginTextView.setOnClickListener(this);
        birthdayEditText  = findViewById(R.id.birthdayEditText);
        final Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.gender));

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);



        final Calendar calendar = Calendar.getInstance();



        birthdayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                           DOB =dayOfMonth +"/" + (month +1) + "/" + year;
                           birthdayEditText.setText(DOB);
                    }
                },year,month,day);

                datePickerDialog.show();



            }
        });

        if(mAuth.getCurrentUser() != null){
            //Send the User to Notes Activity
            startActivity(new Intent(getApplicationContext(),NotesActivity.class));
            finish();
        }

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //birthdayEditText.setError("Select Date of Birth");
                final String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                final String phone = phoneEditText.getText().toString().trim();
                final String name = fullNameEditText.getText().toString().trim();
                final String gender = spinner.getSelectedItem().toString();

                if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    passwordEditText.setError("Password is Required");
                    return;
                }
                if (password.length() < 6) {
                    passwordEditText.setError("Password must be greater than 6 characters");
                    return;
                }
                if (TextUtils.isEmpty(DOB)) {

                    birthdayEditText.setError("Select a Date of Birth");
                    return;

                }






        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    //Send User to Login Page
                    User user = new User(name,email,phone,DOB,gender);
                    FirebaseFirestore.getInstance().collection("users")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.i("Message","Data Saved");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


            });


        }

    });







                    }


}

