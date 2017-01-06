package com.multiapk.library.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MFragmentActivity extends FragmentActivity {

    protected View getContentView() {
        return ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            getWindow().getDecorView().clearAnimation();

            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if (fragments != null && fragments.size() > 0) {
                Fragment fragment = fragments.get(0);
                if (fragment instanceof MFragmentOnBackListener) {
                    boolean canPropagate = ((MFragmentOnBackListener) fragment).onBackPressed();
                    if (canPropagate) {
                        try {
                            getSupportFragmentManager().popBackStackImmediate();
                        } catch (Exception ignored) {
                        }
                        return true;
                    }
                }
            }
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                finish();
            } else {
                try {
                    getSupportFragmentManager().popBackStackImmediate();
                } catch (Exception ignored) {
                }
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
