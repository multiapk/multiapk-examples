package com.multiapk.modules.home;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.common.collect.Lists;
import com.multiapk.R;
import com.multiapk.library.base.MCommonActivity;
import com.multiapk.library.base.MFragmentActivity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.functions.IntFunction;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity extends MFragmentActivity implements EasyPermissions.PermissionCallbacks {

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

        EasyPermissions.requestPermissions(this, "我们需要相机和录音权限1", RC_CAMERA_AND_LOCATION, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO);
        Log.d("krmao", "requestPermissions 执行请求权限 CAMERA + RECORD_AUDIO");

        test();
    }

    private void test() {
        List<Integer> dataList = Lists.newArrayList(1, 3, 2, 4);


        Subscriber<Integer> observer = new Subscriber<Integer>() {

            @Override
            public void onSubscribe(Subscription s) {
                Log.v("maomao", "Observer.onSubscribe:" + s.toString());
                s.request(8);
            }

            @Override
            public void onNext(@NonNull Integer o) {
                Log.d("maomao", "Observer.onNext:" + o);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("maomao", "Observer.onError:", e);
            }

            @Override
            public void onComplete() {
                Log.w("maomao", "Observer.onComplete");
            }
        };

        Flowable.just(1, 2, 3, 4, 5, 6, 7, 8).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@NonNull Integer integer) throws Exception {
                return integer % 2 == 0;
            }
        }).map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(@NonNull Integer integer) throws Exception {
                return integer * 10;
            }
        }).subscribe(observer);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        Log.d("krmao", "onRequestPermissionsResult");
        for (int i = 0; i < permissions.length; i++) {
            Log.w("krmao", "onRequestPermissionsResult: permissions[" + i + "]=" + permissions[i] + " , grantResults[" + i + "]=" + grantResults[i]);
        }
    }

    public static final int RC_CAMERA_AND_LOCATION = 777;

    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    private void methodRequiresTwoPermission() {
        Log.d("krmao", "methodRequiresTwoPermission");
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
            Log.d("krmao", "methodRequiresTwoPermission: 已经获取全部权限");
        } else {
            // Do not have permissions, request them now
            Log.d("krmao", "methodRequiresTwoPermission: 有权限尚未获取");
            EasyPermissions.requestPermissions(this, "我们需要相机和录音权限2", RC_CAMERA_AND_LOCATION, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d("krmao", "onPermissionsGranted:" + requestCode + ":" + perms.toString());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d("krmao", "onPermissionsGranted:" + requestCode + ":" + perms.toString());
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}