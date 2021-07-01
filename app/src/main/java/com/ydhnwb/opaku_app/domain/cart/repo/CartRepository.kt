package com.ydhnwb.opaku_app.domain.cart.repo

import com.ydhnwb.opaku_app.data.cart.dto.AddToCartRequest
import com.ydhnwb.opaku_app.domain.cart.entity.CartEntity
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun myCart() : Flow<BaseResult<List<CartEntity>, Failure>>
    suspend fun addToCart(addToCartRequest: AddToCartRequest) : Flow<BaseResult<CartEntity, Failure>>
    suspend fun deleteCart(cartId: String) : Flow<BaseResult<Unit, Failure>>
}