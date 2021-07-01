package com.ydhnwb.opaku_app.data.register.api

import com.ydhnwb.opaku_app.data.register.dto.RegisterRequest
import com.ydhnwb.opaku_app.data.register.dto.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi {
    @POST("api/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest) : Response<RegisterResponse>
}