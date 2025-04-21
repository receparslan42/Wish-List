package com.receparslan.wishlist.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
class Item(
    var name: String,
    var price: String?,
    var storeName: String?,
    var image: ByteArray?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}