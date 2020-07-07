package com.ingteamsofindia.black_forest.Activity.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ingteamsofindia.black_forest.Activity.Public.LoginActivity;
import com.ingteamsofindia.black_forest.Models.User;
import com.ingteamsofindia.black_forest.Models.UserAccountSettings;
import com.ingteamsofindia.black_forest.Models.UserSettings;
import com.ingteamsofindia.black_forest.R;
import com.ingteamsofindia.black_forest.Util.BottomNavigationViewHelper;
import com.ingteamsofindia.black_forest.Util.FirebaseMethods;
import com.ingteamsofindia.black_forest.Util.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private static final int ACTIVITY_NUMBER = 4;
    private static final int NUM_GRID_COLUMNS = 3;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    private TextView mPosts, mFollowers, mFollowing, mDisplayName, mUsername, mWebsite, mDescription, mProgressText;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private GridView mGridView;
    private MaterialToolbar mToolBar;
    private ImageView mProfileMenu;
    private BottomNavigationViewEx mBottomNavigationViewEx;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Log.d(TAG, "onCreateView: Starting...");
        mPosts = (TextView) view.findViewById(R.id.tvPosts);
        mFollowers = (TextView) view.findViewById(R.id.tvFollowers);
        mFollowing = (TextView) view.findViewById(R.id.tvFollowing);
        mDisplayName = (TextView) view.findViewById(R.id.tvProfileDisplayName);
        mUsername = (TextView) view.findViewById(R.id.tvProfileUsername);
        mWebsite = (TextView) view.findViewById(R.id.tvProfileWebsite);
        mDescription = (TextView) view.findViewById(R.id.tvProfileDescription);
        mProgressText = (TextView) view.findViewById(R.id.ProfileProgressBar_text);
        mProgressBar = (ProgressBar) view.findViewById(R.id.ProfileProgressBar);
        mProfilePhoto = (CircleImageView) view.findViewById(R.id.mCircleProfilePhoto);
        mGridView = (GridView) view.findViewById(R.id.mProfileGrid_view);
        mToolBar = (MaterialToolbar) view.findViewById(R.id.profile_ToolBar);
        mProfileMenu = (ImageView) view.findViewById(R.id.profileMenu);
        mBottomNavigationViewEx = (BottomNavigationViewEx) view.findViewById(R.id.bottomNavViewBar);
        mContext = getActivity();
        mFirebaseMethods = new FirebaseMethods(getActivity());
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressText.setVisibility(View.VISIBLE);
        setupFirebaseAuth();
        setupBottomNavigationView();
        setupToolBar();
        TextView editProfile = (TextView)view.findViewById(R.id.textEditProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to "+mContext.getString(R.string.fragment_edit_profile));
                Intent intent = new Intent(getActivity(),ProfileSettingsActivity.class);
                intent.putExtra(getString(R.string.calling_activity),getString(R.string.profile_activity));
                startActivity(intent);
            }
        });
        return view;
    }

    private void setProfileWidgets(UserSettings userSettings){
        Log.d(TAG, "setProfileWidgets: Setting Widgets with data retrieving from firebase : "+userSettings.toString());
        User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();
        UniversalImageLoader.setImage(settings.getProfile_photo(),mProfilePhoto,null,"");
        mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(settings.getUsername());
        mWebsite.setText(settings.getWebsite());
        mDescription.setText(settings.getDescription());
        mPosts.setText(String.valueOf(settings.getPosts()));
        mFollowers.setText(String.valueOf(settings.getFollowers()));
        mFollowing.setText(String.valueOf(settings.getFollowing()));
    }
    /* Toolbar Started... */
    private void setupToolBar() {
        ((ProfileActivity) getActivity()).setSupportActionBar(mToolBar);
        mProfileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating to account setting...");
                Intent intentProfileMenu = new Intent(mContext, ProfileSettingsActivity.class);
                startActivity(intentProfileMenu);

            }
        });
    }

    /* Bottom Navigation View Started... */
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: Setting Up Bottom_Navigation_View");

        BottomNavigationViewHelper.setupBottomNavigationView(mBottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, mBottomNavigationViewEx);

        Menu menu = mBottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);
    }

    /* ------------------------------- Firebase ------------------------------- */

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
                    mProgressBar.setVisibility(View.GONE);
                    mProgressText.setVisibility(View.GONE);
                } else {
                    Log.d(TAG, "setupFirebaseAuth: Signed_out");
                    mProgressBar.setVisibility(View.GONE);
                    mProgressText.setVisibility(View.GONE);
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                // Retrieve user information for the database
                setProfileWidgets( mFirebaseMethods.getUserSettings(datasnapshot));

                // Retrieve image for the user in question

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mProgressBar.setVisibility(View.GONE);
                mProgressText.setVisibility(View.GONE);


            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void sendUserToTheLoginActivity() {
        Intent loginIntent = new Intent(mContext, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        getActivity().finish();
    }
}
