package com.uas.sfootball.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uas.sfootball.databinding.ItemViewMatchBinding
import com.uas.sfootball.models.Match

class MatchClubAdapter(private val listMatch: List<Match>) : RecyclerView.Adapter<MatchClubAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemViewMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listMatch[position])
    }

    override fun getItemCount(): Int = listMatch.size

    inner class ViewHolder(private val binding: ItemViewMatchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(match: Match) {
            with(binding) {
                tvClubNameHome.text = match.nameHomeTeam
                tvClubNameAway.text = match.nameAwayTeam
                cvClubLogoHome.setImageResource(match.logoHomeTeam)
                cvClubLogoAway.setImageResource(match.logoAwayTeam)
            }
        }
    }
}