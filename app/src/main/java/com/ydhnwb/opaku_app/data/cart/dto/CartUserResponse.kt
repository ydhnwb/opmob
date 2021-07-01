package com.ydhnwb.opaku_app.data.cart.dto

import com.google.gson.annotations.SerializedName

data class CartUserResponse(
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name : String,
    @SerializedName("email") var email : String
)