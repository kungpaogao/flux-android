package org.cornelldti.flux.diningdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.cornelldti.flux.R
import org.cornelldti.flux.data.Menu
import org.cornelldti.flux.databinding.MenuMealFragmentBinding
import org.cornelldti.flux.util.DateTime

class MenuMealFragment() : Fragment() {

    private lateinit var binding: MenuMealFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.menu_meal_fragment, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_MENU) }?.apply {
            val adapter = MenuListAdapter()
            binding.listMenuItems.adapter = adapter
            val menu = Json.decodeFromString<Menu>(getString(ARG_MENU).toString())
            adapter.data = menu.menu
        }
        arguments?.takeIf { it.containsKey(ARG_HOURS) }?.apply {
            Log.i(TAG, "Setting ARG_HOURS")
            val times = getLongArray(ARG_HOURS)
            val startTime =
                times?.get(0)?.times(1000)?.let { DateTime.fromMillisToTimeString(it, context) }
            val endTime =
                times?.get(1)?.times(1000)?.let { DateTime.fromMillisToTimeString(it, context) }

            binding.textOpenHours.text = getString(R.string.open_hours, startTime, endTime)
        }

    }

    companion object {
        const val TAG = "MenuMealFragment"
        const val ARG_MENU = "menu"
        const val ARG_HOURS = "hours"
    }
}