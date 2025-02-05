package com.uas.sfootball.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uas.sfootball.databinding.ItemViewMatchBinding
import com.uas.sfootball.models.Dates
import com.uas.sfootball.models.Match
import com.uas.sfootball.models.MatchesWithDate
import com.uas.sfootball.ui.fragment.home.HomeFragmentDirections

class MatchClubAdapter : RecyclerView.Adapter<MatchClubAdapter.ViewHolder>() {

    private var flattenedMatches: List<Pair<Match, Dates>> = emptyList()
    private val listMatch = mutableListOf<MatchesWithDate>()

    fun updateList(newList: List<MatchesWithDate>) {
        listMatch.clear()
        listMatch.addAll(newList)
        flattenedMatches = listMatch.flatMap { matchesWithDate ->
            matchesWithDate.matches.map { match ->
                match to matchesWithDate.date
            }
        }
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
                Glide.with(cvClubLogoHome.context)
                    .load(match.logoHomeTeam)
                    .into(cvClubLogoHome)

                Glide.with(cvClubLogoAway.context)
                    .load(match.logoAwayTeam)
                    .into(cvClubLogoAway)
            }

            binding.root.setOnClickListener {
                val toDetailMatchFragment = HomeFragmentDirections.actionHomeFragmentToDetailMatchFragment(match.id)
                binding.root.findNavController().navigate(toDetailMatchFragment)
            }
        }
    }
}