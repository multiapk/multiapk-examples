package org.smartrobot.widget.debug

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import com.mlibrary.util.*
import org.smartrobot.R
import org.smartrobot.base.DefaultActivity
import org.smartrobot.base.DefaultBaseFragment
import org.smartrobot.util.*
import org.smartrobot.widget.DefaultListView
import org.smartrobot.widget.titlebar.DefaultTitleBar
import java.util.*

class DefaultDebugFragment : DefaultBaseFragment() {
    protected lateinit var listView: DefaultListView

    protected lateinit var adapter: DebugAdapter
    protected lateinit var editUrl: EditText
    protected lateinit var editLabel: EditText
    protected lateinit var addCustom: TextView

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = LayoutInflater.from(activity).inflate(R.layout.default_debug_fragment, null)
        val DefaultTitleBar = contentView.findViewById(R.id.DefaultTitleBar) as DefaultTitleBar
        listView = contentView.findViewById(R.id.mListView) as DefaultListView
        editUrl = contentView.findViewById(R.id.editUrl) as EditText
        editLabel = contentView.findViewById(R.id.editLabel) as EditText
        addCustom = contentView.findViewById(R.id.addCustom) as TextView
        val hideTV = contentView.findViewById(R.id.hideTV)
        val clearCacheTV = contentView.findViewById(R.id.clearCacheTV)

        clearCacheTV.setOnClickListener { DefaultIntentUtil.goToAppDetails(activity) }
        hideTV.setOnClickListener { DefaultFloatViewUtil.instance.isAwaysHide = true }

        adapter = DebugAdapter(urlList, activity)
        listView.adapter = adapter
        addCustom.setOnClickListener(View.OnClickListener {
            val newEntity = UrlEntity(editLabel.text.toString().trim { it <= ' ' }, editUrl.text.toString().trim { it <= ' ' }, false)
            if (TextUtils.isEmpty(newEntity.label)) {
                DefaultToastUtil.show("请填写标签")
                return@OnClickListener
            }
            if (TextUtils.isEmpty(newEntity.url)) {
                DefaultToastUtil.show("请填写服务地址")
                return@OnClickListener
            }
            editLabel.setText(null)
            editUrl.setText(null)
            if (!urlList!!.contains(newEntity)) {
                urlList!!.add(newEntity)
                saveUrlList()
                adapter.notifyDataSetChanged()
            }
            DefaultSystemUtil.hide(activity)
        })
        DefaultTitleBar.right0BgView.setOnClickListener {
            saveUrlList()
            DefaultToastUtil.show("已保存")
            activity.finish()
        }

