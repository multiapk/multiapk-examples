package com.mctrip.modules.setting;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mctrip.library.base.MFragment;


public class SettingFragment extends MFragment {

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_setting, null);
        TextView textSetting = (TextView) contentView.findViewById(R.id.textSetting);
        textSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mFragmentActivity, "设置模块", Toast.LENGTH_SHORT).show();
            }
        });
        return contentView;
    }
}
