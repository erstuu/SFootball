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
            println("Input URI: $imageUri")

            val inputStream = context.contentResolver.openInputStream(imageUri)
            if (inputStream == null) {
                println("Failed to open input stream for URI: $imageUri")
                return null
            }

            val mimeType = context.contentResolver.getType(imageUri)
            println("MIME type: $mimeType")
            val fileExtension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
            println("File extension: $fileExtension")

            val imageName = "image_${System.currentTimeMillis()}.${fileExtension ?: "jpg"}"
            println("Generated file name: $imageName")

            val file = File(context.filesDir, imageName)
            println("File path: ${file.absolutePath}")

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
            println("Output URI: $outputUri")
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