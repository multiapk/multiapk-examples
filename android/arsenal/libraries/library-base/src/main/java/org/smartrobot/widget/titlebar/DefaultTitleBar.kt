package org.smartrobot.widget.titlebar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import org.smartrobot.R
import org.smartrobot.util.DefaultSystemUtil

/**
 * TitleView样式一
 * “左0--左1(hide)--Title--右1(hide)--右0”
 * “图片、颜色、文字”
 * xmlns:mlibrary="http://schemas.android.com/apk/res-auto"
 */
open class DefaultTitleBar(val mContext: Context, attrs: AttributeSet?) : RelativeLayout(mContext, attrs) {

    lateinit var rootLayout: RelativeLayout

    lateinit var titleText: TextView

    lateinit var left0BgView: RelativeLayout
    lateinit var left0Btn: TextView

    lateinit var left1BgView: RelativeLayout
    lateinit var left1Btn: TextView

    lateinit var right0BgView: RelativeLayout
    lateinit var right0Btn: TextView

    lateinit var right1BgView: RelativeLayout
    lateinit var right1Btn: TextView
    private var mScaledDensity = 3f
    private var mIsLeft0Set = false

    //private int DEFAULT_HEIGHT = 96;

    constructor(context: Context) : this(context, null) {}

    init {
        mScaledDensity = resources.displayMetrics.scaledDensity
        //DEFAULT_HEIGHT = (int) getResources().getDimension(R.dimen.default_titlebar_height);
        val DefaultTitleBarIconPadding = resources.getDimensionPixelSize(R.dimen.default_titlebar_icon_padding)

        initView()

        if (attrs != null) {
            val typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.DefaultTitleBar)
            //title
            setText(titleText, R.styleable.DefaultTitleBar_titleText, null, typedArray)
            setTextColor(titleText, R.styleable.DefaultTitleBar_titleTxtColor, -1, typedArray)
            setTextSize(titleText, R.styleable.DefaultTitleBar_titleSize, -1f, typedArray)
            setTextAppearance(titleText, R.styleable.DefaultTitleBar_titleAppearance, typedArray)
            //left0
            setBackground(left0BgView, R.styleable.DefaultTitleBar_left0BgViewBackground, -1, R.drawable.default_selector, typedArray)
            setLayoutParams(left0BgView, left0Btn, R.styleable.DefaultTitleBar_left0BgWidth, typedArray)
            setBgPadding(left0BgView, R.styleable.DefaultTitleBar_left0BgViewPadding, R.styleable.DefaultTitleBar_left0BgPaddingLeft, R.styleable.DefaultTitleBar_left0BgPaddingRight, R.styleable.DefaultTitleBar_left0BgPaddingTop, R.styleable.DefaultTitleBar_left0BgPaddingBottom, DefaultTitleBarIconPadding, typedArray)
            left0BgView.visibility = typedArray.getInt(R.styleable.DefaultTitleBar_left0Visible, View.VISIBLE)
            setTextAppearance(left0Btn, R.styleable.DefaultTitleBar_left0Appearance, typedArray)
            val transparentColor = resources.getColor(android.R.color.transparent)
            setBackground(left0Btn, R.styleable.DefaultTitleBar_left0Background, transparentColor, -1, typedArray)
            setText(left0Btn, R.styleable.DefaultTitleBar_left0Text, null, typedArray)
            setTextColor(left0Btn, R.styleable.DefaultTitleBar_left0TextColor, -1, typedArray)
            setTextSize(left0Btn, R.styleable.DefaultTitleBar_left0TextSize, -1f, typedArray)
            //left1
            setBackground(left1BgView, R.styleable.DefaultTitleBar_left1BgViewBackground, -1, R.drawable.default_selector, typedArray)
            setLayoutParams(left1BgView, left1Btn, R.styleable.DefaultTitleBar_left1BgWidth, typedArray)
            setBgPadding(left1BgView, R.styleable.DefaultTitleBar_left1BgViewPadding, R.styleable.DefaultTitleBar_left1BgPaddingLeft, R.styleable.DefaultTitleBar_left1BgPaddingRight, R.styleable.DefaultTitleBar_left1BgPaddingTop, R.styleable.DefaultTitleBar_left1BgPaddingBottom, DefaultTitleBarIconPadding, typedArray)
            left1BgView.visibility = typedArray.getInt(R.styleable.DefaultTitleBar_left1Visible, View.INVISIBLE)
            setTextAppearance(left1Btn, R.styleable.DefaultTitleBar_left1Appearance, typedArray)
            setBackground(left1Btn, R.styleable.DefaultTitleBar_left1Background, transparentColor, -1, typedArray)
            setText(left1Btn, R.styleable.DefaultTitleBar_left1Text, null, typedArray)
            setTextColor(left1Btn, R.styleable.DefaultTitleBar_left1TextColor, -1, typedArray)
            setTextSize(left1Btn, R.styleable.DefaultTitleBar_left1TextSize, -1f, typedArray)
            //right0
            setBackground(right0BgView, R.styleable.DefaultTitleBar_right0BgViewBackground, -1, R.drawable.default_selector, typedArray)
            setLayoutParams(right0BgView, right0Btn, R.styleable.DefaultTitleBar_right0BgWidth, typedArray)
            setBgPadding(right0BgView, R.styleable.DefaultTitleBar_right0BgViewPadding, R.styleable.DefaultTitleBar_right0BgPaddingLeft, R.styleable.DefaultTitleBar_right0BgPaddingRight, R.styleable.DefaultTitleBar_right0BgPaddingTop, R.styleable.DefaultTitleBar_right0BgPaddingBottom, DefaultTitleBarIconPadding, typedArray)
            right0BgView.visibility = typedArray.getInt(R.styleable.DefaultTitleBar_right0Visible, View.INVISIBLE)
            setTextAppearance(right0Btn, R.styleable.DefaultTitleBar_right0Appearance, typedArray)
            setBackground(right0Btn, R.styleable.DefaultTitleBar_right0Background, transparentColor, -1, typedArray)
            setText(right0Btn, R.styleable.DefaultTitleBar_right0Text, null, typedArray)
            setTextColor(right0Btn, R.styleable.DefaultTitleBar_right0TextColor, -1, typedArray)
            setTextSize(right0Btn, R.styleable.DefaultTitleBar_right0TextSize, -1f, typedArray)
            //right1
            setBackground(right1BgView, R.styleable.DefaultTitleBar_right1BgViewBackground, -1, R.drawable.default_selector, typedArray)
            setLayoutParams(right1BgView, right1Btn, R.styleable.DefaultTitleBar_right1BgWidth, typedArray)
            setBgPadding(right1BgView, R.styleable.DefaultTitleBar_right1BgViewPadding, R.styleable.DefaultTitleBar_right1BgPaddingLeft, R.styleable.DefaultTitleBar_right1BgPaddingRight, R.styleable.DefaultTitleBar_right1BgPaddingTop, R.styleable.DefaultTitleBar_right1BgPaddingBottom, DefaultTitleBarIconPadding, typedArray)
            right1BgView.visibility = typedArray.getInt(R.styleable.DefaultTitleBar_right1Visible, View.INVISIBLE)
            setTextAppearance(right1Btn, R.styleable.DefaultTitleBar_right1Appearance, typedArray)
            setBackground(right1Btn, R.styleable.DefaultTitleBar_right1Background, transparentColor, -1, typedArray)
            setText(right1Btn, R.styleable.DefaultTitleBar_right1Text, null, typedArray)
            setTextColor(right1Btn, R.styleable.DefaultTitleBar_right1TextColor, -1, typedArray)
            setTextSize(right1Btn, R.styleable.DefaultTitleBar_right1TextSize, -1f, typedArray)
            typedArray.recycle()

            if (!mIsLeft0Set) {
                setBackground(left0Btn, R.styleable.DefaultTitleBar_left0Background, transparentColor, R.drawable.default_menu_back_left_black, typedArray)
            }
        }
        left0BgView.setOnClickListener { DefaultSystemUtil.sendKeyBackEvent(context) }
        initSize()
    }

    private fun initView() {
        val titleBarLayout = LayoutInflater.from(mContext).inflate(R.layout.default_titlebar_layout_2, this, true)
        rootLayout = titleBarLayout.findViewById(R.id.rootLayout) as RelativeLayout
        titleText = titleBarLayout.findViewById(R.id.titleText) as TextView
        left0BgView = titleBarLayout.findViewById(R.id.left0BgView) as RelativeLayout
        left0Btn = titleBarLayout.findViewById(R.id.left0Btn) as TextView
        left1BgView = titleBarLayout.findViewById(R.id.left1BgView) as RelativeLayout
        left1Btn = titleBarLayout.findViewById(R.id.left1Btn) as TextView
        right0BgView = titleBarLayout.findViewById(R.id.right0BgView) as RelativeLayout
        right0Btn = titleBarLayout.findViewById(R.id.right0Btn) as TextView
        right1BgView = titleBarLayout.findViewById(R.id.right1BgView) as RelativeLayout
        right1Btn = titleBarLayout.findViewById(R.id.right1Btn) as TextView
    }

    private fun initSize() {
        /*if (isInEditMode()) {
            processInitSize();
        }
        getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation")
                    public void onGlobalLayout() {
                        getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        processInitSize();
                    }
                }
        );*/
    }

    /*@SuppressWarnings("SuspiciousNameCombination")
    private void processInitSize() {
        int height = getMeasuredHeight();
        //强制 mHeight>= 60dp,ListView footerView 有可能出现==1 的情况
        height = (height <= 0 || height >= 280) ? DEFAULT_HEIGHT : height;
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        left0BgView.setLayoutParams(new LinearLayout.LayoutParams(height, height));
        left1BgView.setLayoutParams(new LinearLayout.LayoutParams(height, height));
        right1BgView.setLayoutParams(new LinearLayout.LayoutParams(height, height));
        right0BgView.setLayoutParams(new LinearLayout.LayoutParams(height, height));
    }*/

    private fun setLayoutParams(bgView: View, childView: View, index: Int, typedArray: TypedArray) {
        try {
            val layoutParams = bgView.layoutParams as RelativeLayout.LayoutParams
            var configWidth = typedArray.getDimensionPixelSize(index, -3).toFloat()
            if (configWidth == RelativeLayout.LayoutParams.WRAP_CONTENT.toFloat() || configWidth >= 0)
                layoutParams.width = configWidth.toInt()
            else if (configWidth == -3f) {
                val resId = typedArray.getResourceId(index, -1)
                if (resId != -1) {
                    configWidth = resources.getDimensionPixelSize(resId).toFloat()
                    layoutParams.width = configWidth.toInt()
                }
            }
            layoutParams.width = if (configWidth == RelativeLayout.LayoutParams.WRAP_CONTENT.toFloat() || configWidth >= 0) layoutParams.width else RelativeLayout.LayoutParams.WRAP_CONTENT
            bgView.layoutParams = layoutParams
            if (layoutParams.width != RelativeLayout.LayoutParams.WRAP_CONTENT) {
                val childLayoutParams = childView.layoutParams as RelativeLayout.LayoutParams
                childLayoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT
                childView.layoutParams = childLayoutParams
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setBgPadding(bgView: View, indexPadding: Int, indexLeft: Int, indexRight: Int, indexTop: Int, indexBottom: Int, defaultPadding: Int, typedArray: TypedArray) {
        var defaultPadding = defaultPadding
        try {
            var padding = typedArray.getDimensionPixelSize(indexPadding, -10000).toFloat()
            var paddingLeft = typedArray.getDimensionPixelSize(indexLeft, -10000).toFloat()
            var paddingRight = typedArray.getDimensionPixelSize(indexRight, -10000).toFloat()
            var paddingTop = typedArray.getDimensionPixelSize(indexTop, -10000).toFloat()
            var paddingBottom = typedArray.getDimensionPixelSize(indexBottom, -10000).toFloat()
            if (padding == -10000f) {
                val resId = typedArray.getResourceId(indexPadding, -10000)
                if (resId != -10000) {
                    padding = resources.getDimensionPixelSize(resId).toFloat()
                }
            }
            if (paddingLeft == -10000f) {
                val resId = typedArray.getResourceId(indexLeft, -10000)
                if (resId != -10000) {
                    paddingLeft = resources.getDimensionPixelSize(resId).toFloat()
                }
            }
            if (paddingRight == -10000f) {
                val resId = typedArray.getResourceId(indexRight, -10000)
                if (resId != -10000) {
                    paddingRight = resources.getDimensionPixelSize(resId).toFloat()
                }
            }
            if (paddingTop == -10000f) {
                val resId = typedArray.getResourceId(indexTop, -10000)
                if (resId != -10000) {
                    paddingTop = resources.getDimensionPixelSize(resId).toFloat()
                }
            }
            if (paddingBottom == -10000f) {
                val resId = typedArray.getResourceId(indexBottom, -10000)
                if (resId != -10000) {
                    paddingBottom = resources.getDimensionPixelSize(resId).toFloat()
                }
            }
            defaultPadding = if (padding == -10000f) defaultPadding else padding.toInt()
            bgView.setPadding(
                    if (paddingLeft == -10000f) defaultPadding else paddingLeft.toInt(),
                    if (paddingTop == -10000f) defaultPadding else paddingTop.toInt(),
                    if (paddingRight == -10000f) defaultPadding else paddingRight.toInt(),
                    if (paddingBottom == -10000f) defaultPadding else paddingBottom.toInt())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setBackground(view: View, index: Int, defaultColor: Int, defaultRes: Int, typedArray: TypedArray) {
        try {
            if (defaultColor != -1)
                view.setBackgroundColor(defaultColor)
            if (defaultRes != -1)
                view.setBackgroundResource(defaultRes)

            val drawable = typedArray.getDrawable(index)
            if (drawable != null) {
                view.setBackgroundDrawable(drawable)
                resetVisible(view)
            } else {
                val color = typedArray.getColor(index, Integer.MAX_VALUE)
                if (color != Integer.MAX_VALUE) {
                    view.setBackgroundColor(color)
                    resetVisible(view)
                } else {
                    val colorStr = typedArray.getString(index)
                    if (!TextUtils.isEmpty(colorStr)) {
                        try {
                            view.setBackgroundColor(Color.parseColor(colorStr))
                            resetVisible(view)
                        } catch (ignored: Exception) {
                        }

                    } else {
                        val resId = typedArray.getResourceId(index, -1)
                        if (resId != -1) {
                            view.setBackgroundResource(resId)
                            resetVisible(view)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setText(textView: TextView, index: Int, defaultString: String?, typedArray: TypedArray) {
        var defaultString = defaultString
        try {
            textView.text = defaultString
            defaultString = typedArray.getString(index)
            if (defaultString == null) {
                val resId = typedArray.getResourceId(index, -1)
                if (resId != -1) {
                    textView.text = resources.getString(resId)
                    resetViewPadding(textView)
                }
            } else {
                textView.text = defaultString
                resetViewPadding(textView)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun resetViewPadding(textView: TextView) {
        /*int viewId = textView.getId();
        if (viewId == R.id.right0Btn) {
            right0BgView.setPadding(0, 0, 0, 0);
        } else if (viewId == R.id.right1Btn) {
            right1BgView.setPadding(0, 0, 0, 0);
        } else if (viewId == R.id.left0Btn) {
            left0BgView.setPadding(0, 0, 0, 0);
        } else if (viewId == R.id.left1Btn) {
            left1BgView.setPadding(0, 0, 0, 0);
        }*/
        resetVisible(textView)
    }

    private fun resetVisible(view: View) {
        val viewId = view.id
        if (viewId == R.id.right0Btn || viewId == R.id.right0BgView) {
            right0BgView.visibility = View.VISIBLE
        } else if (viewId == R.id.right1Btn || viewId == R.id.right1BgView) {
            right1BgView.visibility = View.VISIBLE
        } else if (viewId == R.id.left0Btn || viewId == R.id.left0BgView) {
            mIsLeft0Set = true
            left0BgView.visibility = View.VISIBLE
        } else if (viewId == R.id.left1Btn || viewId == R.id.left1BgView) {
            left1BgView.visibility = View.VISIBLE
        }
    }

    private fun setTextColor(textView: TextView, index: Int, defaultColor: Int, typedArray: TypedArray) {
        try {
            if (defaultColor != -1)
                textView.setTextColor(defaultColor)

            val color = typedArray.getColor(index, Integer.MAX_VALUE)
            if (color != Integer.MAX_VALUE) {
                textView.setTextColor(color)
            } else {
                val colorStr = typedArray.getString(index)
                if (!TextUtils.isEmpty(colorStr)) {
                    try {
                        textView.setTextColor(Color.parseColor(colorStr))
                    } catch (ignored: Exception) {
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setTextSize(textView: TextView, index: Int, defaultSize: Float, typedArray: TypedArray) {
        var defaultSize = defaultSize
        try {
            if (defaultSize != -1f)
                textView.textSize = defaultSize
            defaultSize = typedArray.getDimension(index, -1f)
            if (defaultSize != -1f)
                textView.textSize = defaultSize / mScaledDensity
            else {
                val resId = typedArray.getResourceId(index, -1)
                if (resId != -1) {
                    defaultSize = resources.getDimension(resId)
                    textView.textSize = defaultSize / mScaledDensity
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setTextAppearance(textView: TextView, index: Int, typedArray: TypedArray) {
        try {
            val resId = typedArray.getResourceId(index, -1)
            if (resId != -1) {
                textView.setTextAppearance(mContext, resId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
