package org.cornelldti.flux.diningdetail

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.async
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
        // initialize value based on ViewModel params
        _data.value = Facility(facilityId, facilityName)
    }

    @ExperimentalSerializationApi
    fun getDiningDetail() {
        Log.i(TAG, "Fetching dining list")
        viewModelScope.launch {
            try {
                val facilityInfo = async { Api.retrofitService.getFacilityInfo(facilityId)[0] }
                val howDense = async { Api.retrofitService.getHowDense(facilityId)[0] }
                val facilityHours = async {
                    Api.retrofitService.getFacilityHours(
                        facilityId,
                        DateTime.TODAY,
                        DateTime.TOMORROW
                    )[0]
                }
                val weeksMenu =
                    async {
                        Api.retrofitService.getMenuData(
                            facilityId,
                            DateTime.TODAY
                        )[0].weeksMenus
                    }

                _data.value?.apply {
                    facilityInfo.await().let {
                        isOpen = it.isOpen
                        nextOpen = it.nextOpen
                        closingAt = it.closingAt
                        campusLocation = it.campusLocation
                    }

                    howDense.await().let {
                        density = it.density
                    }

                    weeksMenu.await().let {
                        menus = it
                    }

                    _response.value = this.toString()
                }

            } catch (e: Exception) {
                _response.value = "Failure ${e.message}"
            }
        }
    }

    companion object {
        const val TAG = "DiningDetailViewModel"
    }
}