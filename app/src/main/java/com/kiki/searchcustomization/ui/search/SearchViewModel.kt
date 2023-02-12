package com.kiki.searchcustomization.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiki.searchcustomization.data.DataStoreManager
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
class SearchViewModel @Inject constructor(
    repository: SearchRepository,
    dataStoreManager: DataStoreManager,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    fun setQuery(query: String) {
        _query.value = query
    }

    private val _searchResult =
        MutableStateFlow<Resource<List<SearchModel>>>(Resource.Success(emptyList()))
    val searchResult = _searchResult.asStateFlow()

    init {
        viewModelScope.launch {
            _query.collectLatest { query ->
                if (query.isNotBlank()) {
                    _searchResult.emit(Resource.Loading())
                    dataStoreManager.factors.collectLatest { factor ->
                        delay(1000)
                        repository.search(query, factor).collectLatest { result ->
                            if (result == emptyList<SearchModel>()) {
                                _searchResult.emit(Resource.Error("Tidak ada data"))
                            } else {
                                _searchResult.emit(Resource.Success(result))
                            }
                        }
                    }
                } else {
                    _searchResult.emit(Resource.Success(emptyList()))
                }
            }
        }
    }
}
