package com.ydhnwb.opaku_app.data.profile

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ydhnwb.opaku_app.data.common.util.ApiErrorResponse
import com.ydhnwb.opaku_app.data.profile.api.UserApi
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.profile.entity.UserEntity
import com.ydhnwb.opaku_app.domain.profile.repo.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userApi: UserApi) : UserRepository{
    override suspend fun myProfile(): Flow<BaseResult<UserEntity, Failure>> {
        return flow {
            val response = userApi.profile()
            if(response.isSuccessful){
                val body = response.body()!!
                val user = UserEntity(body.id, body.name, body.email)
                emit(BaseResult.Success(user))
            }else {
                val type = object : TypeToken<ApiErrorResponse>(){}.type
                val err : ApiErrorResponse = Gson().fromJson(response.errorBody()!!.charStream(), type)
                val failure = Failure(code = err.code, message = err.message)
                emit(BaseResult.Error(failure))
            }
        }
    }

}