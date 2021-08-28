package com.duongtv.firstarwithsceneform

import android.graphics.Color
import java.util.*
import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import java.io.File


class Utils {
    companion object {
        val PACKAGE_NAME = BuildConfig.APPLICATION_ID
        fun randomColor(): Int {
            val rnd = Random()
            val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            return color
        }

        fun getRawUri(filename: String): Uri? {
            return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + File.pathSeparator + File.separator + PACKAGE_NAME + "/raw/" + filename)
        }
    }
}