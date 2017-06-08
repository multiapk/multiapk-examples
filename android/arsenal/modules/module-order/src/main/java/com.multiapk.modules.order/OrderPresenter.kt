package com.multiapk.modules.order

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class OrderPresenter(private val view: OrderContract.View) : OrderContract.Presenter {
    private val dataSource: OrderContract.DataSource = OrderDataSource()
    private val subscriptions: CompositeDisposable = CompositeDisposable()

    override fun loadData(forceUpdate: Boolean) {
        view.showLoading()
        subscriptions.add(dataSource.getOrders().delay(5, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe { result ->
            view.hideLoading()
            view.showToast("数据获取结束:${Thread.currentThread().name}")
            view.showData(result)
            view.showToast("数据刷新完毕:${Thread.currentThread().name}")
        })
    }

    override fun subscribe() {
        subscriptions.clear()
        loadData(true)
    }

    override fun unSubscribe() {
        subscriptions.clear()
    }
}

