package com.bryanrantung.moneytrack

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bryanrantung.moneytrack.databinding.ActivityCreateBinding
import com.bryanrantung.moneytrack.model.Transaction
import com.bryanrantung.moneytrack.room.MoneyTrackDao
import com.bryanrantung.moneytrack.utils.Constant
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreateActivity : AppCompatActivity() {
    private var binding: ActivityCreateBinding? = null
    private var mProgressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val cashFlowDao = (application as MoneyTrackApp).db.cashFlowDao()

        setupToolbar()

        setupInput()

        onSubmit(cashFlowDao)
    }

    private fun setupToolbar() {
        val type = intent.getStringExtra(Constant.TYPE)
        setSupportActionBar(binding?.toolBarCreate)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Create ${if (type == Constant.INCOME) "Income" else "Outcome"}"
        }

        binding?.toolBarCreate?.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupInput() {
        if (intent.getStringExtra(Constant.AMOUNT) != null) {
            binding?.etAmount?.setText(intent.getStringExtra(Constant.AMOUNT))
        }

        if (intent.getStringExtra(Constant.TITLE) != null) {
            binding?.etTitle?.setText(intent.getStringExtra(Constant.TITLE))
        }

        if (intent.getStringExtra(Constant.DESCRIPTION) != null) {
            binding?.etDescription?.setText(intent.getStringExtra(Constant.DESCRIPTION))
        }
    }

    private fun generateDate(): String {
        val c = Calendar.getInstance()
        val dateTime = c.time
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(dateTime)
    }

    private fun onSubmit(cashFlowDao: MoneyTrackDao) {
        binding?.btnSubmit?.setOnClickListener {
            showCustomProgressDialog()
            val title = binding?.etTitle?.text
            val description = binding?.etDescription?.text
            val amount = binding?.etAmount?.text
            val type = intent.getStringExtra(Constant.TYPE)
            val date = generateDate()

            if (title.isNullOrEmpty() && description.isNullOrEmpty() && amount.isNullOrEmpty()) {
                Toast.makeText(this, "Please fill all of the field!", Toast.LENGTH_SHORT).show()
            } else {
                val isEdit: Boolean = intent.getBooleanExtra(Constant.IS_EDIT, false)
                val id: String? = intent.getStringExtra(Constant.ID)
                if (isEdit) {
                    editCashFlow(
                        id.toString(),
                        title.toString(),
                        description.toString(),
                        amount.toString(),
                        type.toString(),
                        date,
                        cashFlowDao
                    )
                } else {
                    submitToDatabase(
                        title.toString(),
                        description.toString(),
                        amount.toString(),
                        type.toString(),
                        date,
                        cashFlowDao
                    )
                }
            }
        }
    }

    private fun submitToDatabase(
        title: String,
        description: String,
        amount: String,
        type: String,
        date: String,
        cashFlowDao: MoneyTrackDao
    ) {
        lifecycleScope.launch {
            cashFlowDao.insert(
                Transaction(
                    title = title,
                    description = description,
                    type = type,
                    amount = amount.toInt(),
                    date = date,
                )
            )
            Toast.makeText(this@CreateActivity, "Record Created!", Toast.LENGTH_SHORT).show()
            hideProgressDialog()
            val intent = Intent(
                this@CreateActivity,
                MainActivity::class.java
            )
            startActivity(intent)
            finish()
        }
    }

    private fun editCashFlow(
        id: String,
        title: String,
        description: String,
        amount: String,
        type: String,
        date: String,
        cashFlowDao: MoneyTrackDao
    ) {
        lifecycleScope.launch {
            cashFlowDao.update(
                Transaction(
                    id = id.toInt(),
                    title = title,
                    description = description,
                    type = type,
                    amount = amount.toInt(),
                    date = date,
                )
            )
            Toast.makeText(this@CreateActivity, "Record Updated!", Toast.LENGTH_SHORT).show()
            hideProgressDialog()
            finish()
        }
    }

    private fun showCustomProgressDialog() {
        mProgressDialog = Dialog(this)
        mProgressDialog!!.setContentView(R.layout.loader)
        mProgressDialog!!.show()
    }

    private fun hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}