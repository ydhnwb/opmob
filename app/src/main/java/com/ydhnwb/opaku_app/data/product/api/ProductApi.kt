package com.ydhnwb.opaku_app.data.product.api

import com.ydhnwb.opaku_app.data.product.dto.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {
    @GET("api/product")
    suspend fun all() : Response<List<ProductResponse>>

    @GET("api/product/search")
    suspend fun search(@Query("q") q : String) : Response<List<ProductResponse>>

    @GET("api/product/{id}")
    suspend fun findById(@Path("id") id : String) : Response<ProductResponse>
}