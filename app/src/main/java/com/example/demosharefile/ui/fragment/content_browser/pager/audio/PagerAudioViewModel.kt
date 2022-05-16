package com.example.demosharefile.ui.fragment.content_browser.pager.audio

import com.base.baselibrary.viewmodel.EventViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PagerAudioViewModel @Inject constructor() :
    EventViewModel<PagerAudioViewModel.PagerAudioEvent>() {

    sealed class PagerAudioEvent {

    }

}