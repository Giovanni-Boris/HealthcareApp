package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Entity.User;
import com.example.healthcareapp.Room.Datasource;
import com.example.healthcareapp.Room.UserDAO;
import com.example.healthcareapp.Services.AuthService;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {
    EditText edUsername, edPassword, edEmail, edConfirm;
    Button btn;
    TextView tv;
    private Datasource datasource;
    private UserDAO userDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edUsername = findViewById(R.id.editTextRegUsername);
        edEmail = findViewById(R.id.editTextRegEmail);
        edPassword = findViewById(R.id.editTextRegPassword);
        edConfirm = findViewById(R.id.editTextRegConfirmPassword);
        btn = findViewById(R.id.buttonRegister);
        tv = findViewById(R.id.textViewExistUser);
        datasource = Datasource.newInstance(getApplicationContext());
        userDao = datasource.userDAO();

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edUsername.getText().toString();
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();
                String confirm = edConfirm.getText().toString();
                if(username.length()==0 || password.length()==0 || email.length()==0 || confirm.length()==0)
                    Toast.makeText(getApplicationContext(),"Please fill all details",Toast.LENGTH_SHORT).show();
                else {
                    if(password.compareTo(confirm)==0)
                        registerUser(username, password);
                    else
                        Toast.makeText(getApplicationContext(), "Password and Confirm password didn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void registerUser(String username, String password) {
        Completable.fromAction(() -> {
            User newUser = new User(username, password);
            System.out.println(newUser);
            this.userDao.insert(newUser);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    Toast.makeText(getApplicationContext(), "Register successfully", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }, throwable -> {
                    Log.e("RegisterActivity", "Registration error: " + throwable.getMessage());
                });
    }
}