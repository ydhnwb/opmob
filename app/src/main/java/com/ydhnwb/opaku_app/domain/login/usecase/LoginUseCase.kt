package com.ydhnwb.opaku_app.domain.login.usecase

import com.ydhnwb.opaku_app.data.login.dto.LoginRequest
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.login.entity.LoginEntity
import com.ydhnwb.opaku_app.domain.login.repo.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun invoke(loginRequest: LoginRequest) : Flow<BaseResult<LoginEntity, Failure>>{
        return loginRepository.login(loginRequest)
    }
}