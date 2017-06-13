package org.smartrobot.api

import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.smartrobot.api.exception.DefaultRetrofitException
import org.smartrobot.api.exception.DefaultRetrofitServerException


object DefaultApiManager {
    /*static
    {
        MDebugFragment.addUrl(new MDebugFragment . UrlEntity "主服务", baseUrl, true), new MDebugFragment.UrlEntity("老贺本机", heJinGuoUrl, false));
    }*/

    private val baseUrl = "http://192.168.2.48:7777"
    private val retrofitClient = DefaultRetrofitClient()

    @Synchronized fun getDefaultApi(): DefaultApi {
        return retrofitClient.getApi(baseUrl, DefaultApi::class.java)
    }

    fun <T> compose(): FlowableTransformer<DefaultResponseModel<T>, T> {
        return FlowableTransformer { observable ->
            observable
                    .map { responseModel: DefaultResponseModel<T>? ->
                        if (responseModel != null && responseModel.status == 0) {
                            responseModel.data as T
                        } else {
                            throw DefaultRetrofitServerException(responseModel?.status ?: -1, responseModel?.message ?: "返回数据为空")
                        }
                    }
                    .onErrorResumeNext { throwable: Throwable ->
                        Flowable.error(DefaultRetrofitException.handleException(throwable))
                    }
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
}