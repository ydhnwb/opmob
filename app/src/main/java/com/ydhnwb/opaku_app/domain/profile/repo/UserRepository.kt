package com.ydhnwb.opaku_app.domain.profile.repo

import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.profile.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun myProfile() : Flow<BaseResult<UserEntity, Failure>>
}