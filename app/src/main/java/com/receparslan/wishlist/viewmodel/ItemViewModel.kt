package com.receparslan.wishlist.viewmodel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.receparslan.wishlist.model.Item
import com.receparslan.wishlist.roomDB.InventoryDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemViewModel(application: Application) : AndroidViewModel(application) {
    private val db = InventoryDatabase.getDatabase(getApplication())

    private val itemDAO = db.itemDAO()

    val itemList = mutableStateOf(emptyList<Item>())
    private val selectedItem = mutableStateOf(Item("", "", "", null))

    fun insertItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            itemDAO.insertItem(item)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            itemDAO.deleteItem(item)
        }
    }

    fun getItem(id: Int?): MutableState<Item> {
        id?.let {
            viewModelScope.launch(Dispatchers.IO) {
                selectedItem.value = itemDAO.getItemById(id) ?: Item("", "", "", null)
            }
        } ?: run {
            selectedItem.value = Item("", "", "", null)
        }

        return selectedItem
    }

    fun setItemList() {
        viewModelScope.launch(Dispatchers.IO) {
            itemDAO.getAllItems()?.let { itemList.value = it }
        }
    }
}