package org.cornelldti.flux.diningdetail

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import org.cornelldti.flux.data.Facility
import org.cornelldti.flux.network.Api
import org.cornelldti.flux.network.AuthTokenState
import org.cornelldti.flux.network.FirebaseTokenLiveData
import org.cornelldti.flux.util.DateTime
import java.lang.Exception

class DiningDetailViewModel(val facilityId: String, val facilityName: String) : ViewModel() {

    private val _data = MutableLiveData<Facility>()
    val data: LiveData<Facility>
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
        Log.i(TAG, "id: $facilityId, name: $facilityName")
    }

    @ExperimentalSerializationApi
    fun getDiningDetail() {
        Log.i(TAG, "Fetching dining list")
        viewModelScope.launch {
            try {
                val facilityInfo = Api.retrofitService.getFacilityInfo(facilityId)[0]
                val facilityHours = Api.retrofitService.getFacilityHours(
                    facilityId,
                    DateTime.TODAY,
                    DateTime.TOMORROW
                )[0]
                val menuData = Api.retrofitService.getMenuData(facilityId, DateTime.TODAY)
            } catch (e: Exception) {
                _response.value = "Failure ${e.message}"
            }
        }
    }

    companion object {
        const val TAG = "DiningDetailViewModel"
    }
}