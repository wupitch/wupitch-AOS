package wupitch.android.util

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class GetImageFile(private val context: Context) {

    fun getImageFile(uri: Uri) : File? {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) createImageFileAndroidQ(uri)
        else createImageFileBelowAndroidQ(uri)
    }

    // android 10 (Q) or over.
    private fun createImageFileAndroidQ(uri:Uri): File?{
        return try {
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r", null)
            val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)

            val file = File(context.cacheDir, context.contentResolver.getFileName(uri))
            val outputStream = FileOutputStream(file)

            inputStream.copyTo(outputStream)

            file
        }catch (e:Exception) {
            Log.e("LOG>>","createImageFileAndroidQ ERror : $e")
            null
        }
    }

    //below android 10
    private fun createImageFileBelowAndroidQ(uri : Uri) : File? {
        val path = getRealPathFromURIForGallery(uri)
        return if(path != null) {
            resizeImage(file = File(path))
            File(path)
        }else null
    }

    fun getRealPathFromURIForGallery(uri: Uri): String? {

        var fullPath: String? = null
        val column = "_data"
        var cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            var documentId = cursor.getString(0)
            if (documentId == null) {
                for (i in 0 until cursor.columnCount) {
                    if (column.equals(cursor.getColumnName(i), ignoreCase = true)) {
                        fullPath = cursor.getString(i)
                        break
                    }
                }
            } else {
                documentId = documentId.substring(documentId.lastIndexOf(":") + 1)
                cursor.close()
                val projection = arrayOf(column)
                try {
                    cursor = context.contentResolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        MediaStore.Images.Media._ID + " = ? ",
                        arrayOf(documentId),
                        null
                    )
                    if (cursor != null) {
                        cursor.moveToFirst()
                        fullPath = cursor.getString(cursor.getColumnIndexOrThrow(column))
                    }
                } finally {
                    if (cursor != null) cursor.close()
                }
            }
        }
        return fullPath
    }
}

fun getImageBody(file: File): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        name = "images",
        filename = file.name,
        body = file.asRequestBody("image/*".toMediaType())
    )
}

fun resizeImage(file: File, scaleTo: Int = 1024) {
    val bmOptions = BitmapFactory.Options()
    bmOptions.inJustDecodeBounds = true
    BitmapFactory.decodeFile(file.absolutePath, bmOptions)
    val photoW = bmOptions.outWidth
    val photoH = bmOptions.outHeight

    val scaleFactor = (photoW / scaleTo).coerceAtMost(photoH / scaleTo)

    bmOptions.inJustDecodeBounds = false
    bmOptions.inSampleSize = scaleFactor

    val resized = BitmapFactory.decodeFile(file.absolutePath, bmOptions) ?: return
    file.outputStream().use {
        resized.compress(Bitmap.CompressFormat.JPEG, 75, it)
        resized.recycle()
    }
}



fun ContentResolver.getFileName(fileUri: Uri): String {
    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }

    return name
}