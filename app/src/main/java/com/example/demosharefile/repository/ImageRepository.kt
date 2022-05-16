package com.example.demosharefile.repository

import android.content.ContentUris
import android.os.Build
import android.provider.MediaStore
import com.base.baselibrary.utils.SDKUtils
import com.base.baselibrary.utils.getApplication
import com.base.baselibrary.utils.media_provider.getMedia
import com.example.demosharefile.model.media.AppImage
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepository @Inject constructor() {

    fun fetchImages(): ArrayList<AppImage> {
        val result = ArrayList<AppImage>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.DATA,
        )

        val selection =
            "${MediaStore.Images.Media.MIME_TYPE} =? or ${MediaStore.Images.Media.MIME_TYPE} =?"

        val selectionArgs = arrayOf(
            "image/jpeg", "image/png"
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"

        val list: MutableList<AppImage> =
            getApplication().getMedia(
                onCheckIfAddItem = { list, item ->
                    var isAddFileToList = true//File(item.path).exists()
                    item.fileUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        item.id
                    )
                    if (!SDKUtils.isBuildLargerThan(Build.VERSION_CODES.Q)) {
                        isAddFileToList = File(item.path).exists()
                    }
                    isAddFileToList
                },
                projection = projection,
                selection = selection,
                selectArgs = selectionArgs,
                sortOrder = sortOrder
            )
        result.addAll(list)

        return result
    }

}