        return contentView
    }

    class UrlEntity(var label: String, var url: String, isSelected: Boolean) {
        var isSelected = true

        init {
            this.isSelected = isSelected
        }

        override fun equals(o: Any?): Boolean {
            if (o is UrlEntity) {
                return !TextUtils.isEmpty(label) && label == o.label
            } else {
                return false
            }
        }

        override fun toString(): String {
            return "UrlEntity{" +
                    "label='" + label + '\'' +
                    ", url='" + url + '\'' +
                    ", isSelected=" + isSelected +
                    '}'
        }
    }


    override fun onResume() {
        super.onResume()
        isShown = true
    }

    override fun onPause() {
        super.onPause()
        isShown = false
    }

    override fun onDestroy() {
        super.onDestroy()
        val onCacheCallBack = DefaultCacheManager.instance.get<DefaultCacheManager.OnCacheCallBack<*>>(TAG, TAG)
        onCacheCallBack?.onSuccess(null)
    }

    class DebugAdapter(list: List<UrlEntity>, private val context: Context) : BaseAdapter() {
        private var list: List<UrlEntity>? = ArrayList()

        init {
            this.list = list
            if (this.list == null)
                this.list = ArrayList<UrlEntity>()
        }

        override fun getCount(): Int {
            return list!!.size
        }

        override fun getItem(position: Int): UrlEntity {
            return list!![position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        @SuppressLint("InflateParams")
        override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
            var convertView = convertView
            convertView = LayoutInflater.from(context).inflate(R.layout.default_debug_item, null)
            val radioButton = convertView.findViewById(R.id.radioButton) as RadioButton
            val textView = convertView.findViewById(R.id.textView) as TextView
            val urlEntity = getItem(position)
            radioButton.text = urlEntity.label
            radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked && !urlEntity.isSelected) {
                    urlEntity.isSelected = true
                    for (item in urlList!!) {
                        item.isSelected = item == urlEntity
                    }
                    notifyDataSetChanged()
                }
            }
            radioButton.isChecked = urlEntity.isSelected
            textView.text = urlEntity.url
            textView.setOnClickListener { DefaultToastUtil.show("长按复制") }
            textView.setOnLongClickListener {
                DefaultSystemUtil.copyToClipboard(urlEntity.label, urlEntity.url)
                DefaultToastUtil.show("已复制")
                true
            }
            return convertView
        }
    }

    companion object {
        val TAG = "MDebugFragment"

        @JvmOverloads fun goTo(onCacheCallBack: DefaultCacheManager.OnCacheCallBack<*>? = null) {
            if (!isShown) {
                if (onCacheCallBack != null)
                    DefaultCacheManager.instance.put(TAG, TAG, onCacheCallBack)
                DefaultActivity.startNewTask(DefaultDebugFragment::class.java, null)
            }
        }

        val KEY_CUSTOM_LIST = "KEY_CUSTOM_LIST"
        var isShown = false
        var urlList: MutableList<UrlEntity> = ArrayList()
            get() {
                urlList = DefaultPreferencesUtil.INSTANCE.getList(KEY_CUSTOM_LIST, UrlEntity::class.java)
                if (isEnableLog)
                    DefaultLogUtil.d(TAG, "[getUrlList]:\n" + urlList!!.toString())
                return urlList
            }
        var isEnableLog = false

        fun addUrl(vararg urlEntities: UrlEntity) {
            if (urlEntities != null && urlEntities.size > 0) {
                for (entity in urlEntities) {
                    if (!urlList!!.contains(entity)) {
                        urlList!!.add(entity)
                    }
                }
                var isHasSelectedUrl = false
                for (i in urlList!!.indices.reversed()) {
                    val entity = urlList!![i]
                    if (entity.isSelected) {
                        if (isHasSelectedUrl)
                            entity.isSelected = false
                        isHasSelectedUrl = true
                    }
                }
                saveUrlList()
            }
        }

        @JvmOverloads fun addUrl(label: String, url: String, isSelected: Boolean = false) {
            if (!TextUtils.isEmpty(label) && !TextUtils.isEmpty(url)) {
                addUrl(UrlEntity(label, url, isSelected))
            }
        }

        fun saveUrlList() {
            if (urlList != null) {
                var isHasSelected = false
                for (urlEntity in urlList!!) {
                    if (urlEntity.isSelected) {
                        isHasSelected = true
                        break
                    }
                }
                if (!isHasSelected && urlList!!.size > 0) {
                    urlList!![0].isSelected = true
                }
                if (isEnableLog)
                    DefaultLogUtil.d(TAG, "[saveUrlList]:\n" + urlList!!.toString())
            }
            DefaultPreferencesUtil.INSTANCE.putList(KEY_CUSTOM_LIST, urlList)
        }

        val selectedUrl: String
            get() {
                var url = ""
                val tmpUrlList = urlList
                var isHasSelected = false
                for (urlEntity in tmpUrlList) {
                    if (urlEntity.isSelected) {
                        url = urlEntity.url
                        isHasSelected = true
                        break
                    }
                }
                if (!isHasSelected && tmpUrlList.size > 0) {
                    tmpUrlList[0].isSelected = true
                    urlList!!.clear()
                    urlList = tmpUrlList
                    url = tmpUrlList[0].url
                    saveUrlList()
                }
                return url
            }
    }
}
