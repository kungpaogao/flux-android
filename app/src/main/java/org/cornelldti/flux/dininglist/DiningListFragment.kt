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
import org.cornelldti.flux.R
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

        Log.i("DiningListFragment", "Called ViewModelProvider.get")
        viewModel = ViewModelProvider(this).get(DiningListViewModel::class.java)

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.dining_list_fragment, container, false)

        val adapter = DiningListAdapter(FacilityListener { facilityId ->
            onFacilityClick(facilityId)
        })

        binding.diningList.adapter = adapter

        viewModel.data.observe(
            viewLifecycleOwner,
            {
                adapter.data = it
            })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthState()
    }

    private fun onFacilityClick(facilityId: String) {
        Toast.makeText(context, "Clicked: $facilityId", Toast.LENGTH_SHORT).show()
        val action =
            DiningListFragmentDirections.actionDiningListFragmentToDiningDetailFragment(
                facilityId
            )
        this.findNavController().navigate(action)
    }

    private fun observeAuthState() {
        viewModel.tokenAcquired.observe(viewLifecycleOwner, { token ->
            when (token) {
                AuthTokenState.ACQUIRED -> {
                    Log.i("DiningListFragment", "Authentication success")
                    viewModel.fetchDiningList()
                    // TODO: fix fetching multiple times
                }
                else -> {
                    Log.w("DiningListFragment", "Authentication failure")
                }
            }
        })
    }
}