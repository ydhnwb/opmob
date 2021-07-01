package com.ydhnwb.opaku_app.data.profile.module

import com.ydhnwb.opaku_app.data.common.module.NetworkModule
import com.ydhnwb.opaku_app.data.login.LoginRepositoryImpl
import com.ydhnwb.opaku_app.data.login.api.LoginApi
import com.ydhnwb.opaku_app.data.profile.UserRepositoryImpl
import com.ydhnwb.opaku_app.data.profile.api.UserApi
import com.ydhnwb.opaku_app.domain.login.repo.LoginRepository
import com.ydhnwb.opaku_app.domain.profile.repo.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class UserModule {

    @Singleton
    @Provides
    fun provideUserApi(retrofit: Retrofit) : UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserRepository(userApi: UserApi) : UserRepository {
        return UserRepositoryImpl(userApi)
    }


}