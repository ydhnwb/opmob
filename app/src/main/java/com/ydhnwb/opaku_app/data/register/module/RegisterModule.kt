package com.ydhnwb.opaku_app.data.register.module

import com.ydhnwb.opaku_app.data.common.module.NetworkModule
import com.ydhnwb.opaku_app.data.register.RegisterRepositoryImpl
import com.ydhnwb.opaku_app.data.register.api.RegisterApi
import com.ydhnwb.opaku_app.domain.register.repo.RegisterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class RegisterModule {

    @Singleton
    @Provides
    fun provideRegisterApi(retrofit: Retrofit) : RegisterApi {
        return retrofit.create(RegisterApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRegisterRepository(registerApi: RegisterApi) : RegisterRepository {
        return RegisterRepositoryImpl(registerApi)
    }


}