package com.ydhnwb.opaku_app.domain.transaction.usecase

import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.transaction.entity.TransactionEntity
import com.ydhnwb.opaku_app.domain.transaction.repo.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyTransactionUseCase @Inject constructor(private val transactionRepository: TransactionRepository){
    suspend fun invoke() : Flow<BaseResult<List<TransactionEntity>, Failure>> {
        return transactionRepository.myTransaction()
    }
}