package org.smartrobot.widget.debug

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RadioButton
import android.widget.TextView
import kotlinx.android.synthetic.main.default_debug_view_fragment.*
import org.smartrobot.R
import org.smartrobot.base.DefaultActivity
import org.smartrobot.base.DefaultBaseFragment
import org.smartrobot.util.*
import org.smartrobot.util.rx.RxBus

open class DefaultDebugFragment : DefaultBaseFragment() {
    companion object {
        val TAG = "DefaultDebugFragment"

        fun goTo() {
            if (!isShown)
                DefaultActivity.startNewTask(DefaultDebugFragment::class.java, null)
        }

        val KEY_CUSTOM_LIST = "KEY_CUSTOM_LIST"
        var isShown = false
        var urlList: MutableList<UrlEntity> = DefaultPreferencesUtil.instance.getList(KEY_CUSTOM_LIST, UrlEntity::class.java)

        fun addUrl(vararg urlEntities: UrlEntity) {
            if (urlEntities.isNotEmpty()) {
                urlEntities
                        .filterNot { urlList.contains(it) }
                        .forEach { urlList.add(it) }
                saveUrlList()
            }
        }

        fun addUrl(label: String, url: String, isSelected: Boolean = false) {
            if (!TextUtils.isEmpty(label) && !TextUtils.isEmpty(url))
                addUrl(UrlEntity(label, url, isSelected))
        }

        fun saveUrlList() {
            DefaultLogUtil.d(TAG, "[saveUrlList]:\n" + urlList.toString())
            DefaultPreferencesUtil.instance.putList(KEY_CUSTOM_LIST, urlList)
        }

        fun getCurrentUrl(): String {
            val tmpUrlList = urlList

            var url: String = ""

            for (tmp in tmpUrlList) {
                if (tmp.isSelected) {
                    url = tmp.url
                    break
                }
            }
            if (TextUtils.isEmpty(url) && tmpUrlList.size > 0) {
                tmpUrlList[0].isSelected = true
                urlList = tmpUrlList
                url = tmpUrlList[0].url
                saveUrlList()
            }
            return url
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.default_debug_view_fragment, container, false)
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleBar.right0BgView.setOnClickListener {
            saveUrlList()
            Snackbar.make(titleBar, "保存成功!", Snackbar.LENGTH_SHORT).show()
            activity.finish()
        }

        val adapter = DebugAdapter(urlList, activity)
        listView.adapter = adapter
        addCustom.setOnClickListener(View.OnClickListener {
            val newEntity = UrlEntity(editLabel.text.toString(), editUrl.text.toString(), false)
            if (TextUtils.isEmpty(newEntity.label)) {
                DefaultToastUtil.show("请填写标签")
                return@OnClickListener
            }
            if (TextUtils.isEmpty(newEntity.url)) {
                DefaultToastUtil.show("请填写服务地址")
                return@OnClickListener
            }
            editLabel.text = null
            editUrl.text = null
            if (!urlList.contains(newEntity)) {
                urlList.add(newEntity)
                saveUrlList()
                adapter.notifyDataSetChanged()
            }
            DefaultSystemUtil.hide(activity)
            RxBus.instance.post(UrlChangeEvent(newEntity))
        })

        clearCacheTV.setOnClickListener { DefaultIntentUtil.goToAppDetails(activity) }
        hideTV.setOnClickListener { DefaultFloatViewUtil.instance.isAwaysHide = true }
    }

    override fun onResume() {
        super.onResume()
        isShown = true
    }

    override fun onPause() {
        super.onPause()
        isShown = false
    }

    class DebugAdapter(var list: List<UrlEntity>, private val context: Context) : BaseAdapter() {
        override fun getCount(): Int {
            return list.size
        }

        override fun getItem(position: Int): UrlEntity {
            return list[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, _convertView: View?, parent: ViewGroup?): View {
            val convertView = LayoutInflater.from(context).inflate(R.layout.default_debug_item, parent, false)
            val radioButton = convertView.findViewById(R.id.radioButton) as RadioButton
            val textView = convertView.findViewById(R.id.textView) as TextView
            val urlEntity = getItem(position)
            radioButton.text = urlEntity.label
            radioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked && !urlEntity.isSelected) {
                    urlEntity.isSelected = true
                    for (item in urlList) {
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

    class UrlChangeEvent(var urlEntity: UrlEntity)

    class UrlEntity(var label: String, var url: String, var isSelected: Boolean) {
        override fun equals(other: Any?): Boolean {
            if (other is UrlEntity) {
                return !TextUtils.isEmpty(label) && label == other.label
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

        override fun hashCode(): Int {
            var result = label.hashCode()
            result = 31 * result + url.hashCode()
            result = 31 * result + isSelected.hashCode()
            return result
        }
    }
}
