package org.cornelldti.flux.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Intercepts network request and adds Firebase auth token to headers
 */
class HeaderInterceptor : Interceptor {

    companion object {
        const val TAG = "HeaderInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.i(TAG, "Start intercept")
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${FirebaseTokenLiveData.value}")
            .build()

        return chain.proceed(request)
    }
}