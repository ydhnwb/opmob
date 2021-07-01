package com.ydhnwb.opaku_app.domain.profile.usecase

import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.profile.entity.UserEntity
import com.ydhnwb.opaku_app.domain.profile.repo.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MyProfileUseCase @Inject constructor(private val userRepository: UserRepository){
    suspend fun invoke() : Flow<BaseResult<UserEntity, Failure>> {
        return userRepository.myProfile()
    }
}