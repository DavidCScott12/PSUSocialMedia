package com.example.psusocialmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText login_email_et, login_password_et;
    Button button_login, button_login_sign_up;
    CheckBox login_checkBox;
    ProgressBar progressbar_login;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_email_et = findViewById(R.id.login_email_et);
        login_password_et = findViewById(R.id.login_password_et);
        button_login = findViewById(R.id.button_login);
        button_login_sign_up = findViewById(R.id.button_login_sign_up);
        login_checkBox = findViewById(R.id.login_checkbox);
        progressbar_login = findViewById(R.id.progressbar_login);
        mAuth = FirebaseAuth.getInstance();

        login_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    login_password_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    login_password_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        button_login_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //button_login_sign_up
    }
}