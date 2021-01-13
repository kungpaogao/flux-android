package org.cornelldti.flux.dininglist

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import org.cornelldti.flux.data.Facility
import org.cornelldti.flux.data.toCampusLocation
import org.cornelldti.flux.network.Api
import org.cornelldti.flux.network.AuthTokenState
import org.cornelldti.flux.network.FirebaseTokenLiveData
import java.lang.Exception

class DiningListViewModel: ViewModel() {
    private val _data = MutableLiveData<List<Facility>>()
    val data: LiveData<List<Facility>>
        get() = _data

    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    val tokenAcquired = FirebaseTokenLiveData.map { token ->
        if (token != null) {
            AuthTokenState.ACQUIRED
        } else {
            AuthTokenState.UNACQUIRED
        }
    }

    init {
        Log.i(TAG, "DiningListViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "DiningListViewModel destroyed!")
    }

    @ExperimentalSerializationApi
    fun getDiningList() {
        Log.i(TAG, "Fetching dining list")
        viewModelScope.launch {
            try {
                // make requests
                val facilityList = Api.retrofitService.getFacilityList()
                val facilityInfoList = Api.retrofitService.getFacilityInfo(null)
                val howDenseList = Api.retrofitService.getHowDense(null)

                val facilityListMap = facilityList.associateBy { facility -> facility.id }

                howDenseList.map { howDense ->
                    val facility = facilityListMap[howDense.id]
                    if (facility != null) {
                        facility.density = howDense.density
                    }
                }

                facilityInfoList.map { facilityInfo ->
                    val facility = facilityListMap[facilityInfo.id]
                    facility?.let {
                        it.isOpen = facilityInfo.isOpen
                        it.campusLocation = facilityInfo.campusLocation.toCampusLocation()
                    }
                }

                _data.value = facilityList
                _response.value = facilityList.toString()
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
    }

    companion object {
        const val TAG = "DiningListViewModel"
    }
}