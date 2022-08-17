package com.bryanrantung.moneytrack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bryanrantung.moneytrack.R
import com.bryanrantung.moneytrack.databinding.ItemHistoryBinding
import com.bryanrantung.moneytrack.model.Transaction
import com.bryanrantung.moneytrack.utils.Constant
import com.bryanrantung.moneytrack.utils.Utils


class MoneyTrackAdapter(
    private val cashFlowList: ArrayList<Transaction>,
    private val detailScreen: (id: Int) -> Unit
): RecyclerView.Adapter<MoneyTrackAdapter.MainViewHolder>() {
    inner class MainViewHolder(private val itemBinding: ItemHistoryBinding): RecyclerView.ViewHolder(itemBinding.root) {
        val imageView = itemBinding.ivImage
        val textViewTitle = itemBinding.tvTitle
        val textViewDate = itemBinding.tvDate
        val textViewAmount = itemBinding.tvAmount
        val linerLayoutItemHistory = itemBinding.llItemHistory
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val cashFlow = cashFlowList[position]
        val context = holder.itemView.context

        holder.textViewTitle.text = cashFlow.title
        holder.textViewDate.text = cashFlow.date

        if (cashFlow.type == Constant.OUTCOME) {
            holder.textViewAmount.text = "- ${Utils.toRupiah(cashFlow.amount)}"
            holder.textViewAmount.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.redColor
                )
            )
            holder.imageView.setImageResource(R.drawable.money_out)
        } else {
            holder.textViewAmount.text = "+ ${Utils.toRupiah(cashFlow.amount)}"
            holder.textViewAmount.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.greenColor
                )
            )
            holder.imageView.setImageResource(R.drawable.money_in)
        }

        holder.linerLayoutItemHistory.setOnClickListener {
            detailScreen.invoke(cashFlow.id)
        }
    }

    override fun getItemCount(): Int {
        return cashFlowList.size
    }

}
