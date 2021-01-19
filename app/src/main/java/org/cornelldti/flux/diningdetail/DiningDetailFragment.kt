package org.cornelldti.flux.diningdetail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.serialization.ExperimentalSerializationApi
import org.cornelldti.flux.R
import org.cornelldti.flux.databinding.DiningDetailFragmentBinding
import org.cornelldti.flux.network.AuthTokenState
import org.cornelldti.flux.util.DateTime
import org.cornelldti.flux.util.toISODateString
import java.util.*

class DiningDetailFragment : Fragment() {

    private lateinit var viewModel: DiningDetailViewModel
    private lateinit var viewModelFactory: DiningDetailViewModelFactory

    private lateinit var binding: DiningDetailFragmentBinding

    private lateinit var tabLayoutMediator: TabLayoutMediator

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

        return binding.root
    }

    @ExperimentalSerializationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeAuthState()

        viewModel.response.observe(viewLifecycleOwner, { response ->
            binding.diningDetailResponse.text = response
        })

        viewModel.availability.observe(viewLifecycleOwner, { (string, color) ->
            binding.textAvailabilityNum.text = getString(string)
            binding.cardAvailability.setCardBackgroundColor(context?.let {
                ContextCompat.getColor(
                    it,
                    color
                )
            } ?: Color.TRANSPARENT)
        })

        setupDayChips()

        val adapter = MenuMealAdapter(this)
        val viewPager = binding.viewpagerMenu
        viewPager.adapter = adapter

        viewModel.menu.observe(viewLifecycleOwner, { meals ->
            // remove previous instance of tabLayoutMediator if it exists
            if (this::tabLayoutMediator.isInitialized) tabLayoutMediator.detach()
            adapter.meals = meals
            tabLayoutMediator =
                TabLayoutMediator(binding.tabsMenuMeal, viewPager) { tab, position ->
                    Log.i(TAG, "Setup tab $position")
                    tab.text = meals[position].description.toString()
                }
            tabLayoutMediator.attach()
        })
    }

    private fun setupDayChips() {
        val dayChipGroup = binding.groupDayChips
        for (i in 0..6) {
            val dayCalendar = DateTime.getOffsetFromToday(i)
            val dayChip = dayChipGroup[i] as RadioButton
            val dateDay =
                DateTime.getDayAbbrev(dayCalendar.get(Calendar.DAY_OF_WEEK))
            val dateNumber = dayCalendar.get(Calendar.DATE)
            dayChip.text = HtmlCompat.fromHtml(
                "${dateDay}<br><br><b>${dateNumber}</b>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            dayChip.contentDescription = dayCalendar.time.toISODateString()
        }
        dayChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val selected = group.findViewById<RadioButton>(checkedId)
            Log.i(TAG, "Clicked date ${selected.contentDescription}")
            viewModel.setMenuDay(selected.contentDescription.toString())
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