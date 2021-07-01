package com.ydhnwb.opaku_app.data.transaction.dto

import com.google.gson.annotations.SerializedName

data class CreateTransactionRequest(
    @SerializedName("product_id") val productId: Int
)