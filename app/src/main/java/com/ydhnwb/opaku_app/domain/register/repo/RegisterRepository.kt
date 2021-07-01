package com.ydhnwb.opaku_app.domain.register.repo

import com.ydhnwb.opaku_app.data.register.dto.RegisterRequest
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.register.entity.RegisterEntity
import kotlinx.coroutines.flow.Flow

interface RegisterRepository {
    suspend fun register(registerRequest: RegisterRequest) : Flow<BaseResult<RegisterEntity, Failure>>
}
