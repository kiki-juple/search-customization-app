package com.kiki.searchcustomization.data

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("settings")

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.dataStore

    suspend fun saveLatLng(latLng: LatLng) = dataStore.edit {
        it[LAT] = latLng.latitude
        it[LONG] = latLng.longitude
    }

    val latLng = dataStore.data.map {
        LatLng(
            it[LAT] ?: 0.0,
            it[LONG] ?: 0.0
        )
    }

    companion object {
        private val LAT = doublePreferencesKey("lat")
        private val LONG = doublePreferencesKey("long")
    }
}