package wupitch.android.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding


abstract class BaseActivity<B : ViewBinding>(private val inflate: (LayoutInflater) -> B) :
    AppCompatActivity() {
    protected lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
    }


    // 토스트를 쉽게 띄울 수 있게 해줌.
    fun showCustomToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

//    fun setStatusBar(color : Int) {
//        window.statusBarColor = ContextCompat.getColor(this, color)
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
//        }else {
//            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        }
//    }
//
//    fun setNavBarColor() {
//        window.navigationBarColor = ContextCompat.getColor(this, R.color.bottom_nav_color)
//    }

}