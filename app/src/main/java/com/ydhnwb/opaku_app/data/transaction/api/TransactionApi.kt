package com.ydhnwb.opaku_app.data.transaction.api

import com.ydhnwb.opaku_app.data.transaction.dto.CreateTransactionRequest
import com.ydhnwb.opaku_app.data.transaction.dto.TransactionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TransactionApi {
    @GET("api/transaction")
    suspend fun myTransaction() : Response<List<TransactionResponse>>

    @POST("api/transaction/")
    suspend fun createTransaction(@Body createTransactionRequest: CreateTransactionRequest) : Response<TransactionResponse>
}