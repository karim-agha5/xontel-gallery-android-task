package com.example.gallerydemokarimnabil.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Utils {
    companion object{

        fun openDeviceSettings(activity: Activity){
            activity.startActivity(Intent(Settings.ACTION_SETTINGS))
        }

    }
}