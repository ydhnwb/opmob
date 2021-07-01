package com.ydhnwb.opaku_app.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.ydhnwb.opaku_app.R
import com.ydhnwb.opaku_app.data.register.dto.RegisterRequest
import com.ydhnwb.opaku_app.databinding.ActivityRegisterBinding
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.register.entity.RegisterEntity
import com.ydhnwb.opaku_app.infra.SharedPrefs
import com.ydhnwb.opaku_app.ui.common.extension.isEmail
import com.ydhnwb.opaku_app.ui.common.extension.showGenericAlertDialog
import com.ydhnwb.opaku_app.ui.common.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private val viewModel: RegisterActivityViewModel by viewModels()

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        back()
        register()
        observe()
    }

    private fun register(){
        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if(validate(name, email, password)){
                viewModel.register(RegisterRequest(name, email, password))
            }
        }
    }

    private fun validate(name: String, email: String, password: String) :  Boolean{
        resetAllInputError()

        if(name.isEmpty()){
            setNameError(getString(R.string.error_name_not_valid))
            return false
        }

        if(!email.isEmail()){
            setEmailError(getString(R.string.error_email_not_valid))
            return false
        }

        if(password.length < 8){
            setPasswordError(getString(R.string.error_password_not_valid))
            return false
        }

        return true
    }

    private fun resetAllInputError(){
        setNameError(null)
        setEmailError(null)
        setPasswordError(null)
    }

    private fun setNameError(e: String?){
        binding.nameInput.error = e
    }

    private fun setEmailError(e: String?){
        binding.emailInput.error = e
    }

    private fun setPasswordError(e: String?){
        binding.passwordInput.error = e
    }

    private fun back(){
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun handleStateChange(state: RegisterActivityState){
        when(state){
            is RegisterActivityState.ShowToast -> showToast(state.message)
            is RegisterActivityState.IsLoading -> handleLoading(state.isLoading)
            is RegisterActivityState.SuccessRegister -> handleSuccessRegister(state.registerEntity)
            is RegisterActivityState.ErrorRegister -> handleErrorRegister(state.e)
            is RegisterActivityState.Init -> Unit
        }
    }

    private fun observe(){
        viewModel.mState
            .flowWithLifecycle(lifecycle,  Lifecycle.State.STARTED)
            .onEach { state -> handleStateChange(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleSuccessRegister(registerEntity: RegisterEntity){
        sharedPrefs.saveToken(registerEntity.token)
        setResult(RESULT_OK)
        finish()
    }

    private fun handleErrorRegister(e: Failure){
        showGenericAlertDialog("${e.message} [${e.code}]")
    }

    private fun handleLoading(isLoading: Boolean){
        binding.registerButton.isEnabled = !isLoading
        binding.backButton.isEnabled = !isLoading
        binding.loadingProgressBar.isIndeterminate = isLoading
        if(!isLoading){
            binding.loadingProgressBar.progress = 0
        }
    }


}