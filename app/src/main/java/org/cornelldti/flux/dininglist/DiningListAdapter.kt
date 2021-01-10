package org.cornelldti.flux.dininglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

        fun bind(
            item: Facility,
            itemListener: FacilityListener,
        ) {
            binding.facilityId.text = item.id
            binding.facilityName.text = item.name
            itemView.setOnClickListener {
                itemListener.onClick(item)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DiningListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    interface FacilityListener {
        fun onClick(facility: Facility)
    }


}