package com.ydhnwb.opaku_app.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.transaction.entity.TransactionEntity
import com.ydhnwb.opaku_app.domain.transaction.usecase.GetMyTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val getMyTransactionUseCase: GetMyTransactionUseCase
) : ViewModel(){
    private val mState = MutableStateFlow<TransactionActivityState>(TransactionActivityState.Init)
    val state : StateFlow<TransactionActivityState> get() = mState

    private val mTransactions = MutableStateFlow<List<TransactionEntity>>(mutableListOf())
    val transactions: StateFlow<List<TransactionEntity>> get() = mTransactions

    private fun setLoading(){
        mState.value = TransactionActivityState.IsLoading(true)
    }

    private fun hideLoading(){
        mState.value = TransactionActivityState.IsLoading(false)
    }

    private fun showToast(message: String){
        mState.value = TransactionActivityState.ShowToast(message)
    }

    fun fetchTransaction(){
        viewModelScope.launch {
            getMyTransactionUseCase.invoke()
                .onStart { setLoading() }
                .catch { e ->
                    hideLoading()
                    showToast(e.message.toString())
                }
                .collect { res ->
                    hideLoading()
                    when(res){
                        is BaseResult.Success -> {
                            mTransactions.value = res.data
                        }
                        is BaseResult.Error -> {
                            showToast(res.err.message)
                        }
                    }
                }
        }
    }
}



sealed class TransactionActivityState {
    object Init : TransactionActivityState()
    data class IsLoading(val isLoading: Boolean) : TransactionActivityState()
    data class ShowToast(val message: String) : TransactionActivityState()
}