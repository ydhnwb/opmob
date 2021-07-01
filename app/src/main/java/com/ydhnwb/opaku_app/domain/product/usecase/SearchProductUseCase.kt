package com.ydhnwb.opaku_app.domain.product.usecase

import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.product.entity.ProductEntity
import com.ydhnwb.opaku_app.domain.product.repo.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchProductUseCase @Inject constructor(private val productRepository: ProductRepository) {
    suspend fun invoke(query: String) : Flow<BaseResult<List<ProductEntity>, Failure>>{
        return productRepository.search(query)
    }
}