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
import org.cornelldti.flux.R
import org.cornelldti.flux.data.Facility
import org.cornelldti.flux.databinding.DiningListFragmentBinding

/**
 * A simple [Fragment] subclass.
 * Use the [DiningListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiningListFragment : Fragment(), DiningListAdapter.FacilityListener {

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

        val data = viewModel.data

        val recyclerView = binding.diningList
        recyclerView.adapter = DiningListAdapter(data, this)

        return binding.root
    }

    override fun onClick(facility: Facility) {
        Toast.makeText(context, "Clicked: ${facility.id}", Toast.LENGTH_SHORT).show()
    }
}