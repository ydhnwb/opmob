package com.ydhnwb.opaku_app.data.transaction

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ydhnwb.opaku_app.data.common.util.ApiErrorResponse
import com.ydhnwb.opaku_app.data.transaction.api.TransactionApi
import com.ydhnwb.opaku_app.data.transaction.dto.CreateTransactionRequest
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.transaction.entity.TransactionEntity
import com.ydhnwb.opaku_app.domain.transaction.entity.TransactionUserEntity
import com.ydhnwb.opaku_app.domain.transaction.repo.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(private val transactionApi: TransactionApi) : TransactionRepository {
    override suspend fun myTransaction(): Flow<BaseResult<List<TransactionEntity>, Failure>> {
        return flow {
            val response = transactionApi.myTransaction()
            if (response.isSuccessful){
                val temp = mutableListOf<TransactionEntity>()
                var user : TransactionUserEntity?
                val body = response.body()!!
                body.forEach { t ->
                    user = TransactionUserEntity(t.user.id, t.user.name, t.user.email)
                    temp.add(TransactionEntity(t.id, t.name, t.price, t.image, user!!))
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

    override suspend fun createTransaction(createTransactionRequest: CreateTransactionRequest): Flow<BaseResult<TransactionEntity, Failure>> {
        return  flow {
            val response = transactionApi.createTransaction(createTransactionRequest)
            if (response.isSuccessful){
                val body = response.body()!!
                val user = TransactionUserEntity(body.user.id, body.user.name, body.user.email)
                val trans = TransactionEntity(body.id, body.name, body.price, body.image, user)
                emit(BaseResult.Success(trans))
            }else{
                val type = object : TypeToken<ApiErrorResponse>(){}.type
                val err : ApiErrorResponse = Gson().fromJson(response.errorBody()!!.charStream(), type)
                val failure = Failure(code = err.code, message = err.message)
                println(failure.code)
                emit(BaseResult.Error(failure))
            }
        }
    }
}