package com.ydhnwb.opaku_app.data.login.api

import com.ydhnwb.opaku_app.data.login.dto.LoginRequest
import com.ydhnwb.opaku_app.data.login.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<LoginResponse>
}