package com.duongtv.firstarwithsceneform

import android.graphics.Color
import java.util.*


class Utils {
    companion object {

        fun randomColor(): Int {
            val rnd = Random()
            val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            return color
        }
    }
}