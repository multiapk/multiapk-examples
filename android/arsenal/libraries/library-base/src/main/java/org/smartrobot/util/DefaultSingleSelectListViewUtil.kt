package org.smartrobot.util


import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.smartrobot.R
import java.io.Serializable
import java.util.*

class DefaultSingleSelectListViewUtil {

    lateinit var mDataList: ArrayList<out SingleData<*>>
    lateinit var multiAdapter: MultiAdapter
    lateinit var listView: ListView
    lateinit var activity: Activity

    private constructor() {}

    constructor(activity: Activity, listView: ListView, dataList: ArrayList<out SingleData<*>>) {
        this.activity = activity
        this.listView = listView
        this.mDataList = dataList
        this.multiAdapter = MultiAdapter()
        this.listView.adapter = multiAdapter
    }

    fun notifyDataSetChanged(dataList: ArrayList<out SingleData<*>>) {
        mDataList = dataList
        if (multiAdapter != null) {
            multiAdapter!!.notifyDataSetChanged()
        } else {
            multiAdapter = MultiAdapter()
            listView.adapter = multiAdapter
        }
    }

    inner class MultiAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return mDataList.size
        }

        override fun getItem(position: Int): SingleData<*> {
            return mDataList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        @SuppressLint("InflateParams")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            if (singleCustomLayoutHandler != null) {
                return singleCustomLayoutHandler!!.createItemView(position, convertView, getItem(position))
            }

            val viewHolder: SingleViewHolder
            if (convertView == null) {
                convertView = LayoutInflater.from(activity).inflate(R.layout.default_single_listview_item_layout, null)
                viewHolder = SingleViewHolder()
                viewHolder.imageView = convertView!!.findViewById(R.id.imageView) as ImageView
                viewHolder.mTextView = convertView.findViewById(R.id.mTextView) as TextView
                viewHolder.mItemLayout = convertView.findViewById(R.id.mItemLayout) as LinearLayout
                convertView.tag = viewHolder
            } else {
                viewHolder = convertView.tag as SingleViewHolder
            }

            val SingleData = getItem(position)
            viewHolder.imageView!!.visibility = if (SingleData.isChecked) View.VISIBLE else View.INVISIBLE
            viewHolder.mTextView!!.text = String.format("%s", SingleData.name)
            return convertView
        }
    }

    protected var singleCustomLayoutHandler: SingleCustomLayoutHandler? = null

    interface SingleCustomLayoutHandler {
        fun createItemView(position: Int, convertView: View?, SingleData: SingleData<*>): View
    }

    class SingleViewHolder {
        var mItemLayout: LinearLayout? = null
        var mTextView: TextView? = null
        var imageView: ImageView? = null
    }

    class SingleData<T> : Serializable {
        var id = 0
        var type = 0
        var isChecked = false
        var name = ""
        var districtName = ""
        var arg: T? = null

        protected constructor() {}

        constructor(id: Int, name: String) {
            this.id = id
            this.name = name
        }

        constructor(arg: T) {
            this.arg = arg
        }

        constructor(id: Int, type: Int, isChecked: Boolean, name: String, districtName: String) {
            this.id = id
            this.type = type
            this.isChecked = isChecked
            this.name = name
            this.districtName = districtName
        }

        constructor(id: Int, type: Int, isChecked: Boolean, name: String, districtName: String, arg: T) {
            this.id = id
            this.type = type
            this.isChecked = isChecked
            this.name = name
            this.districtName = districtName
            this.arg = arg
        }

        override fun toString(): String {
            return "id:$id, "
        }
    }
}
