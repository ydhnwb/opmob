package com.ydhnwb.opaku_app.domain.register.usecase

import com.ydhnwb.opaku_app.data.register.dto.RegisterRequest
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.register.entity.RegisterEntity
import com.ydhnwb.opaku_app.domain.register.repo.RegisterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val registerRepository: RegisterRepository) {
    suspend fun invoke(registerRequest: RegisterRequest) : Flow<BaseResult<RegisterEntity, Failure>>{
        return registerRepository.register(registerRequest)
    }
}