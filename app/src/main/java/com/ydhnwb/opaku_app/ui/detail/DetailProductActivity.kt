package com.ydhnwb.opaku_app.ui.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.firebase.analytics.FirebaseAnalytics
import com.ydhnwb.opaku_app.R
import com.ydhnwb.opaku_app.data.transaction.dto.CreateTransactionRequest
import com.ydhnwb.opaku_app.databinding.ActivityDetailProductBinding
import com.ydhnwb.opaku_app.domain.common.base.Failure
import com.ydhnwb.opaku_app.domain.product.entity.ProductEntity
import com.ydhnwb.opaku_app.domain.transaction.entity.TransactionEntity
import com.ydhnwb.opaku_app.infra.DateUtil
import com.ydhnwb.opaku_app.infra.SharedPrefs
import com.ydhnwb.opaku_app.ui.common.extension.gone
import com.ydhnwb.opaku_app.ui.common.extension.showToast
import com.ydhnwb.opaku_app.ui.common.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class DetailProductActivity : AppCompatActivity() {

    private val viewModel: DetailProductViewModel by viewModels()

    private lateinit var binding: ActivityDetailProductBinding

    private lateinit var timer: Timer
    private lateinit var timerTask: TimerTask
    private var duration = 0.0


    @Inject
    lateinit var sharedPref: SharedPrefs

    @Inject
    lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = " "
        fetchProductById()
        observe()
        purchase()
        setupTimer()
        setupTimerTask()
    }

    private fun setupTimer(){
        timer = Timer()
    }

    private fun setProbablyLikedProductLog(){
        val user = sharedPref.getUserData()
        if(user != null){
            val second = duration.roundToInt() % 86400 % 3600 % 60
            if(second in 31..59){
                val bundle = Bundle()
                bundle.putInt("user_id", user.id)
                bundle.putString("user_name", user.name)
                bundle.putString("user_email", user.email)
                bundle.putInt("looking_product_in_seconds", second)
                bundle.putInt("product_id", getProductId())
                analytics.logEvent("probably_liked_product", bundle)
            }
        }
    }

    private fun setupTimerTask(){
        timerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    duration++
                }
            }
        }
        timer.scheduleAtFixedRate(timerTask, 0, 1000)
    }

    private fun setPurchaseSuccessLog(t: TransactionEntity){
        val bundle = Bundle()
        bundle.putInt("product_id", t.id)
        bundle.putString("product_name", t.name)
        bundle.putInt("product_price", t.price)
        bundle.putInt("user_id", t.user.id)
        bundle.putString("user_name", t.user.name)
        bundle.putString("user_email", t.user.email)
        bundle.putString("timestamp", DateUtil.getCurrentTimeStamp())
        analytics.logEvent("purchase_success", bundle)
    }

    private fun setTapPurchaseLog(){
        val p = viewModel.product.value
        p?.let { productEntity ->
            val bundle = Bundle()
            bundle.putInt("product_id", productEntity.id)
            bundle.putString("product_name", productEntity.name)
            bundle.putInt("product_price", productEntity.price)

            val user = sharedPref.getUserData()
            if(user != null){
                bundle.putInt("user_id", user.id)
                bundle.putString("user_name", user.name)
                bundle.putString("user_email", user.email)
            }
            bundle.putString("timestamp", DateUtil.getCurrentTimeStamp())
            analytics.logEvent("purchase_click", bundle)
        }
    }

    private fun purchase(){
        if(sharedPref.getToken().isNotEmpty()){
            binding.content.purchaseButton.visible()
        }else{
            binding.content.purchaseButton.gone()
        }
        binding.content.purchaseButton.setOnClickListener {
            doPurchase()
        }
    }

    private fun doPurchase(){
        setTapPurchaseLog()
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

    private fun setPurchaseError(e: Failure, t: CreateTransactionRequest) {
        val bundle = Bundle()
        bundle.putString("timestamp", DateUtil.getCurrentTimeStamp())
        bundle.putString("error_message", e.message)
        bundle.putInt("error_code", e.code)
        bundle.putInt("product_id", t.productId)
        analytics.logEvent("purchase_error", bundle)
    }

    private fun handleState(state: DetailProductActivityState){
        when(state){
            is DetailProductActivityState.PurchaseError -> setPurchaseError(state.e, state.transactionRequest)
            is DetailProductActivityState.PurchaseSuccess -> setPurchaseSuccessLog(state.transactionEntity)
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

    override fun onDestroy() {
        super.onDestroy()
        setProbablyLikedProductLog()

    }
}