package org.cornelldti.flux.diningdetail

import android.util.Log
import androidx.lifecycle.ViewModel

class DiningDetailViewModel(val facilityId: String) : ViewModel() {
    // TODO: Implement the ViewModel

    init {
        Log.i("DiningDetailViewModel", "Facility ID is $facilityId")
    }

    fun fetchDiningDetail() {
        TODO("fetch dining detail from API")
    }
}