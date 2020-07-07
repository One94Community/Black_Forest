package com.ingteamsofindia.black_forest.Activity.Notification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.ingteamsofindia.black_forest.Activity.Home.HomeActivity;
import com.ingteamsofindia.black_forest.R;
import com.ingteamsofindia.black_forest.Util.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class NotificationActivity extends AppCompatActivity {
    private static final String TAG = "NotificationActivity";
    private Context mContext = NotificationActivity.this;
    private static final int ACTIVITY_NUMBER = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setupBottomNavigationView();
    }
    /* Bottom Navigation View Started... */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: Setting Up Bottom_Navigation_View");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx)findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(this,bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);
    }
}