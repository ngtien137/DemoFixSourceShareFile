package com.base.baselibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.baselibrary.adapter.function.SuperActionMenu
import com.base.baselibrary.adapter.function.SuperDragVertical
import com.base.baselibrary.adapter.function.SuperSelect
import com.base.baselibrary.adapter.listener.IDragVerticalAdapter
import com.base.baselibrary.adapter.listener.ISelectedSuperAdapter
import com.base.baselibrary.adapter.listener.ISuperAdapterListener
import com.base.baselibrary.adapter.listener.ListItemListener
import com.base.baselibrary.adapter.viewholder.SuperHolderBase
import com.base.baselibrary.utils.onDebouncedClick
import com.base.baselibrary.views.rv_touch_helper.ItemTouchHelperCallback
import com.base.baselibrary.views.rv_touch_helper.ItemTouchHelperExtension
import com.base.baselibrary.views.rv_touch_helper.VerticalDragTouchHelper
import org.monora.uprotocol.client.android.BR
import java.util.*


open class SuperAdapter<T : Any>(@LayoutRes private val resLayout: Int) :
    RecyclerView.Adapter<SuperHolderBase>(), ISelectedSuperAdapter, IDragVerticalAdapter {

    //region Super Features

    private var annotationSelected: SuperSelect? = null
    private var annotationDragVertical: SuperDragVertical? = null
    protected var annotationActionMenu: SuperActionMenu? = null
    var onScrolledToLastItem: () -> Unit = {}

    //endregion

    //region properties

    var attachedRecyclerView: RecyclerView? = null

    var liveListSelected = MutableLiveData<Stack<T>>()

    private var lastSelectedPosition = -1

    protected var inflater: LayoutInflater? = null

    private var itemTouchHelperActionMenu: ItemTouchHelperExtension? = null

    var liveModeSelected = MutableLiveData(false)
        private set

    var data: List<T>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var listener: ListItemListener? = null

    fun setListSelected(liveListSelected: MutableLiveData<Stack<T>>, needNotify: Boolean = false) {
        this.liveListSelected = liveListSelected
        if (needNotify)
            notifyDataSetChanged()
    }

    //endregion

    init {
        val annotations = this::class.java.declaredAnnotations
        for (annotation in annotations) {
            when (annotation) {
                is SuperSelect -> {
                    annotationSelected = annotation
                }
                is SuperDragVertical -> {
                    annotationDragVertical = annotation
                }
                is SuperActionMenu -> {
                    annotationActionMenu = annotation
                }
                else -> {
                }
            }
        }
        liveListSelected.value = Stack()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperHolderBase {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.context)
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater!!, resLayout, parent, false
        )
        return SuperHolderBase(binding, annotationActionMenu)
    }

    override fun getItemCount() = data?.size ?: 0

    override fun onBindViewHolder(holder: SuperHolderBase, position: Int) {
        initBindingHolder(holder)
    }

    override fun onBindViewHolder(
        holder: SuperHolderBase,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            initBindingHolder(holder)
            onConfigPayLoad(holder, position, payloads)
        }
    }

    protected fun initBindingHolder(holder: SuperHolderBase) {
        getItem(holder.adapterPosition).let { item ->
            holder.binding.setVariable(BR.item, item)
            holder.binding.setVariable(BR.itemPosition, holder.adapterPosition)
            holder.binding.setVariable(BR.listener, listener)
            holder.binding.setVariable(BR.listSelected, liveListSelected)
            holder.binding.setVariable(BR.modeSelected, liveModeSelected)
            val context = holder.binding.root.context
            checkSelected(holder)
            onConfigViewHolder(holder, holder.adapterPosition)
            if (getDefineLifecycleOwner() != null) {
                holder.binding.lifecycleOwner = getDefineLifecycleOwner()
            } else if (context is LifecycleOwner) {
                holder.binding.lifecycleOwner = context
            }
            holder.binding.executePendingBindings()
        }
    }

    open fun getDefineLifecycleOwner(): LifecycleOwner? {
        return null
    }

    fun setSelectedItem(position: Int) {
        liveListSelected.value?.add(data!![position])
        liveListSelected.value = liveListSelected.value
        lastSelectedPosition = position
    }

    fun setLiveModeSelected(liveModeSelected: MutableLiveData<Boolean>) {
        this.liveModeSelected = liveModeSelected
    }

    override fun selectedAll() {
        changeModeSelect(true)
        liveListSelected.value?.let {
            it.clear()
            it.addAll(data ?: ArrayList())
        }
        liveListSelected.value = liveListSelected.value
        //notifyItemRangeChanged(0, data?.size ?: 0)
    }

    override fun clearAllSelect() {
        liveListSelected.value?.let {
            it.clear()
        }
        liveListSelected.value = liveListSelected.value
        //notifyItemRangeChanged(0, data?.size ?: 0)
        lastSelectedPosition = -1
        changeModeSelect(false)
    }

    fun clearSelectWithoutPayloads() {
        liveListSelected.value?.let {
            it.clear()
        }
        liveListSelected.value = liveListSelected.value
        lastSelectedPosition = -1
        notifyDataSetChanged()
        changeModeSelect(false)
    }

    open fun onHandleLongClickToCheck(item: T, holder: SuperHolderBase): Boolean {
        if (annotationSelected?.validCheckAgainAfterEnableSelectedByLongClick == true) {
            checkValidateCheckWithListener(item, holder)
        }
        return true
    }

    protected fun checkValidateCheckWithListener(item: T, holder: SuperHolderBase) {
        if (listener != null && listener is ISuperAdapterListener<*>) {
            if ((listener as ISuperAdapterListener<T>).onValidateBeforeCheckingItem(
                    item,
                    holder.adapterPosition
                ) || (liveListSelected.value!!.search(item) != -1 && (annotationSelected!!.enableUnSelect && !annotationSelected!!.enableSelectItemMultipleTime))
            ) {
                validateCheck(item, holder)
            }
        } else
            validateCheck(item, holder)
    }

    fun getItem(position: Int) = data?.get(position)

    protected fun checkSelected(holder: SuperHolderBase) {
        annotationSelected?.let { annotationSelected ->
            if (annotationSelected.viewHandleSelectId != -1) {
                getItem(holder.adapterPosition)?.let { item ->
                    holder.itemView.findViewById<View>(annotationSelected.viewHandleSelectId)?.let {
                        val selected = liveListSelected.value!!.search(item) != -1
                        onConfigSelected(holder, holder.adapterPosition, selected)
                        if (annotationSelected.handleByLongClick) {
                            it.setOnLongClickListener {
                                if (!liveModeSelected.value!!)
                                    changeModeSelect(true)
                                onHandleLongClickToCheck(item, holder)
                            }
                            it.onDebouncedClick {
                                if (liveModeSelected.value!!)
                                    checkValidateCheckWithListener(item, holder)
                                else {
                                    checkViewIdHandleSelectSingleClick(holder.adapterPosition)
                                }
                            }
                        } else {
                            it.onDebouncedClick {
                                if (!liveModeSelected.value!!)
                                    changeModeSelect(true)
                                if (liveModeSelected.value!!)
                                    checkValidateCheckWithListener(item, holder)
                                else {
                                    checkViewIdHandleSelectSingleClick(holder.adapterPosition)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected fun checkViewIdHandleSelectSingleClick(position: Int) {
        (listener as ISuperAdapterListener<T>?)?.onViewHandleCheckClicked(
            getItem(position) as T,
            position
        )
    }

    protected fun changeModeSelect(select: Boolean) {
        liveModeSelected.value = select
    }

    private fun validateCheck(item: T, holder: SuperHolderBase) {
        var selected = liveListSelected.value!!.search(item) != -1
        if (annotationSelected!!.enableSelectItemMultipleTime) {
            selected = false
        }
        if (selected) {
            if (annotationSelected!!.enableUnSelect) {
                liveListSelected.value?.remove(item)
                lastSelectedPosition = -1
                notifyItemChanged(holder.adapterPosition, 1)
                selected = false
                if (liveListSelected.value.isNullOrEmpty() && annotationSelected!!.disableSelectModeWhenEmpty) {
                    changeModeSelect(false)
                }
            }
        } else {
            val oldPosition = lastSelectedPosition
            liveListSelected.value?.push(item)
            lastSelectedPosition = holder.adapterPosition
            if (!annotationSelected!!.enableMultiSelect && oldPosition != -1) {
                liveListSelected.value?.remove(getItem(oldPosition))
                notifyItemChanged(oldPosition, 1)
            }
            selected = true
            notifyItemChanged(lastSelectedPosition, 1)
        }
        liveListSelected.value = liveListSelected.value
        if (listener is ISuperAdapterListener<*>?) {
            val pos = holder.adapterPosition
            (listener as ISuperAdapterListener<T>?)?.onItemSelected(
                getItem(pos) as T,
                pos,
                selected
            )
        }
    }

    override fun onMoveItem(touchPosition: Int, targetPosition: Int) {
        annotationDragVertical?.let { annotationDragVertical ->
            (listener as ISuperAdapterListener<T>?)?.onItemSwap(touchPosition, targetPosition)
            data?.let { mItems ->
                if (touchPosition < targetPosition) {
                    for (i in touchPosition until targetPosition) {
                        Collections.swap(mItems, i, i + 1)
                        notifyItemChanged(i, 0)
                        notifyItemChanged(i + 1, 0)
                    }
                } else {
                    for (i in touchPosition downTo targetPosition + 1) {
                        Collections.swap(mItems, i, i - 1)
                        notifyItemChanged(i, 0)
                        notifyItemChanged(i - 1, 0)
                    }
                }
                notifyItemMoved(touchPosition, targetPosition)
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (annotationDragVertical != null) {
            val callback = VerticalDragTouchHelper(this)
            ItemTouchHelper(callback).attachToRecyclerView(recyclerView)
        }
        if (annotationActionMenu != null) {
            val callback = ItemTouchHelperCallback()
            itemTouchHelperActionMenu = ItemTouchHelperExtension(callback).apply {
                attachToRecyclerView(recyclerView)
            }
        }
        this.attachedRecyclerView = recyclerView
        val layoutManager = recyclerView.layoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!data.isNullOrEmpty() && layoutManager != null && layoutManager is LinearLayoutManager) {
                    val lastCompleteShownPosition: Int = layoutManager.findLastVisibleItemPosition()
                    if (lastCompleteShownPosition == data!!.size - 1 && dy > 0) {
                        onScrolledToLastItem.invoke()
                    }
                }
            }
        })
    }

    fun closeAllActionMenu() {
        itemTouchHelperActionMenu?.closeOpened()
    }

}