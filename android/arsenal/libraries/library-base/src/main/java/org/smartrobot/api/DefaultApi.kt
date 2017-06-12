package org.smartrobot.api

import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path


interface DefaultApi {

    @GET("users/{user}/data")
    fun getData(@Path("user") user: String): Flowable<List<Any>>

}