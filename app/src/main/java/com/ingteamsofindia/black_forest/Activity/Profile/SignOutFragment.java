package com.ingteamsofindia.black_forest.Activity.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ingteamsofindia.black_forest.Activity.Home.HomeActivity;
import com.ingteamsofindia.black_forest.Activity.Public.LoginActivity;
import com.ingteamsofindia.black_forest.R;

public class SignOutFragment extends Fragment {
    private static final String TAG = "SignOutFragment";
    /* Firebase */
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView tvSignOut;
    private ProgressBar mProgress_bar;
    private TextView mProgress_Text;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_out, container, false);
        tvSignOut = (TextView)view.findViewById(R.id.tvConfirmSignOut);
        mProgress_bar = (ProgressBar)view.findViewById(R.id.signOutProgress_Bar);
        mProgress_Text = (TextView)view.findViewById(R.id.signOutProgress_Text);

        mProgress_bar.setVisibility(View.GONE);
        mProgress_Text.setVisibility(View.GONE);

        Button button_cancel = (Button)view.findViewById(R.id.button_cancel);
        Button button_sign_out = (Button)view.findViewById(R.id.button_sign_out);

        setupFirebaseAuth();

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        button_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress_bar.setVisibility(View.VISIBLE);
                mProgress_Text.setVisibility(View.VISIBLE);
                mAuth.signOut();
                getActivity().finish();
            }
        });

        return view;
    }
    /* ------------------------------- Firebase ------------------------------- */
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: Starting...");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "setupFirebaseAuth: SignIn" + user.getUid());
                } else {
                    Log.d(TAG, "setupFirebaseAuth: Signed_out");
                    mProgress_bar.setVisibility(View.GONE);
                    mProgress_Text.setVisibility(View.GONE);
                    sendUserToTheLoginActivity();
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void sendUserToTheLoginActivity() {
        Intent loginIntent = new Intent(getActivity(),LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        getActivity().finish();
    }
}