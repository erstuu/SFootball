package com.uas.sfootball.helper

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object StorageHelper {

    fun saveImageToStorage(context: Context, imageUri: Uri): Uri? {
        return try {

            val inputStream = context.contentResolver.openInputStream(imageUri)
            inputStream ?: return null

            val mimeType = context.contentResolver.getType(imageUri)
            val fileExtension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)

            val imageName = "logo_club.${fileExtension ?: "jpg"}"

            val file = File(context.filesDir, imageName)

            val outputStream = FileOutputStream(file)
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            val outputUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            outputUri
        } catch (e: Exception) {
            println("Exception occurred: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    fun getDrawableUri(context: Context, drawableResId: Int): Uri {
        val uri = Uri.parse(
            "android.resource://${context.packageName}/drawable/${
                context.resources.getResourceEntryName(
                    drawableResId
                )
            }"
        )
        println("Generated drawable URI: $uri")
        return uri
    }
}