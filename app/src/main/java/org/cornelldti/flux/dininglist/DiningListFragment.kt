package org.cornelldti.flux.dininglist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.cornelldti.flux.R
import org.cornelldti.flux.data.Facility

/**
 * A simple [Fragment] subclass.
 * Use the [DiningListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiningListFragment : Fragment(), DiningListAdapter.FacilityListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_dining_list, container, false)

        val data = listOf(
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

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.dining_list)
        recyclerView.adapter = DiningListAdapter(data, this)

        return rootView
    }

    override fun onClick(facility: Facility) {
        Toast.makeText(context, "Clicked: ${facility.id}", Toast.LENGTH_LONG).show()
    }
}