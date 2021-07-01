package com.ydhnwb.opaku_app.data.product

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ydhnwb.opaku_app.data.common.util.ApiErrorResponse
import com.ydhnwb.opaku_app.data.product.api.ProductApi
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.product.entity.ProductEntity
import com.ydhnwb.opaku_app.domain.product.repo.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val productApi: ProductApi) : ProductRepository {
    override suspend fun findById(id: String): Flow<BaseResult<ProductEntity, Failure>> {
        return flow {
            val response = productApi.findById(id)
            if(response.isSuccessful){
                val body = response.body()!!
                val p = ProductEntity(body.id, body.name, body.price, body.image)
                emit(BaseResult.Success(p))
            }else{
                val type = object : TypeToken<ApiErrorResponse>(){}.type
                val err : ApiErrorResponse = Gson().fromJson(response.errorBody()!!.charStream(), type)
                val failure = Failure(code = err.code, message = err.message)
                emit(BaseResult.Error(failure))
            }
        }
    }

    override suspend fun all(): Flow<BaseResult<List<ProductEntity>, Failure>> {
        return flow {
            val response = productApi.all()
            if (response.isSuccessful) {
                val body = response.body()!!
                val products = mutableListOf<ProductEntity>()
                body.forEach { productResponse ->
                    products.add(
                        ProductEntity(
                            productResponse.id,
                            productResponse.name,
                            productResponse.price,
                            productResponse.image
                        )
                    )
                }
                emit(BaseResult.Success(products))
            }else{
                val type = object : TypeToken<ApiErrorResponse>(){}.type
                val err : ApiErrorResponse = Gson().fromJson(response.errorBody()!!.charStream(), type)
                val failure = Failure(code = err.code, message = err.message)
                emit(BaseResult.Error(failure))
            }
        }
    }

    override suspend fun search(q: String): Flow<BaseResult<List<ProductEntity>, Failure>> {
        return flow {
            println("the query is ${q}")
            val response = productApi.search(q)
            if (response.isSuccessful) {
                val body = response.body()!!
                val products = mutableListOf<ProductEntity>()
                body.forEach { productResponse ->
                    products.add(
                        ProductEntity(
                            productResponse.id,
                            productResponse.name,
                            productResponse.price,
                            productResponse.image
                        )
                    )
                }
                emit(BaseResult.Success(products))
            }else{
                val type = object : TypeToken<ApiErrorResponse>(){}.type
                val err : ApiErrorResponse = Gson().fromJson(response.errorBody()!!.charStream(), type)
                val failure = Failure(code = err.code, message = err.message)
                emit(BaseResult.Error(failure))
            }
        }
    }
}