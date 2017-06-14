package com.multiapk.modules.order

import android.util.Log
import business.smartrobot.DefaultApplication
import business.smartrobot.database.dao.OrderDao
import business.smartrobot.database.model.Order
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

open class OrderRepository : OrderContract.DataSource {

    val orderDao: OrderDao = DefaultApplication.instance.getDaoSession().orderDao

    override fun getOrders(): Flowable<List<Order>> {
        return Flowable.fromCallable {
            Log.w("krmao", "getOrders:${Thread.currentThread().name}")
            orderDao.loadAll()
        }.subscribeOn(Schedulers.computation())
    }

    override fun getOrder(orderId: Long): Flowable<Order> {
        return Flowable.fromCallable { orderDao.load(orderId) }.subscribeOn(Schedulers.computation())
    }

    override fun deleteAllOrders() {
        orderDao.deleteAll()
    }

    override fun deleteOrder(orderId: Long) {
        orderDao.deleteByKey(orderId)
    }
}
