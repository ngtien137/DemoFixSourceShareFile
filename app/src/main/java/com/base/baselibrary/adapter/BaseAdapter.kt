package com.base.baselibrary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.base.baselibrary.adapter.listener.ListItemListener
import com.base.baselibrary.adapter.viewholder.ViewHolderBase
import org.monora.uprotocol.client.android.BR

open class BaseAdapter<T : Any>(@LayoutRes private val resLayout: Int)

    : RecyclerView.Adapter<ViewHolderBase>() {

    lateinit var inflater: LayoutInflater

    var data: List<T>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var listener: ListItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderBase {
        initLayoutInflaterIfNecessary(parent)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater, resLayout, parent, false
        )
        return ViewHolderBase(binding)
    }

    fun initLayoutInflaterIfNecessary(parent: ViewGroup) {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
    }


    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    fun getItem(itemPosition: Int) = data?.get(itemPosition)

    override fun onBindViewHolder(holder: ViewHolderBase, position: Int) {
        val item = data?.get(position)
        holder.binding.setVariable(BR.item, item)
        holder.binding.setVariable(BR.itemPosition, holder.adapterPosition)
        holder.binding.setVariable(BR.listener, listener)
        onConfigViewHolder(holder, holder.adapterPosition)
        val context = holder.binding.root.context
        if (getDefineLifecycleOwner() != null) {
            holder.binding.lifecycleOwner = getDefineLifecycleOwner()
        } else if (context is LifecycleOwner) {
            holder.binding.lifecycleOwner = context
        }
        holder.binding.executePendingBindings()
    }

    open fun onConfigViewHolder(holder: ViewHolderBase, adapterPosition: Int) {}

    open fun getDefineLifecycleOwner(): LifecycleOwner? {
        return null
    }

}
