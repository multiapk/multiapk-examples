package business.smartrobot.api.exception


import com.jakewharton.retrofit2.adapter.rxjava2.HttpException

import org.json.JSONException

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

object DefaultRetrofitException {

    //对应HTTP的状态码
    private val UNAUTHORIZED = 401
    private val FORBIDDEN = 403
    private val NOT_FOUND = 404
    private val REQUEST_TIMEOUT = 408
    private val INTERNAL_SERVER_ERROR = 500
    private val BAD_GATEWAY = 502
    private val SERVICE_UNAVAILABLE = 503
    private val GATEWAY_TIMEOUT = 504

    fun handleException(throwable: Throwable): DefaultRetrofitApiException {
        val apiException: DefaultRetrofitApiException
        if (throwable is HttpException) {             //HTTP错误
            apiException = DefaultRetrofitApiException(throwable, DefaultRetrofitError.HTTP_ERROR)
            when (throwable.code()) {
                UNAUTHORIZED, FORBIDDEN, NOT_FOUND, REQUEST_TIMEOUT, GATEWAY_TIMEOUT, INTERNAL_SERVER_ERROR, BAD_GATEWAY, SERVICE_UNAVAILABLE -> apiException.displayMessage = "网络连接错误"
                else -> apiException.displayMessage = "网络连接错误"
            }
            return apiException
        } else if (throwable is DefaultRetrofitServerException) {
            val resultException = throwable
            apiException = DefaultRetrofitApiException(resultException, resultException.code)
            apiException.displayMessage = resultException.msg
            return apiException
        } else if (throwable is com.alibaba.fastjson.JSONException
                || throwable is JSONException
                || throwable is ParseException) {
            apiException = DefaultRetrofitApiException(throwable, DefaultRetrofitError.PARSE_ERROR)
            apiException.displayMessage = "数据解析错误"
            return apiException
        } else if (throwable is ConnectException
                || throwable is SocketTimeoutException
                || throwable is UnknownHostException) {
            apiException = DefaultRetrofitApiException(throwable, DefaultRetrofitError.NETWORD_ERROR)
            apiException.displayMessage = "网络连接错误"
            return apiException
        } else {
            apiException = DefaultRetrofitApiException(throwable, DefaultRetrofitError.UNKNOWN)
            apiException.displayMessage = "未知错误"
            return apiException
        }
    }
}
