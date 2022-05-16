package com.example.demosharefile.ui.fragment.content_browser.pager.audio

import androidx.fragment.app.viewModels
import com.base.baselibrary.utils.getAppString
import com.example.demosharefile.ui.fragment.BaseMainFragment
import com.example.demosharefile.ui.fragment.content_browser.pager.IPagerMediaFragment
import dagger.hilt.android.AndroidEntryPoint
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.databinding.FragmentPagerAudioBinding

@AndroidEntryPoint
class PagerAudioFragment : BaseMainFragment<FragmentPagerAudioBinding>(), IPagerMediaFragment {

    companion object {

        @JvmStatic
        fun newInstance() =
            PagerAudioFragment().apply {

            }
    }

    //region properties

    private val viewModel by viewModels<PagerAudioViewModel>()

    //endregion

    //region lifecycle

    override fun getLayoutId(): Int {
        return R.layout.fragment_pager_audio
    }

    override fun setHandleBack(): Boolean {
        return false
    }

    override fun initBinding() {

    }

    override fun initView() {

    }

    override fun getTabTitle(): String {
        return getAppString(R.string.pager_audio)
    }

    //endregion
}