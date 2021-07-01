package com.ydhnwb.opaku_app.domain.cart.usecase

import com.ydhnwb.opaku_app.data.cart.dto.AddToCartRequest
import com.ydhnwb.opaku_app.domain.cart.entity.CartEntity
import com.ydhnwb.opaku_app.domain.cart.repo.CartRepository
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(private val cartRepository: CartRepository) {
    suspend fun invoke(addToCartRequest: AddToCartRequest) : Flow<BaseResult<CartEntity, Failure>> {
        return cartRepository.addToCart(addToCartRequest)
    }
}