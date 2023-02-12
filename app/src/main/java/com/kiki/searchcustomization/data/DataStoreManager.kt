package com.kiki.searchcustomization.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.maps.model.LatLng
import com.kiki.searchcustomization.data.entity.Factor
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

    suspend fun setFactor(factor: Factor) = dataStore.edit {
        it[PRICE] = factor.price
        it[DISTANCE] = factor.distance
        it[RATING] = factor.rating
        it[REVIEW] = factor.review
    }

    val factors = dataStore.data.map {
        Factor(
            it[PRICE] ?: false,
            it[DISTANCE] ?: false,
            it[RATING] ?: false,
            it[REVIEW] ?: false
        )
    }

    companion object {
        private val LAT = doublePreferencesKey("lat")
        private val LONG = doublePreferencesKey("long")
        private val PRICE = booleanPreferencesKey("price")
        private val DISTANCE = booleanPreferencesKey("distance")
        private val RATING = booleanPreferencesKey("rating")
        private val REVIEW = booleanPreferencesKey("review")
    }
}