package org.cornelldti.flux.network

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseTokenLiveData : LiveData<String?>(null) {
    private val auth = FirebaseAuth.getInstance()

    private val idTokenListener =
        FirebaseAuth.IdTokenListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                getUserIdToken(user)
            }
        }

    private fun getUserIdToken(user: FirebaseUser) {
        Log.i("FirebaseTokenLiveData", "Get token")
        user.getIdToken(true).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                value = task.result?.token
            }
        }
    }

    // When this object has an active observer, start observing the FirebaseAuth state to see if
    // there is currently a logged in user.
    override fun onActive() {
        auth.addIdTokenListener(idTokenListener)
    }

    // When this object no longer has an active observer, stop observing the FirebaseAuth state to
    // prevent memory leaks.
    override fun onInactive() {
        auth.removeIdTokenListener(idTokenListener)
    }
}

enum class AuthTokenState {
    ACQUIRED, UNACQUIRED
}