package org.cornelldti.flux.diningdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import org.cornelldti.flux.data.Facility
import org.cornelldti.flux.network.AuthTokenState
import org.cornelldti.flux.network.FirebaseTokenLiveData

class DiningDetailViewModel(val facilityId: String) : ViewModel() {

    private val _data = MutableLiveData<List<Facility>>()
    val data: LiveData<List<Facility>>
        get() = _data

    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    /**
     * LiveData container that maps the token to token state
     */
    val tokenAcquired: LiveData<AuthTokenState> = FirebaseTokenLiveData.map { token ->
        if (token != null) {
            AuthTokenState.ACQUIRED
        } else {
            AuthTokenState.UNACQUIRED
        }
    }

    init {
        Log.i("DiningDetailViewModel", "Facility ID is $facilityId")
    }

    fun getDiningDetail() {

        TODO("fetch dining detail from API")
    }
}