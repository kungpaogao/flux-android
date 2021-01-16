package org.cornelldti.flux.dininglist

import android.graphics.PorterDuff
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.cornelldti.flux.R
import org.cornelldti.flux.data.Facility
import org.cornelldti.flux.databinding.DiningListItemBinding

class DiningListAdapter(
    private val itemListener: FacilityListener
) :
    RecyclerView.Adapter<DiningListAdapter.ViewHolder>() {

    var data = listOf<Facility>()
        set(value) {
            // TODO: add DiffUtil to avoid refreshing when not necessary
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, itemListener)
    }

    override fun getItemCount() = data.size

    class ViewHolder private constructor(private val binding: DiningListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context

        fun bind(
            item: Facility,
            itemListener: FacilityListener,
        ) {
            binding.facilityName.text = item.displayName
            binding.facilityDensity.text = context.getString(item.densityString)
            setPills(item)

            itemView.setOnClickListener {
                it.performHapticFeedback(
                    HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING  // Ignore device's setting. Otherwise, you can use FLAG_IGNORE_VIEW_SETTING to ignore view's setting.
                )
                itemListener.onClick(item)
            }
        }

        private fun setPills(item: Facility) {
            val pills =
                listOf(binding.firstPill, binding.secondPill, binding.thirdPill, binding.fourthPill)
            pills.map { pill ->
                pill.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.flux_grey_light
                    ), PorterDuff.Mode.SRC_ATOP
                )
            }
            if (item.isOpen) {
                val color = when (item.density) {
                    0 -> R.color.flux_green
                    1 -> R.color.flux_yellow
                    2 -> R.color.flux_orange
                    3 -> R.color.flux_red
                    else -> R.color.flux_grey_light
                }
                pills.slice(0..item.density).map { pill ->
                    pill.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            color
                        ), PorterDuff.Mode.SRC_ATOP
                    )
                }
            }
        }

        companion object {
            const val TAG = "DiningListAdapter.ViewHolder"

            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DiningListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}

class FacilityListener(val clickListener: (facility: Facility) -> Unit) {
    fun onClick(facility: Facility) = clickListener(facility)
}