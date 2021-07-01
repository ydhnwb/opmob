package com.ydhnwb.opaku_app.data.cart.api

import com.ydhnwb.opaku_app.data.cart.dto.AddToCartRequest
import com.ydhnwb.opaku_app.data.cart.dto.CartResponse
import com.ydhnwb.opaku_app.data.common.util.ApiDefaultResponse
import com.ydhnwb.opaku_app.domain.cart.entity.CartEntity
import retrofit2.Response
import retrofit2.http.*

interface CartApi {
    @GET("api/cart")
    suspend fun myCart() : Response<List<CartResponse>>

    @POST("api/cart")
    suspend fun addToCart(@Body addToCartRequest: AddToCartRequest) : Response<CartEntity>

    @DELETE("api/cart/{id}")
    suspend fun delete(@Path("id") cartId: String) : Response<ApiDefaultResponse>
}