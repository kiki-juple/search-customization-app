package com.kiki.searchcustomization.data.repository

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.kiki.searchcustomization.data.DataStoreManager
import com.kiki.searchcustomization.data.dao.WartegDao
import com.kiki.searchcustomization.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SplashRepository @Inject constructor(
    private val dao: WartegDao,
    private val dataStoreManager: DataStoreManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun saveLatLng(latLng: LatLng) = withContext(ioDispatcher) {
        dataStoreManager.saveLatLng(latLng)
    }

    suspend fun updateWartegDistance() {
        withContext(ioDispatcher) {
            val allWarteg = async { dao.getWarteg() }
            allWarteg.await().collectLatest {
                dataStoreManager.latLng.collectLatest { userLatLng ->
                    it.forEach { warteg ->
                        val id = warteg.warteg.wartegId
                        val wartegLatLng = LatLng(
                            warteg.warteg.latitude,
                            warteg.warteg.longitude
                        )
                        val distance = SphericalUtil.computeDistanceBetween(
                            wartegLatLng,
                            userLatLng
                        ).div(1000)
                        dao.updateWartegDistance(id, distance)
                    }
                }
            }
        }
    }
}
