package com.kiki.searchcustomization.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.kiki.searchcustomization.data.repository.SplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: SplashRepository) : ViewModel() {

    fun saveLatLng(latLng: LatLng) = viewModelScope.launch {
        repository.saveLatLng(latLng)
    }

    fun updateWartegDistance() = viewModelScope.launch { repository.updateWartegDistance() }
}