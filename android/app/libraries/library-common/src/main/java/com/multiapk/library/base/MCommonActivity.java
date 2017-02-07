package com.multiapk.library.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * 可以放任何Fragment的通用Activity 注意:Fragment内所需参数必需通过bundle方式再获取一次，Activity仅做参数的透明传递
 */
@SuppressWarnings("unused")
public class MCommonActivity extends MFragmentActivity {
    protected static final String KEY_THEME = "KEY_THEME";
    protected static final String KEY_FRAGMENT_CLASS = "KEY_FRAGMENT_CLASS";
    protected static final String KEY_FRAGMENT_ARGS = "KEY_FRAGMENT_ARGS";

    @Override
    public void onCreate(Bundle bundle) {
        try {
            Bundle args = getIntent().getExtras();
            //设置主题
            int themResId = args.getInt(KEY_THEME, 0);
            if (themResId > 0)
                setTheme(themResId);
            super.onCreate(bundle);

            setContentView(new FrameLayout(this));

            String fragmentClassName = args.getString(KEY_FRAGMENT_CLASS);
            Fragment fragment = (Fragment) Class.forName(fragmentClassName).newInstance();
            fragment.setArguments(args.getBundle(KEY_FRAGMENT_ARGS));
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment, fragmentClassName).commitAllowingStateLoss();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Has error in new instance of fragment", e);
        }
    }


    public static void start(Activity activity, String fragmentClassName) {
        activity.startActivity(getIntent(activity, 0, fragmentClassName, null));
    }

    public static Intent getIntent(Context context, int themResId, String fragmentClassName, Bundle args) {
        Intent intent = new Intent(context, MCommonActivity.class);
        intent.putExtra(KEY_FRAGMENT_CLASS, fragmentClassName);
        if (args != null)
            intent.putExtra(KEY_FRAGMENT_ARGS, args);
        if (themResId > 0)
            intent.putExtra(KEY_THEME, themResId);
        return intent;
    }
}
