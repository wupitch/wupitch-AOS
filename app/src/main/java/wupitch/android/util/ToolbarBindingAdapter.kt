package wupitch.android.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import wupitch.android.common.CustomToolbar

@BindingAdapter("on_left_icon_click")
fun onLeftIconClick(view : CustomToolbar, onClick : () -> Unit) {
   view.llLeftIcon.setOnClickListener { onClick() }
}

@BindingAdapter("on_right_icon_click")
fun onRightIconClick(view : CustomToolbar, onClick : () -> Unit) {
   view.llRightIcon.setOnClickListener { onClick() }
}