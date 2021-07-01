package com.ydhnwb.opaku_app.data.cart.dto

import com.google.gson.annotations.SerializedName

data class CartResponse(
    @SerializedName("id") var id: Int,
    @SerializedName("product") var product: CartProductResponse,
    @SerializedName("user") var user: CartUserResponse
)