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
        signInAnonymously()
    }

    private fun signInAnonymously() {
        Log.i("MainActivity", "signInAnon:start")
        auth.signInAnonymously().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.i("MainActivity", "signInAnon:success")
            } else {
                Log.w("MainActivity", "signInAnonymously:failure", task.exception)
                Toast.makeText(
                    baseContext, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}