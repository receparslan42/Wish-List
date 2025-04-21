package com.receparslan.wishlist.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.window.DialogProperties
import com.receparslan.wishlist.model.Item

@Composable
fun DetailScreen(item: MutableState<Item>, deleteFunction: () -> Unit) {
    val openAlertDialog = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            item.value.image?.let {
                Image(
                    bitmap = BitmapFactory.decodeByteArray(item.value.image, 0, item.value.image?.size ?: 0).asImageBitmap(),
                    contentDescription = item.value.name,
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(8.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    NewText("Name : ", FontWeight.Bold)
                    NewText(item.value.name, FontWeight.Light)
                }
                Row {
                    NewText("Price : ", FontWeight.Bold)
                    NewText(item.value.price ?: "", FontWeight.Light)
                }
                Row {
                    NewText("Store Name : ", FontWeight.Bold)
                    NewText(item.value.storeName ?: "", FontWeight.Light)
                }
                Button(
                    onClick = { openAlertDialog.value = true },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(125.dp),
                ) {
                    Text(
                        text = "DELETE",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            shadow = Shadow(Color.LightGray, offset = Offset(2.5f, 5.0f), blurRadius = 3f)
                        ),
                    )
                }

                when {
                    openAlertDialog.value ->
                        openAlertDialog.value = showAlertDialog("DELETE WISH", "Are you sure you want to delete this wish?") {
                            deleteFunction()
                            openAlertDialog.value = false
                        }
                }
            }
        }
    }
}

@Composable
fun NewText(text: String, fontWeight: FontWeight) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        fontSize = 36.sp,
        style = TextStyle(
            fontWeight = fontWeight,
            fontStyle = FontStyle.Italic,
            shadow = Shadow(Color.LightGray, offset = Offset(5.0f, 10.0f), blurRadius = 3f)
        ),
        color = Color.Black
    )
}

@Composable
fun showAlertDialog(
    dialogTitle: String,
    dialogText: String,
    confirmButton: () -> Unit
): Boolean {
    val openAlertDialog = remember { mutableStateOf(true) }

    AlertDialog(
        icon = {
            Icon(imageVector = Icons.Filled.Warning, contentDescription = "Warning")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        confirmButton = {
            Button(
                onClick = confirmButton,
            ) {
                Text("YES")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    openAlertDialog.value = false
                },
            ) {
                Text(text = "NO")
            }
        },
        onDismissRequest = {
            openAlertDialog.value = false
        },
        properties = DialogProperties(dismissOnBackPress = false)
    )

    return openAlertDialog.value
}