package com.multiapk.modules.order

import io.reactivex.Flowable
import org.smartrobot.base.mvp.DefaultBaseDataSource
import org.smartrobot.base.mvp.DefaultBasePresenter
import org.smartrobot.base.mvp.DefaultBaseView
import org.smartrobot.database.model.Order

interface OrderContract {

    interface View : DefaultBaseView<Presenter> {
        fun showToast(message: String)
        fun showData(orders: List<Order>)
    }

    interface Presenter : DefaultBasePresenter {
        fun loadData(forceUpdate: Boolean)
    }

    interface DataSource : DefaultBaseDataSource {

        fun getOrders(): Flowable<List<Order>>

        fun getOrder(orderId: Long): Flowable<Order>

        fun deleteAllOrders()

        fun deleteOrder(orderId: Long)
    }
}