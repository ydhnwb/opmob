package com.ydhnwb.opaku_app.domain.login.repo

import com.ydhnwb.opaku_app.data.login.dto.LoginRequest
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.login.entity.LoginEntity
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun login(loginRequest: LoginRequest) : Flow<BaseResult<LoginEntity, Failure>>
}