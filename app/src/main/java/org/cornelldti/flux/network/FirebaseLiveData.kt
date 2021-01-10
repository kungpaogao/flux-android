package org.cornelldti.flux.network

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseLiveData : LiveData<FirebaseUser?>() {
    private val auth = FirebaseAuth.getInstance()

    private val authStateListener =
        FirebaseAuth.AuthStateListener { firebaseAuth -> value = firebaseAuth.currentUser }

//    var authToken =

    // When this object has an active observer, start observing the FirebaseAuth state to see if
    // there is currently a logged in user.
    override fun onActive() {
        auth.addAuthStateListener(authStateListener)
    }

    // When this object no longer has an active observer, stop observing the FirebaseAuth state to
    // prevent memory leaks.
    override fun onInactive() {
        auth.removeAuthStateListener(authStateListener)
    }

}

enum class AuthenticationState {
    AUTHENTICATED, UNAUTHENTICATED
}