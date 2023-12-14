package com.example.healthcareapp.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.healthcareapp.Entity.Register;
import com.example.healthcareapp.Entity.User;
import com.example.healthcareapp.LoginActivity;
import com.example.healthcareapp.R;
import com.example.healthcareapp.RegisterActivity;
import com.example.healthcareapp.Room.Datasource;
import com.example.healthcareapp.Room.RegisterDAO;
import com.example.healthcareapp.Room.UserDAO;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddRegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRegisterFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText inputGlucemia, inputInsulinaComida, inputMedicamentos, inputTension, inputPeso, inputActividad, inputCarbohidratos, editTextTime;
    Button button_add;
    private Datasource datasource;
    private RegisterDAO registerDAO;

    public AddRegisterFragment() {
    }

    public static AddRegisterFragment newInstance(String param1, String param2) {
        AddRegisterFragment fragment = new AddRegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_register, container, false);

        inputGlucemia = view.findViewById(R.id.inputGlucemia);
        inputInsulinaComida = view.findViewById(R.id.inputInsulinaComida);
        inputMedicamentos = view.findViewById(R.id.inputMedicamentos);
        inputTension = view.findViewById(R.id.inputTension);
        inputPeso = view.findViewById(R.id.inputPeso);
        inputActividad = view.findViewById(R.id.inputActividad);
        inputCarbohidratos = view.findViewById(R.id.inputCarbohidratos);
        editTextTime = view.findViewById(R.id.editTextTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());
        editTextTime.setText(currentDateAndTime);
        button_add = view.findViewById(R.id.button_add);
        datasource = Datasource.newInstance(getActivity().getApplicationContext());
        registerDAO = datasource.registerDAO();
        button_add.setOnClickListener(view1 -> {
            Register register = new Register();
            register.setHora(currentDateAndTime);
            register.setGlucemia(Integer.valueOf(inputGlucemia.getText().toString().trim()));
            register.setInsulina(inputInsulinaComida.getText().toString());
            register.setCarbohidrato(inputCarbohidratos.getText().toString());
            register.setMedicamento(inputMedicamentos.getText().toString());
            register.setActividad(inputActividad.getText().toString());
            register.setTension(inputTension.getText().toString());
            register.setPeso(inputPeso.getText().toString());
            registerGlucosa(register);

        });
        return view;

    }

    @SuppressLint("CheckResult")
    private void registerGlucosa(Register register) {
        Completable.fromAction(() -> {
            this.registerDAO.insertRegister(register);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Toast.makeText(getActivity().getApplicationContext(), "Register succeded ", Toast.LENGTH_SHORT).show();
                    replaceFragments(new HomeFragment());
                }, throwable -> {
                    Log.e("RegisterActivity", "Registration error: " + throwable.getMessage());
                });
    }

    private void replaceFragments(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}