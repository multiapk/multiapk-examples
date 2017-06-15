package org.smartrobot.widget.loading

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import org.smartrobot.R
import org.smartrobot.util.DefaultCustomViewUtil
import java.util.*

/**
 * 继承该类,在构造函数中添加 resetWithCustomViews,实现自定义布局
 * 默认要向上一点点不是居中的,因为titlebar占有一定的高度 宽高必须大于等于 120dp
 */
class DefaultFrameLoadingLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : FrameLayout(context, attrs, defStyle) {

    enum class ViewType {
        //没有数据
        NODATA,
        //加载中
        LOADING,
        //网络异常
        NETWORK_EXCEPTION
    }

    protected var mViewMaps = HashMap<ViewType, View>()

    protected var mInflater: LayoutInflater

    init {
        mInflater = LayoutInflater.from(context)

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DefaultFrameLoadingLayout)
            if (typedArray != null) {
                val loadingLayoutId = typedArray.getResourceId(R.styleable.DefaultFrameLoadingLayout_loadingView, R.layout.default_frameloading_loading_layout)
                val networkExceptionLayoutId = typedArray.getResourceId(R.styleable.DefaultFrameLoadingLayout_failureView, R.layout.default_frameloading_failure_layout)
                val nodataLayoutId = typedArray.getResourceId(R.styleable.DefaultFrameLoadingLayout_emptyView, R.layout.default_frameloading_empty_layout)
                val centerInParent = DefaultCustomViewUtil.getBoolean(typedArray, R.styleable.DefaultFrameLoadingLayout_centerInParent, false)
                val useSmallStyle = DefaultCustomViewUtil.getBoolean(typedArray, R.styleable.DefaultFrameLoadingLayout_useSmallStyle, false)
                val defaultScaleFactor = 0.6f
                var scaleFactor = DefaultCustomViewUtil.getFloat(typedArray, R.styleable.DefaultFrameLoadingLayout_scaleFactor, defaultScaleFactor)
                if (scaleFactor <= 0 || scaleFactor > 1)
                    scaleFactor = defaultScaleFactor
                val drawable = DefaultCustomViewUtil.getDrawable(typedArray, R.styleable.DefaultFrameLoadingLayout_allBackground, Color.parseColor("#FFFFFFFE"), -1)
                typedArray.recycle()

                resetWithCustomViews(loadingLayoutId, networkExceptionLayoutId, nodataLayoutId)
                enableCenterInParent(centerInParent)
                enableUseSmallStyle(useSmallStyle, scaleFactor)
                setViewsBackground(drawable)
            }
        }
    }

    //在构造函数最后
    fun resetWithCustomViews(loadingLayoutId: Int, networkExceptionLayoutId: Int, nodataLayoutId: Int) {
        //remove oldViews
        removeView(mViewMaps[ViewType.LOADING])
        removeView(mViewMaps[ViewType.NETWORK_EXCEPTION])
        removeView(mViewMaps[ViewType.NODATA])
        //add newViews
        mViewMaps.put(ViewType.LOADING, mInflater.inflate(loadingLayoutId, null))
        mViewMaps.put(ViewType.NETWORK_EXCEPTION, mInflater.inflate(networkExceptionLayoutId, null))
        mViewMaps.put(ViewType.NODATA, mInflater.inflate(nodataLayoutId, null))
        addView(mViewMaps[ViewType.LOADING], FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER))
        addView(mViewMaps[ViewType.NETWORK_EXCEPTION], FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER))
        addView(mViewMaps[ViewType.NODATA], FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER))
        hideAll()
    }

    fun showView(viewType: ViewType) {
        processViewToFrontByType(viewType)
    }

    private fun processViewToFrontByType(viewType: ViewType): View? {
        for ((key, view) in mViewMaps) {
            if (key == viewType) {
                view.visibility = View.VISIBLE
                view.bringToFront()
                return view
            } else {
                view.visibility = View.GONE
            }
        }
        return null
    }

    @JvmOverloads fun showView(viewType: ViewType, text: String, appendToNewLine: Boolean = false, removeOldAppend: Boolean = true) {
        val view = processViewToFrontByType(viewType)
        if (view != null) {
            try {
                var textView: TextView? = null
                when (viewType) {
                    DefaultFrameLoadingLayout.ViewType.NODATA -> textView = view.findViewById(R.id.text_empty) as TextView
                    DefaultFrameLoadingLayout.ViewType.LOADING -> textView = view.findViewById(R.id.text_loading) as TextView
                    DefaultFrameLoadingLayout.ViewType.NETWORK_EXCEPTION -> textView = view.findViewById(R.id.text_failure) as TextView
                }
                textView.text = if (appendToNewLine) (if (removeOldAppend) getDefaultText(viewType) else textView.text).toString() + "\n" + text else text
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun updateText(viewType: ViewType, text: String, appendToNewLine: Boolean, removeOldAppend: Boolean) {
        val view = mViewMaps[viewType]
        if (view != null) {
            try {
                var textView: TextView? = null
                when (viewType) {
                    DefaultFrameLoadingLayout.ViewType.NODATA -> textView = view.findViewById(R.id.text_empty) as TextView
                    DefaultFrameLoadingLayout.ViewType.LOADING -> textView = view.findViewById(R.id.text_loading) as TextView
                    DefaultFrameLoadingLayout.ViewType.NETWORK_EXCEPTION -> textView = view.findViewById(R.id.text_failure) as TextView
                }
                textView.text = if (appendToNewLine) (if (removeOldAppend) getDefaultText(viewType) else textView.text).toString() + "\n" + text else text
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun hideView(viewType: ViewType) {
        val view = mViewMaps[viewType]
        if (view != null)
            view.visibility = View.GONE
    }

    fun getDefaultText(viewType: ViewType): String {
        var text: String? = null
        when (viewType) {
            DefaultFrameLoadingLayout.ViewType.NODATA -> text = resources.getString(R.string.default_frameloading_empty)
            DefaultFrameLoadingLayout.ViewType.LOADING -> text = resources.getString(R.string.default_frameloading_loadingnow)
            DefaultFrameLoadingLayout.ViewType.NETWORK_EXCEPTION -> text = resources.getString(R.string.default_frameloading_networkerror)
        }
        return text
    }

    fun getText(viewType: ViewType): String? {
        var text: String? = null
        val view = mViewMaps[viewType]
        if (view != null) {
            var textView: TextView? = null
            when (viewType) {
                DefaultFrameLoadingLayout.ViewType.NODATA -> textView = view.findViewById(R.id.text_empty) as TextView
                DefaultFrameLoadingLayout.ViewType.LOADING -> textView = view.findViewById(R.id.text_loading) as TextView
                DefaultFrameLoadingLayout.ViewType.NETWORK_EXCEPTION -> textView = view.findViewById(R.id.text_failure) as TextView
            }
            text = textView.text.toString().trim { it <= ' ' }
        }
        return text
    }

    //默认要向上一点点不是居中的,因为titlebar占有一定的高度  宽高必须大于等于 120dp
    fun enableCenterInParent(enableCenterLayout: Boolean) {
        try {
            for ((key, view) in mViewMaps) {
                when (key) {
                    DefaultFrameLoadingLayout.ViewType.NODATA -> {
                        view.findViewById(R.id.topEmptyView_empty).visibility = if (enableCenterLayout) View.GONE else View.VISIBLE
                        view.findViewById(R.id.bottomEmptyView_empty).visibility = if (enableCenterLayout) View.GONE else View.VISIBLE
                    }
                    DefaultFrameLoadingLayout.ViewType.LOADING -> {
                        view.findViewById(R.id.topEmptyView_loading).visibility = if (enableCenterLayout) View.GONE else View.VISIBLE
                        view.findViewById(R.id.bottomEmptyView_loading).visibility = if (enableCenterLayout) View.GONE else View.VISIBLE
                    }
                    DefaultFrameLoadingLayout.ViewType.NETWORK_EXCEPTION -> {
                        view.findViewById(R.id.topEmptyView_failure).visibility = if (enableCenterLayout) View.GONE else View.VISIBLE
                        view.findViewById(R.id.bottomEmptyView_failure).visibility = if (enableCenterLayout) View.GONE else View.VISIBLE
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @Suppress("DEPRECATION")
    fun setViewsBackground(drawable: Drawable?) {
        for ((_, view) in mViewMaps) {
            view.setBackgroundDrawable(drawable)
        }
    }

    fun enableUseSmallStyle(enableUseSmallStyle: Boolean, scaleFactor: Float) {
        try {
            for ((key, view) in mViewMaps) {
                var imageView: View? = null
                var textView: TextView? = null
                when (key) {
                    DefaultFrameLoadingLayout.ViewType.NODATA -> {
                        textView = view.findViewById(R.id.text_empty) as TextView
                        imageView = view.findViewById(R.id.imageView_empty)
                    }
                    DefaultFrameLoadingLayout.ViewType.LOADING -> {
                        textView = view.findViewById(R.id.text_loading) as TextView
                        imageView = view.findViewById(R.id.imageView_loading)
                    }
                    DefaultFrameLoadingLayout.ViewType.NETWORK_EXCEPTION -> {
                        textView = view.findViewById(R.id.text_failure) as TextView
                        imageView = view.findViewById(R.id.imageView_failure)
                    }
                }

                if (imageView != null) {
                    val layoutParams = imageView.layoutParams as LinearLayout.LayoutParams
                    layoutParams.height = getPxFromDp(if (enableUseSmallStyle) 60 * scaleFactor else 60F)
                    layoutParams.width = getPxFromDp(if (enableUseSmallStyle) 60 * scaleFactor else 60F)
                    imageView.layoutParams = layoutParams
                }

                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (if (enableUseSmallStyle) 10 else 14).toFloat())
                textView.setTypeface(null, if (enableUseSmallStyle) Typeface.NORMAL else Typeface.BOLD)
                textView.paint.isFakeBoldText = !enableUseSmallStyle
                textView.setPadding(
                        getPxFromDp((if (enableUseSmallStyle) 10 else 30).toFloat()),
                        getPxFromDp((if (enableUseSmallStyle) 3 else 15).toFloat()),
                        getPxFromDp((if (enableUseSmallStyle) 10 else 30).toFloat()),
                        getPxFromDp((if (enableUseSmallStyle) 3 else 15).toFloat()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getPxFromDp(value: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics).toInt()
    }

    fun hideAll() {
        for ((_, view) in mViewMaps) {
            if (true)
                view.visibility = View.GONE
        }
    }

    fun setOnRefreshClickListener(listener: View.OnClickListener) {
        val refreshView = mViewMaps[ViewType.NETWORK_EXCEPTION]
        refreshView?.setOnClickListener { v -> listener.onClick(v) }
    }

    fun setOnClickListener(viewType: ViewType, listener: View.OnClickListener) {
        for ((key, view) in mViewMaps) {
            if (key == viewType)
                view.setOnClickListener(listener)
        }
    }
}
