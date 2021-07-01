package com.ydhnwb.opaku_app.data.login.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email : String,
    @SerializedName("password") val password: String
)