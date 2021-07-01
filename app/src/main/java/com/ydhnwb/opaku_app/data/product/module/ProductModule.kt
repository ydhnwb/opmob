package com.ydhnwb.opaku_app.data.product.module

import com.ydhnwb.opaku_app.data.common.module.NetworkModule
import com.ydhnwb.opaku_app.data.product.ProductRepositoryImpl
import com.ydhnwb.opaku_app.data.product.api.ProductApi
import com.ydhnwb.opaku_app.domain.product.repo.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class ProductModule {

    @Singleton
    @Provides
    fun provideProductApi(retrofit: Retrofit) : ProductApi {
        return retrofit.create(ProductApi::class.java)
    }

    @Singleton
    @Provides
    fun provideProductRepository(productApi: ProductApi) : ProductRepository {
        return ProductRepositoryImpl(productApi)
    }


}