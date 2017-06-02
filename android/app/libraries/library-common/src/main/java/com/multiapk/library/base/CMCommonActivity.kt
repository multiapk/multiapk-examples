package com.multiapk.library.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.FrameLayout

/**
 * 可以放任何Fragment的通用Activity 注意:Fragment内所需参数必需通过bundle方式再获取一次，Activity仅做参数的透明传递
 */
class CMCommonActivity : CMAppCompatActivity() {

    public override fun onCreate(bundle: Bundle?) {
        try {
            val args = intent.extras
            //设置主题
            val themResId = args.getInt(KEY_THEME, 0)
            if (themResId > 0)
                setTheme(themResId)
            super.onCreate(bundle)

            setContentView(FrameLayout(this))

            val fragmentClassName = args.getString(KEY_FRAGMENT_CLASS)
            val fragment = Class.forName(fragmentClassName).newInstance() as Fragment
            fragment.arguments = args.getBundle(KEY_FRAGMENT_ARGS)
            supportFragmentManager.beginTransaction().add(android.R.id.content, fragment, fragmentClassName).commitAllowingStateLoss()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Has error in new instance of fragment", e)
        }

    }

    companion object {
        protected val KEY_THEME = "KEY_THEME"
        protected val KEY_FRAGMENT_CLASS = "KEY_FRAGMENT_CLASS"
        protected val KEY_FRAGMENT_ARGS = "KEY_FRAGMENT_ARGS"


        fun start(activity: Activity, fragmentClassName: String) {
            activity.startActivity(getIntent(activity, 0, fragmentClassName, null))
        }

        fun getIntent(context: Context, themResId: Int, fragmentClassName: String, args: Bundle?): Intent {
            val intent = Intent(context, CMCommonActivity::class.java)
            intent.putExtra(KEY_FRAGMENT_CLASS, fragmentClassName)
            if (args != null)
                intent.putExtra(KEY_FRAGMENT_ARGS, args)
            if (themResId > 0)
                intent.putExtra(KEY_THEME, themResId)
            return intent
        }
    }
}
