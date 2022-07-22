package com.abanoob_samy.otpcode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OTPReceiver extends BroadcastReceiver {

    private OTPReceiverListener otpReceiverListener;

    public OTPReceiver() {
    }

    public void setOtpReceiverListener(OTPReceiverListener otpReceiverListener) {
        this.otpReceiverListener = otpReceiverListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {

            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            if (extras != null) {

                switch (status.getStatusCode()) {

                    case CommonStatusCodes.SUCCESS:
                        // Get SMS message contents
                        String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                        // Extract one-time code from the message and complete verification
                        // by sending the code back to your server.
                        Pattern pattern = Pattern.compile("\\d{6}");
                        Matcher matcher = pattern.matcher(message);

                        if (matcher.find()) {

                            String otp = matcher.group(0);

                            if (this.otpReceiverListener != null) {
                                this.otpReceiverListener.onOTPSuccess(otp);
                            }
                            else {
                                if (this.otpReceiverListener != null) {
                                    this.otpReceiverListener.onOTPTimeout();
                                }
                            }
                        }
                        break;

                    case CommonStatusCodes.TIMEOUT:
                        // Waiting for SMS timed out (5 minutes)
                        // Handle the error ...

                        if (this.otpReceiverListener != null) {
                            this.otpReceiverListener.onOTPTimeout();
                        }
                        break;
                }
            }
        }
    }

    public interface OTPReceiverListener {

        void onOTPSuccess(String otp);

        void onOTPTimeout();
    }
}
