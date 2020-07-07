package com.ingteamsofindia.black_forest.Activity.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ingteamsofindia.black_forest.Activity.Home.HomeActivity;
import com.ingteamsofindia.black_forest.Activity.Public.LoginActivity;
import com.ingteamsofindia.black_forest.R;
import com.ingteamsofindia.black_forest.Util.BottomNavigationViewHelper;
import com.ingteamsofindia.black_forest.Util.FirebaseMethods;
import com.ingteamsofindia.black_forest.Util.SectionStatePagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

public class ProfileSettingsActivity extends AppCompatActivity {
    private static final String TAG = "ProfileAccountSettingAc";
    private static final int ACTIVITY_NUMBER = 4;
    private SectionStatePagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private RelativeLayout mRelativeLayout;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        Log.d(TAG, "onCreate: Started...");
        setupBottomNavigationView();
        mViewPager = (ViewPager) findViewById(R.id.container);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relLayout_Main);

        setupSettingsList();
        setupFragments();
        setupBackArrow();
        getIntentIncoming();
    }
    protected void getIntentIncoming(){
        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.calling_activity))){
            setViewPager(pagerAdapter.getFragmentNumber(getString(R.string.fragment_edit_profile)));
        }
    }

    private void setupFragments() {
        Log.d(TAG, "setupFragments: Started...");
        pagerAdapter = new SectionStatePagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragment(new EditProfileFragment(),getString(R.string.fragment_edit_profile));
        pagerAdapter.addFragment(new SignOutFragment(),getString(R.string.fragment_signOut));
    }

    private void setViewPager(int fragmentNumber){
        mRelativeLayout.setVisibility(View.GONE);
        Log.d(TAG, "setViewPager: navigating to fragment # "+fragmentNumber);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(fragmentNumber);
    }


    private void setupBackArrow() {
        ImageView backArrow = (ImageView) findViewById(R.id.editProfile_back_Arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating back to Profile ProfileAccount");
                finish();
            }
        });
    }

    private void setupSettingsList() {
        Log.d(TAG, "setupSettingsList: Initializing AccountSettings List");
        ListView listView = (ListView) findViewById(R.id.lvAccountSetting);
        ArrayList<String> options = new ArrayList<>();
        options.add(getString(R.string.fragment_edit_profile));
        options.add(getString(R.string.fragment_signOut));

        ArrayAdapter adapter = new ArrayAdapter(ProfileSettingsActivity.this, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d(TAG, "onItemClick: navigating to fragment :"+ position);
                setViewPager(position);
            }
        });
    }

    /* Bottom Navigation View Started... */
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: Setting Up Bottom_Navigation_View");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(this, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);
    }

}