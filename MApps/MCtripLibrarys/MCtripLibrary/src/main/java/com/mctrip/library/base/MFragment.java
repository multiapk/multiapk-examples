package com.mctrip.library.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class MFragment extends Fragment {
    protected FragmentActivity mFragmentActivity;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentActivity = (FragmentActivity) activity;
    }
}
