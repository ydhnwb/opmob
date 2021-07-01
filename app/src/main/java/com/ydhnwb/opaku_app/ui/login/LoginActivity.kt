package com.ydhnwb.opaku_app.ui.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.ydhnwb.opaku_app.R
import com.ydhnwb.opaku_app.data.login.dto.LoginRequest
import com.ydhnwb.opaku_app.databinding.ActivityLoginBinding
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.login.entity.LoginEntity
import com.ydhnwb.opaku_app.infra.SharedPrefs
import com.ydhnwb.opaku_app.ui.common.extension.isEmail
import com.ydhnwb.opaku_app.ui.common.extension.showGenericAlertDialog
import com.ydhnwb.opaku_app.ui.common.extension.showToast
import com.ydhnwb.opaku_app.ui.main.MainActivity
import com.ydhnwb.opaku_app.ui.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val viewModel: LoginActivityViewModel by viewModels()

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    private val openRegisterActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            goToMainActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        login()
        goToRegisterActivity()
        observe()
    }


    private fun login(){
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if(validate(email, password)){
                val loginRequest = LoginRequest(email, password)
                viewModel.login(loginRequest)
            }
        }
    }

    private fun validate(email: String, password: String) : Boolean{
        resetAllInputError()
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
        setEmailError(null)
        setPasswordError(null)
    }

    private fun setEmailError(e : String?){
        binding.emailInput.error = e
    }

    private fun setPasswordError(e: String?){
        binding.passwordInput.error = e
    }

    private fun observe(){
        viewModel.mState
            .flowWithLifecycle(lifecycle,  Lifecycle.State.STARTED)
            .onEach { state -> handleStateChange(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleStateChange(state: LoginActivityState){
        when(state){
            is LoginActivityState.Init -> Unit
            is LoginActivityState.ErrorLogin -> handleErrorLogin(state.err)
            is LoginActivityState.SuccessLogin -> handleSuccessLogin(state.loginEntity)
            is LoginActivityState.ShowToast -> showToast(state.message)
            is LoginActivityState.IsLoading -> handleLoading(state.isLoading)
        }
    }

    private fun handleErrorLogin(e: Failure){
        showGenericAlertDialog("${e.message} [${e.code}]")
    }

    private fun handleLoading(isLoading: Boolean){
        binding.loginButton.isEnabled = !isLoading
        binding.registerButton.isEnabled = !isLoading
        binding.loadingProgressBar.isIndeterminate = isLoading
        if(!isLoading){
            binding.loadingProgressBar.progress = 0
        }
    }

    private fun handleSuccessLogin(loginEntity: LoginEntity){
        sharedPrefs.saveToken(loginEntity.token)
        showToast("Welcome ${loginEntity.name}")
        goToMainActivity()
    }

    private fun goToRegisterActivity(){
        binding.registerButton.setOnClickListener {
            openRegisterActivity.launch(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun goToMainActivity(){
        setResult(Activity.RESULT_OK)
        finish()
    }
}