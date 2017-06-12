package org.smartrobot.api

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.fastjson.FastJsonConverterFactory

open class DefaultRetrofitClient {
    protected var builder: Retrofit.Builder = Retrofit.Builder()
            .addConverterFactory(FastJsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(DefaultOkHttpClient.INSTANCE.okHttpClient)

    protected var currentClazz: Class<*>? = null
    protected var currentUrl: String? = null

    fun <T> getApi(url: String, clazz: Class<T>): T {
        this.currentClazz = clazz
        this.currentUrl = url
        return builder.baseUrl(url).build().create(clazz)
    }
}