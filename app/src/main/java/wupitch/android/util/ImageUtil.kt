package wupitch.android.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class GetRealPath(private val context: Context) {

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