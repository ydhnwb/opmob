package com.ydhnwb.opaku_app.data.transaction.dto

import com.google.gson.annotations.SerializedName

data class TransactionResponse (
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String,
    @SerializedName("price") var price: Int,
    @SerializedName("image") var image: String,
    @SerializedName("user") var user: TransactionUserResponse
)