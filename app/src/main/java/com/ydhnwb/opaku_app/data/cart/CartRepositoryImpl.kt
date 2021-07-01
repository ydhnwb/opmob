package com.ydhnwb.opaku_app.data.cart

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ydhnwb.opaku_app.data.cart.api.CartApi
import com.ydhnwb.opaku_app.data.cart.dto.AddToCartRequest
import com.ydhnwb.opaku_app.data.cart.dto.CartUserResponse
import com.ydhnwb.opaku_app.data.common.util.ApiErrorResponse
import com.ydhnwb.opaku_app.domain.cart.entity.CartEntity
import com.ydhnwb.opaku_app.domain.cart.entity.CartProductEntity
import com.ydhnwb.opaku_app.domain.cart.entity.CartUserEntity
import com.ydhnwb.opaku_app.domain.cart.repo.CartRepository
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(private val cartApi: CartApi) : CartRepository{
    override suspend fun myCart(): Flow<BaseResult<List<CartEntity>, Failure>> {
        return flow {
            val response = cartApi.myCart()
            if(response.isSuccessful){
                val temp = mutableListOf<CartEntity>()
                val body = response.body()!!
                body.forEach { c ->
                    val user = CartUserEntity(c.user.id, c.user.name, c.user.email)
                    val product = CartProductEntity(c.product.id, c.product.name, c.product.price, c.product.image)
                    temp.add(CartEntity(c.id, product, user))
                }
                emit(BaseResult.Success(temp))
            }else{
                val type = object : TypeToken<ApiErrorResponse>(){}.type
                val err : ApiErrorResponse = Gson().fromJson(response.errorBody()!!.charStream(), type)
                val failure = Failure(code = err.code, message = err.message)
                emit(BaseResult.Error(failure))
            }
        }
    }

    override suspend fun addToCart(addToCartRequest: AddToCartRequest): Flow<BaseResult<CartEntity, Failure>> {
        return flow {
            val res = cartApi.addToCart(addToCartRequest)
            if(res.isSuccessful){
                val body = res.body()!!
                val user = CartUserEntity(body.user.id, body.user.name, body.user.email)
                val product = CartProductEntity(body.product.id, body.product.name, body.product.price, body.product.image)
                val cart = CartEntity(body.id, product, user)
                emit(BaseResult.Success(cart))
            }else{
                val type = object : TypeToken<ApiErrorResponse>(){}.type
                val err : ApiErrorResponse = Gson().fromJson(res.errorBody()!!.charStream(), type)
                val failure = Failure(code = err.code, message = err.message)
                emit(BaseResult.Error(failure))
            }
        }
    }

    override suspend fun deleteCart(cartId: String): Flow<BaseResult<Unit, Failure>> {
        return flow {
            val res = cartApi.delete(cartId)
            if(res.isSuccessful){
                emit(BaseResult.Success(Unit))
            }else{
                val type = object : TypeToken<ApiErrorResponse>(){}.type
                val err : ApiErrorResponse = Gson().fromJson(res.errorBody()!!.charStream(), type)
                val failure = Failure(code = err.code, message = err.message)
                emit(BaseResult.Error(failure))
            }
        }
    }
}