package com.ingteamsofindia.black_forest.Activity.AddPost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.ingteamsofindia.black_forest.Activity.Home.CameraFragment;
import com.ingteamsofindia.black_forest.Activity.Home.HomeActivity;
import com.ingteamsofindia.black_forest.R;
import com.ingteamsofindia.black_forest.Util.BottomNavigationViewHelper;
import com.ingteamsofindia.black_forest.Util.Permissions;
import com.ingteamsofindia.black_forest.Util.SectionPagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class AddPostActivity extends AppCompatActivity {
    private static final String TAG = "AddPostActivity";
    private Context mContext = AddPostActivity.this;
    private static final int ACTIVITY_NUMBER = 2;
    private static final int VERIFY_PERMISSION_REQUEST = 1;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        if (checkPermissionArray(Permissions.PERMISSIONS)){
            setupViewPager();

        }else {
            verifyPermissions(Permissions.PERMISSIONS);
        }
//        setupBottomNavigationView();
    }

    /**
     * return the current tab number
     * 0 = GalleryFragment
     * 1 = PhotoFragment
     * @return
     */
    public int getCurrentTabNumber(){
        return mViewPager.getCurrentItem();
    }
    private void setupViewPager(){
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new PhotoFragment());
        mViewPager = (ViewPager)findViewById(R.id.container);
        mViewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setText(R.string.gallery);
        tabLayout.getTabAt(1).setText(R.string.photos);
    }

    /*Verify all the permissions pass*/
    private void verifyPermissions(String[] permissions) {
        Log.d(TAG, "verifyPermissions: verifyPermissions...");
        ActivityCompat.requestPermissions(AddPostActivity.this,permissions,VERIFY_PERMISSION_REQUEST);
    }

    private boolean checkPermissionArray(String[] permissions) {
        Log.d(TAG, "checkPermissionArray: checkPermissionArray");
        for (int i=0; i<permissions.length;i++){
            String check = permissions[i];
            if (!checkPermissions(check)){
                return false;
            }
        }
        return true;

    }

    boolean checkPermissions(String permission) {
        Log.d(TAG, "checkPermissions: checkPermissions"+permission);
        int permissionRequest = ActivityCompat.checkSelfPermission(AddPostActivity.this, permission);
        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermissions: Permissions are not Granted" + permission);
            return false;
        }else {
            Log.d(TAG, "checkPermissions: Permissions Was Granted" + permission);
            return true;
        }
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