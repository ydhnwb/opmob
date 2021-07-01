package com.ydhnwb.opaku_app.ui.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ydhnwb.opaku_app.databinding.ItemProductBinding
import com.ydhnwb.opaku_app.domain.product.entity.ProductEntity

class SearchProductAdapter(private val products: MutableList<ProductEntity>) : RecyclerView.Adapter<SearchProductAdapter.ViewHolder>() {
    private var onTapListener: SearchAdapterListener? = null

    interface SearchAdapterListener {
        fun onTap(p: ProductEntity)
    }

    fun setItemTapListener(l: SearchAdapterListener){
        onTapListener = l
    }

    fun update(prods: List<ProductEntity>){
        products.clear()
        products.addAll(prods)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val itemBinding: ItemProductBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(p: ProductEntity){
            itemBinding.productImage.load(p.image)
            itemBinding.productName.text = p.name
            itemBinding.productPrice.text = "Rp.${p.price}"
            itemBinding.root.setOnClickListener {
                onTapListener?.onTap(p)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(products[position])

    override fun getItemCount() = products.size
}