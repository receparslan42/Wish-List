package com.receparslan.wishlist.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.receparslan.wishlist.model.Item

@Composable
fun ListScreen(itemList: List<Item>, navController: NavController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FAB(onClick = {
                navController.navigate("add_item_screen")
            })
        }
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(itemList) { ItemRow(it, navController) }
        }
    }
}

@Composable
fun ItemRow(item: Item, navController: NavController) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                navController.navigate("detail_screen/${item.id}")
            }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        item.image?.let {
            Image(
                bitmap = BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap(),
                contentDescription = item.name,
                modifier = Modifier
                    .size(125.dp)
                    .clip(CircleShape)
                    .border(BorderStroke(5.dp, Color.Black), CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = item.name,
                textAlign = TextAlign.Center,
                fontSize = 50.sp,
                color = Color.Black,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    shadow = Shadow(Color.LightGray, offset = Offset(5.0f, 10.0f), blurRadius = 3f)
                )
            )
        }
    }
}

@Composable
fun FAB(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add Item"
        )
    }
}