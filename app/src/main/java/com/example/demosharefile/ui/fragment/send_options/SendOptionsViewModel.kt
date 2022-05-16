package com.example.demosharefile.ui.fragment.send_options

import com.base.baselibrary.viewmodel.EventViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SendOptionsViewModel @Inject constructor() :
    EventViewModel<SendOptionsViewModel.SendOptionsEvent>() {

    fun onClickManualAddress() {
        send(SendOptionsEvent.EventManualAddress)
    }

    sealed class SendOptionsEvent {
        object EventManualAddress : SendOptionsEvent()
    }

}