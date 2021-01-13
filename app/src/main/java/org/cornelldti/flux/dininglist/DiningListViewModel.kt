package org.cornelldti.flux.dininglist

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import org.cornelldti.flux.data.Facility
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
        Log.i("DiningListViewModel", "DiningListViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("DiningListViewModel", "DiningListViewModel destroyed!")
    }

    @ExperimentalSerializationApi
    fun getDiningList() {
        Log.i("DiningListViewModel", "Fetching dining list")
        viewModelScope.launch {
            try {
                val listResult = Api.retrofitService.getFacilityList()
                _data.value = listResult
                _response.value = listResult.toString()
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
    }


}