package org.cornelldti.flux.diningdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import org.cornelldti.flux.R
import org.cornelldti.flux.databinding.DiningDetailFragmentBinding

class DiningDetailFragment : Fragment() {

    companion object {
        fun newInstance() = DiningDetailFragment()
    }

    private lateinit var viewModel: DiningDetailViewModel
    private lateinit var viewModelFactory: DiningDetailViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: DiningDetailFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.dining_detail_fragment,
            container,
            false
        )



        viewModelFactory =
            DiningDetailViewModelFactory(DiningDetailFragmentArgs.fromBundle(requireArguments()).facilityId)

        viewModel = ViewModelProvider(this, viewModelFactory).get(DiningDetailViewModel::class.java)

        binding.facilityId.text = viewModel.facilityId
        
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}