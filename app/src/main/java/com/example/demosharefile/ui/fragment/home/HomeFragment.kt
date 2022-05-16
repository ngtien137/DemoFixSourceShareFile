package com.example.demosharefile.ui.fragment.home

import androidx.fragment.app.viewModels
import com.example.demosharefile.ui.fragment.BaseMainFragment
import dagger.hilt.android.AndroidEntryPoint
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.databinding.FragmentHomeBinding

@AndroidEntryPoint
class HomeFragment : BaseMainFragment<FragmentHomeBinding>() {

    private val viewModel by viewModels<HomeViewModel>()


    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initBinding() {
        binding.homeViewModel = viewModel
    }

    override fun initView() {
        viewModel.receiveFlow(this, onCollect = {
            when (it) {
                HomeViewModel.HomeEvent.EventReceive -> {
                    rootActivity.grantPermissionStorage {

                    }
                }
                HomeViewModel.HomeEvent.EventSend -> {
                    rootActivity.grantPermissionStorage {
                        navigateTo(
                            R.id.homeFragment,
                            HomeFragmentDirections.actionHomeFragmentToContentBrowserFragment()
                        )
                    }
                }
            }
        })
    }

}