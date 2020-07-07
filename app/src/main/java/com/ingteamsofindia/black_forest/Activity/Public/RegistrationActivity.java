package com.ingteamsofindia.black_forest.Activity.Public;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ingteamsofindia.black_forest.R;
import com.ingteamsofindia.black_forest.Util.FirebaseMethods;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = "RegistrationActivity";
    private EditText regEmail, regUsername, regPassword;
    private String email, username, password;
    private TextView mRegProgressText;
    private ProgressBar mRegProgressbar;
    /**
     * Firebase
     **/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private Context mContext;

    private String append = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Log.d(TAG, "onCreate: Started...");
        mAuth = FirebaseAuth.getInstance();
        regEmail = (EditText) findViewById(R.id.editTextRegEmailAddress);
        regUsername = (EditText) findViewById(R.id.editTextRegUsername);
        regPassword = (EditText) findViewById(R.id.editTextRegPassword);
        Button regButton = (Button) findViewById(R.id.reg_button);

        mRegProgressbar = (ProgressBar) findViewById(R.id.regProgress_Bar);
        mRegProgressText = (TextView) findViewById(R.id.regProgress_Text);
        mRegProgressbar.setVisibility(View.GONE);
        mRegProgressText.setVisibility(View.GONE);
        mContext = RegistrationActivity.this;
        firebaseMethods = new FirebaseMethods(mContext);
        setupFirebaseAuth();

        mAuthenticationSignUp();
    }

    private void mAuthenticationSignUp() {
        //        initialize the button for logging in
        Button regButton = (Button) findViewById(R.id.reg_button);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = regEmail.getText().toString();
                username = regUsername.getText().toString();
                password = regPassword.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    mRegProgressbar.setVisibility(View.VISIBLE);
                    mRegProgressText.setVisibility(View.VISIBLE);

                    firebaseMethods.mRegistrationToNewEmail(email, password, username);
                } else {
                    if (TextUtils.isEmpty(email)) {
                        regEmail.setError("Please Enter Valid Email ID.");
                        regEmail.requestFocus();
                    }
                    if (TextUtils.isEmpty(username)) {
                        regUsername.setError("Please Enter Valid Full Name.");
                        regUsername.requestFocus();
                    }
                    if (TextUtils.isEmpty(password)) {
                        regPassword.setError("Please Enter Valid Password.");
                        regPassword.requestFocus();
                    }
                }
            }
        });
    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: Starting...");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "setupFirebaseAuth: SignIn" + user.getUid());
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            if (firebaseMethods.checkIfUsernameExists(username,datasnapshot)){
                                // 1st check: Make sure the username is not already in use.
                                append = myRef.push().getKey().substring(3,10);
                                Log.d(TAG, "onDataChange: username already exists. Appending random string to username :"+append);
                                mRegProgressbar.setVisibility(View.GONE);
                                mRegProgressText.setVisibility(View.GONE);
                            }
                            username = username+append;
                            // add new user to the database
                            firebaseMethods.addNewUser(email,username,"","","",0000000000);
                            Toast.makeText(mContext, "SignUp Successful, Sending Verification Email.", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                            mRegProgressbar.setVisibility(View.GONE);
                            mRegProgressText.setVisibility(View.GONE);
                        }
                    });
                    mRegProgressbar.setVisibility(View.GONE);
                    mRegProgressText.setVisibility(View.GONE);



                } else {
                    Log.d(TAG, "setupFirebaseAuth: Signed_out");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}