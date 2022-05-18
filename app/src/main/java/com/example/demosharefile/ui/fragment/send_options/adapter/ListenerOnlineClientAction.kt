package com.example.demosharefile.ui.fragment.send_options.adapter

import com.base.baselibrary.adapter.listener.ListItemListener
import org.monora.uprotocol.client.android.model.ClientRoute

interface ListenerOnlineClientAction : ListItemListener {

    fun onConnectClient(clientRoute: ClientRoute)

}