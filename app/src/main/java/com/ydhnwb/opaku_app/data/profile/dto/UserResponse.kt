package com.ydhnwb.opaku_app.data.profile.dto

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id") var id : Int,
    @SerializedName("name") var name : String,
    @SerializedName("email") var email : String
)