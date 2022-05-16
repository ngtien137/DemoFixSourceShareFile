package com.example.demosharefile.ui.activity

import android.content.*
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import com.base.baselibrary.activity.BaseActivity
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.app.Activity
import org.monora.uprotocol.client.android.backend.Backend
import org.monora.uprotocol.client.android.data.SharedTextRepository
import org.monora.uprotocol.client.android.databinding.LayoutUserProfileBinding
import org.monora.uprotocol.client.android.viewmodel.ChangelogViewModel
import org.monora.uprotocol.client.android.viewmodel.CrashLogViewModel
import org.monora.uprotocol.client.android.viewmodel.UserProfileViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: Activity() {
    private val changelogViewModel: ChangelogViewModel by viewModels()

    private val crashLogViewModel: CrashLogViewModel by viewModels()

    private val userProfileViewModel: UserProfileViewModel by viewModels()




}