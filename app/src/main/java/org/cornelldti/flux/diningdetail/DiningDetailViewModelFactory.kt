package org.cornelldti.flux.diningdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class DiningDetailViewModelFactory(private val facilityId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiningDetailViewModel::class.java)) {
            return DiningDetailViewModel(facilityId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}