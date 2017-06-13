package org.smartrobot.widget.recyclerview

import android.content.Context
import android.support.v7.widget.RecyclerView

import com.mlibrary.widget.recyclerview.helper.DefaultRecyclerViewItemTouchHelperAdapter

import java.util.ArrayList
import java.util.Collections

abstract class DefaultRecyclerViewAdapter<Entity, ViewHolder : RecyclerView.ViewHolder>(context: Context, dataList: List<Entity>) : RecyclerView.Adapter<ViewHolder>(), DefaultRecyclerViewItemTouchHelperAdapter {
    var context: Context
        protected set
    protected var dataList: List<Entity>? = null

    init {
        context = context
        setDataList(dataList)
    }

    fun getDataList(): MutableList<Entity> {
        if (dataList == null)
            dataList = ArrayList<Entity>()
        return dataList
    }

    protected fun setDataList(dataList: List<Entity>) {
        this.dataList = dataList
    }

    override fun getItemCount(): Int {
        return getDataList().size
    }

    fun remove(position: Int) {
        if (position >= 0 && position < getDataList().size) {
            getDataList().removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, getDataList().size - position)
        }
    }

    fun removeAll() {
        val oldSize = getDataList().size
        getDataList().clear()
        notifyItemRangeRemoved(0, oldSize)
    }

    fun add(newList: List<Entity>?) {
        if (newList != null && !newList.isEmpty()) {
            val oldSize = getDataList().size
            getDataList().addAll(newList)
            notifyItemRangeChanged(oldSize - 1, newList.size + 1)
        }
    }

    fun add(entity: Entity, position: Int) {
        if (position >= 0 && position <= getDataList().size) {
            getDataList().add(entity)
            notifyItemInserted(position)
            notifyItemRangeChanged(position, getDataList().size - position)
        }
    }

    fun notifyItemRangeChanged() {
        notifyItemRangeChanged(0, getDataList().size)
    }

    override fun onItemDismiss(position: Int) {
        remove(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(getDataList(), fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }
}