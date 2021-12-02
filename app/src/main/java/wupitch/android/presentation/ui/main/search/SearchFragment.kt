package wupitch.android.presentation.ui.main.search

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentSearchBinding
import wupitch.android.util.changeTabFont

@AndroidEntryPoint
class SearchFragment
    : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::bind, R.layout.fragment_search) {

    private var selectedTab : Int = 0
    private lateinit var pagerAdapter: SearchVPAdapter
    val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("selected_tab")?.let { id ->
            selectedTab = id
        }
        arguments?.getInt("districtId")?.let { id ->
            viewModel.setDistrictId(if(id == -1) null else id)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPager()
        setEditTextListener()
        setEditTextButtons()
        setEditTextSearchListener()
        binding.viewpagerSearch.currentItem = selectedTab
        binding.tablayoutSearch.setScrollPosition(selectedTab, 0f, true)

    }

    private fun setEditTextSearchListener() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                viewModel.performSearch(binding.tablayoutSearch.selectedTabPosition, binding.etSearch.text.toString())
                setKeyboardDown()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setEditTextButtons() {
        binding.ivDelete.setOnClickListener {
            binding.etSearch.text.clear()
            binding.etSearch.apply {
                clearFocus()
                val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view?.windowToken, 0)
            }

        }
        binding.ivLeft.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setEditTextListener() {
        binding.etSearch.addTextChangedListener {
            it?.let {
                binding.ivDelete.isVisible = it.isNotEmpty()
            }
        }
    }

    private val tabLayoutOnPageChangeListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.position?.let {
                binding.tablayoutSearch.changeTabFont(it)
            }
        }
        override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
        override fun onTabReselected(tab: TabLayout.Tab?) = Unit
    }

    private fun setViewPager() {
        pagerAdapter = SearchVPAdapter(this)
        binding.viewpagerSearch.adapter = pagerAdapter

        TabLayoutMediator(binding.tablayoutSearch, binding.viewpagerSearch) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.search_tab_crew)
                else -> getString(R.string.search_tab_impromptu)
            }
        }.attach()

        binding.tablayoutSearch.apply {
            changeTabFont(0)
            addOnTabSelectedListener(tabLayoutOnPageChangeListener)
        }

    }

    private fun setKeyboardDown() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }


}