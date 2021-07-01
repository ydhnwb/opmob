package com.ydhnwb.opaku_app.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.product.entity.ProductEntity
import com.ydhnwb.opaku_app.domain.product.usecase.GetAllProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllProductUseCase: GetAllProductUseCase
) : ViewModel() {
    private val uiState = MutableStateFlow<HomeFragmentState>(HomeFragmentState.Init)
    val state : StateFlow<HomeFragmentState> get() = uiState

    private val mProducts = MutableStateFlow<List<ProductEntity>>(mutableListOf())
    val products : StateFlow<List<ProductEntity>> get() = mProducts

    private fun showToast(message: String){
        uiState.value = HomeFragmentState.ShowToast(message)
    }

    private fun setLoading(){
        uiState.value = HomeFragmentState.IsLoading(true)
    }

    private fun hideLoading(){
        uiState.value = HomeFragmentState.IsLoading(false)
    }

    fun fetchAllProducts(){
        viewModelScope.launch {
            getAllProductUseCase.all()
                .onStart {
                    setLoading()
                }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.message.toString())
                }
                .collect { result ->
                    hideLoading()
                    when(result){
                        is BaseResult.Success -> {
                            mProducts.value = result.data
                        }
                        is BaseResult.Error -> {
                            showToast(result.err.message)
                        }
                    }
                }
        }
    }

}

sealed class HomeFragmentState {
    object Init : HomeFragmentState()
    data class IsLoading(val isLoading : Boolean) : HomeFragmentState()
    data class ShowToast(val message : String) : HomeFragmentState()
}