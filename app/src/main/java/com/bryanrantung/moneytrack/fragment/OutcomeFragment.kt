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
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch


class OutcomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val content = inflater.inflate(R.layout.fragment_outcome, container, false) as ConstraintLayout

        btnNewOutcome(content)

        val incomeHistory = (activity?.applicationContext as MoneyTrackApp).db.cashFlowDao()
        loadOutcomeHistory(incomeHistory, content)
        loadTotalOutcome(incomeHistory, content)

        return content
    }

    private fun btnNewOutcome(content: View) {
        val btnNewOutcome: Button = content.findViewById(R.id.btnNewOutcome)
        btnNewOutcome.setOnClickListener {
            val intent = Intent(content.context, CreateActivity::class.java)
            intent.putExtra(Constant.TYPE, Constant.OUTCOME)
            startActivity(intent)
        }

        val btnCreateNewOutcome: Button = content.findViewById(R.id.btnCreateNewOutcome)
        btnCreateNewOutcome.setOnClickListener {
            val intent = Intent(content.context, CreateActivity::class.java)
            intent.putExtra(Constant.TYPE, Constant.OUTCOME)
            startActivity(intent)
        }
    }


    private fun loadOutcomeHistory(cashFlowDao: MoneyTrackDao, content: View) {
        lifecycleScope.launch {
            cashFlowDao.fetchAllIncome(Constant.OUTCOME).collect {
                val cashFlowList = ArrayList(it)
                setupHistoryOutcome(cashFlowList, cashFlowDao, content)
            }
        }
    }

    private fun setupHistoryOutcome(
        cashFlowList: ArrayList<Transaction>,
        cashFlowDao: MoneyTrackDao,
        content: View
    ) {
        val cashFlowAdapter = MoneyTrackAdapter(cashFlowList) { id ->
            navigateToDetailActivity(id, content)
        }
        val rvOutcomeHistory: RecyclerView = content.findViewById(R.id.rvOutcomeHistory)
        val llEmptyOutcomeHistory: LinearLayout = content.findViewById(R.id.llEmptyOutcomeHistory)

        if (cashFlowList.isNotEmpty()) {
            rvOutcomeHistory.visibility = View.VISIBLE
            llEmptyOutcomeHistory.visibility = View.GONE
            rvOutcomeHistory.adapter = cashFlowAdapter
            rvOutcomeHistory.layoutManager = LinearLayoutManager(content.context)
        } else {
            rvOutcomeHistory.visibility = View.INVISIBLE
            llEmptyOutcomeHistory.visibility = View.VISIBLE
        }
    }

    private fun loadTotalOutcome(cashFlowDao: MoneyTrackDao, content: View) {
        lifecycleScope.launch {
            val totalOutcome: Int? = cashFlowDao.calculateIncome(Constant.OUTCOME)
            setupTotalOutcome(totalOutcome, content)
        }
    }

    private fun setupTotalOutcome(
        totalOutcome: Int?,
        content: View
    ) {
        val tvTotalOutcome: TextView = content.findViewById(R.id.tvTotalOutcome)
        tvTotalOutcome.text =  Utils.toRupiah(totalOutcome ?: 0)
    }

    private fun navigateToDetailActivity(id: Int, content: View) {
        val intent = Intent(content.context, DetailItemActivity::class.java)
        intent.putExtra(Constant.ID, id)
        startActivity(intent)
    }
}