package com.github.tei.imamu.util

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.loader.content.CursorLoader

class RealPathUtil
{
    companion object
    {
        fun getRealPath(context: Context, fileUri: Uri): String?
        {
            // SDK < API11
            return when
            {
                Build.VERSION.SDK_INT < 11 ->
                {
                    getRealPathFromURI_BelowAPI11(context, fileUri)
                }
                Build.VERSION.SDK_INT < 19 ->
                {
                    getRealPathFromURI_API11to18(context, fileUri)
                }
                else                       ->
                {
                    getRealPathFromURI_API19(context, fileUri)
                }
            }
        }

        @SuppressLint("NewApi")
        fun getRealPathFromURI_API11to18(context: Context, contentUri: Uri): String?
        {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            var result: String? = null
            val cursorLoader = CursorLoader(context, contentUri, proj, null, null, null)
            val cursor: Cursor? = cursorLoader.loadInBackground()
            if (cursor != null)
            {
                val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                result = cursor.getString(column_index)
                cursor.close()
            }
            return result
        }

        private fun getRealPathFromURI_BelowAPI11(context: Context, contentUri: Uri): String?
        {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.getContentResolver()
                .query(contentUri, proj, null, null, null)
            var column_index = 0
            var result = ""
            if (cursor != null)
            {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                result = cursor.getString(column_index)
                cursor.close()
                return result
            }
            return result
        }

        /**
         * Get a file path from a Uri. This will get the the path for Storage Access
         * Framework Documents, as well as the _data field for the MediaStore and
         * other file-based ContentProviders.
         *
         * @param context The context.
         * @param uri     The Uri to query.
         * @author paulburke
         */
        @SuppressLint("NewApi")
        fun getRealPathFromURI_API19(context: Context, uri: Uri): String?
        {
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri))
            {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri))
                {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex())
                        .toTypedArray()
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true))
                    {
                        return Environment.getExternalStorageDirectory()
                            .toString() + "/" + split[1]
                    }

                    // TODO handle non-primary volumes
                }
                else if (isDownloadsDocument(uri))
                {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri: Uri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                    return getDataColumn(context, contentUri, null, null)
                }
                else if (isMediaDocument(uri))
                {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex())
                        .toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    when (type)
                    {
                        "image" ->
                        {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        }
                        "video" ->
                        {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        }
                        "audio" ->
                        {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1]
                    )
                    return getDataColumn(context, contentUri!!, selection, selectionArgs)
                }
            }
            else if ("content".equals(uri.scheme, ignoreCase = true))
            {

                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)
            }
            else if ("file".equals(uri.scheme, ignoreCase = true))
            {
                return uri.getPath()
            }
            return null
        }

        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.
         *
         * @param context       The context.
         * @param uri           The Uri to query.
         * @param selection     (Optional) Filter used in the query.
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file path.
         */
        private fun getDataColumn(context: Context, uri: Uri, selection: String?, selectionArgs: Array<String>?): String?
        {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column
            )
            try
            {
                cursor = context.getContentResolver()
                    .query(uri, projection, selection, selectionArgs, null)
                if (cursor != null && cursor.moveToFirst())
                {
                    val index: Int = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }
            }
            finally
            {
                if (cursor != null) cursor.close()
            }
            return null
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        private fun isExternalStorageDocument(uri: Uri): Boolean
        {
            return "com.android.externalstorage.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        private fun isDownloadsDocument(uri: Uri): Boolean
        {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        private fun isMediaDocument(uri: Uri): Boolean
        {
            return "com.android.providers.media.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is Google Photos.
         */
        private fun isGooglePhotosUri(uri: Uri): Boolean
        {
            return "com.google.android.apps.photos.content" == uri.getAuthority()
        }
    }
}