package com.ydhnwb.opaku_app.data.cart.dto

import com.google.gson.annotations.SerializedName

data class CartProductResponse(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price : Int,
    @SerializedName("image") val image: String
)