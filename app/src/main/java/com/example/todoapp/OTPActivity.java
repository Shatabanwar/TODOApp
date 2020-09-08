package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText phone,codeEnter;
    Button nextBtn;
    TextView state,resendOtpBtn;
    ProgressBar progressBar;
    CountryCodePicker ccp;
    String verificationId;
    PhoneAuthProvider.ForceResendingToken token;
    Boolean verificationInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);


        phone = findViewById(R.id.phone);
        codeEnter = findViewById(R.id.codeEnter);
        nextBtn = findViewById(R.id.nextBtn);
        progressBar = findViewById(R.id.progressBar);
        state = findViewById(R.id.state);
        resendOtpBtn = findViewById(R.id.resendOtpBtn);
        mAuth = FirebaseAuth.getInstance();
        ccp = findViewById(R.id.ccp);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!verificationInProgress) {


                    if (!phone.getText().toString().isEmpty() && phone.getText().toString().length() == 10) {

                        String phoneNumber = "+" + ccp.getSelectedCountryCode() + phone.getText().toString();

                        progressBar.setVisibility(View.VISIBLE);
                        state.setText("Sending OTP...");
                        state.setVisibility(View.VISIBLE);

                        requestOTP(phoneNumber);


                    } else {
                        phone.setError("Invalid Phone Number");
                    }
                }else{
                    String otp = codeEnter.getText().toString();
                    if(!otp.isEmpty() && otp.length() == 6){
                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId,otp);
                        verifyAuth(phoneAuthCredential);
                    } else{
                        codeEnter.setError("Invalid OTP");
                    }
                }
            }
        });
    }
    private void verifyAuth(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(OTPActivity.this, "Verified Successfully!", Toast.LENGTH_SHORT).show();
                    //Send to Notes Activity
                    startActivity(new Intent(getApplicationContext(),NotesActivity.class));
                } else{
                    Toast.makeText(OTPActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void requestOTP(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                token = forceResendingToken;
                progressBar.setVisibility(View.GONE);
                state.setVisibility(View.GONE);
                codeEnter.setVisibility(View.VISIBLE);
                nextBtn.setText("Verify OTP");
                verificationInProgress = true;

            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                           if(task.isSuccessful()){
                               Toast.makeText(OTPActivity.this, "Verified Successfully!", Toast.LENGTH_SHORT).show();
                               //Send to Notes Activity
                               startActivity(new Intent(getApplicationContext(),NotesActivity.class));
                           } else{
                               Toast.makeText(OTPActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                           }
                    }
                });



            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                e.printStackTrace();
                Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}