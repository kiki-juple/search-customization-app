package com.kiki.searchcustomization.data.dao

import androidx.room.*
import com.kiki.searchcustomization.data.entity.Menu
import com.kiki.searchcustomization.data.entity.Warteg
import com.kiki.searchcustomization.data.entity.WartegWithMenu
import kotlinx.coroutines.flow.Flow

@Dao
interface WartegDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWarteg(vararg warteg: Warteg)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMenu(vararg menu: Menu)

    @Transaction
    @Query("SELECT * FROM warteg")
    fun getWarteg(): Flow<List<WartegWithMenu>>

    @Transaction
    @Query("SELECT * FROM warteg WHERE rating >= 4.8")
    fun getTopWarteg(): Flow<List<WartegWithMenu>>

    @Transaction
    @Query("SELECT * FROM warteg LEFT JOIN menu ON wartegId = menuId WHERE menu.name LIKE '%' || :query || '%' ORDER BY price")
    fun getCheapestWarteg(query: String): Flow<List<WartegWithMenu>>
}