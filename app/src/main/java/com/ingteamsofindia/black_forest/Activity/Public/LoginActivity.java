package com.ingteamsofindia.black_forest.Activity.Public;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ingteamsofindia.black_forest.Activity.Home.HomeActivity;
import com.ingteamsofindia.black_forest.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText loginEmail,loginPassword;

    private TextView mLoginProgressText;
    private ProgressBar mLoginProgressbar;

    //    Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: Started...");

        loginEmail = (EditText)findViewById(R.id.editTextLoginEmailAddress);
        loginPassword = (EditText)findViewById(R.id.editTextLoginPassword);

        mLoginProgressText = (TextView) findViewById(R.id.loginProgress_Text);
        mLoginProgressbar = (ProgressBar)findViewById(R.id.loginProgress_Bar);
        mLoginProgressbar.setVisibility(View.GONE);
        mLoginProgressText.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();

        setupRegistrationLink();
        mAuthenticationSignIn();
    }

    private void mAuthenticationSignIn(){
        //        initialize the button for logging in
        Button loginButton = (Button)findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    mLoginProgressbar.setVisibility(View.VISIBLE);
                    mLoginProgressText.setVisibility(View.VISIBLE);
                    mSignInWithEmailAndPassword(email, password);
                } else {
                    if (TextUtils.isEmpty(email)) {
                        loginEmail.setError("Please Enter Valid Email ID.");
                        loginEmail.requestFocus();
                    }
                    if (TextUtils.isEmpty(password)) {
                        loginPassword.setError("Please Enter Valid Password.");
                        loginPassword.requestFocus();
                    }
                }
            }
        });
    }

    private void mSignInWithEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d(TAG, "onSuccess: Login Success");
                FirebaseUser user = mAuth.getCurrentUser();
                try {
                    if (user.isEmailVerified()){
                        Log.d(TAG, "onSuccess: Successful Email is Verified.");
                        sendUserToTheHomeActivity();
                    }else{
                        Toast.makeText(LoginActivity.this, "Email is not Verified \nCheck your Inbox.", Toast.LENGTH_LONG).show();
                        mLoginProgressbar.setVisibility(View.GONE);
                        mLoginProgressText.setVisibility(View.GONE);
                        mAuth.signOut();
                    }
                }catch (NullPointerException e){
                    Log.e(TAG, "onSuccess: NullPointerException :"+e.getMessage());
                }
                mLoginProgressbar.setVisibility(View.GONE);
                mLoginProgressText.setVisibility(View.GONE);
                sendUserToTheHomeActivity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Login Failure");
                mLoginProgressbar.setVisibility(View.GONE);
                mLoginProgressText.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                //Part 27
            }
        });
    }

    private void sendUserToTheHomeActivity() {
        Intent loginIntent = new Intent(LoginActivity.this,HomeActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }


    private void setupRegistrationLink() {
        Log.d(TAG, "setupRegistrationLink: Started");
        TextView mLinkToRegistration = (TextView)findViewById(R.id.mRegistrationLink);
        mLinkToRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(regIntent);
            }
        });
    }
}