package com.example.demosharefile.ui.fragment.content_browser

import com.base.baselibrary.viewmodel.EventViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContentBrowserViewModel @Inject constructor() :
    EventViewModel<ContentBrowserViewModel.ContentBrowserEvent>() {

    sealed class ContentBrowserEvent {

    }

}