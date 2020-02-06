package com.money.algofocus_android_assignment.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.money.algofocus_android_assignment.R;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    Button Submit;
    TextView Error_email, Error_pass, Erroe_conpass, tv_login;
    EditText Email, Password, Confirmpass;
    FirebaseAuth Signup;
    ProgressBar progressBar;

    private String email;
    private String password;


    private static String emailPattern = "[a-zA-Z+{n}0-9._-]+@[a-z]+\\.+[a-z]+";
    private static String passwordPattern = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{8,}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Signup = FirebaseAuth.getInstance();

        //find and set lister on layout component//
        findViewById();
        clickListener();
    }





    private void clickListener() {
        Submit.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }

    private void findViewById() {
        Submit = findViewById(R.id.Register);
        Error_email = findViewById(R.id.tv_err_email);
        Error_pass = findViewById(R.id.tv_err_password);
        Erroe_conpass = findViewById(R.id.tv_err_cnfnpassword);
        Email = findViewById(R.id.et_email);
        Password = findViewById(R.id.et_pass);
        Confirmpass = findViewById(R.id.et_confirm_pass);
        progressBar = findViewById(R.id.progressBar);
        tv_login = findViewById(R.id.tv_login);



            }




    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.Register:
                if (Email.getText().toString().isEmpty()) {
                    Error_email.setText("Email is required");
                    Email.setBackgroundResource(R.xml.btn_error_red);
                    Email.requestFocus();
                    Email.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {
                        }

                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        @SuppressLint("ResourceType")
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            Error_email.setText("");
                            Email.setBackgroundResource(R.xml.edt_text_gray_border);
                        }
                    });
                    return;
                } else if (!Email.getText().toString().matches(emailPattern)) {
                    Error_email.setText("Invalid Email");
                    Email.setBackgroundResource(R.xml.btn_error_red);
                    Email.requestFocus();
                    Email.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {
                        }

                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        @SuppressLint("ResourceType")
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            Error_email.setText("");
                            Email.setBackgroundResource(R.xml.edt_text_gray_border);
                        }
                    });
                    return;
                } else if (Password.getText().toString().isEmpty()) {
                    Error_pass.setText("Password is required");
                    Password.setBackgroundResource(R.xml.btn_error_red);
                    Password.requestFocus();
                    Password.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {
                        }

                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        @SuppressLint("ResourceType")
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            Error_pass.setText("");
                            Password.setBackgroundResource(R.xml.edt_text_gray_border);
                        }
                    });
                    return;
                } else if (!Password.getText().toString().matches(passwordPattern)) {
                    Error_pass.setText("Invalid Password");
                    Password.setBackgroundResource(R.xml.btn_error_red);
                    Password.requestFocus();
                    Password.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {
                        }

                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        @SuppressLint("ResourceType")
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            Error_pass.setText("");
                            Password.setBackgroundResource(R.xml.edt_text_gray_border);
                        }
                    });
                    return;

                } else if (Confirmpass.getText().toString().isEmpty()) {
                    Erroe_conpass.setText("Confirm Password is required");
                    Confirmpass.setBackgroundResource(R.xml.btn_error_red);
                    Confirmpass.requestFocus();
                    Confirmpass.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {
                        }

                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        @SuppressLint("ResourceType")
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            Erroe_conpass.setText("");
                            Confirmpass.setBackgroundResource(R.xml.edt_text_gray_border);
                        }
                    });
                    return;
                } else if (!Confirmpass.getText().toString().matches(passwordPattern)) {
                    Erroe_conpass.setText("Invalid Password");
                    Confirmpass.requestFocus();
                    Confirmpass.setBackgroundResource(R.xml.btn_error_red);
                    Confirmpass.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {
                        }

                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        @SuppressLint("ResourceType")
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            Erroe_conpass.setText("");
                            Confirmpass.setBackgroundResource(R.xml.edt_text_gray_border);
                        }
                    });
                    return;

                } else if (!Confirmpass.getText().toString().equals(Password.getText().toString())) {
                    Toast.makeText(this, "Password not Match", Toast.LENGTH_LONG).show();
                    Confirmpass.requestFocus();
                    return;
                } else {

                    progressBar.setVisibility(View.VISIBLE);
                    email = Email.getText().toString();
                    password = Password.getText().toString();
                    SignUpUser();

                }
                break;
            case R.id.tv_login:
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();


        }
    }


    private void SignUpUser() {

        Signup.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignUp.this, "Sign Up successful" , Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Authentication failed:" + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    }
