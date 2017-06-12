package org.smartrobot.api

import android.widget.Toast
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.smartrobot.base.DefaultBaseApplication
import org.smartrobot.util.network.DefaultNetworkUtil

object DefaultRetrofitSchedulers {
    fun <T> compose(): FlowableTransformer<T, T> {
        return FlowableTransformer { observable ->
            observable
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe {
                        if (!DefaultNetworkUtil.isNetworkAvailable()) {
                            Toast.makeText(DefaultBaseApplication.INSTANCE, "网络请求失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
}