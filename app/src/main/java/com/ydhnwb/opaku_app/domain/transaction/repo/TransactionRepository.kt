package com.ydhnwb.opaku_app.domain.transaction.repo

import com.ydhnwb.opaku_app.data.transaction.dto.CreateTransactionRequest
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.transaction.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun myTransaction() : Flow<BaseResult<List<TransactionEntity>, Failure>>
    suspend fun createTransaction(createTransactionRequest: CreateTransactionRequest) : Flow<BaseResult<TransactionEntity, Failure>>
}