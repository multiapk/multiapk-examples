package com.multiapk.modules.order

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.smartrobot.util.rx.RxBus
import org.smartrobot.util.rx.RxTestEvent
import java.util.concurrent.TimeUnit

class OrderPresenter(private val view: OrderContract.View) : OrderContract.Presenter {
    private val dataSource: OrderContract.DataSource = OrderRepository()
    private val subscriptions: CompositeDisposable = CompositeDisposable()

    override fun loadData(forceUpdate: Boolean) {
        view.showLoading()
        subscriptions.add(dataSource.getOrders().delay(700, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe { result ->
            view.hideLoading()
            view.showToast("数据获取结束:${Thread.currentThread().name}")
            view.showData(result)
            view.showToast("数据刷新完毕:${Thread.currentThread().name}")
            RxBus.instance.post(RxTestEvent("在订单页面设置"))
        })
    }

    override fun subscribe() {
        subscriptions.clear()
        loadData(false)
    }

    override fun unSubscribe() {
        subscriptions.clear()
    }
}