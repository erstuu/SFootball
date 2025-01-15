package com.uas.sfootball.ui.fragment.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.uas.sfootball.R
import com.uas.sfootball.SFootballApplication
import com.uas.sfootball.ViewModelFactory
import com.uas.sfootball.databinding.FragmentDetailMatchBinding
import com.uas.sfootball.helper.DatePickerHelper

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
        toolbar.setNavigationIcon(R.drawable.ic_arrow_30)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupView() {
        viewModel.getMatchById(args.matchId).observe(viewLifecycleOwner) { matchWithDate ->
            if (matchWithDate.matches.isNotEmpty()) {
                val match = matchWithDate.matches[0]
                val date = "${matchWithDate.date.day} ${matchWithDate.date.month} ${matchWithDate.date.year}"
                val hour = "${matchWithDate.date.hour}:${matchWithDate.date.minute}"
                binding.tvDate.text = date
                binding.tvClubNameHome.text = match.nameHomeTeam
                binding.tvClubNameAway.text = match.nameAwayTeam
                binding.tvScore.text = match.score ?: hour
                binding.tvStadiumDetail.text = match.stadium ?: "-"
                Glide.with(requireContext())
                    .load(match.logoHomeTeam)
                    .into(binding.cvClubLogoHome)
                Glide.with(requireContext())
                    .load(match.logoAwayTeam)
                    .into(binding.cvClubLogoAway)
            } else {
                Toast.makeText(requireContext(), "No match data found", Toast.LENGTH_SHORT).show()
            }

            binding.btnEditMatch.setOnClickListener {
                val toEditFragment = DetailMatchFragmentDirections.actionDetailMatchFragmentToEditFragment(args.matchId)
                findNavController().navigate(toEditFragment)
            }
        }
    }

}