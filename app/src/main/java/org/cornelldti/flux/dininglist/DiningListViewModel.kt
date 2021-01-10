package org.cornelldti.flux.dininglist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import org.cornelldti.flux.data.Facility
import org.cornelldti.flux.network.AuthenticationState
import org.cornelldti.flux.network.FirebaseLiveData

class DiningListViewModel: ViewModel() {
    private val _data = MutableLiveData<List<Facility>>()
    val data: LiveData<List<Facility>>
        get() = _data

    val authState = Transformations.map(FirebaseLiveData()) { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
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

    fun fetchDiningList() {
        TODO("fetch dining list from API")
    }


}