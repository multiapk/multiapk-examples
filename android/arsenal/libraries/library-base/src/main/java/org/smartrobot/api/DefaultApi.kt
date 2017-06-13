package org.smartrobot.api

import io.reactivex.Flowable
import org.smartrobot.api.model.UserModel
import retrofit2.http.GET
import retrofit2.http.Path


interface DefaultApi {

    @GET("/user/{userId}")
    fun getUser(@Path("userId") userId: Int): Flowable<DefaultResponseModel<UserModel>>
}