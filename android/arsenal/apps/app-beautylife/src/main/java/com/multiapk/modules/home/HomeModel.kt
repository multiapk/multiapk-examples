package com.multiapk.modules.home

import com.google.auto.value.AutoValue

import java.io.Serializable

@AutoValue
internal abstract class HomeModel : Serializable {
    abstract fun address(): String

    abstract fun name(): String

    abstract fun area(): String
}