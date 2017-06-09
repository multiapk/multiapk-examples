package org.smartrobot.util.rx

import org.reactivestreams.Subscriber

abstract class RxBusSubscriber<T> : Subscriber<T> {

    override fun onNext(t: T) {
        try {
            onEvent(t)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onComplete() {}

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }

    protected abstract fun onEvent(t: T)

}
