package com.kiki.searchcustomization.data.dao

import androidx.room.*
import com.kiki.searchcustomization.data.entity.Menu
import com.kiki.searchcustomization.data.entity.SearchModel
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
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM warteg")
    fun getWarteg(): Flow<List<WartegWithMenu>>

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM warteg WHERE wartegId = :id")
    fun getWartegById(id: Int): Flow<List<WartegWithMenu>>

    @Query("UPDATE warteg SET distance = :distance WHERE wartegId = :id")
    fun updateWartegDistance(id: Int, distance: Double)

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM warteg WHERE rating >= 4.5 ORDER BY rating DESC")
    fun getTopWarteg(): Flow<List<WartegWithMenu>>

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT wartegId, warteg.name, address, latitude, longitude, distance, rating, review, SUM(price) as total_price FROM warteg INNER JOIN menu ON wartegId = menuId GROUP BY wartegId ORDER BY total_price")
    fun getCheapestWarteg(): Flow<List<WartegWithMenu>>

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM warteg INNER JOIN menu ON wartegId = menuId WHERE menu.name LIKE '%' || :query || '%' GROUP BY wartegId ORDER BY price")
    fun searchWarteg(query: String): Flow<List<SearchModel>>
}