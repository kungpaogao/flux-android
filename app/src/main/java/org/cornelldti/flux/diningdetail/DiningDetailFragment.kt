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
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.serialization.ExperimentalSerializationApi
import org.cornelldti.flux.R
import org.cornelldti.flux.databinding.DiningDetailFragmentBinding
import org.cornelldti.flux.network.AuthTokenState
import org.cornelldti.flux.util.DateTime
import org.cornelldti.flux.util.toISODateString
import org.cornelldti.flux.util.toTimeString
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

        return binding.root
    }

    @ExperimentalSerializationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeAuthState()

        /**
         * Set navigate up actions for app bar
         */
        binding.diningDetailAppbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.diningDetailAppbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.dining_detail_refresh -> {
                    // refresh info by fetching data again
                    viewModel.getDiningDetail()
                    true
                }
                else -> false
            }
        }

        /**
         * Observe last updated datetime to set last updated text
         */
        viewModel.lastUpdated.observe(viewLifecycleOwner, { date ->
            binding.textLastUpdated.text =
                getString(R.string.last_updated, date.toTimeString(context))
        })

        /**
         * Observe facility info to set hours in app bar subtitle
         */
        viewModel.info.observe(viewLifecycleOwner, { info ->
            binding.diningDetailAppbar.subtitle = when {
                info.isOpen -> {
                    val closingAt =
                        DateTime.fromMillisToTimeString(info.closingAt.times(1000), context)
                    HtmlCompat.fromHtml(
                        getString(R.string.closing_at, closingAt),
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                }
                info.nextOpen == -1L -> {
                    getText(R.string.closed_red)
                }
                else -> {
                    val nextOpen =
                        DateTime.fromMillisToTimeString(info.nextOpen.times(1000), context)
                    HtmlCompat.fromHtml(
                        getString(R.string.next_open, nextOpen),
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                }
            }
        })

        /**
         * Observe availability data to set information in availability card
         */
        viewModel.availability.observe(
            viewLifecycleOwner,
            { (string, color) ->
                binding.textAvailabilityNum.text = getString(string)
                binding.cardAvailability.setCardBackgroundColor(context?.let {
                    ContextCompat.getColor(
                        it,
                        color
                    )
                } ?: Color.TRANSPARENT)
            })

        setupDayChips()

        setupMenus()
    }

    /**
     * Sets ViewPager adapter and observes menus to set meal tabs and adapter data
     */
    private fun setupMenus() {
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

    /**
     * Sets text for date chips and adds listener to set menu based on day
     */
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