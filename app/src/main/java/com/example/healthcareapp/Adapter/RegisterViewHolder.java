package com.example.healthcareapp.Adapter;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Entity.Register;
import com.example.healthcareapp.R;

public class RegisterViewHolder extends RecyclerView.ViewHolder {
    TextView reg_glucemia, reg_date, reg_id;
    public RegisterViewHolder(@NonNull View itemView) {
        super(itemView);
        reg_id = itemView.findViewById(R.id.register_id_text);
        reg_glucemia= itemView.findViewById(R.id.register_glucemia_txt);
        reg_date= itemView.findViewById(R.id.register_fecha_txt);
    }
    public void bind(final Register register, final RegisterAdapter.OnItemClickListener listener) {
        reg_glucemia.setText(String.valueOf(register.getGlucemia()));
        reg_date.setText(String.valueOf(register.getHora()));
        reg_id.setText(String.valueOf(register.getId()));
        reg_id.setText(String.valueOf(register.getId()));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(register);
            }
        });
    }
}