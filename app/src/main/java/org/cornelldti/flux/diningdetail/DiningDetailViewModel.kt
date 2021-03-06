package org.cornelldti.flux.diningdetail

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import org.cornelldti.flux.data.*
import org.cornelldti.flux.network.Api
import org.cornelldti.flux.network.AuthTokenState
import org.cornelldti.flux.network.FacilityInfo
import org.cornelldti.flux.network.FirebaseTokenLiveData
import org.cornelldti.flux.util.DateTime
import java.lang.Exception
import java.util.*

class DiningDetailViewModel(private val facilityId: String, val facilityName: String) :
    ViewModel() {

    private val _data = MutableLiveData<Facility>()

    private val _info = MutableLiveData<FacilityInfo>()
    val info: LiveData<FacilityInfo>
        get() = _info

    private val _menu = MutableLiveData<List<Menu>>()
    val menu: LiveData<List<Menu>>
        get() = _menu

    private var menuFilter: String = DateTime.TODAY

    private val _availability = MutableLiveData<Pair<Int, Int>>()

    /**
     * Pair of resource IDs for (string, color) corresponding to facility's availability
     */
    val availability: LiveData<Pair<Int, Int>>
        get() = _availability

    private val _lastUpdated = MutableLiveData<Date>()
    val lastUpdated: LiveData<Date>
        get() = _lastUpdated

    private val _loadingStatus = MutableLiveData<LoadingStatus>()
    val loadingStatus: LiveData<LoadingStatus>
        get() = _loadingStatus

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

    /**
     * Sets menu based on selected date
     *
     * @param date selected date in ISO date string format
     */
    fun setMenuDay(date: String) {
        menuFilter = date
        _menu.value = _data.value?.weekMenu?.get(date)
    }

    /**
     * Calls Retrofit (network) to get facility info, density, and menu information.
     * Then, combines responses and sets corresponding data and response.
     */
    @ExperimentalSerializationApi
    fun getDiningDetail() {
        Log.i(TAG, "Fetching dining list")
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.IN_PROGRESS

            try {
                val facilityInfo = async { Api.retrofitService.getFacilityInfo(facilityId)[0] }
                val howDense = async { Api.retrofitService.getHowDense(facilityId)[0] }
                val weeksMenus =
                    async {
                        Api.retrofitService.getMenuData(
                            facilityId,
                            DateTime.TODAY
                        )[0].weeksMenus
                    }

                _data.value?.apply {
                    facilityInfo.await().let {
                        _info.value = it
                        // TODO: delete since don't really need to set info
                        isOpen = it.isOpen
                        nextOpen = it.nextOpen
                        closingAt = it.closingAt
                        campusLocation = it.campusLocation
                    }

                    howDense.await().let {
                        density = it.density
                        _availability.value = Pair(densityStringResource, densityColorResource)
                    }

                    weeksMenus.await().let { menus ->
                        weekMenu = menus.associateBy({ it.date }, { it.menus })
                        _menu.value = weekMenu[menuFilter]
                    }
                }

                _lastUpdated.value = DateTime.NOW
                _loadingStatus.value = LoadingStatus.SUCCESS
            } catch (e: Exception) {
                Log.w(TAG, "getDiningDetail: Failure ${e.message}")
                _loadingStatus.value = LoadingStatus.ERROR
            }
        }
    }

    companion object {
        const val TAG = "DiningDetailViewModel"
    }
}