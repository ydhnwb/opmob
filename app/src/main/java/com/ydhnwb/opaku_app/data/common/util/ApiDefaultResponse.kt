package com.ydhnwb.opaku_app.data.common.util

import com.google.gson.annotations.SerializedName

data class ApiDefaultResponse (
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)