package com.ydhnwb.opaku_app.data.transaction.module

import com.ydhnwb.opaku_app.data.common.module.NetworkModule
import com.ydhnwb.opaku_app.data.transaction.TransactionRepositoryImpl
import com.ydhnwb.opaku_app.data.transaction.api.TransactionApi
import com.ydhnwb.opaku_app.domain.transaction.repo.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class TransactionModule {

    @Singleton
    @Provides
    fun provideTransactionApi(retrofit: Retrofit) : TransactionApi {
        return retrofit.create(TransactionApi::class.java)
    }

    @Singleton
    @Provides
    fun provideTransactionRepository(transactionApi: TransactionApi) : TransactionRepository {
        return TransactionRepositoryImpl(transactionApi)
    }


}