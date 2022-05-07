package com.example.realestateapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.parse.ParseUser

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        findViewById<TextView>(R.id.to_login).setOnClickListener {
            goToLoginActivity()
        }

        findViewById<Button>(R.id.signupBtn).setOnClickListener {
            val username = findViewById<EditText>(R.id.et_newUsername).text.toString().trim()
            val password = findViewById<EditText>(R.id.et_newPassword).text.toString().trim()
            val email = findViewById<EditText>(R.id.et_newEmail).text.toString().trim()
            signUpUser(username, password, email)
        }
    }

    private fun signUpUser(username: String, password: String, email: String) {
        // Create the ParseUser
        val user = ParseUser()

        // Set fields for the user to be created
        user.setUsername(username)
        user.setPassword(password)
        user.put("email", email)

        user.signUpInBackground { e ->
            if (e == null) {
                goToMainActivity()
                Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show()
            } else {
                e.printStackTrace()
                Toast.makeText(this, "Sign up unsuccessful", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this@SignupActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToLoginActivity() {
        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
        intent.putExtra("wasAppJustLaunched", false)
        startActivity(intent)
        finish()
    }

    companion object {
        const val TAG = "SignupActivity"
    }
}