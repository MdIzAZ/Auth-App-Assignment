package com.example.authapp.presentation.onboard_screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(
    profileImage: ImageBitmap?,
    onProfileChange: (ImageBitmap, String) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {

    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var fileName by remember { mutableStateOf(getDateAndTime()) }


    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri?.let { uri ->
                    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                    val rotatedBitmap = rotateBitmapIfRequired(context, bitmap, uri)
                    onProfileChange(rotatedBitmap.asImageBitmap(), "$fileName")
                }
            }
        }

    fun createImageFile(): File {
        val storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        if (storageDir != null && !storageDir.exists()) {
            storageDir.mkdirs()
        }

        return File.createTempFile("Customer", ".png", storageDir)
    }



    fun onCameraClick() {
        val photoFile = createImageFile()
        fileName = photoFile.name
        imageUri =
            FileProvider.getUriForFile(context, "${context.packageName}.FileProvider", photoFile)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            putExtra("android.intent.extras.CAMERA_FACING", 1)
        }

        imageUri?.let {
            cameraLauncher.launch(cameraIntent)
        }
        MediaScannerConnection.scanFile(context, arrayOf(photoFile.absolutePath), null, null)
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopCenter),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Image(
                bitmap = profileImage ?: ImageBitmap(1, 1),
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            )

            IconButton(
                onClick = {
                    onCameraClick()
                },
                modifier = Modifier
                    .padding(8.dp)
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = "Camera Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ElevatedButton(
                shape = RoundedCornerShape(5.dp),
                onClick = onBackClick,
                content = {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back Icon"
                    )
                }
            )
            ElevatedButton(
                shape = RoundedCornerShape(5.dp),
                onClick = onNextClick,
                content = {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Next Icon"
                    )
                }
            )
        }

    }
}


fun rotateBitmapIfRequired(context: Context, bitmap: Bitmap, uri: Uri): Bitmap {
    val input = context.contentResolver.openInputStream(uri) ?: return bitmap
    val exif = ExifInterface(input)
    val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

    val rotation = when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90
        ExifInterface.ORIENTATION_ROTATE_180 -> 180
        ExifInterface.ORIENTATION_ROTATE_270 -> 270
        else -> 0
    }

    return if (rotation != 0) {
        val matrix = Matrix()
        matrix.postRotate(rotation.toFloat())
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    } else {
        bitmap
    }
}



private fun getDateAndTime(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH-mm-ss", Locale.getDefault())
    val currentDate = Date()
    val formattedDate = dateFormat.format(currentDate)
    val formattedTime = timeFormat.format(currentDate)
    return "$formattedDate $formattedTime"
}