package com.money.algofocus_android_assignment.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.money.algofocus_android_assignment.R;

public class Login extends AppCompatActivity implements View.OnClickListener {


    private static final int RC_SIGN_IN = 234;

    //Tag for the logs optional
    private static final String TAG = "Algofocus";

    Button Submit;
    TextView Error_email, Error_pass,tv_signup;
    EditText Email, Password;

    private LoginButton loginButton;


    private FirebaseAuth Login;
    private FirebaseAuth.AuthStateListener authStateListener;

    private CallbackManager callbackManager;


    ImageView google_login, img_fb;
    ProgressBar progressBar;


    private GoogleSignInClient mGoogleSignInClient;

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        Login = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    goMainScreen();
                }
            }
        };

        findViewById();
        clickListener();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    protected void onStart() {
        super.onStart();


        if (Login.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, PermisionNeed.class));
            finish();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    private void clickListener() {
        Submit.setOnClickListener(this);
        google_login.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        tv_signup.setOnClickListener(this);

    }

    private void findViewById() {
        Submit = findViewById(R.id.login);
        Error_email = findViewById(R.id.tv_err_email);
        Error_pass = findViewById(R.id.tv_err_password);
        Email = findViewById(R.id.et_email);
        Password = findViewById(R.id.et_pass);
        progressBar = findViewById(R.id.progressBar);
        google_login = findViewById(R.id.google_login);
        loginButton = findViewById(R.id.loginButton);
        img_fb = findViewById(R.id.img_fb);
        tv_signup = findViewById(R.id.tv_signup);

    }


    @SuppressLint({"ResourceType", "SetTextI18n"})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login:
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

                } else {

                    progressBar.setVisibility(View.VISIBLE);
                    email = Email.getText().toString();
                    password = Password.getText().toString();
                    LoginUser();


                }
                //Social login button action//
            case R.id.google_login:
                GoogleLogin();

                break;
            case R.id.tv_signup:

                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                finish();

            case R.id.loginButton:


                Facebooklogin();


        }
    }



    private void Facebooklogin() {
        loginButton.setPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                goMainScreen();
                Intent intent = new Intent(Login.this, PermisionNeed.class);
               // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login Cancle", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Login Error", Toast.LENGTH_SHORT).show();
            }
        });


    }




    private void goMainScreen() {
        Intent intent = new Intent(this, PermisionNeed.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }





    private void handleFacebookAccessToken(AccessToken accessToken) {
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        Login.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Error Facebook Login", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void GoogleLogin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(Login.this, "error" +e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
         callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        Login.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = Login.getCurrentUser();

                            Toast.makeText(Login.this, "User Signed In", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, PermisionNeed.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }

    private void LoginUser() {

        Login.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (Password.length() < 8) {
                                Toast.makeText(Login.this, "Password must be atlest 8 character", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Login.this, "Authentication failed, check your email and password or sign up", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Intent intent = new Intent(Login.this, PermisionNeed.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}
