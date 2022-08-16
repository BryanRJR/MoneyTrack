package com.bryanrantung.moneytrack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.bryanrantung.moneytrack.databinding.ActivityMainBinding
import com.bryanrantung.moneytrack.fragment.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.menu_main_dashboard -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.menu_main_income -> {
                    true
                }
                R.id.menu_main_outcome -> {
                    true
                }
                R.id.menu_main_profile -> {
                    true
                }
                else -> false
            }
        }
    }

    // replace fragment
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_main, fragment)
            .commit()
    }

    // stop fragment
    private fun stopFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .remove(fragment)
            .commit()
    }
}