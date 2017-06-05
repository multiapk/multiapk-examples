package org.smartrobot.base

import android.support.v4.app.Fragment

open class DefaultBaseFragment : Fragment() {

    interface OnBackPressedListener {
        /**
         * @return true表示事件不再传播，false表示事件继续传播
         */
        fun onBackPressed(): Boolean
    }

}
