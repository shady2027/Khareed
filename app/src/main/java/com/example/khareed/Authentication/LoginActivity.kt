package com.example.khareed.Authentication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.khareed.Activity.MainActivity
import com.example.khareed.Location.LocationActivity
import com.example.khareed.R
import com.example.khareed.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signUpText.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            val pass = binding.editTextTextPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                binding.progressBar.visibility = View.VISIBLE // Show the progress bar


                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, LocationActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Failed To Login, Try Again", Toast.LENGTH_SHORT)
                            .show()
                    }
                    binding.progressBar.visibility =
                        View.INVISIBLE // Hide the progress bar after login attempt
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onStart() {
        super.onStart()
        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity() // Close the app when back button is pressed
    }
}