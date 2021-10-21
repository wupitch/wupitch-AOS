package wupitch.android.common

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import wupitch.android.R

class CustomToolbar : Toolbar {

    lateinit var leftIcon: ImageView
    lateinit var title: TextView
    lateinit var rightIcon: ImageView
    lateinit var llRightIcon : LinearLayout
    lateinit var llLeftIcon : LinearLayout

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
        getAttrs(attrs)

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
        getAttrs(attrs, defStyleAttr)
    }

    private fun init(context: Context?) {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_toolbar, this, false)
        addView(view)

        leftIcon = findViewById(R.id.iv_toolbar_left)
        title = findViewById(R.id.tv_toolbar_title)
        rightIcon = findViewById(R.id.iv_toolbar_right)
        llRightIcon = findViewById(R.id.ll_toolbar_right)
        llLeftIcon = findViewById(R.id.ll_toolbar_left)
    }

    private fun getAttrs(attrs: AttributeSet?) {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar)
        setTypeArray(typedArray)
    }

    private fun getAttrs(attrs: AttributeSet?, defStyle: Int) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar, defStyle, 0)
        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {

        val leftIconDrawable = typedArray.getResourceId(R.styleable.CustomToolbar_leftIcon, 0)
        leftIcon.setImageResource(leftIconDrawable)

        val rightIconDrawable = typedArray.getResourceId(R.styleable.CustomToolbar_rightIcon, 0)
        rightIcon.setImageResource(rightIconDrawable)

        val text = typedArray.getText(R.styleable.CustomToolbar_titleText)
        title.text = text

        typedArray.recycle()
    }

    fun setLeftIconClickListener(listener: OnClickListener) {
        leftIcon.setOnClickListener(listener)
    }

    fun setRightIconClickListener(listener: OnClickListener) {
        rightIcon.setOnClickListener(listener)
    }
}