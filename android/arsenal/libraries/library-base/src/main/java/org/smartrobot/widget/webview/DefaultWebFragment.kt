package org.smartrobot.widget.webview

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JsResult
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

import com.mlibrary.widget.loading.DefaultFrameLoadingLayout
import com.mlibrary.widget.titlebar.DefaultTitleBar

import org.smartrobot.R
import org.smartrobot.base.DefaultActivity
import org.smartrobot.base.DefaultBaseFragment

open class DefaultWebFragment : DefaultBaseFragment(), DefaultBaseFragment.OnBackPressedListener {
    protected lateinit var mUrl: String
    protected lateinit var mHtmlData: String
    protected lateinit var mTitle: String
    protected var failureUrl: String? = null
    protected var mIsHideTitle = true

    protected var mWebView: WebView? = null
    protected lateinit var DefaultTitleBar: DefaultTitleBar
    protected lateinit var frameLayoutLoading: DefaultFrameLoadingLayout

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        @SuppressLint("InflateParams")
        val contentView = LayoutInflater.from(activity).inflate(R.layout.default_web_fragment, null)
        DefaultTitleBar = contentView.findViewById(R.id.DefaultTitleBar) as DefaultTitleBar
        mWebView = contentView.findViewById(R.id.mWebView) as WebView
        frameLayoutLoading = contentView.findViewById(R.id.mFrameLayoutLoading) as DefaultFrameLoadingLayout

