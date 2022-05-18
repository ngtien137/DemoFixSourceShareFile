package com.example.demosharefile.ui.fragment.send_options

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.baselibrary.viewmodel.EventViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.monora.uprotocol.client.android.data.ClientRepository
import org.monora.uprotocol.client.android.data.OnlineClientRepository
import org.monora.uprotocol.client.android.database.model.UClient
import org.monora.uprotocol.client.android.database.model.UClientAddress
import org.monora.uprotocol.client.android.model.ClientRoute
import org.monora.uprotocol.client.android.protocol.NoAddressException
import org.monora.uprotocol.client.android.viewmodel.ConnectionState
import org.monora.uprotocol.client.android.viewmodel.content.ClientContentViewModel
import org.monora.uprotocol.core.CommunicationBridge
import org.monora.uprotocol.core.persistence.PersistenceProvider
import org.monora.uprotocol.core.protocol.ConnectionFactory
import javax.inject.Inject

@HiltViewModel
class SendOptionsViewModel @Inject constructor(
    val clientRepository: ClientRepository,
    onlineClientRepository: OnlineClientRepository,
    val connectionFactory: ConnectionFactory,
    val persistenceProvider: PersistenceProvider,
) :
    EventViewModel<SendOptionsViewModel.SendOptionsEvent>() {

    val onlineClients = onlineClientRepository.getOnlineClients()

    val clients = clientRepository.getAll()

    val liveConnectionStatus = MutableLiveData(ClientConnectionStatus.None)

    val liveConnecting = MutableLiveData(false)

    fun mapListClientToContentClientViewModel(list: List<ClientRoute>?): List<ClientContentViewModel> {
        val validList = list ?: ArrayList()
        return validList.map { ClientContentViewModel(it.client) }
    }

    //region connection
    private var job: Job? = null

    fun startConnect(client: UClient, address: UClientAddress?): Job = job ?: viewModelScope.launch(
        Dispatchers.IO
    ) {
        val addresses = address?.let { listOf(it.inetAddress) } ?: clientRepository.getAddresses(
            client.clientUid
        ).map {
            it.inetAddress
        }

        try {
            if (addresses.isEmpty()) {
                throw NoAddressException()
            }

            //state.postValue(ConnectionState.Connecting())
            liveConnecting.postValue(true)
            val bridge = CommunicationBridge.Builder(
                connectionFactory, persistenceProvider, addresses
            ).apply {
                setClearBlockedStatus(true)
                setClientUid(client.clientUid)
            }

            send(SendOptionsEvent.ClientConnected(bridge.connect()))
            //state.postValue(ConnectionState.Connected(bridge.connect()))
        } catch (e: Exception) {
            //state.postValue(ConnectionState.Error(e))
            send(SendOptionsEvent.ClientConnectedError(e))
        } finally {
            job = null
            liveConnecting.postValue(false)
        }
    }.also { job = it }

    //endregion


    fun onClickManualAddress() {
        send(SendOptionsEvent.EventManualAddress)
    }

    sealed class SendOptionsEvent {
        object EventManualAddress : SendOptionsEvent()
        class ClientConnected(val bridge: CommunicationBridge) : SendOptionsEvent()
        class ClientConnectedError(val e: Exception) : SendOptionsEvent()
    }

    sealed class ConnectionState(val isConnecting: Boolean = false, val isError: Boolean = false) {
        class Connected(val bridge: CommunicationBridge) : ConnectionState()

        class Error(val e: Exception) : ConnectionState(isError = true)

        class Connecting : ConnectionState(isConnecting = true)
    }

    sealed class ClientConnectionStatus {
        object None : ClientConnectionStatus()
        object Connecting : ClientConnectionStatus()
        object Connected : ClientConnectionStatus()
        object Error : ClientConnectionStatus()
    }

}