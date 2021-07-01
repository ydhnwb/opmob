package com.ydhnwb.opaku_app.domain.product.usecase

import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.product.entity.ProductEntity
import com.ydhnwb.opaku_app.domain.product.repo.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllProductUseCase @Inject constructor(private val productRepository: ProductRepository) {
    suspend fun all() : Flow<BaseResult<List<ProductEntity>, Failure>> {
        return productRepository.all()
    }
}