package com.mctrip.modules.home;

import android.os.Bundle;
import android.view.View;

import com.mctrip.R;
import com.mctrip.library.base.MCommonActivity;
import com.mctrip.library.base.MFragmentActivity;

public class HomeActivity extends MFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.cardViewMineModule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MCommonActivity.start(HomeActivity.this, "com.mctrip.modules.mine.MineFragment");
            }
        });
        findViewById(R.id.cardViewSettingModule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MCommonActivity.start(HomeActivity.this, "com.mctrip.modules.setting.SettingFragment");
            }
        });
        findViewById(R.id.cardViewHospitalModule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MCommonActivity.start(HomeActivity.this, "com.mctrip.modules.hospital.HospitalFragment");
            }
        });
        findViewById(R.id.cardViewDeviceModule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MCommonActivity.start(HomeActivity.this, "com.mctrip.modules.device.DeviceFragment");
            }
        });
    }
}