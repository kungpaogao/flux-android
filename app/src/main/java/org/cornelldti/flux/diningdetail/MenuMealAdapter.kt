package org.cornelldti.flux.diningdetail

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.cornelldti.flux.data.Menu

class MenuMealAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    var meals = listOf<Menu>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = meals.size

    override fun createFragment(position: Int): Fragment {
        return MenuMealFragment(meals[position])
    }

}