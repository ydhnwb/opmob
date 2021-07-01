package com.ydhnwb.opaku_app.data.login

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ydhnwb.opaku_app.data.common.util.ApiErrorResponse
import com.ydhnwb.opaku_app.data.login.api.LoginApi
import com.ydhnwb.opaku_app.data.login.dto.LoginRequest
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.login.entity.LoginEntity
import com.ydhnwb.opaku_app.domain.login.repo.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val loginApi: LoginApi) : LoginRepository{
    override suspend fun login(loginRequest: LoginRequest): Flow<BaseResult<LoginEntity, Failure>> {
        return flow {
            val response = loginApi.login(loginRequest)
            if(response.isSuccessful){
                val body = response.body()!!
                val loginEntity = LoginEntity(body.id!!, body.name!!, body.email!!, body.token!!)
                emit(BaseResult.Success(loginEntity))
            }else{
                val type = object : TypeToken<ApiErrorResponse>(){}.type
                val err : ApiErrorResponse = Gson().fromJson(response.errorBody()!!.charStream(), type)
                val failure = Failure(code = err.code, message = err.message)
                emit(BaseResult.Error(failure))
            }
        }
    }
}