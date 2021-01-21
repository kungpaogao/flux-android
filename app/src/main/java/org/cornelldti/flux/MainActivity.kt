package org.cornelldti.flux

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("MainActivity", "Created MainActivity")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // perform anonymous sign-in authentication
        this.lifecycleScope.launch {
            Firebase.auth.signInAnonymously().await()
        }
    }
}