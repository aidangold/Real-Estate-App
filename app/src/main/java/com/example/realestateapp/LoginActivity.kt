package com.example.realestateapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.example.realestateapp.fragments.ResultsFragment
import com.example.realestateapp.models.Listing
import com.google.gson.Gson
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (ParseUser.getCurrentUser() != null) {
            goToMainActivity()
        }

        findViewById<Button>(R.id.login_button).setOnClickListener {
            val username = findViewById<EditText>(R.id.et_username).text.toString().trim()
            val password = findViewById<EditText>(R.id.et_password).text.toString().trim()
            loginUser(username, password)
        }

        findViewById<TextView>(R.id.to_signup).setOnClickListener {
            goToSignupActivity()
        }
    }

    private fun loginUser (username: String, password: String) {
        initializeStorage()
        ParseUser.logInInBackground(username, password, ({ user, e ->
            if (user != null) {
                Log.i(TAG, "Successfully logged in")
                goToMainActivity()
            } else {
                e.printStackTrace()
                Toast.makeText(this, "Error logging in", Toast.LENGTH_SHORT).show()
            }})
        )
    }

    private fun goToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToSignupActivity() {
        val intent = Intent(this@LoginActivity, SignupActivity::class.java)
        startActivity(intent)
        finish()
    }

    // creates empty internal storage file
    private fun initializeStorage() {
        val editor = PreferenceManager.getDefaultSharedPreferences(this).edit()
        val gson = Gson()
        val listing: MutableList<Listing> = mutableListOf()
        editor.putString("results", gson.toJson(listing))
        editor.apply()
    }

    companion object {
        const val TAG = "LoginActivity"
    }
}