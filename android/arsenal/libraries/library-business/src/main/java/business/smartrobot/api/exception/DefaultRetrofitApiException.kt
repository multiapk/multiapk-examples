package business.smartrobot.api.exception

class DefaultRetrofitApiException(throwable: Throwable, val code: Int) : Exception(throwable) {
    var displayMessage: String? = null
}
