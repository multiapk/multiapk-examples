package com.multiapk.library.rx


import org.reactivestreams.Subscriber

/**
 * 为RxBus使用的Subscriber, 主要提供next事件的try,catch
 *
 *
 * Created by YoKeyword on 16/7/20.
 */
abstract class CMRxBusSubscriber<T> : Subscriber<T> {

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
