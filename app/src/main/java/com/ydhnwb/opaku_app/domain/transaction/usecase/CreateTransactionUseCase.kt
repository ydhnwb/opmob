package com.ydhnwb.opaku_app.domain.transaction.usecase

import com.ydhnwb.opaku_app.data.transaction.dto.CreateTransactionRequest
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.transaction.entity.TransactionEntity
import com.ydhnwb.opaku_app.domain.transaction.repo.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateTransactionUseCase @Inject constructor(private val transactionRepository: TransactionRepository) {
    suspend fun invoke(createTransactionRequest: CreateTransactionRequest) : Flow<BaseResult<TransactionEntity, Failure>> {
        return transactionRepository.createTransaction(createTransactionRequest)
    }
}