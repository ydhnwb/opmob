package com.ydhnwb.opaku_app.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ydhnwb.opaku_app.data.transaction.dto.CreateTransactionRequest
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.product.entity.ProductEntity
import com.ydhnwb.opaku_app.domain.product.usecase.FindProductByIdUseCase
import com.ydhnwb.opaku_app.domain.transaction.usecase.CreateTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailProductViewModel @Inject constructor(
    private val getDetailProductUseCase: FindProductByIdUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase
) : ViewModel(){
    private val mProduct = MutableStateFlow<ProductEntity?>(null)
    val product: StateFlow<ProductEntity?> get() = mProduct

    private val mState = MutableStateFlow<DetailProductActivityState>(DetailProductActivityState.Init)
    val state : StateFlow<DetailProductActivityState> get() = mState

    private fun setLoading(){
        mState.value = DetailProductActivityState.IsLoading(true)
    }

    private fun hideLoading(){
        mState.value = DetailProductActivityState.IsLoading(false)
    }

    private fun showToast(message: String){
        mState.value = DetailProductActivityState.ShowToast(message)
    }

    fun purchase(createTransactionRequest: CreateTransactionRequest) {
        viewModelScope.launch {
            createTransactionUseCase.invoke(createTransactionRequest)
                .onStart { setLoading() }
                .catch { e ->
                    hideLoading()
                    showToast(e.message.toString()) }
                .collect { res ->
                    hideLoading()
                    when(res){
                        is BaseResult.Success -> {
                            showToast("Purchase success")
                        }
                        is BaseResult.Error -> {
                            showToast(res.err.message)
                        }
                    }
                }
        }
    }

    fun findProductById(id: String){
        viewModelScope.launch {
            getDetailProductUseCase.invoke(id)
                .onStart { setLoading() }
                .catch { e ->
                    hideLoading()
                    showToast(e.message.toString())
                }
                .collect { res ->
                    hideLoading()
                    when(res){
                        is BaseResult.Success -> {
                            mProduct.value = res.data
                        }
                        is BaseResult.Error -> {
                            showToast(res.err.message)
                        }
                    }
                }
        }
    }
}

sealed class DetailProductActivityState {
    object Init: DetailProductActivityState()
    data class IsLoading(val isLoading: Boolean) : DetailProductActivityState()
    data class ShowToast(val message: String) : DetailProductActivityState()
}