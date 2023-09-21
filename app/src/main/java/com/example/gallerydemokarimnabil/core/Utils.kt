package com.example.gallerydemokarimnabil.core

import android.app.Activity
import android.content.Intent
import android.provider.Settings

class Utils {
    companion object{

        fun openDeviceSettings(activity: Activity){
            activity.startActivity(Intent(Settings.ACTION_SETTINGS))
        }

    }
}