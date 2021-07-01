package com.ydhnwb.opaku_app.data.cart.dto

import com.google.gson.annotations.SerializedName

data class AddToCartRequest(
    @SerializedName("product_id") val productId : Int
)