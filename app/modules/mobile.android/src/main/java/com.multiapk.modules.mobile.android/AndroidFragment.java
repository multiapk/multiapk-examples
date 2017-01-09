package com.multiapk.modules.mobile.android;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mlibrary.multiapk.core.hotpatch.Hotpatch;
import com.multiapk.library.base.MFragment;

import java.io.File;

public class AndroidFragment extends MFragment {
    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_android, null);
        Button buttonAndroid = (Button) contentView.findViewById(R.id.buttonAndroid);
        buttonAndroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Hotpatch.instance.installPatch("com.multiapk.modules.mobile.ios", 1, new File("/storage/emulated/0/ios.patch"));
                    Toast.makeText(mFragmentActivity, "合成成功", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mFragmentActivity, "合成失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return contentView;
    }
}
