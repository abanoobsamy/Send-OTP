package com.abanoob_samy.otpcode;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.ChooseHolder> {

    @NonNull
    @Override
    public ChooseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ChooseHolder extends RecyclerView.ViewHolder {
        
        public ChooseHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
