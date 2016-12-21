package com.mctrip.modules.device;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mctrip.library.base.MCommonActivity;
import com.mctrip.library.base.MFragment;

public class DeviceFragment extends MFragment {
    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_device, null);
        Button buttonAndroid = (Button) contentView.findViewById(R.id.buttonAndroid);
        Button buttonIos = (Button) contentView.findViewById(R.id.buttonIos);
        buttonAndroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MCommonActivity.start(mFragmentActivity, "com.mctrip.modules.device.android.AndroidFragment");
            }
        });
        buttonIos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MCommonActivity.start(mFragmentActivity, "com.mctrip.modules.device.ios.IosFragment");
            }
        });
        return contentView;
    }
}
