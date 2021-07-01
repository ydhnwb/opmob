package com.ydhnwb.opaku_app.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ydhnwb.opaku_app.domain.common.base.BaseResult
import com.ydhnwb.opaku_app.domain.profile.entity.UserEntity
import com.ydhnwb.opaku_app.domain.profile.usecase.MyProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileFragmentViewModel @Inject constructor(
    private val myProfileUseCase: MyProfileUseCase
) : ViewModel() {
    private val mState= MutableStateFlow<ProfileFragmentState>(ProfileFragmentState.Init)
    val state : StateFlow<ProfileFragmentState> get() = mState

    private val mUser = MutableStateFlow<UserEntity?>(null)
    val user : StateFlow<UserEntity?> get() = mUser

    private fun setLoading(){
        mState.value = ProfileFragmentState.IsLoading(true)
    }

    private fun hideLoading(){
        mState.value = ProfileFragmentState.IsLoading(false)
    }

    private fun showToast(msg: String){
        mState.value = ProfileFragmentState.ShowToast(msg)
    }

    fun fetchProfile(){
        viewModelScope.launch {
            myProfileUseCase.invoke()
                .onStart {
                    setLoading()
                }
                .catch { e ->
                    hideLoading()
                    showToast(e.message.toString())
                }
                .collect { res ->
                    hideLoading()
                    when(res){
                        is BaseResult.Success -> {
                            mUser.value = res.data
                        }
                        is BaseResult.Error -> {
                            if (res.err.code != 400 && res.err.code != 401){
                                showToast(res.err.message)
                            }
                        }
                    }
                }
        }
    }

}

sealed class ProfileFragmentState {
    object Init : ProfileFragmentState()
    data class IsLoading(val isLoading: Boolean) : ProfileFragmentState()
    data class ShowToast(val message : String) : ProfileFragmentState()
}