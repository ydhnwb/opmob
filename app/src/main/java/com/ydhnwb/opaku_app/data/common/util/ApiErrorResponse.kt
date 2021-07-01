package com.ydhnwb.opaku_app.data.common.util

import com.google.gson.annotations.SerializedName

data class ApiErrorResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)