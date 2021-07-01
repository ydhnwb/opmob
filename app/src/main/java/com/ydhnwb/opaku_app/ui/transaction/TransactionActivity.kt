package com.ydhnwb.opaku_app.ui.transaction

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ydhnwb.opaku_app.R
import com.ydhnwb.opaku_app.databinding.ActivityTransactionBinding
import com.ydhnwb.opaku_app.domain.transaction.entity.TransactionEntity
import com.ydhnwb.opaku_app.ui.common.extension.gone
import com.ydhnwb.opaku_app.ui.common.extension.showToast
import com.ydhnwb.opaku_app.ui.common.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionBinding
    private val viewModel : TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupRecycler()
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24)
        binding.toolbar.setNavigationOnClickListener { finish() }
        observe()
        fetchTransaction()
    }

    private fun setupRecycler(){
        val a = TransactionAdapter(mutableListOf())
        binding.content.transactionRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@TransactionActivity)
            adapter = a
        }
    }

    private fun fetchTransaction(){
        viewModel.fetchTransaction()
    }

    private fun observe(){
        observeState()
        observeTransaction()
    }

    private fun observeTransaction(){
        viewModel.transactions.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleTransaction(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleTransaction(transactions: List<TransactionEntity>){
        binding.content.transactionRecyclerView.adapter?.let {
            if(it is TransactionAdapter){
                it.update(transactions)
            }
        }
    }

    private fun observeState(){
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleState(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: TransactionActivityState){
        when(state){
            is TransactionActivityState.ShowToast -> showToast(state.message)
            is TransactionActivityState.IsLoading -> handleLoading(state.isLoading)
            is TransactionActivityState.Init -> Unit
        }
    }

    private fun handleLoading(isLoading: Boolean){
        if(isLoading){
            binding.content.loadingProgressBar.visible()
        }else{
            binding.content.loadingProgressBar.gone()
        }
    }
}