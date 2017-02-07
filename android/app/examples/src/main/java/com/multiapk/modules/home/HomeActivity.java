package com.multiapk.modules.home;

import android.os.Bundle;
import android.view.View;

import com.multiapk.R;
import com.multiapk.library.base.MCommonActivity;
import com.multiapk.library.base.MFragmentActivity;

public class HomeActivity extends MFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.cardViewComputerModule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MCommonActivity.start(HomeActivity.this, "com.multiapk.modules.computer.ComputerFragment");
            }
        });

        findViewById(R.id.cardViewMobileModule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MCommonActivity.start(HomeActivity.this, "com.multiapk.modules.mobile.MobileFragment");
            }
        });
    }
}