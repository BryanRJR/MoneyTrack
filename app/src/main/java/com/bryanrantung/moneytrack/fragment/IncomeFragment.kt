package com.bryanrantung.moneytrack.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bryanrantung.moneytrack.CreateActivity
import com.bryanrantung.moneytrack.DetailItemActivity
import com.bryanrantung.moneytrack.MoneyTrackApp
import com.bryanrantung.moneytrack.R
import com.bryanrantung.moneytrack.adapter.MoneyTrackAdapter
import com.bryanrantung.moneytrack.model.Transaction
import com.bryanrantung.moneytrack.room.MoneyTrackDao
import com.bryanrantung.moneytrack.utils.Constant
import com.bryanrantung.moneytrack.utils.Utils
import kotlinx.coroutines.launch


class IncomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val content = inflater.inflate(R.layout.fragment_income, container, false) as ConstraintLayout

        btnNewOutcome(content)

        val incomeHistory = (activity?.applicationContext as MoneyTrackApp).db.cashFlowDao()
        loadIncomeHistory(incomeHistory, content)
        loadTotalIncome(incomeHistory, content)
        return content
    }

    private fun btnNewOutcome(content: View) {
        val btnNewOutcome: Button = content.findViewById(R.id.btnNewOutcome)
        btnNewOutcome.setOnClickListener {
            val intent = Intent(content.context, CreateActivity::class.java)
            intent.putExtra(Constant.TYPE, Constant.INCOME)
            startActivity(intent)
        }

        val btnCreateNewIncome: Button = content.findViewById(R.id.btnCreateNewIncome)
        btnCreateNewIncome.setOnClickListener {
            val intent = Intent(content.context, CreateActivity::class.java)
            intent.putExtra(Constant.TYPE, Constant.INCOME)
            startActivity(intent)
        }
    }

    private fun loadIncomeHistory(moneyTrackDao: MoneyTrackDao, content: View) {
        lifecycleScope.launch {
            moneyTrackDao.fetchAllIncome(Constant.INCOME).collect {
                val cashFlowList = ArrayList(it)
                setupHistoryCashFlow(cashFlowList, moneyTrackDao, content)
            }
        }
    }

    private fun setupHistoryCashFlow(
        cashFlowList: ArrayList<Transaction>,
        moneyTrackDao: MoneyTrackDao,
        content: View
    ) {
        val cashFlowAdapter =MoneyTrackAdapter(cashFlowList) { id ->
            navigateToDetailActivity(id, content)
        }
        val rvIncomeHistory: RecyclerView = content.findViewById(R.id.rvIncomeHistory)
        val llEmptyIncomeHistory: LinearLayout = content.findViewById(R.id.llEmptyIncomeHistory)

        if (cashFlowList.isNotEmpty()) {
            rvIncomeHistory.visibility = View.VISIBLE
            llEmptyIncomeHistory.visibility = View.GONE
            rvIncomeHistory.adapter = cashFlowAdapter
            rvIncomeHistory.layoutManager = LinearLayoutManager(content.context)
        } else {
            rvIncomeHistory.visibility = View.INVISIBLE
            llEmptyIncomeHistory.visibility = View.VISIBLE
        }
    }

    private fun loadTotalIncome(cashFlowDao: MoneyTrackDao, content: View) {
        lifecycleScope.launch {
            val totalIncome: Int? = cashFlowDao.calculateIncome(Constant.INCOME)
            setupTotalIncome(totalIncome, content)
        }
    }

    private fun setupTotalIncome(
        totalIncome: Int?,
        content: View
    ) {
        val tvTotalIncome: TextView = content.findViewById(R.id.tvTotalIncome)
        tvTotalIncome.text =  Utils.toRupiah(totalIncome ?: 0)
    }

    private fun navigateToDetailActivity(id: Int, content: View) {
        val intent = Intent(content.context, DetailItemActivity::class.java)
        intent.putExtra(Constant.ID, id)
        startActivity(intent)
    }
}