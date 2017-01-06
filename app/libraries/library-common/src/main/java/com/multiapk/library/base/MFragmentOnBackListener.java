package com.multiapk.library.base;

public interface MFragmentOnBackListener {
    /**
     * @return true表示事件不再传播，false表示事件继续传播
     */
    boolean onBackPressed();
}
