package wupitch.android.util

import android.graphics.Typeface
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.forEachIndexed
import com.google.android.material.tabs.TabLayout
import wupitch.android.R

fun TabLayout.changeTabFont(selectedPosition : Int) {
    val vg = this.getChildAt(0) as ViewGroup
    val tabsCount = vg.childCount
    for( i in 0 until tabsCount){
        val vgTab = vg.getChildAt(i) as ViewGroup
        vgTab.forEachIndexed { index, _ ->
            val tabViewChild = vgTab.getChildAt(index)
            if(tabViewChild is TextView) {
                tabViewChild.setTextBold(i == selectedPosition)
            }
        }
    }
}

fun TextView.setTextBold (toBold : Boolean) {
    this.typeface = ResourcesCompat.getFont(this.context, if(toBold) R.font.roboto_bold else R.font.roboto_regular)
}