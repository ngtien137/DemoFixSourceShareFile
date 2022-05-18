package com.example.demosharefile.ui.fragment.send_options.adapter

import androidx.lifecycle.LifecycleOwner
import com.base.baselibrary.adapter.BaseAdapter
import com.base.baselibrary.adapter.viewholder.ViewHolderBase
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.databinding.ItemOnlineClientBinding
import org.monora.uprotocol.client.android.model.ClientRoute
import org.monora.uprotocol.client.android.viewmodel.content.ClientContentViewModel

class AdapterOnlineClients(private var lifecycleOwner: LifecycleOwner) :
    BaseAdapter<ClientRoute>(R.layout.item_online_client) {

    override fun getDefineLifecycleOwner(): LifecycleOwner? {
        return lifecycleOwner
    }

    override fun onConfigViewHolder(holder: ViewHolderBase, adapterPosition: Int) {
        getItem(adapterPosition)?.let { item ->
            val binding = holder.binding
            if (binding is ItemOnlineClientBinding) {
                binding.clientContentViewModel = ClientContentViewModel(item.client)
            }
        }
        super.onConfigViewHolder(holder, adapterPosition)
    }

}