package com.abanoob_samy.otpcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.abanoob_samy.otpcode.databinding.ActivityVerifyOtpBinding;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerifyOTPActivity extends AppCompatActivity {

    private ActivityVerifyOtpBinding binding;

    private FirebaseAuth mAuth;
    private String verificationId;

    private OTPReceiver otpReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        verificationId = getIntent().getStringExtra("verificationId");

        callBacks();

        // Get All Input fields Quickly.
        autoOTPReceiver();
    }

    private void autoOTPReceiver() {

        otpReceiver = new OTPReceiver();

        this.registerReceiver(otpReceiver, new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION));

        otpReceiver.setOtpReceiverListener(new OTPReceiver.OTPReceiverListener() {
            @Override
            public void onOTPSuccess(String otp) {

//                int o1 = Character.getNumericValue(otp.charAt(0));
//                int o2 = Character.getNumericValue(otp.charAt(1));
//                int o3 = Character.getNumericValue(otp.charAt(2));
//                int o4 = Character.getNumericValue(otp.charAt(3));
//                int o5 = Character.getNumericValue(otp.charAt(4));
//                int o6 = Character.getNumericValue(otp.charAt(5));
//
//                input1.setText(String.valueOf(o1));
//                input2.setText(String.valueOf(o2));
//                input3.setText(String.valueOf(o3));
//                input4.setText(String.valueOf(o4));
//                input5.setText(String.valueOf(o5));
//                input6.setText(String.valueOf(o6));

                binding.firstPinView.setText(otp);

                //Button automatically click || make onClickListener automatic.
                binding.btnVerifyOTP.performClick();
            }

            @Override
            public void onOTPTimeout() {

                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callBacks() {

        binding.btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pinViewCodeOTP = binding.firstPinView.getText().toString();

                // validating if the OTP text field is empty or not.
                if (TextUtils.isEmpty(pinViewCodeOTP)) {
                    // if the OTP text field is empty display
                    // a message to user to enter OTP
                    Toast.makeText(getApplicationContext(), "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    // if OTP field is not empty calling
                    // method to verify the OTP.
                    if (verificationId == null) {

                        Toast.makeText(getApplicationContext(), "verificationId == null",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    verifyCode(pinViewCodeOTP);
                }
            }
        });
    }

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {

        binding.btnVerifyOTP.setVisibility(View.GONE);
        binding.progressBarVerify.setVisibility(View.VISIBLE);

        // below line is used for getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // if the code is correct and the task is successful
                        // we are sending our user to new activity.
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        // if the code is not correct then we are
                        // displaying an error message to the user.
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}