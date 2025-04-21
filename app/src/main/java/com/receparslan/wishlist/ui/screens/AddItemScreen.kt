package com.receparslan.wishlist.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import coil3.compose.AsyncImage
import com.receparslan.wishlist.R
import com.receparslan.wishlist.model.Item
import java.io.ByteArrayOutputStream

@Composable
fun AddItemScreen(saveFunction: (item: Item) -> Unit) {
    // Local variables to store the item name, price, and store name
    var itemName by remember { mutableStateOf("") }
    var itemPrice by remember { mutableStateOf("") }
    var storeName by remember { mutableStateOf("") }

    // Local variable to store the selected image URI
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Get the context
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImagePicker(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) { selectedImageUri = it }

        Column(Modifier.weight(1f)) {
            Spacer(Modifier.size(25.dp))

            TextField(
                value = itemName,
                onValueChange = { itemName = it },
                label = { Text("Item Name") },
                placeholder = { Text("Enter item name") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.border(BorderStroke(1.dp, Color.LightGray), CircleShape)
            )

            Spacer(Modifier.size(25.dp))

            TextField(
                value = itemPrice,
                onValueChange = { itemPrice = it },
                label = { Text("Item Price") },
                placeholder = { Text("Enter item price") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.border(BorderStroke(1.dp, Color.LightGray), CircleShape)
            )

            Spacer(Modifier.size(25.dp))

            TextField(
                value = storeName,
                onValueChange = { storeName = it },
                label = { Text("Store Name") },
                placeholder = { Text("Enter store name") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.border(BorderStroke(1.dp, Color.LightGray), CircleShape)
            )

            Spacer(Modifier.size(25.dp))

            Button(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    val imageByteArray = selectedImageUri?.let {
                        resizeImage(context, it, 1000)
                    } ?: ByteArray(0)

                    val item = Item(
                        name = itemName,
                        price = itemPrice,
                        storeName = storeName,
                        image = imageByteArray
                    )

                    saveFunction(item)
                },
            ) {
                Text(
                    text = "SAVE",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        shadow = Shadow(Color.LightGray, offset = Offset(2.5f, 5.0f), blurRadius = 3f)
                    )
                )
            }
        }
    }
}

@Composable
fun ImagePicker(modifier: Modifier, onImageSelected: (Uri?) -> Unit) {
    // Local variable to store the selected image URI
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Get the context and activity
    val context = LocalContext.current
    val activity = LocalActivity.current

    // Check the android version for the permission request
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.READ_MEDIA_IMAGES
    else
        Manifest.permission.READ_EXTERNAL_STORAGE

    // Create a launcher for the gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { selectedImageUri = it }

    // Create a launcher for the permission request
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted)
            galleryLauncher.launch("image/*") // Launch the gallery
        else
            Toast.makeText(context, "Permission is required to access the gallery.", Toast.LENGTH_LONG)
                .show() // Show a toast message to inform the user
    }

    var shouldShowRequest = false // Local variable to show the permission request dialog

    Box(
        modifier = modifier.clickable {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
                galleryLauncher.launch("image/*")
            else if (activity?.shouldShowRequestPermissionRationale(permission) == true)
                shouldShowRequest = true
            else
                permissionLauncher.launch(permission)
        }
    ) {
        if (shouldShowRequest)
            Snackbar(action = { permissionLauncher.launch(permission) }) {
                Text("Permission is required to access the gallery.")
            }

        selectedImageUri?.let {
            AsyncImage(
                model = selectedImageUri,
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentScale = ContentScale.Fit
            )
            onImageSelected(it)

        } ?: Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(8.dp),
            painter = painterResource(R.drawable.select_image),
            contentDescription = "Select a image",
            contentScale = ContentScale.Fit
        )
    }
}

// Function to resize the image
fun resizeImage(context: Context, uri: Uri, maxSize: Int): ByteArray? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val byteArrayOutputStream = ByteArrayOutputStream()

        val originalBitmap = BitmapFactory.decodeStream(inputStream)

        var width = originalBitmap.width
        var height = originalBitmap.height

        val ratio = width.toFloat() / height.toFloat()

        if (ratio >= 1) {
            width = maxSize
            height = width / ratio.toInt()
        } else {
            height = maxSize
            width = (height * ratio).toInt()
        }

        val resizedBitmap = originalBitmap.scale(width, height, false)

        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        byteArrayOutputStream.toByteArray()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}