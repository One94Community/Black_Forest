package com.ingteamsofindia.black_forest.Util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ingteamsofindia.black_forest.Activity.AddPost.AddPostActivity;
import com.ingteamsofindia.black_forest.Activity.Home.HomeActivity;
import com.ingteamsofindia.black_forest.Activity.Notification.NotificationActivity;
import com.ingteamsofindia.black_forest.Activity.Profile.ProfileActivity;
import com.ingteamsofindia.black_forest.R;
import com.ingteamsofindia.black_forest.Activity.Search.SearchActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";
    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d(TAG, "setupBottomNavigationView: Setting up bottom Navigation_View");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
        bottomNavigationViewEx.enableShiftingMode(false);
    }
    public static void enableNavigation(final Context context, BottomNavigationViewEx viewEx){
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_Home:
                        Intent intent1 = new Intent(context, HomeActivity.class);
                        //ACTIVITY_NUM = 0
                        context.startActivity(intent1);
                        break;
                    case R.id.ic_Search:
                        Intent intent2 = new Intent(context, SearchActivity.class);
                        //ACTIVITY_NUM = 1
                        context.startActivity(intent2);
                        break;
                    case R.id.ic_AddPost:
                        Intent intent3 = new Intent(context, AddPostActivity.class);
                        //ACTIVITY_NUM = 2
                        context.startActivity(intent3);
                        break;
                    case R.id.ic_Notification:
                        Intent intent4 = new Intent(context, NotificationActivity.class);
                        //ACTIVITY_NUM = 3
                        context.startActivity(intent4);
                        break;
                    case R.id.ic_Profile:
                        Intent intent5 = new Intent(context, ProfileActivity.class);
                        //ACTIVITY_NUM = 4
                        context.startActivity(intent5);
                        break;
                }
                return false;
            }
        });
    }
}
