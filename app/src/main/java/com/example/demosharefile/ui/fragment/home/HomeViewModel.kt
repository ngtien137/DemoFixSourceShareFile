package com.example.demosharefile.ui.fragment.home

import com.base.baselibrary.viewmodel.EventViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : EventViewModel<HomeViewModel.HomeEvent>() {

    fun clickSend() {
        send(HomeEvent.EventSend)
    }

    fun clickReceive() {
        send(HomeEvent.EventReceive)
    }

    sealed class HomeEvent {
        object EventSend : HomeEvent()
        object EventReceive : HomeEvent()
    }

}