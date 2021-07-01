package com.ydhnwb.opaku_app.infra

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    fun getCurrentTimeStamp(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
        return sdf.format(Date())
    }
}