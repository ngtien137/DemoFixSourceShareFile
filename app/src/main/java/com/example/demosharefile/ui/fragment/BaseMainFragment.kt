package com.example.demosharefile.ui.fragment

import androidx.databinding.ViewDataBinding
import com.base.baselibrary.fragment.BaseNavigationFragment
import com.example.demosharefile.ui.activity.MainActivity

abstract class BaseMainFragment<BD : ViewDataBinding> : BaseNavigationFragment<BD, MainActivity>() {
}