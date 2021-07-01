package com.ydhnwb.opaku_app.data.login.module

import com.ydhnwb.opaku_app.data.common.module.NetworkModule
import com.ydhnwb.opaku_app.data.login.LoginRepositoryImpl
import com.ydhnwb.opaku_app.data.login.api.LoginApi
import com.ydhnwb.opaku_app.domain.login.repo.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class LoginModule {

    @Singleton
    @Provides
    fun provideLoginApi(retrofit: Retrofit) : LoginApi {
        return retrofit.create(LoginApi::class.java)
    }

    @Singleton
    @Provides
    fun provideLoginRepository(loginApi: LoginApi) : LoginRepository {
        return LoginRepositoryImpl(loginApi)
    }


}