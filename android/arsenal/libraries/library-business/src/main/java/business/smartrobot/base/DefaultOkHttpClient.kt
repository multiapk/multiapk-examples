package business.smartrobot.base

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

enum class DefaultOkHttpClient {
    INSTANCE;

    var okHttpClient: OkHttpClient
    var CONNECT_TIMEOUT_SECONDS = 10
    var READ_TIMEOUT_SECONDS = 10
    var WRITE_TIMEOUT_SECONDS = 10

    init {
        okHttpClient = OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
    }
}
