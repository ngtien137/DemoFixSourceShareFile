package com.example.demosharefile.utils

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.base.baselibrary.adapter.BaseAdapter
import com.base.baselibrary.adapter.BaseListAdapter
import com.base.baselibrary.adapter.SuperAdapter
import com.base.baselibrary.utils.DimensUtils
import com.base.baselibrary.utils.SDKUtils
import com.base.baselibrary.utils.onDebouncedClick
import com.base.baselibrary.utils.onDebouncedClickGlobal
import com.base.baselibrary.views.rv_touch_helper.ItemTouchHelperExtension
import com.base.baselibrary.views.rv_touch_helper.VerticalDragTouchHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.demosharefile.model.media.AppImage
import com.google.android.material.appbar.AppBarLayout
import org.monora.uprotocol.client.android.GlideApp
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.util.Graphics
import org.monora.uprotocol.client.android.util.picturePath
import org.monora.uprotocol.core.protocol.Client
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@BindingConversion
fun convertBooleanToVisibility(visible: Boolean): Int {
    return if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("tv_apply_height")
fun TextView.applyHeightToNone(height: Float) {
    layoutParams.height = height.toInt()
}


@BindingAdapter("tv_apply_marquee")
fun TextView.applyMarquee(apply: Boolean?) {
    post {
        apply?.let {
            if (it) {
                ellipsize = TextUtils.TruncateAt.MARQUEE
                isSelected = true
            }
        }
    }
}

@BindingAdapter("tv_get_file_name_from_path")
fun TextView.setNameFromFile(path: String?) {
    path?.let {
        val file = File(it)
        text = file.name
    }
}

@BindingAdapter("sw_checked_listener")
fun CompoundButton.applyCheckedListener(checkedListener: CompoundButton.OnCheckedChangeListener?) {
    setOnCheckedChangeListener(checkedListener)
}

@BindingAdapter("translate_from_bottom")
fun View.translateFromBottom(isOpen: Boolean?) {
    post {
        if (translationY.toInt() == height && isOpen == true) {
            animate().translationY(0f)
        } else if (translationY.toInt() == 0 && isOpen != true) {
            animate().translationY(height.toFloat())
        }
    }
}

@BindingAdapter("translate_from_end")
fun View.translateFromEnd(isOpen: Boolean?) {
    post {
        if (translationX.toInt() == width && isOpen == true) {
            animate().translationY(0f)
        } else if (translationX.toInt() == 0 && isOpen != true) {
            animate().translationY(width.toFloat())
        }
    }
}

@BindingAdapter("debounceClick")
fun View.onDebouncedClick(listener: View.OnClickListener?) {
    listener?.let {
        this.onDebouncedClick {
            listener.onClick(this)
        }
    }

}

@BindingAdapter("debounceClickGlobal")
fun View.onDebouncedClickGlobalBinding(listener: View.OnClickListener?) {
    listener?.let {
        this.onDebouncedClickGlobal {
            listener.onClick(this)
        }
    }
}

@BindingAdapter("longClick")
fun View.onLongClickView(listener: View.OnLongClickListener) {
    setOnLongClickListener { v -> listener.onLongClick(v) }
}

@BindingAdapter("rv_apply_item_touch_helper")
fun RecyclerView.applyItemTouchHelper(itemTouchHelperExtension: ItemTouchHelperExtension?) {
    itemTouchHelperExtension?.attachToRecyclerView(this)
}

@BindingAdapter("rv_vertical_drag")
fun RecyclerView.enableVerticalDrag(enable: Boolean?) {
    enable?.let {
        adapter?.let {
            if (adapter is SuperAdapter<*>) {
                val callback = VerticalDragTouchHelper(adapter as SuperAdapter<*>)
                ItemTouchHelper(callback).attachToRecyclerView(this)
            }
        }
    }
}

@BindingAdapter("rv_snap_linear")
fun RecyclerView.attachLinearSnapHelper(snap: Boolean? = true) {
    if (snap == true) {
        LinearSnapHelper().attachToRecyclerView(this)
    }
}

@BindingAdapter("rv_set_adapter")
fun <T : Any> RecyclerView.applyAdapter(applyAdapter: BaseAdapter<T>?) {
    applyAdapter?.apply {
        adapter = applyAdapter
    }
}

@BindingAdapter("rv_set_adapter")
fun <T : RecyclerView.ViewHolder> RecyclerView.applyAdapter(applyAdapter: RecyclerView.Adapter<T>?) {
    applyAdapter?.apply {
        adapter = applyAdapter
    }
}

@BindingAdapter("rv_set_adapter")
fun <T : Any> RecyclerView.applyAdapter(applyAdapter: SuperAdapter<T>?) {
    applyAdapter?.apply {
        adapter = applyAdapter
    }
}

@BindingAdapter("rv_set_adapter")
fun <T : Any> RecyclerView.applyListAdapter(applyAdapter: BaseListAdapter<T>?) {
    applyAdapter?.apply {
        adapter = applyAdapter
    }
}

@BindingAdapter("rv_set_fix_size")
fun RecyclerView.setFixSize(set: Boolean?) {
    setHasFixedSize(set ?: false)
}

@BindingAdapter("img_set_drawable_id")
fun ImageView.setDrawableById(id: Int) {
    setImageResource(id)
}

@BindingAdapter("anim_visible")
fun View.startAnimVisible(visible: Int?) {
    if (visible == null) {
        return
    }
    if (visible == View.VISIBLE && this.visibility != View.VISIBLE) {
        this.visibility = visible
        alpha = 0f
        animate().alpha(1f)
    } else if ((visible == View.GONE && this.visibility != View.GONE) || (visible == View.INVISIBLE && this.visibility != View.INVISIBLE)) {
        alpha = 1f
        animate().alpha(0f)
        postDelayed({
            this.visibility = visible
        }, 300)
    }
}

@BindingAdapter("glide_load_splash")
fun ImageView.glideLoadDrawableSplash(drawable: Drawable?) {
    drawable?.let {
        if (SDKUtils.isBuildLargerThan(Build.VERSION_CODES.N)) {
            post {
                Glide.with(this).load(drawable).override(width, height).into(this)
            }
        } else {
            glideLoadDrawableNoOverride(drawable)
        }
    }
}

@BindingAdapter("glide_load_drawable")
fun ImageView.glideLoadDrawable(drawable: Drawable?) {
    drawable?.let {
        post {
            Glide.with(this).load(drawable).override(width, height).into(this)
        }
    }
}

@BindingAdapter("glide_load_drawable_no_override")
fun ImageView.glideLoadDrawableNoOverride(drawable: Drawable?) {
    drawable?.let {
        Glide.with(this).load(drawable).into(this)
    }
}

@BindingAdapter("glide_load_path")
fun ImageView.glideLoadDrawable(path: String?) {
    path?.let {
        post {
            Glide.with(this).load(path).override(width, height).into(this)
        }
    }
}

@BindingAdapter("glide_load_drawable_id")
fun ImageView.glideLoadDrawable(id: Int?) {
    id?.let {
        post {
            Glide.with(this).load(id).override(width, height).into(this)
        }
    }
}

@BindingAdapter("tv_set_underline_text")
fun TextView.tvSetUnderlineText(text: String?) {
    val content = SpannableString(text ?: "")
    content.setSpan(UnderlineSpan(), 0, content.length, 0)
    setText(content)
}

@BindingAdapter("img_load_drawable_id")
fun ImageView.imgLoadDrawableById(id: Int?) {
    id?.let {
        setImageResource(id)
    }
}

@BindingAdapter("marginTopWithFullScreen")
fun View.setMarginTop(space: Float) {
    if (this.layoutParams is ConstraintLayout.LayoutParams) {
        val params = this.layoutParams as ConstraintLayout.LayoutParams
        val barHeight = DimensUtils.convertDpToPixel(24f)
        var statusBarHeight = 0
        val resourceId = context.resources.getIdentifier(
            "status_bar_height",
            "dimen",
            "android"
        )
        if (resourceId > 0) {
            statusBarHeight = context.resources.getDimensionPixelOffset(resourceId)
        }
        if (statusBarHeight > barHeight) {
            val actionBarSize = statusBarHeight + space
            params.setMargins(0, actionBarSize.toInt(), 0, 0)
        } else {
            params.setMargins(0, space.toInt(), 0, 0)
        }
    }
}

@BindingAdapter("marginBottomWithFullScreen")
fun View.setMarginBottom(space: Float) {
    if (this.layoutParams is ConstraintLayout.LayoutParams) {
        val params = this.layoutParams as ConstraintLayout.LayoutParams
        DimensUtils.convertDpToPixel(24f)
        var statusBarHeight = 0
        val resourceId = context.resources.getIdentifier(
            "navigation_bar_height",
            "dimen",
            "android"
        )
        if (resourceId > 0) {
            statusBarHeight = context.resources.getDimensionPixelOffset(resourceId)
        }
        if (SDKUtils.isBuildLargerThan(Build.VERSION_CODES.R) && statusBarHeight > 0) {
            val actionBarSize = statusBarHeight + space
            params.setMargins(0, 0, 0, actionBarSize.toInt())
        } else {
            params.setMargins(0, 0, 0, space.toInt())
        }
    }
}

@BindingAdapter("ab_none_shadow")
fun AppBarLayout.setNoneShadow(apply: Boolean?) {
    if (apply == true) {
        outlineProvider = null
    }
}

//Your binding write below

@BindingAdapter("isLinearGradient", "linearGradientColors")
fun TextView.appTitleLinearGradient(isLinearGradient: Boolean?, linearGradientColors: IntArray?) {
    if (isLinearGradient == true && linearGradientColors != null) {
        val paint: TextPaint = this.paint
        val width = paint.measureText(this.text.toString())

        val textShader: Shader = LinearGradient(
            0f, 0f, width, this.textSize, linearGradientColors, null, Shader.TileMode.CLAMP
        )
        this.setTextColor(linearGradientColors.firstOrNull() ?: Color.BLACK)
        this.paint.shader = textShader
    }
}

enum class Directions {
    LEFT, TOP, RIGHT, BOTTOM
}

@BindingAdapter("drawableTextView", "isShowDrawableTextView", "directionTextView")
fun TextView.setCustomDrawableTextView(
    drawableTextView: Int?,
    isShowDrawableTextView: Boolean?,
    directionTextView: Directions?
) {
    isShowDrawableTextView?.let {
        if (!isShowDrawableTextView) {
            this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        } else {
            drawableTextView?.let {
                val resDrawable = ResourcesCompat.getDrawable(resources, drawableTextView, null)
                when (directionTextView) {
                    Directions.LEFT -> {
                        this.setCompoundDrawablesWithIntrinsicBounds(resDrawable, null, null, null)
                    }
                    Directions.TOP -> {
                        this.setCompoundDrawablesWithIntrinsicBounds(null, resDrawable, null, null)
                    }
                    Directions.RIGHT -> {
                        this.setCompoundDrawablesWithIntrinsicBounds(null, null, resDrawable, null)
                    }
                    Directions.BOTTOM -> {
                        this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, resDrawable)
                    }
                }
            }
        }
    }

}

