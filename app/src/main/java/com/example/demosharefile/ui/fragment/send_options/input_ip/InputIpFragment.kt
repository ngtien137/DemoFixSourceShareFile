package com.example.demosharefile.ui.fragment.send_options.input_ip

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.base.baselibrary.utils.observer
import com.example.demosharefile.viewmodel.MainViewModel
import com.example.demosharefile.ui.fragment.BaseMainFragment
import dagger.hilt.android.AndroidEntryPoint
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.databinding.FragmentInputIpBinding
import org.monora.uprotocol.core.protocol.communication.client.UnauthorizedClientException
import java.net.UnknownHostException

@AndroidEntryPoint
class InputIpFragment : BaseMainFragment<FragmentInputIpBinding>() {

    //region properties

    private val viewModel by viewModels<InputIpViewModel>()

    private val mainViewModel by activityViewModels<MainViewModel>()

    //endregion

    //region lifecycle

    override fun getLayoutId(): Int {
        return R.layout.fragment_input_ip
    }

    override fun initBinding() {
        binding.viewModel = viewModel
    }

    override fun initView() {
        viewModel.receiveFlow(this, onCollect = {
            when (it) {
                InputIpViewModel.EventInputIp.ClickConfirm -> {
                    val address = viewModel.getTextIp()
                    if (address.isEmpty()) {
                        binding.edtIp.error = getString(R.string.host_address_invalid_notice)
                    } else {
                        binding.edtIp.error = null
                        viewModel.connect(address)
                    }
                }
            }
        })
        observer(viewModel.state) {
            when (it) {
                is ManualConnectionState.Loading -> {

                }
                is ManualConnectionState.Error -> when (it.exception) {
                    is UnknownHostException -> binding.edtIp.error =
                        getString(R.string.error_unknown_host)
                    is UnauthorizedClientException -> binding.edtIp.error =
                        getString(R.string.error_not_allowed)
                    else -> binding.edtIp.error = it.exception.message
                }
                is ManualConnectionState.Loaded -> if (!it.isUsed) {
                    it.isUsed = true

//                    findNavController().navigate(
//                        ManualConnectionFragmentDirections.actionManualConnectionFragmentToAcceptClientFragment(
//                            it.clientRoute
//                        )
//                    )
                }
            }
        }
    }

    //endregion

}