        //set color size start======================================================================
        var titleBarBgColor = 0
        var titleBarBgRes = 0
        var titleBarTextColor = 0
        var titleBarTextSize = 0
        val bundle = arguments
        if (bundle != null) {
            mUrl = bundle.getString(KEY_URL)
            mHtmlData = bundle.getString(KEY_DATA, "")
            mTitle = bundle.getString(KEY_TITLE)
            failureUrl = bundle.getString("failureUrl", null)
            mIsHideTitle = bundle.getBoolean(KEY_HIDE_TITLE, true)
            titleBarBgColor = bundle.getInt("titleBarBgColor", defaultTitleBarBgColor)
            titleBarBgRes = bundle.getInt("titleBarBgRes", defaultTitleBarBgRes)
            titleBarTextColor = bundle.getInt("titleBarTextColor", defaultTitleBarTextColor)
            titleBarTextSize = bundle.getInt("titleBarTextSize", defaultTitleBarTextSize)
        }
        if (titleBarBgColor <= 0) titleBarBgColor = defaultTitleBarBgColor
        if (titleBarBgRes <= 0) titleBarBgRes = defaultTitleBarBgRes
        if (titleBarTextColor <= 0) titleBarTextColor = defaultTitleBarTextColor
        if (titleBarTextSize <= 0) titleBarTextSize = defaultTitleBarTextSize
        DefaultTitleBar.titleText.text = mTitle
        DefaultTitleBar.visibility = if (mIsHideTitle) View.GONE else View.VISIBLE
        DefaultTitleBar.titleText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, titleBarTextSize.toFloat())
        DefaultTitleBar.titleText.setTextColor(titleBarTextColor)
        DefaultTitleBar.setBackgroundResource(titleBarBgRes)
        if (defaultTitleBarBgColor > 0)
            DefaultTitleBar.setBackgroundColor(titleBarBgColor)
        //set color size end  ======================================================================

        initWebView()

        if (!URLUtil.isValidUrl(mUrl)) {
            if (URLUtil.isValidUrl(failureUrl))
                mWebView!!.loadUrl(failureUrl)
            else
                frameLayoutLoading.showView(DefaultFrameLoadingLayout.ViewType.NETWORK_EXCEPTION, "加载失败,Url不正确:\n" + mUrl, false, true)
        } else {
            frameLayoutLoading.setOnRefreshClickListener { mWebView!!.loadUrl(mUrl) }
            if (!TextUtils.isEmpty(mHtmlData)) {
                mWebView!!.loadDataWithBaseURL(null, mHtmlData, "text/html", "utf-8", null)
            } else
                mWebView!!.loadUrl(mUrl)
        }
        return contentView
    }


    protected fun initWebView() {
        DefaultWebViewUtil.initWebView(mWebView)
        //mWebView.addJavascriptInterface(null, null);
        mWebView!!.setWebViewClient(object : WebViewClient() {

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                view.stopLoading()
                if (TextUtils.isEmpty(failureUrl)) {
                    mWebView!!.goBack()
                    frameLayoutLoading.showView(DefaultFrameLoadingLayout.ViewType.NETWORK_EXCEPTION,
                            if (error == null) frameLayoutLoading.getDefaultText(DefaultFrameLoadingLayout.ViewType.NETWORK_EXCEPTION) else "error",
                            false, true)
                } else {
                    view.loadUrl(failureUrl)
                }
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                Log.d("HTML5", "shouldOverrideUrlLoading:" + url)
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                super.onPageStarted(view, url, favicon)
                Log.d("HTML5", "onPageStarted:" + url)
                frameLayoutLoading.showView(DefaultFrameLoadingLayout.ViewType.LOADING)
            }

            override fun onPageFinished(view: WebView, url: String) {
                Log.d("HTML5", "onPageFinished:" + url)
                frameLayoutLoading.hideAll()
            }
        })
        mWebView!!.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                Log.d("HTML5", "onProgressChanged:" + newProgress)
                frameLayoutLoading.updateText(DefaultFrameLoadingLayout.ViewType.LOADING, frameLayoutLoading.getDefaultText(DefaultFrameLoadingLayout.ViewType.NODATA) + " " + newProgress + "%", false, true)
            }

            override fun onConsoleMessage(message: String, lineNumber: Int, sourceID: String) {
                Log.w("HTML5", "#$lineNumber:$sourceID")
                Log.d("HTML5", message)
            }

            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                Log.d("HTML5", "onReceivedTitle:" + title)
                if (!mIsHideTitle && TextUtils.isEmpty(mTitle)) {
                    DefaultTitleBar.titleText.text = mTitle
                }
            }

            override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
                Log.d("HTML5", "onJsAlert:" + message)
                return super.onJsAlert(view, url, message, result)
            }
        })
    }

    override fun onBackPressed(): Boolean {
        if (mWebView != null && mWebView!!.canGoBack()) {
            mWebView!!.goBack()
            return true
        }
        return false
    }

    companion object {
        protected var TAG = "MWebFragment"
        protected val KEY_URL = "key_url"
        protected val KEY_DATA = "key_data"
        protected val KEY_TITLE = "key_title"

        protected val KEY_HIDE_TITLE = "key_hide_title"

        val SCHEME = "native"
        val HOST = "home"

        @ColorInt
        protected var defaultTitleBarBgColor: Int = 0
        @DrawableRes
        protected var defaultTitleBarBgRes: Int = R.drawable.default_b_e2_white_normal_shape
        protected var defaultTitleBarTextColor = Color.BLACK
        protected var defaultTitleBarTextSize = 18

        fun goToCompleteHttpUrl(activity: Activity, url: String) {
            goTo(activity, DefaultWebViewUtil.getCompleteHttpUrl(url), false)
        }

        fun goToCompleteHttpUrl(activity: Activity, url: String, title: String) {
            goTo(activity, DefaultWebViewUtil.getCompleteHttpUrl(url), title, null)
        }

        fun goToCompleteHttpUrl(activity: Activity, url: String, title: String, failureUrl: String) {
            goTo(activity, DefaultWebViewUtil.getCompleteHttpUrl(url), false, title, 0, 0, 0, 0, failureUrl)
        }

        fun goToCompleteHttpUrl(activity: Activity, url: String, isHideTitle: Boolean) {
            goTo(activity, DefaultWebViewUtil.getCompleteHttpUrl(url), isHideTitle, null)
        }

        fun goToCompleteHttpUrl(activity: Activity, url: String, isHideTitle: Boolean, title: String,
                                titleBarBgColor: Int,
                                titleBarBgRes: Int,
                                titleBarTextColor: Int,
                                titleBarTextSize: Int,
                                failureUrl: String
        ) {
            goTo(activity, DefaultWebViewUtil.getCompleteHttpUrl(url), isHideTitle, title, titleBarBgColor, titleBarBgRes, titleBarTextColor, titleBarTextSize, failureUrl)
        }

        @JvmOverloads fun goTo(activity: Activity, url: String, title: String, failureUrl: String? = null) {
            goTo(activity, url, false, title, 0, 0, 0, 0, failureUrl)
        }

        @JvmOverloads fun goTo(activity: Activity, url: String, isHideTitle: Boolean = false, failureUrl: String? = null) {
            goTo(activity, url, isHideTitle, null, 0, 0, 0, 0, failureUrl)
        }

        fun goTo(activity: Activity, url: String, isHideTitle: Boolean, title: String?,
                 titleBarBgColor: Int,
                 titleBarBgRes: Int,
                 titleBarTextColor: Int,
                 titleBarTextSize: Int,
                 failureUrl: String?
        ) {
            if (activity.isFinishing) {
                Log.e(TAG, "上下文无效")
                return
            }
            val bundle = Bundle()
            bundle.putString(KEY_URL, url)
            bundle.putBoolean(KEY_HIDE_TITLE, isHideTitle)
            bundle.putString(KEY_TITLE, title)
            bundle.putString("failureUrl", failureUrl)
            bundle.putInt("titleBarBgColor", titleBarBgColor)
            bundle.putInt("titleBarBgRes", titleBarBgRes)
            bundle.putInt("titleBarTextColor", titleBarTextColor)
            bundle.putInt("titleBarTextSize", titleBarTextSize)
            DefaultActivity.start(activity, DefaultWebFragment::class.java, bundle)
        }

        fun goToWithData(activity: Activity, htmlStr: String, isHideTitle: Boolean, title: String,
                         titleBarBgColor: Int,
                         titleBarBgRes: Int,
                         titleBarTextColor: Int,
                         titleBarTextSize: Int,
                         failureUrl: String
        ) {
            if (activity.isFinishing) {
                Log.e(TAG, "上下文无效")
                return
            }
            val bundle = Bundle()
            bundle.putString(KEY_DATA, htmlStr)
            bundle.putBoolean(KEY_HIDE_TITLE, isHideTitle)
            bundle.putString(KEY_TITLE, title)
            bundle.putString("failureUrl", failureUrl)
            bundle.putInt("titleBarBgColor", titleBarBgColor)
            bundle.putInt("titleBarBgRes", titleBarBgRes)
            bundle.putInt("titleBarTextColor", titleBarTextColor)
            bundle.putInt("titleBarTextSize", titleBarTextSize)
            DefaultActivity.start(activity, DefaultWebFragment::class.java, bundle)
        }
    }
}
