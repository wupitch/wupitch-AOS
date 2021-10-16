package wupitch.android.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import wupitch.android.util.LoadingDialog

abstract class BaseFragment<B : ViewBinding>(private val bind: (View) -> B, @LayoutRes layoutResId: Int, )
    : Fragment(layoutResId) {

    private var _binding: B? = null
    protected val binding get() = _binding!!

    lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = bind(super.onCreateView(inflater, container, savedInstanceState)!!)
        loadingDialog = LoadingDialog(requireContext())
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun showLoadingDialog(context: Context) {
        loadingDialog.show()
    }

    fun dismissLoadingDialog() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    fun showCustomToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }


}