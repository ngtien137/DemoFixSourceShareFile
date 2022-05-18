package com.example.demosharefile.ui.activity

import androidx.lifecycle.ViewModel
import com.example.demosharefile.model.media.AppImage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivityMainViewModel @Inject constructor() : ViewModel() {

    val listSelectedImages = ArrayList<AppImage>()

    fun applySelectedImages(list: Collection<AppImage>?) {
        listSelectedImages.clear()
        listSelectedImages.addAll(list ?: ArrayList())
    }

}