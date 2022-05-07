package com.example.realestateapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.example.realestateapp.fragments.ResultsFragment
import com.example.realestateapp.fragments.SearchFragment
import com.example.realestateapp.fragments.WishlistFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.parse.ParseUser

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var fragmentManager: FragmentManager
    lateinit var fragmentToShow: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        fragmentManager = supportFragmentManager

        setupNavOnClick()

        bottomNavigationView.selectedItemId = R.id.action_search
    }

    private fun setupNavOnClick() {
        bottomNavigationView.setOnItemSelectedListener {
            item ->

            when (item.itemId) {
                R.id.action_search -> {
                    fragmentToShow = SearchFragment()
                }
                R.id.action_results -> {
                    fragmentToShow = ResultsFragment()
                }
                R.id.action_wishlist -> {
                    fragmentToShow = WishlistFragment()
                }
                R.id.action_signout -> {
                    confirmLogout()
                }
            }
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit()
            true
        }
    }

    private fun confirmLogout() {
        MaterialAlertDialogBuilder(this)
            .setMessage("Are you sure you want to sign out?")
            .setNegativeButton("Cancel") { dialog, which ->
            }
            .setPositiveButton("Confirm") { dialog, which ->
                logoutUser()
            }
            .show()
    }

    private fun logoutUser() {
        ParseUser.logOut()
        goToLoginActivity()
    }

    private fun goToLoginActivity() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}

