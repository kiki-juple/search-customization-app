package com.kiki.searchcustomization.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class WartegWithMenu(
    @Embedded val warteg: Warteg,
    @Relation(
        parentColumn = "wartegId",
        entityColumn = "menuId"
    )
    val menu: List<Menu>
)
