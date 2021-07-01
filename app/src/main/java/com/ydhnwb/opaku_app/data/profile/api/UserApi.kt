package com.ydhnwb.opaku_app.data.profile.api

import com.ydhnwb.opaku_app.data.profile.dto.UserResponse
import retrofit2.Response
import retrofit2.http.GET

interface UserApi {
    @GET("api/user/profile")
    suspend fun profile() : Response<UserResponse>
}