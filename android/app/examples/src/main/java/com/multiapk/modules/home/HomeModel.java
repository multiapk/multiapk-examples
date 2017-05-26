package com.multiapk.modules.home;

import com.google.auto.value.AutoValue;

import java.io.Serializable;

@AutoValue
abstract class HomeModel implements Serializable {
    public abstract String address();

    public abstract String name();

    public abstract String area();
}