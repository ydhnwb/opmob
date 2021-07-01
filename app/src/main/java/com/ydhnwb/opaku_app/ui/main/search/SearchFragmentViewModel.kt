package com.ydhnwb.opaku_app.ui.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.product.entity.ProductEntity
import com.ydhnwb.opaku_app.domain.product.usecase.SearchProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFragmentViewModel @Inject constructor(
    private val searchProductUseCase: SearchProductUseCase
) : ViewModel() {

    private val mState = MutableStateFlow<SearchFragmentState>(SearchFragmentState.Init)
    val state: StateFlow<SearchFragmentState> get() = mState

    private val mProducts = MutableStateFlow<List<ProductEntity>>(mutableListOf())
    val products : StateFlow<List<ProductEntity>> get() = mProducts

    private fun setLoading(){
        mState.value = SearchFragmentState.IsLoading(true)
    }

    private fun hideLoading(){
        mState.value = SearchFragmentState.IsLoading(false)
    }

    private fun showToast(msg : String){
        mState.value = SearchFragmentState.ShowToast(msg)
    }

    fun searchProduct(q: String){
        viewModelScope.launch {
            searchProductUseCase.invoke(q)
                .onStart { setLoading() }
                .catch { e ->
                    hideLoading()
                    showToast(e.message.toString())
                }
                .collect { res ->
                    hideLoading()
                    when(res){
                        is BaseResult.Success -> {
                            mProducts.value = res.data
                        }
                        is BaseResult.Error -> {
                            showToast(res.err.message)
                        }
                    }
                }
        }
    }

}

sealed class SearchFragmentState {
    object Init : SearchFragmentState()
    data class IsLoading(val isLoading : Boolean) : SearchFragmentState()
    data class ShowToast(val message: String) : SearchFragmentState()
}