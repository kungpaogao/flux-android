package org.cornelldti.flux.dininglist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.serialization.ExperimentalSerializationApi
import org.cornelldti.flux.data.Facility
import org.cornelldti.flux.network.Api
import org.cornelldti.flux.network.AuthTokenState
import org.cornelldti.flux.network.FirebaseTokenLiveData
import retrofit2.Call
import retrofit2.Response

class DiningListViewModel: ViewModel() {
    private val _data = MutableLiveData<List<Facility>>()
    val data: LiveData<List<Facility>>
        get() = _data

    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    val tokenAcquired = Transformations.map(FirebaseTokenLiveData) { token ->
        if (token != null) {
            AuthTokenState.ACQUIRED
        } else {
            AuthTokenState.UNACQUIRED
        }
    }

    init {
        Log.i("DiningListViewModel", "DiningListViewModel created!")
        _data.value = listOf(
            Facility("f1", "Facility One"),
            Facility("f2", "Facility Two"),
            Facility("f3", "Facility Two"),
            Facility("f4", "Facility Two"),
            Facility("f5", "Facility Two"),
            Facility("f6", "Facility Two"),
            Facility("f7", "Facility Two"),
            Facility("f8", "Facility Two"),
            Facility("f9", "Facility Two"),
            Facility("f10", "Facility Two"),
            Facility("f11", "Facility Two"),
            Facility("f12", "Facility Two"),
            Facility("f13", "Facility Two"),
            Facility("f14", "Facility Two"),
        )
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("DiningListViewModel", "DiningListViewModel destroyed!")
    }

    @ExperimentalSerializationApi
    fun getDiningList() {
        Log.i("DiningListViewModel", "Fetching dining list")
        Api.retrofitService.getFacilityList().enqueue(
            object : retrofit2.Callback<List<Facility>> {
                override fun onResponse(
                    call: Call<List<Facility>>,
                    response: Response<List<Facility>>
                ) {
                    _response.value = response.body().toString()
                }

                override fun onFailure(call: Call<List<Facility>>, t: Throwable) {
                    _response.value = "Failure: ${t.message}"
                }

            }
        )
    }


}