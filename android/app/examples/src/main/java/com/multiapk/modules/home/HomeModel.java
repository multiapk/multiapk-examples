package com.multiapk.modules.home;

import com.google.auto.value.AutoValue;

import java.io.Serializable;

/**
 * Created by krmao on 2017/5/26.
 */

@AutoValue
public abstract class HomeModel implements Serializable {
    public abstract String bar();
}