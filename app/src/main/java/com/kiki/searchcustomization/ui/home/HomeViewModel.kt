package com.kiki.searchcustomization.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiki.searchcustomization.data.Resource
import com.kiki.searchcustomization.data.entity.WartegWithMenu
import com.kiki.searchcustomization.data.repository.WartegRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WartegRepository
) : ViewModel() {

    private val _topWarteg = MutableStateFlow<Resource<List<WartegWithMenu>>>(Resource.Loading())
    val topWarteg = _topWarteg.asStateFlow()

    private val _warteg = MutableStateFlow<Resource<List<WartegWithMenu>>>(Resource.Loading())
    val warteg = _warteg.asStateFlow()

    private val _cheapestWarteg =
        MutableStateFlow<Resource<List<WartegWithMenu>>>(Resource.Loading())
    val cheapestWarteg = _cheapestWarteg.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                repository.getAllWarteg().collectLatest {
                    _warteg.emit(it)
                }
            }
            launch {
                repository.getTopWarteg().collectLatest {
                    _topWarteg.emit(it)
                }
            }
            launch {
                repository.getCheapestWarteg().collectLatest {
                    _cheapestWarteg.emit(it)
                }
            }
        }
    }
}