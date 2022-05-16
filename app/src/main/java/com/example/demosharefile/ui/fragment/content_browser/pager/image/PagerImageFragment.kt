package com.example.demosharefilekotlin.ui.fragment.content_browser.pager.image

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.base.baselibrary.utils.getAppString
import com.base.baselibrary.utils.observer
import com.example.demosharefilekotlin.R
import com.example.demosharefilekotlin.databinding.FragmentPagerImageBinding
import com.example.demosharefilekotlin.model.media.AppImage
import com.example.demosharefilekotlin.ui.activity.MainViewModel
import com.example.demosharefile.ui.fragment.BaseMainFragment
import com.example.demosharefilekotlin.ui.fragment.content_browser.ContentBrowserFragmentDirections
import com.example.demosharefile.ui.fragment.content_browser.pager.IPagerMediaFragment
import com.example.demosharefile.ui.fragment.content_browser.pager.adaper.AdapterContentImage
import com.example.demosharefile.ui.fragment.content_browser.pager.adaper.ListenerAdapterContentImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PagerImageFragment : BaseMainFragment<FragmentPagerImageBinding>(), IPagerMediaFragment,
    ListenerAdapterContentImage {

    private val adapter by lazy {
        AdapterContentImage(rootActivity).also {
            it.listener = this
        }
    }

    private val viewModel by viewModels<PagerImageViewModel>()

    private val mainViewModel by activityViewModels<MainViewModel>()

    companion object {

        @JvmStatic
        fun newInstance() =
            PagerImageFragment().apply {

            }
    }

    //region lifecycle

    override fun getLayoutId(): Int {
        return R.layout.fragment_pager_image
    }

    override fun initBinding() {
        binding.viewModel = viewModel
        adapter.liveSelectedImage = viewModel.liveSelectedImages
        binding.adapter = adapter
    }

    override fun initView() {
        viewModel.loadImages()
        observer(viewModel.liveImages) {
            adapter.data = it
        }
        viewModel.receiveFlow(this, onCollect = {
            when (it) {
                PagerImageViewModel.PagerImageEvent.ClickSend -> {
                    mainViewModel.applySelectedImages(viewModel.getListSelectedImages())
                    navigateTo(
                        R.id.contentBrowserFragment,
                        ContentBrowserFragmentDirections.actionContentBrowserFragmentToSendOptionsFragment()
                    )
                }
            }
        })
    }

    override fun getTabTitle(): String {
        return getAppString(R.string.pager_image)
    }


    //endregion

    //region list image

    override fun onClickImage(appImage: AppImage) {
        adapter.checkSelected(appImage)
    }

    //endregion
}