package org.cornelldti.flux

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("MainActivity", "Created MainActivity")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
        signInAnonymously()
    }

    private fun signInAnonymously() {
        auth.signInAnonymously().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("MainActivity", "signInAnon:success")
                val user = auth.currentUser
                requestToken(user)
                // updateUI
            } else {
                Log.w("MainActivity", "signInAnonymously:failure", task.exception)
                Toast.makeText(
                    baseContext, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun requestToken(user: FirebaseUser?) {
        user!!.getIdToken(true).addOnCompleteListener { task ->
            if (task.isSuccessful) {
//                FacilityDatabase.setToken(it.result!!.token!!)
//                _tokenAcquired.value = true
                Log.i("MainActivity", "Token acquired: ${task.result?.token}")
            }
        }
    }
}