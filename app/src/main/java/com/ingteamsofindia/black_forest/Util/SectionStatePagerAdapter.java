package com.ingteamsofindia.black_forest.Util;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SectionStatePagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "SectionStatePagerAdapte";
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final HashMap<Fragment, Integer> mFragments = new HashMap<>();
    private final HashMap<String, Integer> mFragmentNumbers = new HashMap<>();
    private final HashMap<Integer, String> mFragmentName = new HashMap<>();

    public SectionStatePagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


    public void addFragment(Fragment fragment, String fragmentName) {
        mFragmentList.add(fragment);
        mFragments.put(fragment, mFragmentList.size() - 1);
        mFragmentNumbers.put(fragmentName, mFragmentList.size() - 1);
        mFragmentName.put(mFragmentList.size() - 1, fragmentName);
    }

    /**
     * return the fragment with the name @param
     *
     * @param fragmentName
     * @param
     **/
    public Integer getFragmentNumber(String fragmentName) {
        if (mFragmentNumbers.containsKey(fragmentName)) {
            return mFragmentNumbers.get(fragmentName);
        } else {
            return null;
        }
    }

    /**
     * return the fragment with the name @param
     *
     * @param fragment
     * @param
     **/
    public Integer getFragmentNumber(Fragment fragment) {
        if (mFragmentNumbers.containsKey(fragment)) {
            return mFragmentNumbers.get(fragment);
        } else {
            return null;
        }
    }

    /**
     * return the fragment with the name @param
     *
     * @param fragmentNumber
     * @param
     **/
    public String getFragmentName(Integer fragmentNumber) {
        if (mFragmentName.containsKey(fragmentNumber)) {
            return mFragmentName.get(fragmentNumber);
        } else {
            return null;
        }
    }

}
