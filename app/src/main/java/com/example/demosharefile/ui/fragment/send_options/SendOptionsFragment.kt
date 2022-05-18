package com.example.demosharefile.ui.fragment.send_options

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.base.baselibrary.utils.observer
import com.base.baselibrary.views.ext.appToast
import com.example.demosharefile.ui.activity.ActivityMainViewModel
import com.example.demosharefile.ui.fragment.BaseMainFragment
import com.example.demosharefile.ui.fragment.send_options.adapter.AdapterOnlineClients
import com.example.demosharefile.ui.fragment.send_options.adapter.ListenerOnlineClientAction
import dagger.hilt.android.AndroidEntryPoint
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.database.model.UTransferItem
import org.monora.uprotocol.client.android.databinding.FragmentSendOptionsBinding
import org.monora.uprotocol.client.android.model.ClientRoute
import org.monora.uprotocol.client.android.viewmodel.ClientPickerViewModel
import org.monora.uprotocol.core.protocol.Direction
import kotlin.random.Random

@AndroidEntryPoint
class SendOptionsFragment : BaseMainFragment<FragmentSendOptionsBinding>(),
    ListenerOnlineClientAction {

    //region properties

    private val viewModel by viewModels<SendOptionsViewModel>()

    private val mainViewModel by activityViewModels<ActivityMainViewModel>()

    private val sharingViewModel: ShareFileViewModel by viewModels()

    private val clientPickerViewModel: ClientPickerViewModel by activityViewModels()

    private val adapter by lazy {
        AdapterOnlineClients(rootActivity).also {
            it.listener = this
        }
    }

    //endregion

    //region lifecycle

    override fun getLayoutId(): Int {
        return R.layout.fragment_send_options
    }

    override fun initBinding() {
        binding.viewModel = viewModel
        binding.adapter = adapter
    }

    override fun initView() {
        viewModel.receiveFlow(this, onCollect = {
            when (it) {
                SendOptionsViewModel.SendOptionsEvent.EventManualAddress -> {

                }
                is SendOptionsViewModel.SendOptionsEvent.ClientConnected -> {
                    clientPickerViewModel.bridge.postValue(it.bridge)
                }
                is SendOptionsViewModel.SendOptionsEvent.ClientConnectedError -> {
                    appToast("Error")
                }
            }
        })

        observer(viewModel.onlineClients) {
            adapter.data = it
        }

        clientPickerViewModel.bridge.observe(viewLifecycleOwner) { bridge ->
            //val (groupId, items) = contentBrowserViewModel.items ?: return@observe
            val groupId = Random.nextLong()
            val items = mainViewModel.listSelectedImages.mapIndexed {index, it->
                UTransferItem(
                    index.toLong(),
                    groupId,
                    it.name,
                    it.mimeType,
                    it.size,
                    null,
                    it.fileUri?.toString() ?: "",
                    Direction.Outgoing
                )
            }
            sharingViewModel.consume(bridge, groupId, items)
            appToast("Prepare for send file")
        }

    }

    //endregion

    //region list client

    override fun onConnectClient(clientRoute: ClientRoute) {
        viewModel.startConnect(clientRoute.client, clientRoute.address)
        appToast("Connect Client ${clientRoute.client.nickname}")
    }

    //endregion

}