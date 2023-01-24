package com.kiki.searchcustomization.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiki.searchcustomization.data.Resource
import com.kiki.searchcustomization.data.entity.SearchModel
import com.kiki.searchcustomization.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: SearchRepository) : ViewModel() {

    private val _searchResult = MutableStateFlow<Resource<List<SearchModel>>>(Resource.Loading())
    val searchResult = _searchResult.asStateFlow()

    private val _query = MutableStateFlow("")
    fun setQuery(query: String) {
        _query.value = query
    }

    init {
        viewModelScope.launch {
//            _query.collectLatest { query ->
//                Log.e("viewmodel", query)
//                if (query.isNotBlank()) {
//                    withContext(this.coroutineContext) {
//                        repository.searchWarteg(query)
//                    }.collectLatest {
//                        _searchResult.emit(it)
//                    }
//                }
//            }
            _query.collectLatest {
                delay(1500L)
                if (it.isNotBlank()) repository.searchWarteg(it).collectLatest { result ->
                    _searchResult.emit(result)
                }
            }
        }
    }

}