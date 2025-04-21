package com.receparslan.wishlist.roomDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.receparslan.wishlist.model.Item

@Dao
interface ItemDAO {
    @Insert
    suspend fun insertItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)

    @Query("SELECT * FROM items WHERE id = :id")
    fun getItemById(id: Int): Item?

    @Query("SELECT * FROM items ORDER BY name ASC")
    fun getAllItems(): List<Item>?
}