package com.example.demosharefile.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.base.baselibrary.activity.BaseActivity
import com.base.baselibrary.utils.ActivityUtils
import com.base.baselibrary.utils.ActivityUtils.getResultLauncherForIntent
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.app.Activity
import org.monora.uprotocol.client.android.backend.Backend
import org.monora.uprotocol.client.android.data.SharedTextRepository
import org.monora.uprotocol.client.android.databinding.ActivityMainBinding
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


    private val REQUEST_PERMISSION = 1
    private val REQUEST_APP_SETTING = 10
    private var REQUEST_CUSTOM_INTENT = 137

    private var onAllow: (() -> Unit)? = null
    private var onDenied: (() -> Unit)? = null
    private var onResult: (resultCode: ActivityResult) -> Unit = {}
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    val listStoragePermission = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        resultLauncher = getResultLauncherForIntent {
            onResult.invoke(it)
        }
        if (isOnlyPortraitScreen())
            setPortraitScreen()
        super.onCreate(savedInstanceState)
        if (fixSingleTask()) {
            if (!isTaskRoot) {
                finish()
                return
            }
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        initView()
    }

    private fun initView(){

    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun setPortraitScreen() {
        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (e: Exception) {
        }
    }

    fun grantPermissionStorage(onAllow: () -> Unit) {
        doRequestPermission(listStoragePermission, onAllow, onDenied = {
            grantPermissionStorage(onAllow)
        })
    }

    open fun isOnlyPortraitScreen() = true

    open fun fixSingleTask(): Boolean = false

    protected open fun isOpenSettingIfCheckNotAskAgainPermission() = true

    fun checkPermission(
        permissions: Array<String>
    ): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions.forEach {
                if (checkSelfPermission(it) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    return false
                }
            }
        }
        return true
    }

    fun doRequestPermission(
        permissions: Array<String>,
        onAllow: () -> Unit = {}, onDenied: () -> Unit = {}
    ) {
        this.onAllow = onAllow
        this.onDenied = onDenied
        if (checkPermission(permissions)) {
            onAllow()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, REQUEST_PERMISSION)
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (checkPermission(permissions)) {
            onAllow?.invoke()
        } else {
            if (isOpenSettingIfCheckNotAskAgainPermission()) {
                for (i in permissions.indices) {
                    val rationale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        shouldShowRequestPermissionRationale(permissions[i])
                    } else {
                        false
                    }
                    if (!rationale && grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        onPermissionRejectAbsolute()
                        return
                    }
                }
            }
            onDenied?.invoke()
        }
    }

    fun openAppSetting(onResult: (result: ActivityResult) -> Unit = {}) {
        this.onResult = onResult
        ActivityUtils.openAppSetting(this, resultLauncher)
    }

    open fun onPermissionRejectAbsolute() {
        //Permisisions Don't ask again
    }

    //region save action
    private var isKeepSafeAction = false
    private var safeAction: (() -> Unit)? = null
    var isPause = false

    fun doSafeAction(action: () -> Unit) {
        if (!isPause) {
            action()
        } else {
            safeAction = action
            isKeepSafeAction = true
        }
    }

    override fun onPause() {
        isPause = true
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        isPause = false
        if (isKeepSafeAction) {
            safeAction?.invoke()
            safeAction = null
            isKeepSafeAction = false
        }
    }

    //endregion

}