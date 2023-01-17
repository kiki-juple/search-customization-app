package com.kiki.searchcustomization.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kiki.searchcustomization.util.WARTEG_TABLE_NAME
import kotlinx.serialization.Serializable

@Entity(tableName = WARTEG_TABLE_NAME)
@Serializable
data class Warteg(
    @PrimaryKey(autoGenerate = false)
    val wartegId: Int,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val distance: Double? = null,
    val rating: Double,
    val review: Int
)
