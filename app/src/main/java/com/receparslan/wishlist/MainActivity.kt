package com.receparslan.wishlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.receparslan.wishlist.model.Item
import com.receparslan.wishlist.ui.screens.AddItemScreen
import com.receparslan.wishlist.ui.screens.DetailScreen
import com.receparslan.wishlist.ui.screens.ListScreen
import com.receparslan.wishlist.ui.theme.WishListTheme
import com.receparslan.wishlist.viewmodel.ItemViewModel

class MainActivity : ComponentActivity() {

    @Suppress("unused", "RedundantSuppression")
    private val viewModel: ItemViewModel by viewModels<ItemViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController() // Nav controller is created to navigate between screens

            WishListTheme {
                Scaffold(Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = "list_screen"
                    ) {
                        composable("list_screen") {
                            viewModel.setItemList() // Initial item list is set
                            val itemList by remember { viewModel.itemList }

                            ListScreen(itemList, navController)
                        }

                        composable("add_item_screen") {
                            AddItemScreen { item: Item ->
                                viewModel.insertItem(item)
                                navController.navigate("list_screen") {
                                    popUpTo("list_screen") { inclusive = true }
                                }
                            }
                        }

                        composable("detail_screen/{itemID}", arguments = listOf(navArgument("itemID") { type = NavType.IntType })) {
                            val itemID = it.arguments?.getInt("itemID")
                            val item = viewModel.getItem(itemID)

                            DetailScreen(item) {
                                viewModel.deleteItem(item.value)
                                navController.navigate("list_screen") {
                                    popUpTo("list_screen") { inclusive = true }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}