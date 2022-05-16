package com.example.demosharefile.ui.fragment.content_browser

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.demosharefile.ui.fragment.content_browser.pager.adaper.AdapterPagerMedia
import com.example.demosharefile.ui.fragment.content_browser.pager.audio.PagerAudioFragment
import com.example.demosharefile.ui.fragment.BaseMainFragment
import com.example.demosharefilekotlin.ui.fragment.content_browser.pager.image.PagerImageFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.databinding.FragmentContentBrowserBinding

@AndroidEntryPoint
class ContentBrowserFragment : BaseMainFragment<FragmentContentBrowserBinding>() {

    private val viewModel by viewModels<ContentBrowserViewModel>()

    private lateinit var pagerAdapter: AdapterPagerMedia

    private val listFragment = ArrayList<Fragment>().also {
        it.add(PagerAudioFragment.newInstance())
        it.add(PagerImageFragment.newInstance())
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_content_browser
    }

    override fun initBinding() {

    }

    override fun initView() {
        pagerAdapter = getPagerAdapter()
        binding.pager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = pagerAdapter.getTitle(position)
        }.attach()
        viewModel.receiveFlow(this, onCollect = {

        })
    }

    private fun getPagerAdapter() = AdapterPagerMedia(this, listFragment)

}