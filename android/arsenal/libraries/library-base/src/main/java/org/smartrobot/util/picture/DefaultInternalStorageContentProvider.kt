/*
package org.smartrobot.util.picture

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor

import java.io.File
import java.io.FileNotFoundException
import java.util.HashMap

*/
/*
 * The solution is taken from here: http://stackoverflow.com/questions/10042695/how-to-get-camera-result-as-a-uri-in-data-folder
 *//*


class DefaultInternalStorageContentProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        try {
            val mFile = File(context!!.filesDir, DefaultPictureFragment.Companion.getTEMP_PHOTO_FILE_NAME())
            if (!mFile.exists()) {
                mFile.createNewFile()
                context!!.contentResolver.notifyChange(CONTENT_URI, null)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    override fun getType(uri: Uri): String? {
        val path = uri.toString()
        for (extension in MIME_TYPES.keys) {
            if (path.endsWith(extension)) {
                return MIME_TYPES[extension]
            }
        }
        return null
    }

    @Throws(FileNotFoundException::class)
    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        val f = File(context!!.filesDir, DefaultPictureFragment.Companion.getTEMP_PHOTO_FILE_NAME())
        if (f.exists()) {
            return ParcelFileDescriptor.open(f, ParcelFileDescriptor.MODE_READ_WRITE)
        }
        throw FileNotFoundException(uri.path)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return null
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    companion object {
        val CONTENT_URI = Uri.parse("content://com.mlibrary.crop/")
        private val MIME_TYPES = HashMap<String, String>()

        init {
            MIME_TYPES.put(".jpg", "image/jpeg")
            MIME_TYPES.put(".jpeg", "image/jpeg")
        }
    }
}*/
