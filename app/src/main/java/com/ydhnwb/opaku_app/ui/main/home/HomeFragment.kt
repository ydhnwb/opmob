package com.ydhnwb.opaku_app.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.ydhnwb.opaku_app.R
import com.ydhnwb.opaku_app.databinding.FragmentHomeBinding
import com.ydhnwb.opaku_app.domain.product.entity.ProductEntity
import com.ydhnwb.opaku_app.infra.SharedPrefs
import com.ydhnwb.opaku_app.ui.common.extension.gone
import com.ydhnwb.opaku_app.ui.common.extension.showToast
import com.ydhnwb.opaku_app.ui.common.extension.visible
import com.ydhnwb.opaku_app.ui.detail.DetailProductActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel : HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var analytic : FirebaseAnalytics

    @Inject
    lateinit var sharedPref: SharedPrefs


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        setupRecyclerView()
        observe()
        fetchProducts()
    }

    private fun setProductLog(product: ProductEntity){
        sharedPref.getUserData()?.let { user ->
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.METHOD, "product_adapter_on_tap")
            bundle.putString("product_name", product.name)
            bundle.putString("product_id", product.id.toString())
            bundle.putString("product_price", product.price.toString())
            bundle.putString("user_id", user.id.toString())
            bundle.putString("user_name", user.name)
            bundle.putString("user_email", user.email)
            analytic.logEvent("item_tapped", bundle)
        }
    }

    private fun fetchProducts() = viewModel.fetchAllProducts()

    private fun observe(){
        observeState()
        observeProduct()
    }

    private fun observeProduct(){
        viewModel.products.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { e -> handleProducts(e) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeState(){
        viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun handleState(state : HomeFragmentState){
        when(state){
            is HomeFragmentState.ShowToast -> requireActivity().showToast(state.message)
            is HomeFragmentState.IsLoading -> handleLoading(state.isLoading)
            is HomeFragmentState.Init -> Unit
        }
    }

    private fun setupRecyclerView(){
        val anAdapter = HomeProductAdapter(mutableListOf())
        anAdapter.setItemTapListener(object: HomeProductAdapter.HomeProductAdapterListener{
            override fun onTap(p: ProductEntity) {
                startActivity(Intent(requireActivity(), DetailProductActivity::class.java).apply {
                    putExtra("product_id", p.id)
                    setProductLog(p)
                })
            }
        })

        binding.productRecyclerView.apply {
            adapter = anAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun handleLoading(isShow: Boolean){
        if(isShow){
            binding.loadingProgressBar.visible()
        }else{
            binding.loadingProgressBar.gone()
        }
    }

    private fun handleProducts(products: List<ProductEntity>){
        binding.productRecyclerView.adapter?.let { a ->
            if(a is HomeProductAdapter){
                a.update(products)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}