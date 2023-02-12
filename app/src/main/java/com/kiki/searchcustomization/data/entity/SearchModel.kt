package com.kiki.searchcustomization.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class SearchModel(
    @Relation(
        parentColumn = "menuId",
        entityColumn = "wartegId"
    )
    val warteg: Warteg,
    @Embedded val menu: Menu
)