package com.example.demosharefile.ui.fragment.content_browser.pager.image

import androidx.lifecycle.MutableLiveData
import com.base.baselibrary.viewmodel.Event
import com.base.baselibrary.viewmodel.EventViewModel
import com.base.baselibrary.viewmodel.ViewModelUtils.doJob
import com.base.baselibrary.views.ext.loge
import com.example.demosharefile.model.media.AppImage
import com.example.demosharefile.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PagerImageViewModel @Inject constructor(private val imageRepository: ImageRepository) :
    EventViewModel<PagerImageViewModel.PagerImageEvent>() {

    val eventLoading = MutableLiveData(Event())

    val liveImages = MutableLiveData(ArrayList<AppImage>())

    val liveSelectedImages = MutableLiveData<Stack<AppImage>>()

    fun loadImages() {
        eventLoading.value = Event(true)
        this.doJob({
            imageRepository.fetchImages()
        }, {
            liveImages.value = it
            loge("List Image : ${it.size}")
            eventLoading.value = Event(false)
        })
    }

    fun getListSelectedImages() = liveSelectedImages.value ?: Stack()

    fun onClickSend() {
        send(PagerImageEvent.ClickSend)
    }

    sealed class PagerImageEvent {
        object ClickSend : PagerImageEvent()
    }

}