package com.ydhnwb.opaku_app.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ydhnwb.opaku_app.data.login.dto.LoginRequest
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.login.entity.LoginEntity
import com.ydhnwb.opaku_app.domain.login.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginActivityViewModel @Inject constructor(private val loginUseCase: LoginUseCase): ViewModel() {
    private val state = MutableStateFlow<LoginActivityState>(LoginActivityState.Init)
    val mState: StateFlow<LoginActivityState> get() = state


    private fun setLoading(){
        state.value = LoginActivityState.IsLoading(true)
    }

    private fun hideLoading(){
        state.value = LoginActivityState.IsLoading(false)
    }

    private fun showToast(message: String){
        state.value = LoginActivityState.ShowToast(message)
    }


    fun login(loginRequest: LoginRequest){
        viewModelScope.launch {
            loginUseCase.invoke(loginRequest)
                .onStart {
                    setLoading()
                }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.message.toString())
                }
                .collect { res ->
                    hideLoading()
                    when(res){
                        is BaseResult.Success -> {
                            state.value = LoginActivityState.SuccessLogin(res.data)
                        }
                        is BaseResult.Error -> {
                            state.value = LoginActivityState.ErrorLogin(res.err)
                        }
                    }
                }
        }
    }



}

sealed class LoginActivityState  {
    object Init : LoginActivityState()
    data class IsLoading(val isLoading: Boolean) : LoginActivityState()
    data class ShowToast(val message: String) : LoginActivityState()
    data class SuccessLogin(val loginEntity: LoginEntity) : LoginActivityState()
    data class ErrorLogin(val err: Failure) : LoginActivityState()
}