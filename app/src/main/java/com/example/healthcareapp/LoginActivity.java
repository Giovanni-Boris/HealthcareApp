package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Room.Datasource;
import com.example.healthcareapp.Room.UserDAO;
import com.example.healthcareapp.Services.AuthService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class LoginActivity extends AppCompatActivity {
    EditText edUsername, edPassword;
    Button btn;
    TextView tv;
    private Datasource datasource;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edUsername = findViewById(R.id.editTextRegEmail);
        edPassword = findViewById(R.id.editTextRegConfirmPassword);
        btn = findViewById(R.id.buttonRegister);
        tv = findViewById(R.id.textViewExistUser);
        datasource = Datasource.newInstance(getApplicationContext());
        UserDAO userDao = datasource.userDAO();
        authService = new AuthService(userDao);
        btn.setOnClickListener(view -> {
            String username = edUsername.getText().toString();
            String password = edPassword.getText().toString();
            if(username.length()==0 || password.length()==0)
                Toast.makeText(getApplicationContext(),"Please fill all details",Toast.LENGTH_SHORT).show();
            else {
                authService.loginUserAndGetToken(username, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(token -> {
                            Toast.makeText(getApplicationContext(), "Login Succeded ", Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("username", username);
                            editor.putString("token", token);
                            editor.apply();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }, throwable -> {
                            Toast.makeText(getApplicationContext(), "Invalid Username and Password", Toast.LENGTH_SHORT).show();
                        });

            }
        });

        tv.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        } );

    }
}