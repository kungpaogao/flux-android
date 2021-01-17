package org.cornelldti.flux.diningdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import org.cornelldti.flux.R
import org.cornelldti.flux.data.Menu
import org.cornelldti.flux.databinding.MenuMealFragmentBinding

class MenuMealFragment(val menu: Menu) : Fragment() {

    private lateinit var binding: MenuMealFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.menu_meal_fragment, container, false
        )

        val adapter = MenuListAdapter()
        binding.listMenuItems.adapter = adapter
        adapter.data = menu.menu

        return binding.root
    }
}