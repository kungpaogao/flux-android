package org.cornelldti.flux.dininglist

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import org.cornelldti.flux.data.CampusLocation
import org.cornelldti.flux.data.Facility
import org.cornelldti.flux.data.toCampusLocation
import org.cornelldti.flux.network.Api
import org.cornelldti.flux.network.AuthTokenState
import org.cornelldti.flux.network.FirebaseTokenLiveData
import java.lang.Exception

class DiningListViewModel : ViewModel() {
    private val _data = MutableLiveData<List<Facility>>()

    /**
     * Public LiveData that shows filtered version of `_data`
     */
    val data: LiveData<List<Facility>>
        get() = _data.map { list ->
            list.filter { facility ->
                filter == facility.campusLocation || filter == null
            }
        }

    private var filter: CampusLocation? = null

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
        Log.i(TAG, "DiningListViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "DiningListViewModel destroyed!")
    }

    /**
     * Sets list filter to given location
     */
    fun updateLocationFilter(location: CampusLocation?) {
        // check null to make sure that we don't attempt `Transformations.map` on null LiveData
        if (_data.value != null) {
            filter = location
            /* TODO: think of better way of force refreshing data;
                maybe, make filter LiveData so that `data` can observe both `_data` and `filter`
             */
            _data.value = _data.value
        }
    }

    /**
     * Calls Retrofit (network) to get facility list, info, and density information.
     * Then, combines responses and sets data and response.
     */
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