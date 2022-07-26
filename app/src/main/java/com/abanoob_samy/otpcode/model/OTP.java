package com.abanoob_samy.otpcode.model;

public class OTP {

    boolean checked;
    String title;

    public OTP(boolean checked, String title) {
        this.checked = checked;
        this.title = title;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
