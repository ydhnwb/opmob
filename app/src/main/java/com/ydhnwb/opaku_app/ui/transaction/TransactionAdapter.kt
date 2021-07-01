package com.ydhnwb.opaku_app.ui.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ydhnwb.opaku_app.databinding.ItemTransactionBinding
import com.ydhnwb.opaku_app.domain.transaction.entity.TransactionEntity

class TransactionAdapter(private val transactions: MutableList<TransactionEntity>) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    interface Listener {
        fun onTap(t: TransactionEntity)
    }

    private var listener: Listener? = null

    fun setOnTapListener(l: Listener){
        listener = l
    }

    fun update(list: List<TransactionEntity>){
        transactions.clear()
        transactions.addAll(list)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val itemBinding: ItemTransactionBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(t: TransactionEntity){
            itemBinding.transactionImage.load(t.image)
            itemBinding.transactionName.text = t.name
            itemBinding.transactionPrice.text = "Rp.${t.price}"
            itemBinding.root.setOnClickListener {
                listener?.onTap(t)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(transactions[position])

    override fun getItemCount() = transactions.size
}