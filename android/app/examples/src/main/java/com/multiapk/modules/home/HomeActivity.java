package com.multiapk.modules.home;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import com.jakewharton.rxbinding2.view.RxView;
import com.multiapk.R;
import com.multiapk.library.base.MCommonActivity;
import com.multiapk.library.base.MFragmentActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class HomeActivity extends MFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RxView.clicks(findViewById(R.id.cardViewComputerModule)).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                Log.d("krmao", "accept");
                MCommonActivity.start(HomeActivity.this, "com.multiapk.modules.computer.ComputerFragment");
            }
        });

        findViewById(R.id.cardViewMobileModule).setOnClickListener(view -> MCommonActivity.start(HomeActivity.this, "com.multiapk.modules.mobile.MobileFragment"));

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS)
                .subscribe(granted -> {
                    if (granted) {
                        // All permissions were granted//
                        Log.d("krmao", "All permissions were granted");
                    } else {
                        //One or more permissions was denied//
                        Log.d("krmao", "One or more permissions was denied");
                    }
                });
        rxPermissions.setLogging(true);
        rxPermissions.requestEach(Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE)
                .subscribe(permission -> { // will emit 2 Permission objects
                    if (permission.granted) {
                        // `permission.name` is granted !
                        Log.d("krmao", "permission:" + permission.name + " is granted");
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // Denied permission without ask never again
                        Log.d("krmao", "Denied permission without ask never again");
                        //rxPermissions.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE);
                    } else {
                        // Denied permission with ask never again
                        // Need to go to the settings
                        Log.d("krmao", "Need to go to the settings");
                    }
                });
    }
}