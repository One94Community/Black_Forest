package com.ingteamsofindia.black_forest.Activity.AddPost;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ingteamsofindia.black_forest.R;
import com.ingteamsofindia.black_forest.Util.Permissions;

public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";
    private static final int PHOTO_FRAGMENT = 1;
    private static final int GALLERY_FRAGMENT = 2;
    private static final int CAMERA_REQUEST_CODE = 5002;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        Log.d(TAG, "onCreateView: Started...");
        Button btnLaunchCamera = (Button) view.findViewById(R.id.launchCamera);
        btnLaunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Launching Camera");

                if (((AddPostActivity) getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT) {
                    if (((AddPostActivity) getActivity()).checkPermissions(Permissions.CAMERA_PERMISSIONS[0])) {
                        Log.d(TAG, "onClick: starting camera");
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                    } else {
                        Intent intent = new Intent(getActivity(), AddPostActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE)
            Log.d(TAG, "onActivityResult: Done taking a photo");
            Log.d(TAG, "onActivityResult: attempting to navigate to final share screen.");

            //navigate to the final share screen to publish photo
    }
}
