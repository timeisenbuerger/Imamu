package com.github.tei.imamu.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ImageUtil
{
    companion object
    {
        fun saveImage(bitmap: Bitmap, context: Context, name: String) : String
        {
            val rootPath = RealPathUtil.getRealPath(context, context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
                .toUri())

            val imagesDir = File(rootPath + "/RezeptFotos")
            if (!imagesDir.exists())
            {
                imagesDir.mkdir()
            }

            val folderName = "RezeptFotos"

            if (android.os.Build.VERSION.SDK_INT >= 29)
            {
                val values = contentValues(name)
                values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + folderName)
                values.put(MediaStore.Images.Media.IS_PENDING, true)
                // RELATIVE_PATH and IS_PENDING are introduced in API 29.

                val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                if (uri != null)
                {
                    saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    context.contentResolver.update(uri, values, null, null)
                }
            }
            else
            {
                val directory = File(Environment.getExternalStorageDirectory()
                    .toString() + File.separator + folderName
                )
                // getExternalStorageDirectory is deprecated in API 29

                if (!directory.exists())
                {
                    directory.mkdirs()
                }
                val fileName = "$name.png"
                val file = File(directory, fileName)
                saveImageToStream(bitmap, FileOutputStream(file))
                if (file.absolutePath != null)
                {
                    val values = contentValues(name)
                    values.put(MediaStore.Images.Media.DATA, file.absolutePath)
                    // .DATA is deprecated in API 29
                    context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                }
            }

            return Environment.getExternalStorageDirectory().absolutePath + "/Pictures/RezeptFotos/" + name + ".png"
        }

        private fun contentValues(name: String): ContentValues
        {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DISPLAY_NAME, name)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            return values
        }

        private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?)
        {
            if (outputStream != null)
            {
                try
                {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
        }
    }
}