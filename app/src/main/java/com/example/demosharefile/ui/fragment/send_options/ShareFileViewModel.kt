package com.example.demosharefile.ui.fragment.send_options

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.monora.uprotocol.client.android.data.FileRepository
import org.monora.uprotocol.client.android.data.TransferRepository
import org.monora.uprotocol.client.android.database.model.Transfer
import org.monora.uprotocol.client.android.database.model.UTransferItem
import org.monora.uprotocol.client.android.protocol.closeQuietly
import org.monora.uprotocol.core.CommunicationBridge
import org.monora.uprotocol.core.TransportSeat
import org.monora.uprotocol.core.protocol.Direction
import javax.inject.Inject

@HiltViewModel
class ShareFileViewModel @Inject constructor(
    private val transportSeat: TransportSeat,
    private val transferRepository: TransferRepository,
    private val fileRepository: FileRepository,
) : ViewModel() {
    private var consumer: Job? = null

    private val _state = MutableLiveData<SharingState>()

    val state = liveData {
        emitSource(_state)
    }

    fun consume(bridge: CommunicationBridge, groupId: Long, contents: List<UTransferItem>) {
        if (consumer != null) return

        val transfer = Transfer(
            groupId,
            bridge.remoteClient.clientUid,
            Direction.Outgoing,
            fileRepository.appDirectory.originalUri.toString(),
        )

        consumer = viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.postValue(SharingState.Running)

                val result = bridge.requestFileTransfer(transfer.id, contents) {
                    runBlocking {
                        transferRepository.insert(transfer)
                    }
                }

                _state.postValue(SharingState.Success(transfer))

                if (result) {
                    transportSeat.beginFileTransfer(bridge, bridge.remoteClient, groupId, Direction.Outgoing)
                }
                Log.e("APPTESTTTT: ", "Transfer "+result)
            } catch (e: Exception) {
                Log.e("APPTESTTTT: ", "Transfer Error "+e)
                _state.postValue(SharingState.Error(e))
                e.printStackTrace()
                bridge.closeQuietly()
            } finally {
                consumer = null
            }
        }
    }
}

sealed class SharingState {
    object Running : SharingState()

    class Success(val transfer: Transfer) : SharingState()

    class Error(val exception: Exception) : SharingState()
}