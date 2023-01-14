package com.kiki.searchcustomization.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiki.searchcustomization.data.Resource
import com.kiki.searchcustomization.data.repository.WartegRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: WartegRepository) : ViewModel() {

    val topWarteg = repository.getTopWarteg()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Resource.Loading())

    val warteg = repository.getAllWarteg()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Resource.Loading())
}