package org.cornelldti.flux.dininglist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.serialization.ExperimentalSerializationApi
import org.cornelldti.flux.R
import org.cornelldti.flux.data.CampusLocation
import org.cornelldti.flux.data.Facility
import org.cornelldti.flux.databinding.DiningListFragmentBinding
import org.cornelldti.flux.network.AuthTokenState

/**
 * A simple [Fragment] subclass.
 * Use the [DiningListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiningListFragment : Fragment() {

    private lateinit var binding: DiningListFragmentBinding
    private lateinit var viewModel: DiningListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.i(TAG, "Called ViewModelProvider.get")
        viewModel = ViewModelProvider(this).get(DiningListViewModel::class.java)

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.dining_list_fragment, container, false)

        val adapter = DiningListAdapter(FacilityListener { facility ->
            onFacilityClick(facility)
        })

        binding.diningList.adapter = adapter

        viewModel.data.observe(
            viewLifecycleOwner,
            {
                adapter.data = it
            })

        return binding.root
    }

    @ExperimentalSerializationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeAuthState()

        viewModel.response.observe(viewLifecycleOwner, { binding.responseText.text = it })

        setChipListener()
    }

    /**
     * Handle click of a facility in the facility list RecyclerView
     */
    private fun onFacilityClick(facility: Facility) {
        Toast.makeText(context, "Clicked: ${facility.id}", Toast.LENGTH_SHORT).show()
        val action =
            DiningListFragmentDirections.actionDiningListFragmentToDiningDetailFragment(
                facility.id, facility.displayName
            )
        this.findNavController().navigate(action)
    }

    private fun setChipListener() {
        binding.diningListFilter.check(R.id.chip_filter_all)
        binding.diningListFilter.setOnCheckedChangeListener { _, isChecked ->
            Log.i(TAG, "Clicked chip: $isChecked")
            viewModel.updateLocationFilter(
                when (isChecked) {
                    R.id.chip_filter_all -> null
                    R.id.chip_filter_north -> CampusLocation.NORTH
                    R.id.chip_filter_west -> CampusLocation.WEST
                    R.id.chip_filter_central -> CampusLocation.CENTRAL
                    else -> null
                }
            )
        }
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
                    viewModel.getDiningList()
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