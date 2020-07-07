package com.ingteamsofindia.black_forest.Activity.Profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ingteamsofindia.black_forest.Models.User;
import com.ingteamsofindia.black_forest.Models.UserAccountSettings;
import com.ingteamsofindia.black_forest.Models.UserSettings;
import com.ingteamsofindia.black_forest.R;
import com.ingteamsofindia.black_forest.Util.FirebaseMethods;
import com.ingteamsofindia.black_forest.Util.UniversalImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {
    private static final String TAG = "EditProfileFragment";
    private CircleImageView mProfilePhoto;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;
    private EditText mDisplayName, mUsername, mWebsite, mDescription, mPhoneNumber;

    private TextView mChangeProfilePhoto, mEmail;
    private ProgressBar mProgressBar;

    //vars
    private UserSettings mUserSettings;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_Photo);
        mDisplayName = (EditText) view.findViewById(R.id.editTextFullName);
        mUsername = (EditText) view.findViewById(R.id.editTextUsername);
        mWebsite = (EditText) view.findViewById(R.id.editTextWebsite);
        mDescription = (EditText) view.findViewById(R.id.editTextDescription);
        mEmail = (TextView) view.findViewById(R.id.editTextEmailID);
        mPhoneNumber = (EditText) view.findViewById(R.id.editTextPhone);
        mChangeProfilePhoto = (TextView) view.findViewById(R.id.changePhotoText);

//      back arrow for navigating to back profile
        ImageView backArrow = (ImageView) view.findViewById(R.id.editProfile_back_Arrow);
        mFirebaseMethods = new FirebaseMethods(getActivity());
        setupFirebaseAuth();
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating back to profile Activity");
                getActivity().finish();
            }
        });

        ImageView checkmark = (ImageView) view.findViewById(R.id.saveChange);
        checkmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to save changes.");
                saveProfileSettings();
            }
        });
        return view;
    }

    /**
     * Retrieves the data contained in the widgets and submits it to the database
     * Before donig so it chekcs to make sure the username chosen is unqiue
     */
    private void saveProfileSettings() {
        final String displayName = mDisplayName.getText().toString();
        final String username = mUsername.getText().toString();
        final String website = mWebsite.getText().toString();
        final String description = mDescription.getText().toString();
        final String email = mEmail.getText().toString();
        final long phoneNumber = Long.parseLong(mPhoneNumber.getText().toString());

        //case1: if the user made a change to their Username
        if (!mUserSettings.getUser().getUsername().equals(username)) {

            checkIfUsernameExists(username);
        }
/**
 * change the rest of the settings that do not require uniqueness
 */
        if(!mUserSettings.getSettings().getDisplay_name().equals(displayName)){
            //update displayname
            mFirebaseMethods.updateUserAccountSettings(displayName, null, null, 0);
        }
        if(!mUserSettings.getSettings().getWebsite().equals(website)){
            //update website
            mFirebaseMethods.updateUserAccountSettings(null, website, null, 0);
        }
        if(!mUserSettings.getSettings().getDescription().equals(description)){
            //update description
            mFirebaseMethods.updateUserAccountSettings(null, null, description, 0);
        }
        if(!(mUserSettings.getUser().getPhone_number() == (phoneNumber))){
            //update phoneNumber
            mFirebaseMethods.updateUserAccountSettings(null, null, null, phoneNumber);
        }


    }

    /**
     * Check is @param username already exists in teh database
     *
     * @param username
     */
    private void checkIfUsernameExists(final String username) {
        Log.d(TAG, "checkIfUsernameExists: Checking if  " + username + " already exists.");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.db_name_users))
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    //add the username
                    mFirebaseMethods.updateUsername(username);
                    Toast.makeText(getActivity(), "saved username.", Toast.LENGTH_SHORT).show();

                }
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if (singleSnapshot.exists()) {
                        Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + singleSnapshot.getValue(User.class).getUsername());
                        Toast.makeText(getActivity(), "That username already exists.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void setProfileWidgets(UserSettings userSettings) {
        Log.d(TAG, "setProfileWidgets: Setting Widgets with data retrieving from firebase : " + userSettings.toString());

        mUserSettings = userSettings;
        User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();
        UniversalImageLoader.setImage(settings.getProfile_photo(), mProfilePhoto, null, "");
        mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(settings.getUsername());
        mWebsite.setText(settings.getWebsite());
        mDescription.setText(settings.getDescription());
        mEmail.setText(user.getEmail());
        mPhoneNumber.setText(String.valueOf(user.getPhone_number()));

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

                } else {
                    Log.d(TAG, "setupFirebaseAuth: Signed_out");

                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                // Retrieve user information for the database
                setProfileWidgets(mFirebaseMethods.getUserSettings(datasnapshot));

                // Retrieve image for the user in question

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


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
}
