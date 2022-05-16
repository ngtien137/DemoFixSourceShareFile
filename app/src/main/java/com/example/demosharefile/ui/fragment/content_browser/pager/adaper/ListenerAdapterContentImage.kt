package com.example.demosharefile.ui.fragment.content_browser.pager.adaper

import com.base.baselibrary.adapter.listener.ListItemListener
import com.example.demosharefile.model.media.AppImage

interface ListenerAdapterContentImage : ListItemListener {

    fun onClickImage(appImage: AppImage)
}