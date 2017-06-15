package business.smartrobot.api

import business.smartrobot.api.exception.DefaultRetrofitException
import business.smartrobot.api.exception.DefaultRetrofitServerException
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.smartrobot.util.http.DefaultRetrofitClient
import org.smartrobot.util.rx.RxBus
import org.smartrobot.widget.debug.DefaultDebugFragment


object DefaultApiManager {

    private val URL_FAT = "fat.smartrobot.com"
    private val URL_UAT = "uat.smartrobot.com"
    private val URL_PRO = "pro.smartrobot.com"
    private var URL_MAIN = URL_PRO

    private val retrofitClient = DefaultRetrofitClient()


    fun init() {
//        if (BuildConfig.DEBUG) {
        DefaultDebugFragment.addUrl("FAT", URL_FAT, false)
        DefaultDebugFragment.addUrl("UAT", URL_UAT, false)
        DefaultDebugFragment.addUrl("PRO", URL_PRO, true)
        RxBus.instance.toObservable(DefaultDebugFragment.UrlChangeEvent::class.java).subscribe { urlChangeEvent ->
            URL_MAIN = urlChangeEvent.urlEntity.url
        }
//        }
    }

    @Synchronized fun getDefaultApi(): DefaultApi {
        return retrofitClient.getApi(URL_MAIN, DefaultApi::class.java)
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