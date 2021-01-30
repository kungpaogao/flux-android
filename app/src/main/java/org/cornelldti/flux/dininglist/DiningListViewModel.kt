package org.cornelldti.flux.dininglist

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import org.cornelldti.flux.data.CampusLocation
import org.cornelldti.flux.data.Facility
import org.cornelldti.flux.data.LoadingStatus
import org.cornelldti.flux.data.SortOrder
import org.cornelldti.flux.network.Api
import org.cornelldti.flux.network.AuthTokenState
import org.cornelldti.flux.network.FirebaseTokenLiveData
import java.lang.Exception
import java.util.*
import kotlin.Comparator

class DiningListViewModel : ViewModel() {
    private val _data = MutableLiveData<List<Facility>>()

    /**
     * Public LiveData that shows filtered version of `_data`
     */
    val data: LiveData<List<Facility>>
        get() = _data.map { list ->
            list.filter { facility ->
                locationFilter == facility.campusLocation || locationFilter == null
            }.filter { facility ->
                facility.displayName.toLowerCase(Locale.US)
                    .contains(queryFilter ?: "")
            }.sortedWith(sortOrderFilter)
        }

    private var locationFilter: CampusLocation? = null
    private var queryFilter: String? = null

    // default sort order is by crowdedness, then display name
    private var sortOrderFilter: Comparator<Facility> =
        compareBy({ it.density }, { it.displayName })

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

    private val _loadingStatus = MutableLiveData<LoadingStatus>()
    val loadingStatus: LiveData<LoadingStatus>
        get() = _loadingStatus

    /**
     * Refreshes data value to reapply filters
     */
    private fun refreshData() {
        /* TODO: think of better way of force refreshing data;
            maybe, make filter LiveData so that `data` can observe both `_data` and `filter`
         */
        // check null to make sure that we don't attempt `Transformations.map` on null LiveData
        if (_data.value != null) _data.value = _data.value
    }

    /**
     * Updates list filter to given location and refreshes dining list data
     */
    fun updateLocationFilter(location: CampusLocation?) {
        locationFilter = location
        refreshData()
    }

    /**
     * Updates search filter to given query and refreshes dining list data
     */
    fun updateSearchQuery(query: String) {
        queryFilter = query.toLowerCase(Locale.US)
        refreshData()
    }

    /**
     * Updates sort filter with given [SortOrder] and refreshes dining list data
     */
    fun updateSortFilter(sortOrder: SortOrder) {
        sortOrderFilter = when (sortOrder) {
            SortOrder.ALPHABETICAL -> compareBy { it.displayName }
            SortOrder.CROWDEDNESS -> compareBy({ it.density }, { it.displayName })
            SortOrder.LOCATION -> compareBy()
        }
        refreshData()
    }

    /**
     * Calls Retrofit (network) to get facility list, info, and density information.
     * Then, combines responses and sets data and response.
     */
    @ExperimentalSerializationApi
    fun getDiningList() {
        Log.i(TAG, "Fetching dining list")
        viewModelScope.launch {
            // set loading state
            _loadingStatus.value = LoadingStatus.IN_PROGRESS

            try {
                // make requests
                val facilityList = async { Api.retrofitService.getFacilityList() }
                val facilityInfoList = async { Api.retrofitService.getFacilityInfo() }
                val howDenseList = async { Api.retrofitService.getHowDense() }

                val facilityListMap =
                    facilityList.await().associateBy { facility -> facility.id }

                howDenseList.await().map { howDense ->
                    val facility = facilityListMap[howDense.id]
                    if (facility != null) {
                        facility.density = howDense.density
                    }
                }

                facilityInfoList.await().map { facilityInfo ->
                    val facility = facilityListMap[facilityInfo.id]
                    facility?.apply {
                        isOpen = facilityInfo.isOpen
                        campusLocation = facilityInfo.campusLocation
                    }
                }

                _data.value = facilityList.await()
                _loadingStatus.value = LoadingStatus.SUCCESS
            } catch (e: Exception) {
                Log.w(TAG, "getDiningList error: ${e.message}")
                _loadingStatus.value = LoadingStatus.ERROR
            }
        }
    }

    companion object {
        const val TAG = "DiningListViewModel"
    }
}