package com.example.a216487_cikguizwan_lab01

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object StorageUtils {
    fun createImageFileUri(context: Context): Pair<File, Uri> {
        // Create a unique time-stamped name identifier
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

        // Target a generic subfolder inside the app's internal root storage files path
        val storageDir = File(context.filesDir, "profile_photos")

        // Force create the folder directory if it does not physically exist yet on the device disk
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        // Generate a temporary file reference securely
        val file = File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )

        // Generate the provider address path token
        val authority = "${context.packageName}.fileprovider"
        val uri = FileProvider.getUriForFile(context, authority, file)

        return Pair(file, uri)
    }
}