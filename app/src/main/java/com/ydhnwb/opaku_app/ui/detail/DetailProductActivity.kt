package com.ydhnwb.opaku_app.ui.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.load
import com.ydhnwb.opaku_app.R
import com.ydhnwb.opaku_app.data.transaction.dto.CreateTransactionRequest
import com.ydhnwb.opaku_app.databinding.ActivityDetailProductBinding
import com.ydhnwb.opaku_app.domain.product.entity.ProductEntity
import com.ydhnwb.opaku_app.ui.common.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DetailProductActivity : AppCompatActivity() {

    private val viewModel: DetailProductViewModel by viewModels()

    private lateinit var binding: ActivityDetailProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = " "
        fetchProductById()
        observe()
        purchase()
    }

    private fun purchase(){
        binding.content.purchaseButton.setOnClickListener {
            doPurchase()
        }
    }

    private fun doPurchase(){
        val createTransactionRequest = CreateTransactionRequest(productId = getProductId())
        viewModel.purchase(createTransactionRequest)
    }

    private fun observe(){
        observeState()
        observeProduct()
    }

    private fun observeState(){
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleState(it) }
            .launchIn(lifecycleScope)
    }

    private fun observeProduct(){
        viewModel.product.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleProduct(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleProduct(product: ProductEntity?){
        product?.let {
            binding.productImage.load(it.image)
            binding.content.productName.text = it.name
            binding.content.productPrice.text = "Rp.${it.price}"
        }
    }

    private fun handleState(state: DetailProductActivityState){
        when(state){
            is DetailProductActivityState.ShowToast -> showToast(state.message)
            is DetailProductActivityState.IsLoading -> handleLoading(state.isLoading)
            is DetailProductActivityState.Init -> Unit
        }
    }

    private fun handleLoading(isLoading: Boolean){
        binding.content.purchaseButton.isEnabled = !isLoading
    }

    private fun fetchProductById(){
        viewModel.findProductById(getProductId().toString())
    }

    private fun getProductId() = intent.getIntExtra("product_id", -1)
}