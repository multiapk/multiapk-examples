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

class DefaultMultiSelectListViewUtil {

    lateinit var mDataList: ArrayList<MultiData>
    lateinit var multiAdapter: MultiAdapter
    lateinit var listView: ListView
    lateinit var activity: Activity

    private constructor() {}

    constructor(activity: Activity, listView: ListView) {
        this.activity = activity
        this.listView = listView
        this.mDataList = ArrayList<MultiData>()
        this.multiAdapter = MultiAdapter()
        this.listView.adapter = multiAdapter
    }

    fun notifyDataSetChanged(dataList: ArrayList<MultiData>) {
        mDataList = dataList
        multiAdapter.notifyDataSetChanged()
    }

    inner class MultiAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return mDataList.size
        }

        override fun getItem(position: Int): MultiData {
            return mDataList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        @SuppressLint("InflateParams")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

            if (multiCustomLayoutHandler != null) {
                return multiCustomLayoutHandler!!.createItemView(position, convertView, getItem(position))
            }
            var contentView = convertView
            val viewHolder: MultiViewHolder
            if (contentView == null) {
                contentView = LayoutInflater.from(activity).inflate(R.layout.default_multi_listview_item_layout, null)
                viewHolder = MultiViewHolder()
                viewHolder.mCheckBox = contentView!!.findViewById(R.id.mCheckBox) as CheckBox
                viewHolder.mTextView = contentView.findViewById(R.id.mTextView) as TextView
                viewHolder.mItemLayout = contentView.findViewById(R.id.mItemLayout) as LinearLayout
                contentView.tag = viewHolder
            } else {
                viewHolder = contentView.tag as MultiViewHolder
            }

            viewHolder.mCheckBox!!.setOnCheckedChangeListener { _, isChecked ->
                mDataList[position].isChecked = isChecked
                viewHolder.mCheckBox!!.isChecked = mDataList[position].isChecked
            }

            val multiData = getItem(position)
            viewHolder.mCheckBox!!.isChecked = multiData.isChecked
            viewHolder.mTextView!!.text = String.format("%s，%s", multiData.name, multiData.districtName)
            viewHolder.mItemLayout!!.setOnClickListener {
                /**POI类型：0:景点 1:购物 2:娱乐 3:餐馆 */
                /**POI类型：0:景点 1:购物 2:娱乐 3:餐馆 */
                //val multiData = getItem(position)
            }
            return contentView
        }
    }

    protected var multiCustomLayoutHandler: MultiCustomLayoutHandler? = null

    interface MultiCustomLayoutHandler {
        fun createItemView(position: Int, convertView: View?, multiData: MultiData): View
    }

    class MultiViewHolder {
        var mItemLayout: LinearLayout? = null
        var mTextView: TextView? = null
        var mCheckBox: CheckBox? = null
    }

    class MultiData : Serializable {
        var id = 0
        var type = 0
        var isChecked = false
        var name = ""
        var districtName = ""
        var `object`: Any? = null

        private constructor() {}

        constructor(id: Int, type: Int, isChecked: Boolean, name: String, districtName: String) {
            this.id = id
            this.type = type
            this.isChecked = isChecked
            this.name = name
            this.districtName = districtName
        }

        override fun toString(): String {
            return "id:$id, "
        }
    }
}
