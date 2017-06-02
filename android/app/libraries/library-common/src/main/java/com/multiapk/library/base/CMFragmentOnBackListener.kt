package com.multiapk.library.base

interface CMFragmentOnBackListener {
    /**
     * @return true表示事件不再传播，false表示事件继续传播
     */
    fun onBackPressed(): Boolean
}
