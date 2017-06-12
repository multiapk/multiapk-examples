package org.smartrobot.api


import io.reactivex.subscribers.DisposableSubscriber

abstract class DefaultRetrofitSubscriber<T> : DisposableSubscriber<T>() {
    override fun onComplete() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(p0: Throwable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        onFailure(p0.toString())
    }

    override fun onNext(p0: T) {
        onSuccess(p0)
    }

    abstract fun onSuccess(result: T)

    abstract fun onFailure(msg: String)

}
