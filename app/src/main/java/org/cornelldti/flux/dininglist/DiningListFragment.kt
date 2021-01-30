package org.cornelldti.flux.dininglist

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.serialization.ExperimentalSerializationApi
import org.cornelldti.flux.R
import org.cornelldti.flux.data.CampusLocation
import org.cornelldti.flux.data.Facility
import org.cornelldti.flux.data.LoadingStatus
import org.cornelldti.flux.data.SortOrder
import org.cornelldti.flux.databinding.DiningListFragmentBinding
import org.cornelldti.flux.network.AuthTokenState
import org.cornelldti.flux.util.getAttr

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

        return binding.root
    }

    @ExperimentalSerializationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeAuthState()

        /**
         * set list adapter and observe changes
         */
        val adapter = DiningListAdapter(FacilityListener { facility ->
            onFacilityClick(facility)
        })
        binding.diningList.adapter = adapter

        viewModel.data.observe(
            viewLifecycleOwner,
            {
                adapter.data = it
            })

        setChipListener()

        setAppBarActionListener()

        binding.diningListRefresh.setOnRefreshListener {
            viewModel.getDiningList()
        }

        viewModel.loadingStatus.observe(viewLifecycleOwner, {
            when (it) {
                LoadingStatus.SUCCESS -> {
                    // TODO: don't show on first load
                    Snackbar.make(
                        binding.diningListRefresh,
                        "List is up to date.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    binding.diningListRefresh.isRefreshing = false
                }
                LoadingStatus.ERROR -> {
                    Snackbar.make(
                        binding.diningListRefresh,
                        "Error updating data.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    binding.diningListRefresh.isRefreshing = false
                }
                LoadingStatus.IN_PROGRESS -> binding.diningListRefresh.isRefreshing = true
                else -> binding.diningListRefresh.isRefreshing = false
            }
        })
    }

    /**
     * Sets listener for dining list app bar actions: search and sort
     */
    private fun setAppBarActionListener() {

        binding.diningListAppbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.dining_list_search -> {
                    menuItem.expandActionView()
                    val searchView = menuItem.actionView as SearchView
                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String): Boolean {
                            return onQueryTextChange(query)
                        }

                        override fun onQueryTextChange(query: String): Boolean {
                            viewModel.updateSearchQuery(query)
                            return false
                        }
                    })
                    menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                        override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                            binding.diningListLayoutAppbar.layoutParams.height =
                                getAttr(R.attr.actionBarSize) * 2
                            binding.diningListRefresh.isEnabled = false
                            binding.diningListLayoutAppbar.setExpanded(false)
                            return true
                        }

                        override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                            binding.diningListLayoutAppbar.layoutParams.height =
                                resources.getDimensionPixelSize(R.dimen.app_bar_layout_large)
                            binding.diningListRefresh.isEnabled = true
                            binding.diningListLayoutAppbar.setExpanded(true)
                            return true
                        }
                    })
                    false
                }
                R.id.dining_list_sort_alpha -> {
                    viewModel.updateSortFilter(SortOrder.ALPHABETICAL)
                    false
                }
                R.id.dining_list_sort_crowd -> {
                    viewModel.updateSortFilter(SortOrder.CROWDEDNESS)
                    false
                }
                else -> false
            }
        }
    }

    /**
     * Handle click of a facility in the facility list RecyclerView
     */
    private fun onFacilityClick(facility: Facility) {
        val action =
            DiningListFragmentDirections.actionDiningListFragmentToDiningDetailFragment(
                facility.id, facility.displayName
            )
        this.findNavController().navigate(action)
    }

    /**
     * Set listeners for location filter chips
     * TODO: remove listener on destroy?
     */
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