package org.cornelldti.flux.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirebaseLiveData : LiveData<FirebaseUser?>() {
    private val auth = Firebase.auth

    private val idTokenListener =
        FirebaseAuth.IdTokenListener { firebaseAuth -> value = firebaseAuth.currentUser }

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

// Map user to token LiveData
val FirebaseTokenLiveData: LiveData<String?> =
    Transformations.switchMap(FirebaseLiveData()) { user ->
        liveData {
            val data = user?.getIdToken(true)?.await()?.token
            emit(data)
        }
    }

enum class AuthTokenState {
    ACQUIRED, UNACQUIRED
}