package org.cornelldti.flux.diningdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.cornelldti.flux.data.Facility
import java.lang.IllegalArgumentException

class DiningDetailViewModelFactory(
    private val facilityId: String,
    private val facilityName: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiningDetailViewModel::class.java)) {
            return DiningDetailViewModel(facilityId, facilityName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}