package com.multiapk.modules.home;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class HomeActivityUnitTest {

    public List<Integer> dataList = null;

    @Before
    public void init() {
        dataList = Lists.newArrayList(1, 3, 2, 4);
    }

    @Test
    public void test() {
        Subscriber<Integer> observer = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("Observer.onSubscribe:" + s.toString());
                s.request(8);
            }

            @Override
            public void onNext(@NonNull Integer o) {
                System.out.println("Observer.onNext:" + o);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("Observer.onError:");
            }

            @Override
            public void onComplete() {
                System.out.println("Observer.onComplete");
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