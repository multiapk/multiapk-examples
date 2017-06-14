package org.smartrobot.widget.recyclerview

import android.content.Context
import android.support.v7.widget.RecyclerView
import org.smartrobot.widget.recyclerview.helper.DefaultRecyclerViewItemTouchHelperAdapter
import java.util.*

abstract class DefaultRecyclerViewAdapter<Entity, ViewHolder : RecyclerView.ViewHolder>(var context: Context, dataList: ArrayList<Entity>) : RecyclerView.Adapter<ViewHolder>(), DefaultRecyclerViewItemTouchHelperAdapter {
    var dataList: ArrayList<Entity> = ArrayList<Entity>()

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun remove(position: Int) {
        if (position >= 0 && position < dataList.size) {
            dataList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, dataList.size - position)
        }
    }

    fun removeAll() {
        val oldSize = dataList.size
        dataList.clear()
        notifyItemRangeRemoved(0, oldSize)
    }

    fun add(newList: List<Entity>?) {
        if (newList != null && !newList.isEmpty()) {
            val oldSize = dataList.size
            dataList.addAll(newList)
            notifyItemRangeChanged(oldSize - 1, newList.size + 1)
        }
    }

    fun add(entity: Entity, position: Int) {
        if (position >= 0 && position <= dataList.size) {
            dataList.add(entity)
            notifyItemInserted(position)
            notifyItemRangeChanged(position, dataList.size - position)
        }
    }

    fun notifyItemRangeChanged() {
        notifyItemRangeChanged(0, dataList.size)
    }

    override fun onItemDismiss(position: Int) {
        remove(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(dataList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }
}