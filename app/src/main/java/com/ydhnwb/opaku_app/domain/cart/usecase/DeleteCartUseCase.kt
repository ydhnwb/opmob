package com.ydhnwb.opaku_app.domain.cart.usecase

import com.ydhnwb.opaku_app.domain.cart.repo.CartRepository
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteCartUseCase @Inject constructor(private val cartRepository: CartRepository) {
    suspend fun invoke(cartId: String) : Flow<BaseResult<Unit, Failure>> {
        return cartRepository.deleteCart(cartId)
    }
}