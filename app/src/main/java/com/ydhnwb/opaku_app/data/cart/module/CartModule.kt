package com.ydhnwb.opaku_app.data.cart.module

import com.ydhnwb.opaku_app.data.cart.CartRepositoryImpl
import com.ydhnwb.opaku_app.data.cart.api.CartApi
import com.ydhnwb.opaku_app.data.common.module.NetworkModule
import com.ydhnwb.opaku_app.domain.cart.repo.CartRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class CartModule {

    @Singleton
    @Provides
    fun provideCartApi(retrofit: Retrofit) : CartApi {
        return retrofit.create(CartApi::class.java)
    }

    @Singleton
    @Provides
    fun provideCartRepository(cartApi: CartApi) : CartRepository {
        return CartRepositoryImpl(cartApi)
    }


}