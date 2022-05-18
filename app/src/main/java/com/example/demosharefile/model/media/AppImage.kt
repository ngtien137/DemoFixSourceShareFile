package com.example.demosharefile.model.media

import android.net.Uri
import android.provider.MediaStore
import com.base.baselibrary.utils.media_provider.MediaInfo

class AppImage() : AppMedia() {
    @MediaInfo(MediaStore.Images.Media._ID)
    override var id: Long = -1L

    @MediaInfo(MediaStore.Images.Media.DISPLAY_NAME)
    override var name: String = ""

    @MediaInfo(MediaStore.Images.Media.DATA)
    override var path: String = ""

    @MediaInfo(MediaStore.Images.Media.DATE_ADDED)
    override var dateAdded: Long = -1

    @MediaInfo(MediaStore.Images.Media.DATE_MODIFIED)
    override var dateModified: Long = -1

    @MediaInfo(MediaStore.Images.Media.MIME_TYPE)
    override var mimeType: String = ""

    @MediaInfo(MediaStore.Images.Media.SIZE)
    override var size: Long = 0L

    @MediaInfo(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
    var bucketName: String? = ""

    override fun getUri(): Uri {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }
}