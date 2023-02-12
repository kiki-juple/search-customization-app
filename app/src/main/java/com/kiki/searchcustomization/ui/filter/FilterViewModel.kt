package com.kiki.searchcustomization.ui.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiki.searchcustomization.data.DataStoreManager
import com.kiki.searchcustomization.data.entity.Factor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(private val dataStoreManager: DataStoreManager) :
    ViewModel() {

    fun setFactor(factor: Factor) {
        viewModelScope.launch { dataStoreManager.setFactor(factor) }
    }

    val getFactor = dataStoreManager.factors
        .shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000)
        )
}