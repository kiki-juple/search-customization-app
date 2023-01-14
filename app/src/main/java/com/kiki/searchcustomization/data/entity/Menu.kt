package com.kiki.searchcustomization.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kiki.searchcustomization.util.MENU_TABLE_NAME
import kotlinx.serialization.Serializable

@Entity(tableName = MENU_TABLE_NAME)
@Serializable
data class Menu(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val menuId: Int,
    val name: String,
    val price: Double
)
