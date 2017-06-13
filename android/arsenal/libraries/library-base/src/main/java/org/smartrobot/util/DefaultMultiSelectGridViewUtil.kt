package org.smartrobot.util

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout

import org.smartrobot.R

import java.io.Serializable
import java.util.ArrayList

class DefaultMultiSelectGridViewUtil {

    lateinit var mDataList: ArrayList<GMultiData>
    lateinit var multiAdapter: MultiAdapter
    lateinit var gridView: GridView
    lateinit var activity: Activity

    private constructor() {}

    constructor(activity: Activity, gridView: GridView) {
        this.activity = activity
        this.gridView = gridView
        this.mDataList = ArrayList<GMultiData>()
        this.multiAdapter = MultiAdapter()
        this.gridView.adapter = multiAdapter
    }

    fun notifyDataSetChanged(dataList: ArrayList<GMultiData>) {
        mDataList = dataList
        if (multiAdapter != null) {
            multiAdapter!!.notifyDataSetChanged()
        } else {
            multiAdapter = MultiAdapter()
            gridView.adapter = multiAdapter
        }
    }

    inner class MultiAdapter : BaseAdapter() {
        private val itemWidth = ((DefaultSystemUtil.width - DefaultSystemUtil.getPxFromDp(60f)) / 5.0f).toInt()

        override fun getCount(): Int {
            return mDataList.size
        }

        override fun getItem(position: Int): GMultiData {
            return mDataList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        @SuppressLint("InflateParams")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            if (multiCustomLayoutHandler != null) {
                return multiCustomLayoutHandler!!.createItemView(position, convertView, getItem(position))
            }

            val viewHolder: MultiViewHolder
            if (convertView == null) {
                convertView = LayoutInflater.from(activity).inflate(R.layout.default_multi_grid_item_layout, null)
                viewHolder = MultiViewHolder()
                viewHolder.mCheckImg = convertView!!.findViewById(R.id.mCheckImg) as ImageView
                viewHolder.layout = convertView.findViewById(R.id.layout) as RelativeLayout
                viewHolder.imageView = convertView.findViewById(R.id.imageView) as ImageView
                convertView.tag = viewHolder
            } else {
                viewHolder = convertView.tag as MultiViewHolder
            }

            viewHolder.layout!!.layoutParams = LinearLayout.LayoutParams(itemWidth, itemWidth)

            val _position = position

            val multiData = getItem(position)
            viewHolder.mCheckImg!!.visibility = if (multiData.isChecked) View.VISIBLE else View.GONE
            //MFrescoUtil.showProgressiveImage(multiData.imageUrl, viewHolder.mSimpleDraweeView);
            viewHolder.layout!!.setOnClickListener {
                mDataList[_position].isChecked = !mDataList[_position].isChecked
                viewHolder.mCheckImg!!.visibility = if (mDataList[_position].isChecked) View.VISIBLE else View.GONE
            }
            return convertView
        }
    }

    protected var multiCustomLayoutHandler: MultiCustomLayoutHandler? = null

    interface MultiCustomLayoutHandler {
        fun createItemView(position: Int, convertView: View?, multiData: GMultiData): View
    }

    class MultiViewHolder {
        var imageView: ImageView? = null
        var mCheckImg: ImageView? = null
        var layout: RelativeLayout? = null
    }

    class GMultiData : Serializable {
        var id = 0
        var type = 0
        var isChecked = false
        var name = ""
        var districtName = ""
        var imageUrl: String = ""

        private constructor() {}

        constructor(imageUrl: String, name: String, id: Int) {
            this.imageUrl = imageUrl
            this.name = name
            this.id = id
        }

        constructor(id: Int, type: Int, isChecked: Boolean, name: String, districtName: String, imageUrl: String) {
            this.id = id
            this.type = type
            this.isChecked = isChecked
            this.name = name
            this.districtName = districtName
            this.imageUrl = imageUrl
        }

        override fun toString(): String {
            return "id:$id, "
        }
    }
}
