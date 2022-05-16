package com.example.demosharefile.ui.fragment.send_options

import androidx.fragment.app.viewModels
import com.example.demosharefile.ui.fragment.BaseMainFragment
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.databinding.FragmentSendOptionsBinding

class SendOptionsFragment : BaseMainFragment<FragmentSendOptionsBinding>() {

    //region properties

    private val viewModel by viewModels<SendOptionsViewModel>()

    //endregion

    override fun getLayoutId(): Int {
        return R.layout.fragment_send_options
    }

    override fun initBinding() {
        binding.viewModel = viewModel
    }

    override fun initView() {
        viewModel.receiveFlow(this, onCollect = {
            when (it) {
                SendOptionsViewModel.SendOptionsEvent.EventManualAddress -> {

                }
            }
        })
    }

}