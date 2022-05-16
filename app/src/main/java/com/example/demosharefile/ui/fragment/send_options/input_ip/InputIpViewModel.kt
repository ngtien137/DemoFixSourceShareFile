package com.example.demosharefile.ui.fragment.send_options.input_ip

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.base.baselibrary.viewmodel.EventViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.monora.uprotocol.client.android.model.ClientRoute
import org.monora.uprotocol.client.android.util.clientRoute
import org.monora.uprotocol.core.CommunicationBridge
import org.monora.uprotocol.core.persistence.PersistenceProvider
import org.monora.uprotocol.core.protocol.ConnectionFactory
import java.net.InetAddress
import javax.inject.Inject

@HiltViewModel
class InputIpViewModel @Inject constructor(
    private var connectionFactory: ConnectionFactory,
    private var persistenceProvider: PersistenceProvider
) : EventViewModel<InputIpViewModel.EventInputIp>() {

    private val _state = MutableLiveData<ManualConnectionState>()

    private var _job: Job? = null

    val liveIpText = MutableLiveData("")

    fun getTextIp() = liveIpText.value ?: ""

    val state = liveData {
        emitSource(_state)
    }

    fun onClickConfirm() {
        send(EventInputIp.ClickConfirm)
    }

    fun connect(address: String) = _job ?: viewModelScope.launch(Dispatchers.IO) {
        _state.postValue(ManualConnectionState.Loading())

        try {
            val inetAddress = InetAddress.getByName(address)
            CommunicationBridge.connect(connectionFactory, persistenceProvider, inetAddress).use {
                it.send(false)
                _state.postValue(ManualConnectionState.Loaded(it.clientRoute))
            }
        } catch (e: Exception) {
            _state.postValue(ManualConnectionState.Error(e))
        } finally {
            _job = null
        }
    }.also { _job = it }

    sealed class EventInputIp {
        object ClickConfirm : EventInputIp()
    }

}

sealed class ManualConnectionState(val loading: Boolean = false) {
    class Loading : ManualConnectionState(true)

    class Error(val exception: Exception) : ManualConnectionState()

    class Loaded(val clientRoute: ClientRoute, var isUsed: Boolean = false) :
        ManualConnectionState()
}