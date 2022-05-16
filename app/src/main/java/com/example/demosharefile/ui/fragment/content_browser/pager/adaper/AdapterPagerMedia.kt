package com.example.demosharefile.ui.fragment.content_browser.pager.adaper

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.demosharefile.ui.fragment.content_browser.pager.IPagerMediaFragment

class AdapterPagerMedia(fragment: Fragment, private val listFragment:ArrayList<Fragment>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return listFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return listFragment[position]
    }

    fun getTitle(position: Int): String {
        var title = ""
        val fragment = listFragment[position]
        if (fragment is IPagerMediaFragment) {
            title = fragment.getTabTitle()
        }
        return title
    }


}