package com.ydhnwb.opaku_app.domain.product.repo

import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.product.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun findById(id : String) : Flow<BaseResult<ProductEntity,Failure>>
    suspend fun all() : Flow<BaseResult<List<ProductEntity>, Failure>>
    suspend fun search(q: String) : Flow<BaseResult<List<ProductEntity>, Failure>>
}