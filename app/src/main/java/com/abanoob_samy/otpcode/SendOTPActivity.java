package com.abanoob_samy.otpcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.abanoob_samy.otpcode.databinding.ActivitySendOtpBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOTPActivity extends AppCompatActivity {

    private static final String TAG = "SendOTPActivity";

    private ActivitySendOtpBinding binding;

    private Activity mActivity;

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mActivity = SendOTPActivity.this;

        mAuth = FirebaseAuth.getInstance();

        CallBacks();
    }

    private void CallBacks() {

        binding.btnSendOTP.setOnClickListener(v -> {

            String code = binding.countryCodePicker.getSelectedCountryCode();
            String phoneNumber = binding.etInputMobileNumber.getText().toString();

            if (TextUtils.isEmpty(binding.etInputMobileNumber.getText().toString())
                    || phoneNumber.trim().isEmpty() || phoneNumber.length() <= 6) {
                // when mobile number text field is empty
                // displaying a toast message.
                //that will still return until the user enter the number
                Toast.makeText(mActivity, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
            }
            else {
                // if the text field is not empty we are calling our
                // send OTP method for getting OTP from Firebase.
                String phoneNumberWithCode = "+" + code + phoneNumber; // +201276666918
                setUpPhoneAuthVerification(binding.btnSendOTP, phoneNumberWithCode);
            }
        });
    }

    private void setUpPhoneAuthVerification(Button btnGet, String phoneNumberWithCode) {

        binding.progressBarSend.setVisibility(View.VISIBLE);
        btnGet.setVisibility(View.GONE);

        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                Log.d(TAG, "onVerificationCompleted:" + credential);

                binding.progressBarSend.setVisibility(View.GONE);
                btnGet.setVisibility(View.VISIBLE);

//                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.w(TAG, "onVerificationFailed"+ e.getMessage() + " : ", e);

                binding.progressBarSend.setVisibility(View.GONE);
                btnGet.setVisibility(View.VISIBLE);

                Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                Intent intent = new Intent(mActivity, VerifyOTPActivity.class);
                intent.putExtra("mobile", phoneNumberWithCode);
                intent.putExtra("verificationId", mVerificationId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK); // to not back for this activity.
                startActivity(intent);

                binding.btnSendOTP.setVisibility(View.VISIBLE);
                binding.progressBarSend.setVisibility(View.GONE);

                Toast.makeText(mActivity, "OTP Send Successfully.", Toast.LENGTH_SHORT).show();
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumberWithCode)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void setUpStartActivity() {

        Intent intent = new Intent(mActivity,
                HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //which means we already have a user.
        if (mAuth.getCurrentUser() != null) {

            setUpStartActivity();
        }
    }
}