@BindingAdapter("formatDate")
fun TextView.formatDate(date: Date?) {
    date?.let {
        try {
            val dateFormat = SimpleDateFormat("MMM dd,yyyy", Locale.ENGLISH)
            val dateString = dateFormat.format(date)
            this.text = dateString
        } catch (e: Exception) {
        }
    }
}

@BindingAdapter("scrollable")
fun setScrollable(appBarLayout: AppBarLayout, scrollable: Boolean) {
    val layoutParams = appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
    val behavior = (layoutParams.behavior as? AppBarLayout.Behavior) ?: AppBarLayout.Behavior()
    behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
        override fun canDrag(appBarLayout: AppBarLayout): Boolean = scrollable
    })
    layoutParams.behavior = behavior
}

@BindingAdapter("glide_load_image")
fun ImageView.glideLoadMediaImage(media: AppImage?) {
    media?.let {
        post {
            Glide.with(this).load(media.fileUri ?: media.path).placeholder(R.color.white)
                .override(width, height).into(this)
        }
    }
}

@BindingAdapter("glide_load_client_image")
fun loadPictureOfClient(imageView: ImageView, client: Client?) {
    if (client == null) return

    try {
        val default = Graphics.createIconBuilder(imageView.context).buildRound(client.clientNickname)

        GlideApp.with(imageView)
            .load(imageView.context.getFileStreamPath(client.picturePath))
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(default)
            .error(default)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    } catch (ignored: Exception) {
    }
}