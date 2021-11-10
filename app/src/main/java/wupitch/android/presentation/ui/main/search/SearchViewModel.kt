package wupitch.android.presentation.ui.main.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(

): ViewModel() {

    private var _searchKeyword = MutableLiveData<String>()
    val searchKeyword : LiveData<String> = _searchKeyword


    fun performSearch(category : Int, keyword : String) {
        _searchKeyword.value = keyword
        //todo search from server.
        Log.d("{SearchViewModel.performSearch}", "category : $category keyword : ${_searchKeyword.value}")

    }
}