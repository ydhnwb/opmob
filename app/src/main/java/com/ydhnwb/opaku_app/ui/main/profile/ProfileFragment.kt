package com.ydhnwb.opaku_app.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.load
import com.ydhnwb.opaku_app.R
import com.ydhnwb.opaku_app.databinding.FragmentProfileBinding
import com.ydhnwb.opaku_app.domain.profile.entity.UserEntity
import com.ydhnwb.opaku_app.infra.SharedPrefs
import com.ydhnwb.opaku_app.ui.common.extension.gone
import com.ydhnwb.opaku_app.ui.common.extension.showToast
import com.ydhnwb.opaku_app.ui.common.extension.visible
import com.ydhnwb.opaku_app.ui.login.LoginActivity
import com.ydhnwb.opaku_app.ui.transaction.TransactionActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileFragmentViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    private val openLoginActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            refresh()
        }
    }

    private fun doSignOut(){
        binding.signoutButton.setOnClickListener {
            signOut()
        }
    }

    private fun signOut(){
        sharedPrefs.clear()
        checkIsLoggedIn()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)
        goToTransactionActivity()
        goToLogin()
        observe()
        doSignOut()
    }

    private fun goToLogin(){
        binding.altContent.altLoginButton.setOnClickListener {
            openLoginActivity.launch(Intent(requireActivity(), LoginActivity::class.java))
        }
    }

    private fun observe(){
        observeState()
        observeUser()
    }

    private fun observeState(){
        viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeUser(){
        viewModel.user.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { user -> handleUser(user) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: ProfileFragmentState){
        when(state){
            is ProfileFragmentState.ShowToast -> requireActivity().showToast(state.message)
            is ProfileFragmentState.IsLoading -> {}
            is ProfileFragmentState.Init -> Unit
        }
    }

    private fun handleUser(user: UserEntity?){
        user?.let {
            binding.profilePicture.load("https://img.pngio.com/user-logos-user-logo-png-1920_1280.png")
            binding.profileNameTextView.text = it.name
            binding.profileEmailTextView.text = it.email
        }
    }

    private fun goToTransactionActivity(){
        binding.myTransactionButton.setOnClickListener {
            startActivity(Intent(requireActivity(), TransactionActivity::class.java))
        }
    }


    private fun fetchProfile(){
        if(sharedPrefs.getToken().isNotEmpty()){
            viewModel.fetchProfile()
        }
    }

    private fun checkIsLoggedIn(){
        if (sharedPrefs.getToken().isEmpty()){
            binding.primaryContent.gone()
            binding.altContent.root.visible()
        }else{
            binding.primaryContent.visible()
            binding.altContent.root.gone()
        }
    }

    override fun onStart() {
        super.onStart()
        checkIsLoggedIn()
        fetchProfile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun refresh(){
//        requireActivity().showToast("OK LOGIN")
    }
}