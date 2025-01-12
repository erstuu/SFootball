package com.uas.sfootball.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uas.sfootball.databinding.ItemViewDateBinding
import com.uas.sfootball.models.Dates

class DateOfMatchAdapter(
    private val dateDayList: List<Pair<String, String>>,
    private val onDateSelected: (Int) -> Unit
) : RecyclerView.Adapter<DateOfMatchAdapter.DateViewHolder>() {

    private var selectedPosition: Int = -1

    inner class DateViewHolder(private val binding: ItemViewDateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(date: String, day: String, isSelected: Boolean) {
            binding.tvDay.text = day
            binding.tvDate.text = date
            binding.root.isSelected = isSelected
            binding.root.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = adapterPosition

                // Notify item change for previous and new positions
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)

                // Trigger the callback
                onDateSelected(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val binding = ItemViewDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val (date, day) = dateDayList[position]
        holder.bind(date, day, position == selectedPosition)
    }

    override fun getItemCount(): Int = dateDayList.size
}
