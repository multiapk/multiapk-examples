package org.smartrobot.widget.debug

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RadioButton
import android.widget.TextView
import kotlinx.android.synthetic.main.default_debug_view_fragment.*
import org.smartrobot.R
import org.smartrobot.base.DefaultActivity
import org.smartrobot.base.DefaultBaseApplication
import org.smartrobot.base.DefaultBaseFragment
import org.smartrobot.util.*
import org.smartrobot.util.rx.RxBus

open class DefaultDebugFragment : DefaultBaseFragment() {
    companion object {
        val TAG = "DefaultDebugFragment"
        val debugBroadcastReceiver: DebugBroadcastReceiver = DebugBroadcastReceiver()

        fun goTo() {
            if (!isShown)
                DefaultActivity.startNewTask(DefaultDebugFragment::class.java, null)
        }

        val KEY_CUSTOM_LIST = "KEY_CUSTOM_LIST"
        var isShown = false
        var urlList: MutableList<UrlEntity> = DefaultPreferencesUtil.instance.getList(KEY_CUSTOM_LIST, UrlEntity::class.java)

        fun addUrl(vararg urlEntities: UrlEntity) {
            if (urlEntities.isNotEmpty()) {
                for (tmpUrlEntity in urlEntities)
                    if (!urlList.contains(tmpUrlEntity))
                        urlList.add(tmpUrlEntity)
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

        val NOTIFICATION_ID = 771

        @Suppress("DEPRECATION")
        fun showDebugNotification(isShowDebugNotification: Boolean) {
            if (isShowDebugNotification) {
                val builder = android.support.v7.app.NotificationCompat.Builder(DefaultBaseApplication.instance)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("smartrobot-title")
                        .setContentText("smartrobot-text")
                        .setAutoCancel(false)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setOngoing(true)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setSubText("smartrobot-subtext")
                        .setTicker("smartrobot-ticker")
                        .setStyle(
                                /*NotificationCompat.DecoratedCustomViewStyle()*/
                                android.support.v4.app.NotificationCompat.BigPictureStyle()
                                        .bigPicture(BitmapFactory.decodeResource(DefaultBaseApplication.instance.resources, R.drawable.android_yellow))
                                        .bigLargeIcon(BitmapFactory.decodeResource(DefaultBaseApplication.instance.resources, R.drawable.ic_launcher))
                                        .setBigContentTitle("bigtitle")
                                        .setSummaryText("summarytext")

                                /*android.support.v4.app.NotificationCompat.BigTextStyle()
                                        .bigText("bigtext")
                                        .setBigContentTitle("bigtitle")
                                        .setSummaryText("summarytext")*/
                        )
                        .addAction(R.drawable.ic_done, "FAT", PendingIntent.getBroadcast(DefaultBaseApplication.instance, 0, Intent(DebugBroadcastReceiver.ACTION).putExtra("type", 0), PendingIntent.FLAG_UPDATE_CURRENT))
                        .addAction(R.drawable.ic_edit, "UAT", PendingIntent.getBroadcast(DefaultBaseApplication.instance, 1, Intent(DebugBroadcastReceiver.ACTION).putExtra("type", 1), PendingIntent.FLAG_UPDATE_CURRENT))
                        .addAction(R.drawable.ic_launcher, "PRO", PendingIntent.getBroadcast(DefaultBaseApplication.instance, 2, Intent(DebugBroadcastReceiver.ACTION).putExtra("type", 2), PendingIntent.FLAG_UPDATE_CURRENT))

                DefaultNotificationUtil.showNotifyToFragment(DefaultBaseApplication.instance, NOTIFICATION_ID, Notification.FLAG_NO_CLEAR, builder, DefaultDebugFragment::class.java, Bundle(), PendingIntent.FLAG_CANCEL_CURRENT)

                DefaultBaseApplication.instance.registerReceiver(debugBroadcastReceiver, IntentFilter(DebugBroadcastReceiver.ACTION))
            }
        }

        fun cancelDebugNotification(isShowDebugNotification: Boolean) {
            if (isShowDebugNotification) {
                DefaultNotificationUtil.cancelNotify(DefaultBaseApplication.instance, NOTIFICATION_ID)
                DefaultBaseApplication.instance.unregisterReceiver(debugBroadcastReceiver)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.default_debug_view_fragment, container, false)
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                RxBus.instance.post(UrlChangeEvent(newEntity))
                adapter.notifyDataSetChanged()
            }
            DefaultSystemUtil.hide(activity)
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
                    saveUrlList()
                    RxBus.instance.post(UrlChangeEvent(urlEntity))
                    notifyDataSetChanged()
                    Snackbar.make(radioButton, "环境已经切换!", Snackbar.LENGTH_SHORT).show()
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

    class DebugBroadcastReceiver : BroadcastReceiver() {

        companion object {
            val ACTION = "org.smartrobot.action.debug"
        }

        override fun onReceive(context: Context?, intent: Intent) {
            DefaultSystemUtil.closeStatusBar()
            val type: Int = intent.getIntExtra("type", 0)
            Log.w("krmao", "DebugBroadcastReceiver:type=" + type)

            val urlEntity: UrlEntity = urlList[type]
            for (item in urlList) {
                item.isSelected = item == urlEntity
            }
            saveUrlList()
            RxBus.instance.post(UrlChangeEvent(urlEntity))
        }
    }
}
