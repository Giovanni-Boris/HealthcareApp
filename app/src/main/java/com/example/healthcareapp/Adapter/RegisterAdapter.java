package com.example.healthcareapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Entity.Register;
import com.example.healthcareapp.R;

import java.util.List;

public class RegisterAdapter extends RecyclerView.Adapter<RegisterViewHolder> {
    private final List<Register> registers;
    private final OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(Register  register);
    }
    public RegisterAdapter(List<Register> registers,  OnItemClickListener listener ) {

        this.registers = registers;
        this.listener = listener;

    }
    @NonNull
    @Override
    public RegisterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_row, parent, false);
        return new RegisterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegisterViewHolder holder, int position) {
        Register register = registers.get(position);
        holder.bind(register, listener);
    }

    @Override
    public int getItemCount() {
        return registers.size();
    }
}