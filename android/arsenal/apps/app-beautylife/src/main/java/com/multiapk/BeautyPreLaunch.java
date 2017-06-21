package com.multiapk;

import android.content.Context;
import android.taobao.atlas.runtime.AtlasPreLauncher;
import android.util.Log;

public class BeautyPreLaunch implements AtlasPreLauncher {
    @Override
    public void initBeforeAtlas(Context context) {
        Log.d("krmao", "prelaunch invokded");
    }
}
