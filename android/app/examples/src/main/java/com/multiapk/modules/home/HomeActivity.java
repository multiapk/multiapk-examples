package com.multiapk.modules.home;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.multiapk.R;
import com.multiapk.library.base.MCommonActivity;
import com.multiapk.library.base.MFragmentActivity;
import com.tbruyelle.rxpermissions2.Permission;
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
                Toast.makeText(HomeActivity.this,new AutoValue_HomeModel("address-shanghai", "name-mkrcpp", "area-5000").toString(),Toast.LENGTH_SHORT).show();
                MCommonActivity.start(HomeActivity.this, "com.multiapk.modules.computer.ComputerFragment");
            }
        });

        Log.e("krmao", new AutoValue_HomeModel("address-soho", "name-krmao", "area-180").toString());
        findViewById(R.id.cardViewMobileModule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MCommonActivity.start(HomeActivity.this, "com.multiapk.modules.mobile.MobileFragment");
            }
        });

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {
                        if (granted) {
                            // All permissions were granted//
                            Log.d("krmao", "All permissions were granted");
                        } else {
                            //One or more permissions was denied//
                            Log.d("krmao", "One or more permissions was denied");
                        }
                    }
                });
        rxPermissions.setLogging(true);
        rxPermissions.requestEach(Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(@NonNull Permission permission) throws Exception {
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
                    }
                });
    }
}