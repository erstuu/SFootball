package com.uas.sfootball.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uas.sfootball.R
import com.uas.sfootball.databinding.ItemViewDateBinding
import com.uas.sfootball.helper.SmoothScrollHelper
import com.uas.sfootball.models.MDate
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class DateOfMatchAdapter(
    private val days: List<String>,
    private val dates: List<Int>,
    private val date: MDate,
    private val onItemClickListener: (String, Int, String) -> Unit
) : RecyclerView.Adapter<DateOfMatchAdapter.DateViewHolder>() {

    private var selectedPosition = -1

    init {
        setCurrentDateAsSelected()
    }

    inner class DateViewHolder(private val binding: ItemViewDateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(day: String, dateName: Int, date: MDate, isSelected: Boolean) {
            binding.tvDay.text = day
            binding.tvDate.text = dateName.toString()

            if (isSelected) {
                binding.tvDay.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                binding.tvDate.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
            } else {
                binding.tvDay.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey))
                binding.tvDate.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey))
            }

            binding.root.setOnClickListener {
                val previousSelectedPosition = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(previousSelectedPosition)
                notifyItemChanged(selectedPosition)

                onItemClickListener(dateName.toString(), date.month, date.year)

                val itemWidth = binding.root.resources.getDimensionPixelSize(R.dimen.item_width)
                val offset = SmoothScrollHelper.calculateOffset(binding.root.context, itemWidth)
                (binding.root.parent as RecyclerView).layoutManager?.let { layoutManager ->
                    if (layoutManager is LinearLayoutManager) {
                        layoutManager.scrollToPositionWithOffset(adapterPosition, offset)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val binding = ItemViewDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val dateNames = dates[position]
        val day = days[position]
        val date = date
        val isSelected = position == selectedPosition
        holder.bind(day, dateNames, date, isSelected)
    }

    override fun getItemCount(): Int = dates.size

    fun getSelectedPosition(): Int {
        return selectedPosition
    }

    fun setSelectedPosition(position: Int) {
        val previousSelectedPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousSelectedPosition)
        notifyItemChanged(selectedPosition)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setCurrentDateAsSelected() {
        val currentDate = LocalDate.now()
        val currentDayOfMonth = currentDate.dayOfMonth

        selectedPosition = dates.indexOfFirst { it == currentDayOfMonth }
    }
}