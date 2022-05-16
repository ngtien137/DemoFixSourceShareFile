package com.example.demosharefile.ui.fragment.content_browser.pager.adaper

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.base.baselibrary.adapter.BaseAdapter
import com.base.baselibrary.adapter.viewholder.ViewHolderBase
import com.example.demosharefile.model.media.AppImage
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.databinding.ItemContentImageBinding
import java.util.*

class AdapterContentImage(private val viewLifecycleOwner: LifecycleOwner) :
    BaseAdapter<AppImage>(R.layout.item_content_image) {

    var liveSelectedImage = MutableLiveData<Stack<AppImage>>()

    fun checkSelected(image: AppImage) {
        val list = liveSelectedImage.value ?: Stack()
        if (list.search(image) == -1) {
            liveSelectedImage.value = list.also {
                it.add(image)
            }
        } else {
            liveSelectedImage.value = list.also {
                it.remove(image)
            }
        }
    }

    override fun onConfigViewHolder(holder: ViewHolderBase, adapterPosition: Int) {
        getItem(adapterPosition)?.let { item ->
            val binding = holder.binding
            if (binding is ItemContentImageBinding) {
                binding.listSelected = liveSelectedImage
            }
        }
    }

    override fun getDefineLifecycleOwner(): LifecycleOwner {
        return viewLifecycleOwner
    }
}