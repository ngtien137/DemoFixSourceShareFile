package com.example.demosharefile.model.media

import android.net.Uri
import android.provider.MediaStore
import com.base.baselibrary.utils.media_provider.MediaModelBase
import java.io.File

open class AppMedia(
    open var id: Long = -1L,
    open var name: String = "",
    open var path: String = "",
    open var dateAdded: Long = -1,
    open var dateModified: Long = -1,
    open var mimeType:String = "",
    open var size:Long = 0L
) : MediaModelBase() {

    var fileUri: Uri? = null


    override fun getUri(): Uri {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    fun getFileName(): String {
        return File(path).name
    }

    fun getExtension(): String {
        return File(path).extension
    }

    fun getNameWithoutExtension(): String {
        return File(path).nameWithoutExtension
    }
}