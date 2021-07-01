package com.ydhnwb.opaku_app.ui.main.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.ydhnwb.opaku_app.R
import com.ydhnwb.opaku_app.databinding.FragmentSearchBinding
import com.ydhnwb.opaku_app.domain.product.entity.ProductEntity
import com.ydhnwb.opaku_app.infra.DateUtil
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
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel : SearchFragmentViewModel by viewModels()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPref: SharedPrefs

    @Inject
    lateinit var analytics: FirebaseAnalytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)
        setupRecyclerView()
        setupSearchInput()
        observe()
    }

    private fun setSearchLog(q: String){
        val bundle = Bundle()
        bundle.putString("search_query", q)
        bundle.putString("timestamp", DateUtil.getCurrentTimeStamp())
        bundle.putBoolean("is_logged_in", sharedPref.getToken().isNotEmpty())
        if(sharedPref.getToken().isNotEmpty()){
            val user = sharedPref.getUserData()
            user?.let {
                bundle.putInt("user_id", it.id)
                bundle.putString("user_name", it.name)
                bundle.putString("user_email", it.email)
            }
        }
        analytics.logEvent("search_product", bundle)
    }

    private fun setupSearchInput(){
        binding.searchEditText.setOnEditorActionListener { _, action, _ ->
            if (action == EditorInfo.IME_ACTION_SEARCH){
                val q = binding.searchEditText.text.toString().trim()
                if(q.isNotEmpty()){
                    searchProduct(q)
                    setSearchLog(q)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            return@setOnEditorActionListener false
        }
    }
    
    private fun searchProduct(q : String){
        viewModel.searchProduct(q)
    }
    
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


    private fun handleState(state : SearchFragmentState){
        when(state){
            is SearchFragmentState.ShowToast -> requireActivity().showToast(state.message)
            is SearchFragmentState.IsLoading -> handleLoading(state.isLoading)
            is SearchFragmentState.Init -> Unit
        }
    }

    private fun setupRecyclerView(){
        val anAdapter = SearchProductAdapter(mutableListOf())
        anAdapter.setItemTapListener(object: SearchProductAdapter.SearchAdapterListener{
            override fun onTap(p: ProductEntity) {
                startActivity(Intent(requireActivity(), DetailProductActivity::class.java).apply {
                    putExtra("product_id", p.id)
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
            if(a is SearchProductAdapter){
                a.update(products)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}