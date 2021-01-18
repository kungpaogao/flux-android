package org.cornelldti.flux.diningdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.cornelldti.flux.data.Menu

class MenuMealAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    var meals = listOf<Menu>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = meals.size

    override fun createFragment(position: Int): Fragment {
        val fragment = MenuMealFragment()
        fragment.arguments = Bundle().apply {
            putString(MenuMealFragment.ARG_MENU, Json.encodeToString(meals[position]))
        }
        return fragment
    }

}