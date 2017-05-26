package com.multiapk.test;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    private List<Integer> dataList = null;

    @Before
    private void init() {
        dataList = Lists.newArrayList(1, 3, 2, 4);
    }

    @Test
    private void test() {
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

        Flowable.fromIterable(dataList).filter(new Predicate<Integer>() {
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
}