package com.watchclock.tracker.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.media.ExifInterface
import java.io.File
import java.io.FileOutputStream

object ImageHelper {

    private const val MAX_DIMENSION = 1024
    private const val JPEG_QUALITY = 85

    fun getImagesDir(context: Context): File {
        return File(context.filesDir, "images").also { it.mkdirs() }
    }

    fun buildFilename(itemId: Long): String {
        val timestamp = System.currentTimeMillis()
        return "${itemId}_${timestamp}.jpg"
    }

    /**
     * Compresses [uri] image to max 1024x1024 JPEG at 85% quality and saves to app's images dir.
     * Returns the saved file path, or null on failure.
     */
    fun saveAndCompressImage(context: Context, uri: Uri, itemId: Long): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val original = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            val rotated = correctOrientation(context, uri, original)
            val scaled = scaleBitmap(rotated)

            val file = File(getImagesDir(context), buildFilename(itemId))
            FileOutputStream(file).use { out ->
                scaled.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, out)
            }

            if (rotated != original) rotated.recycle()
            if (scaled != rotated) scaled.recycle()
            original.recycle()

            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun scaleBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        if (width <= MAX_DIMENSION && height <= MAX_DIMENSION) return bitmap

        val scale = MAX_DIMENSION.toFloat() / maxOf(width, height)
        val newWidth = (width * scale).toInt()
        val newHeight = (height * scale).toInt()
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    private fun correctOrientation(context: Context, uri: Uri, bitmap: Bitmap): Bitmap {
        return try {
            val stream = context.contentResolver.openInputStream(uri) ?: return bitmap
            val exif = ExifInterface(stream)
            stream.close()
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                else -> return bitmap
            }
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (e: Exception) {
            bitmap
        }
    }

    fun deleteImage(filePath: String) {
        try { File(filePath).delete() } catch (_: Exception) {}
    }
}
