package org.smartrobot.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.FrameLayout

open class DefaultActivity : DefaultBaseActivity() {

    companion object {
        val KEY_THEME = "KEY_THEME"
        val KEY_FRAGMENT_CLASS = "KEY_FRAGMENT_CLASS"
        val KEY_FRAGMENT_ARGS = "KEY_FRAGMENT_ARGS"

        fun start(from: Activity, fragmentClass: Class<*>) {
            start(from, fragmentClass, null)
        }

        fun start(from: Activity, fragmentClass: Class<*>, args: Bundle?) {
            start(from, fragmentClass, args, 0)
        }

        fun startNewTask(fragmentClass: Class<*>, args: Bundle?) {
            DefaultBaseApplication.instance.startActivity(getNewTaskIntent(DefaultBaseApplication.instance, 0, fragmentClass, args))
        }

        fun startSingleTask(from: Activity, fragmentClass: Class<*>, args: Bundle) {
            DefaultBaseApplication.instance.startActivity(getSingleTaskIntent(from, 0, fragmentClass, args))
        }

        fun start(activity: Activity, fragmentClass: Class<*>, args: Bundle?, themResId: Int) {
            activity.startActivity(getIntent(activity, themResId, fragmentClass, args))
        }

        fun start(activity: Activity, intent: Intent?) {
            activity.startActivity(intent)
        }

        fun start(activity: Activity, fragmentClassName: String) {
            activity.startActivity(getIntent(activity, 0, fragmentClassName, null))
        }

        fun startByCustomAnimation(activity: Activity, fragmentClass: Class<*>, args: Bundle, enterAnim: Int, exitAnim: Int) {
            activity.startActivity(getIntent(activity, fragmentClass, args))
            activity.overridePendingTransition(enterAnim, exitAnim)
        }


        fun startForResultByCustomAnimation(activity: Activity, requestCode: Int, fragmentClass: Class<*>, args: Bundle, enterAnim: Int, exitAnim: Int) {
            startForResultByCustomAnimation(activity, 0, requestCode, fragmentClass, args, enterAnim, exitAnim)
        }

        fun startForResultByCustomAnimation(activity: Activity, themResId: Int, requestCode: Int, fragmentClass: Class<*>, args: Bundle, enterAnim: Int, exitAnim: Int) {
            startForResult(activity, themResId, requestCode, fragmentClass, args)
            activity.overridePendingTransition(enterAnim, exitAnim)
        }

        fun startForResultByCustomAnimation(fragment: Fragment, requestCode: Int, fragmentClass: Class<*>, args: Bundle, enterAnim: Int, exitAnim: Int) {
            startForResultByCustomAnimation(fragment, 0, requestCode, fragmentClass, args, enterAnim, exitAnim)
        }

        fun startForResultByCustomAnimation(fragment: Fragment, themResId: Int, requestCode: Int, fragmentClass: Class<*>, args: Bundle, enterAnim: Int, exitAnim: Int) {
            startForResult(fragment, themResId, requestCode, fragmentClass, args)
            fragment.activity.overridePendingTransition(enterAnim, exitAnim)
        }

        //================================//================================//================================
        //base
        /* Activity页面发起的，再由Activity来接收结果 如果由Fragment来接收结果，需要使用 {@link #startForResult(Fragment, int, Class, Bundle)} */
        fun startForResult(activity: Activity, reqCode: Int, fragmentClass: Class<*>, args: Bundle) {
            startForResult(activity, 0, reqCode, fragmentClass, args)
        }

        fun startForResult(activity: Activity, themResId: Int, reqCode: Int, fragmentClass: Class<*>, args: Bundle) {
            activity.startActivityForResult(getIntent(activity, themResId, fragmentClass, args), reqCode)
        }

        /* 由Fragment页面发起的，再由Fragment接收结果 */
        fun startForResult(fragment: Fragment, reqCode: Int, fragmentClass: Class<*>, args: Bundle) {
            startForResult(fragment, 0, reqCode, fragmentClass, args)
        }

        /* 由Fragment页面发起的，再由Fragment接收结果 */
        fun startForResult(fragment: Fragment, themResId: Int, reqCode: Int, fragmentClass: Class<*>, args: Bundle) {
            fragment.startActivityForResult(getIntent(fragment.activity, themResId, fragmentClass, args), reqCode)
        }

        fun getIntent(context: Context, fragmentClass: Class<*>, args: Bundle): Intent {
            return getIntent(context, 0, fragmentClass, args)
        }

        fun getIntent(context: Context, themResId: Int, fragmentClass: Class<*>, args: Bundle?): Intent {
            return getIntent(context, themResId, fragmentClass.canonicalName, args)
        }

        fun getIntent(context: Context, themResId: Int, fragmentClassName: String, args: Bundle?): Intent {
            val intent = Intent(context, DefaultActivity::class.java)
            intent.putExtra(KEY_FRAGMENT_CLASS, fragmentClassName)
            if (args != null)
                intent.putExtra(KEY_FRAGMENT_ARGS, args)
            if (themResId > 0)
                intent.putExtra(KEY_THEME, themResId)
            return intent
        }

        fun getNewTaskIntent(context: Context, themResId: Int, fragmentClass: Class<*>, args: Bundle?): Intent {
            val intent = Intent(context, DefaultActivity::class.java)
            intent.putExtra(KEY_FRAGMENT_CLASS, fragmentClass.canonicalName)
            if (args != null)
                intent.putExtra(KEY_FRAGMENT_ARGS, args)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (themResId > 0)
                intent.putExtra(KEY_THEME, themResId)
            return intent
        }

        fun getSingleTaskIntent(context: Context, themResId: Int, fragmentClass: Class<*>, args: Bundle?): Intent {
            val intent = Intent(context, DefaultActivity::class.java)
            intent.putExtra(KEY_FRAGMENT_CLASS, fragmentClass.canonicalName)
            if (args != null)
                intent.putExtra(KEY_FRAGMENT_ARGS, args)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            if (themResId > 0)
                intent.putExtra(KEY_THEME, themResId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            val args = intent.extras
            //设置主题
            val themResId = args.getInt(KEY_THEME, 0)
            if (themResId > 0)
                setTheme(themResId)
            super.onCreate(savedInstanceState)

            setContentView(FrameLayout(this))

            val fragmentClassName = args.getString(KEY_FRAGMENT_CLASS)
            val fragment = Class.forName(fragmentClassName).newInstance() as Fragment
            fragment.arguments = args.getBundle(KEY_FRAGMENT_ARGS)
            supportFragmentManager.beginTransaction().add(android.R.id.content, fragment, fragmentClassName).commitAllowingStateLoss()
        } catch (e: Exception) {
            Log.e(DefaultActivity::javaClass.name, "Has error in new instance of fragment", e)
        }
    }
}