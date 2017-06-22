package com.multiapk.modules.order

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import business.smartrobot.database.model.Order
import kotlinx.android.synthetic.main.order_fragment_order.*
import org.jetbrains.annotations.NotNull
import org.smartrobot.base.DefaultBaseFragment

class OrderFragment : DefaultBaseFragment(), OrderContract.View {

    lateinit private var presenter: OrderContract.Presenter
    private var orderAdapter: OrderAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.order_fragment_order, null)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.inflateMenu(R.menu.order_menu)
        toolbar.setOnMenuItemClickListener {
            Snackbar.make(toolbar, "您点击了:" + it?.title, Snackbar.LENGTH_SHORT).show()
            true
        }
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = orderAdapter

        presenter = OrderPresenter(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe()
    }

    override fun onPause() {
        super.onPause()
        presenter.unSubscribe()
    }

    override fun showToast(message: String) {
        Snackbar.make(toolbar, message, Snackbar.LENGTH_SHORT).show()
    }

    lateinit var progressDialog: ProgressDialog
    override fun showLoading() {
        progressDialog = ProgressDialog.show(context, "请稍等，正在请求数据...", "请求订单")
    }

    override fun hideLoading() {
        progressDialog.dismiss()
    }

    override fun showData(orders: List<Order>) {
        if (orderAdapter == null) {
            orderAdapter = OrderAdapter(orders)
            recyclerView.adapter = orderAdapter
        } else {
            orderAdapter?.notifyDataSetChanged(orders)
        }
    }

    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textTV: TextView = itemView.findViewById(R.id.text) as TextView

    }

    private class OrderAdapter(var dataList: List<Order>) : RecyclerView.Adapter<ViewHolder>() {

        fun notifyDataSetChanged(@NotNull dataList: List<Order>) {
            this.dataList = dataList
            notifyDataSetChanged()
        }


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textTV.text = "${dataList[position].id} ${dataList[position].createDate} ${dataList[position].payStatus}"
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false))
        }

    }
}
