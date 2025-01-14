package com.uas.sfootball.ui.fragment.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.uas.sfootball.SFootballApplication
import com.uas.sfootball.ViewModelFactory
import com.uas.sfootball.databinding.FragmentDetailMatchBinding

class DetailMatchFragment : Fragment() {

    private var _binding: FragmentDetailMatchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailMatchViewModel by viewModels {
        val repository = (requireActivity().application as SFootballApplication).repository
        ViewModelFactory(repository)
    }
    private val args: DetailMatchFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailMatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupToolbar()
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar
        toolbar.isTitleCentered = true
    }

    private fun setupView() {
        viewModel.getMatchById(args.matchId).observe(viewLifecycleOwner) {
            val date = "${it.date.day} ${it.date.month} ${it.date.year}"
            val hour = "${it.date.hour}:${it.date.minute}"
            binding.tvDate.text = date
            binding.tvClubNameHome.text = it.matches[0].nameHomeTeam
            binding.tvClubNameAway.text = it.matches[0].nameAwayTeam
            binding.tvScore.text = it.matches[0].score ?: hour
            Glide.with(requireContext())
                .load(it.matches[0].logoHomeTeam)
                .into(binding.cvClubLogoHome)
            Glide.with(requireContext())
                .load(it.matches[0].logoAwayTeam)
                .into(binding.cvClubLogoAway)
        }
    }

}