package org.cornelldti.flux.diningdetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.serialization.ExperimentalSerializationApi
import org.cornelldti.flux.R
import org.cornelldti.flux.databinding.DiningDetailFragmentBinding
import org.cornelldti.flux.network.AuthTokenState

class DiningDetailFragment : Fragment() {

    private lateinit var viewModel: DiningDetailViewModel
    private lateinit var viewModelFactory: DiningDetailViewModelFactory

    private lateinit var binding: DiningDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModelFactory =
            DiningDetailFragmentArgs.fromBundle(requireArguments()).let {
                DiningDetailViewModelFactory(
                    it.facilityId,
                    it.facilityName
                )
            }

        viewModel = ViewModelProvider(this, viewModelFactory).get(DiningDetailViewModel::class.java)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dining_detail_fragment,
            container,
            false
        )

        binding.diningDetailAppbar.title = viewModel.facilityName
        binding.diningDetailAppbar.subtitle = viewModel.facilityId

        val adapter = DiningDetailMenuListAdapter()
        binding.listMenuItems.adapter = adapter
        viewModel.menu.observe(viewLifecycleOwner, {
            adapter.data = it
        })

        viewModel.availability.observe(viewLifecycleOwner, {
            binding.textAvailabilityNum.text = getString(it)
        })

//        viewModel.meals.observe(viewLifecycleOwner, {
//            val tabLayout = binding.tabsMenuMeal
//            TabLayoutMediator(tabLayout, viewPager)
//        })

        return binding.root
    }

    @ExperimentalSerializationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeAuthState()

        viewModel.response.observe(viewLifecycleOwner, { response ->
            binding.diningDetailResponse.text = response
        })
    }

    /**
     * Observe state of auth token and make network request when token is acquired
     */
    @ExperimentalSerializationApi
    private fun observeAuthState() {
        viewModel.tokenAcquired.observe(viewLifecycleOwner, { token ->
            when (token) {
                AuthTokenState.ACQUIRED -> {
                    Log.i(TAG, "Authentication success")
                    viewModel.getDiningDetail()
                    // TODO: fix fetching multiple times
                }
                else -> {
                    Log.w(TAG, "Authentication failure")
                }
            }
        })
    }

    companion object {
        const val TAG = "DiningListFragment"
    }

}