package org.smartrobot.widget.webview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebView

import org.smartrobot.base.DefaultBaseApplication

@SuppressLint("SetJavaScriptEnabled")
object DefaultWebViewUtil {
    fun getCompleteHttpUrl(url: String): String {
        var completeUrl = url
        if (!TextUtils.isEmpty(url) && !completeUrl.startsWith("http"))
            completeUrl = "http://" + completeUrl
        return completeUrl
    }

    fun initWebView(webView: WebView?, userAgent: String) {
        if (webView != null) {
            webView.settings.userAgentString = userAgent
            initWebView(webView, userAgent)
        }
    }

    fun initWebView(webView: WebView?) {
        if (webView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                //使用File协议(加载本地的html 例如：file:///sdcard/index.html )，存在跨源安全威胁
                webView.settings.allowFileAccessFromFileURLs = true
                webView.settings.allowUniversalAccessFromFileURLs = true
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                webView.settings.allowContentAccess = true
            }

            webView.isHorizontalScrollBarEnabled = true
            webView.isVerticalScrollBarEnabled = false

            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true
            webView.settings.useWideViewPort = false
            webView.settings.setSupportMultipleWindows(true)

            val dbPath = DefaultBaseApplication.instance.getDir("db_web_cache", Context.MODE_PRIVATE).path
            webView.settings.setAppCacheEnabled(true)
            webView.settings.setAppCachePath(dbPath)

            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            webView.setWebChromeClient(object : WebChromeClient() {
                override fun onConsoleMessage(message: String, lineNumber: Int, sourceID: String) {
                    Log.w("HTML5", "#$lineNumber:$sourceID")
                    Log.d("HTML5", message)
                }
            })
        }
    }
}
