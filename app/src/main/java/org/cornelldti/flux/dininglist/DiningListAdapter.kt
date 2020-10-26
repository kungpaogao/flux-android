package org.cornelldti.flux.dininglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.cornelldti.flux.R
import org.cornelldti.flux.data.Facility

class DiningListAdapter(
    private var data: List<Facility>,
    private val itemListener: FacilityListener
) :
    RecyclerView.Adapter<DiningListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_dining_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.facilityId.text = item.id
        holder.facilityName.text = item.name
        holder.itemView.setOnClickListener {
            itemListener.onClick(item)
        }
    }

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val facilityId: TextView = itemView.findViewById(R.id.facility_id)
        val facilityName: TextView = itemView.findViewById(R.id.facility_name)
    }

    interface FacilityListener {
        fun onClick(facility: Facility)
    }
}