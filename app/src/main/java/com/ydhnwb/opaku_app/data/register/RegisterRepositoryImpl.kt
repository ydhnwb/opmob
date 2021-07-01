package com.ydhnwb.opaku_app.data.register

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ydhnwb.opaku_app.data.common.util.ApiErrorResponse
import com.ydhnwb.opaku_app.data.register.api.RegisterApi
import com.ydhnwb.opaku_app.data.register.dto.RegisterRequest
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.register.entity.RegisterEntity
import com.ydhnwb.opaku_app.domain.register.repo.RegisterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RegisterRepositoryImpl constructor(private val registerApi: RegisterApi) : RegisterRepository {
    override suspend fun register(registerRequest: RegisterRequest): Flow<BaseResult<RegisterEntity, Failure>> {
        return flow {
            val response = registerApi.register(registerRequest)
            if(response.isSuccessful){
                val body = response.body()!!
                val registerEntity = RegisterEntity(body.id!!, body.name!!, body.email!!, body.token!!)
                emit(BaseResult.Success(registerEntity))
            }else{
                val type = object : TypeToken<ApiErrorResponse>(){}.type
                val err : ApiErrorResponse = Gson().fromJson(response.errorBody()!!.charStream(), type)
                val failure = Failure(code = err.code, message = err.message)
                emit(BaseResult.Error(failure))
            }
        }
    }
}