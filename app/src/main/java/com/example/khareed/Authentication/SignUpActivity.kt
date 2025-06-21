package com.example.khareed.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.khareed.Activity.MainActivity
import com.example.khareed.Location.LocationActivity
import com.example.khareed.R
import com.example.khareed.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.textView6.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.signUpButton.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            val pass = binding.editTextTextPassword.text.toString()
            val username = binding.etUserName.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && username.isNotEmpty()) {
                if (username.length <= 20) {
                    binding.progressBarSignUp.visibility = View.VISIBLE // Show the progress bar

                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, LocationActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Wrong credentials!! Try Again", Toast.LENGTH_SHORT).show()
                        }
                    binding.progressBarSignUp.visibility = View.INVISIBLE // Hide the progress bar after login attempt
                }
                } else {
                    Toast.makeText(this, "Username should not exceed 20 characters", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity() // Close the app when back button is pressed
    }

}