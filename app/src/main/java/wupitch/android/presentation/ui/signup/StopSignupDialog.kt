package wupitch.android.presentation.ui.signup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import dagger.hilt.android.qualifiers.ApplicationContext
import wupitch.android.databinding.DialogStopSignupBinding

class StopSignupDialog (
    @ApplicationContext context : Context,
    val listener : OnStopSignupClick
        ): Dialog(context) {

    private lateinit var binding : DialogStopSignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogStopSignupBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setContentView(binding.root)

        binding.dialog = this

    }
}