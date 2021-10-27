package wupitch.android.util

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TableLayout
import android.widget.TableRow
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class RadioBtnGroupTableLayout : TableLayout, View.OnClickListener {

    private var activeRadioButton: RadioButton? = null

    /**
     * @param context
     */
    constructor(context: Context?) : super(context) {
    }

    /**
     * @param context
     * @param attrs
     */
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
    }

    override fun onClick(v: View) {
        Log.d("{RadioBtnGroupTableLayout.onClick}", "clicked!")
        val rb = v as RadioButton
        if (activeRadioButton != null) {
            activeRadioButton!!.isChecked = false
        }
        rb.isChecked = true
        activeRadioButton = rb
        _checkedRadioBtnTag.value = activeRadioButton!!.tag.toString().toInt()
    }

    /* (non-Javadoc)
     * @see android.widget.TableLayout#addView(android.view.View, int, android.view.ViewGroup.LayoutParams)
     */
    override fun addView(
        child: View, index: Int,
        params: ViewGroup.LayoutParams?
    ) {
        super.addView(child, index, params)
        setChildrenOnClickListener(child as TableRow)
    }

    /* (non-Javadoc)
     * @see android.widget.TableLayout#addView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    override fun addView(child: View, params: ViewGroup.LayoutParams?) {
        super.addView(child, params)
        setChildrenOnClickListener(child as TableRow)
    }

    private fun setChildrenOnClickListener(tr: TableRow) {
        val c: Int = tr.childCount
        for (i in 0 until c) {
            val v: View = tr.getChildAt(i)
            if (v is RadioButton) {
                v.setOnClickListener(this)
            }
        }
    }

    private val _checkedRadioBtnTag = MutableLiveData<Int>()
    var checkedRadioBtnTag : LiveData<Int> = _checkedRadioBtnTag
}