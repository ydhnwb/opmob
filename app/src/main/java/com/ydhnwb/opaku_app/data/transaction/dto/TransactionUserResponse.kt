package com.ydhnwb.opaku_app.data.transaction.dto

import com.google.gson.annotations.SerializedName

data class TransactionUserResponse(
    @SerializedName("id") var id : Int,
    @SerializedName("name") var name : String,
    @SerializedName("email") var email: String
)