package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPass;
    private Button btnLogin;
    private TextView mForgetPassword;
    private TextView mSignupHere;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginDetails();

    }

    private void loginDetails() {

        mEmail = findViewById(R.id.email_login);
        mPass = findViewById(R.id.password_login);
        btnLogin = findViewById(R.id.btn_login);
        mForgetPassword = findViewById(R.id.forget_password);
        mSignupHere = findViewById(R.id.signup_reg);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmail.getText().toString().trim();
                String pass = mPass.getText().toString().trim();

                if (TextUtils.isEmpty(pass)) {

                    mEmail.setError("Email is required.");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    mPass.setError("Password is required");
                    return;
                }


            }
        });

        //Registration activity

        mSignupHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });

        //Reset password activity..

        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ReseatActivity.class));
            }
        });
    }


}
