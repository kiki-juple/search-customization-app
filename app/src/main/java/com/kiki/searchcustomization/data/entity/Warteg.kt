package com.kiki.searchcustomization.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kiki.searchcustomization.util.WARTEG_TABLE_NAME

@Entity(tableName = WARTEG_TABLE_NAME)
data class Warteg(
    @PrimaryKey(autoGenerate = false)
    val wartegId: Int,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val distance: Double? = null,
    val rating: Double,
    val review: Int,
)
