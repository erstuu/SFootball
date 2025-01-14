package com.uas.sfootball.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uas.sfootball.databinding.ItemViewMatchBinding
import com.uas.sfootball.models.Dates
import com.uas.sfootball.models.Match
import com.uas.sfootball.models.MatchesWithDate

class MatchClubAdapter(private val listMatch: MutableList<MatchesWithDate>) : RecyclerView.Adapter<MatchClubAdapter.ViewHolder>() {

    private var flattenedMatches: List<Pair<Match, Dates>> = emptyList()

    fun updateList(newList: List<MatchesWithDate>) {
        listMatch.clear()
        listMatch.addAll(newList)
        flattenedMatches = listMatch.flatMap { matchesWithDate ->
            matchesWithDate.matches.map { match ->
                match to matchesWithDate.date
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemViewMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (match, date) = flattenedMatches[position]
        holder.bind(match, date)
    }

    override fun getItemCount(): Int = flattenedMatches.size

    inner class ViewHolder(private val binding: ItemViewMatchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(match: Match, date: Dates) {
            val hour = "${date.hour}:${date.minute}"
            with(binding) {
                if (match.score != null) {
                    tvDateTime.text = match.score
                } else {
                    tvDateTime.text = hour
                }
                tvClubNameHome.text = match.nameHomeTeam
                tvClubNameAway.text = match.nameAwayTeam
                cvClubLogoHome.setImageResource(match.logoHomeTeam)
                cvClubLogoAway.setImageResource(match.logoAwayTeam)
            }
        }
    }
}