package com.kiki.searchcustomization.data.database

import android.content.Context
import androidx.annotation.RawRes
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kiki.searchcustomization.R
import com.kiki.searchcustomization.data.dao.WartegDao
import com.kiki.searchcustomization.data.entity.Menu
import com.kiki.searchcustomization.data.entity.Warteg
import com.kiki.searchcustomization.util.MENU_TABLE_NAME
import com.kiki.searchcustomization.util.WARTEG_TABLE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [Warteg::class, Menu::class],
    exportSchema = false,
    version = 1
)
abstract class WartegDatabase : RoomDatabase() {

    abstract fun dao(): WartegDao

    class PrepopulateCallback @Inject constructor(
        private val scope: CoroutineScope,
        @ApplicationContext private val context: Context,
        private val dao: Provider<WartegDao>
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            scope.launch {
                fillWartegData()
                fillMenuData()
            }
        }

        private fun fillWartegData() {
            val jsonArray = loadJsonArray(R.raw.warteg, WARTEG_TABLE_NAME)
            try {
                if (jsonArray != null) {
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        dao.get().insertWarteg(
                            Warteg(
                                item.getInt("wartegId"),
                                item.getString("name"),
                                item.getString("address"),
                                item.getDouble("latitude"),
                                item.getDouble("longitude"),
                                item.getDouble("rating"),
                                item.getInt("review")
                            )
                        )
                    }
                }
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
        }

        private fun fillMenuData() {
            val jsonArray = loadJsonArray(R.raw.menu, MENU_TABLE_NAME)
            try {
                if (jsonArray != null) {
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        dao.get().insertMenu(
                            Menu(
                                menuId = item.getInt("menuId"),
                                name = item.getString("name"),
                                price = item.getDouble("price")
                            )
                        )
                    }
                }
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
        }

        private fun loadJsonArray(@RawRes raw: Int, name: String): JSONArray? {
            val builder = StringBuilder()
            val inputStream = context.resources.openRawResource(raw)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            try {
                while (reader.readLine().also { line = it } != null) {
                    builder.append(line)
                }
                val json = JSONObject(builder.toString())
                return json.getJSONArray(name)
            } catch (exception: IOException) {
                exception.printStackTrace()
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
            return null
        }
    }
}