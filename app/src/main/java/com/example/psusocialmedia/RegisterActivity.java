package com.example.psusocialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText register_email_et, register_password_et, register_confirmpassword_et;
    Button button_register, button_sign_up_login;
    CheckBox register_checkBox;
    ProgressBar progressbar_register;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_email_et = findViewById(R.id.register_email_et);
        register_password_et = findViewById(R.id.register_password_et);
        register_confirmpassword_et = findViewById(R.id.register_confirmpassword_et);
        button_register = findViewById(R.id.button_register);
        button_sign_up_login = findViewById(R.id.button_sign_up_login);
        register_checkBox = findViewById(R.id.register_checkbox);
        progressbar_register = findViewById(R.id.progressbar_register);
        mAuth = FirebaseAuth.getInstance();

        register_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    register_password_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    register_confirmpassword_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    register_password_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    register_confirmpassword_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = register_email_et.getText().toString();
                String pass = register_password_et.getText().toString();
                String cpass = register_confirmpassword_et.getText().toString();

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(pass) ||!TextUtils.isEmpty(cpass))
                {
                    if(pass.equals(cpass))
                    {
                        progressbar_register.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    progressbar_register.setVisibility(View.INVISIBLE);
                                    sendToMain();
                                }else{
                                    String Error = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this,"Error: "+Error, Toast.LENGTH_SHORT);
                                }
                            }
                        });
                    }else{
                        progressbar_register.setVisibility(View.INVISIBLE);
                        Toast.makeText(RegisterActivity.this,"Passwords Do Not Match", Toast.LENGTH_SHORT);
                    }
                }else{
                    Toast.makeText(RegisterActivity.this,"Please Fill All Fields", Toast.LENGTH_SHORT);
                }
            }
        });
        button_sign_up_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendToMain() {
        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}