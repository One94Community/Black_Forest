package com.ingteamsofindia.black_forest.Activity.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.ingteamsofindia.black_forest.R;
import com.ingteamsofindia.black_forest.Util.BottomNavigationViewHelper;
import com.ingteamsofindia.black_forest.Util.GridImageAdapter;
import com.ingteamsofindia.black_forest.Util.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUMBER = 4;
    private static final int NUM_GRID_COLUMNS = 3;
    private Context mContext = ProfileActivity.this;
    private CircleImageView mProfilePhoto;
    private ProgressBar mProgressBar_Profile;
    private TextView mProfileProgressBarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();

//        setupBottomNavigationView();
//        setupToolBar();
//        setupActivityWidgets();
//        setProfilePhotos();
//        tempGridSetup();


    }
    private void init(){
        Log.d(TAG, "init: inflating "+getString(R.string.fragment_Profile));
        ProfileFragment fragment = new ProfileFragment();
        FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,fragment);
        transaction.addToBackStack(getString(R.string.fragment_Profile));
        transaction.commit();

    }

//    private void tempGridSetup(){
//        ArrayList<String> imgURLs = new ArrayList<>();
//        imgURLs.add("https://i.redd.it/9bf67ygj710z.jpg");
//        imgURLs.add("https://www.uni-regensburg.de/Fakultaeten/phil_Fak_II/Psychologie/Psy_II/beautycheck/english/prototypen/m_unsexy.jpg");
//        imgURLs.add("https://i.redd.it/9bf67ygj710z.jpg");
//        imgURLs.add("https://i.redd.it/9bf67ygj710z.jpg");
//        imgURLs.add("https://i.redd.it/9bf67ygj710z.jpg");
//        imgURLs.add("https://i.redd.it/9bf67ygj710z.jpg");
//        imgURLs.add("https://i.redd.it/9bf67ygj710z.jpg");
//        imgURLs.add("https://i.redd.it/9bf67ygj710z.jpg");
//        imgURLs.add("https://i.redd.it/9bf67ygj710z.jpg");

//
//        setupImageGrid(imgURLs);
//    }
//
//    private void setupImageGrid(ArrayList<String> imgURL){
//        GridView gridView =(GridView)findViewById(R.id.grid_view);
//
//        int gridWidth = getResources().getDisplayMetrics().widthPixels;
//        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
//        gridView.setColumnWidth(imageWidth);
//
//        GridImageAdapter adapter = new GridImageAdapter(mContext, R.layout.layout_grid_imageview, "", imgURL);
//        gridView.setAdapter(adapter);
//
//    }
//
//    private void setProfilePhotos(){
//        Log.d(TAG, "setProfilePhotos: setting profile photo.");
//        String imageURL = "www.uni-regensburg.de/Fakultaeten/phil_Fak_II/Psychologie/Psy_II/beautycheck/english/prototypen/w_sexy_gr.jpg";
//        UniversalImageLoader.setImage(imageURL,mProfilePhoto,mProgressBar_Profile,"https://");
//    }
//
//    private void setupToolBar() {
//        MaterialToolbar materialToolbar = (MaterialToolbar)findViewById(R.id.profile_ToolBar);
//        setSupportActionBar(materialToolbar);
//        ImageView profileMenu = (ImageView)findViewById(R.id.profileMenu);
//        profileMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: Navigating to account setting...");
//                Intent intentProfileMenu = new Intent(ProfileActivity.this,ProfileSettingsActivity.class);
//                startActivity(intentProfileMenu);
//
//            }
//        });
//    }
//    private void setupActivityWidgets(){
//        mProfilePhoto = (CircleImageView) findViewById(R.id.profilePhotos);
//        mProgressBar_Profile = (ProgressBar) findViewById(R.id.ProfileProgressBar);
//        mProfileProgressBarText = (TextView) findViewById(R.id.ProfileProgressBar_text);
//
//    }
//    /* Bottom Navigation View Started... */
//    private void setupBottomNavigationView(){
//        Log.d(TAG, "setupBottomNavigationView: Setting Up Bottom_Navigation_View");
//        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx)findViewById(R.id.bottomNavViewBar);
//        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
//        BottomNavigationViewHelper.enableNavigation(this,bottomNavigationViewEx);
//
//        Menu menu = bottomNavigationViewEx.getMenu();
//        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
//        menuItem.setChecked(true);
//    }
}