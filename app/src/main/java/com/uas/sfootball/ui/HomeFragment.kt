package com.uas.sfootball.ui

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.uas.sfootball.R
import com.uas.sfootball.SFootballApplication
import com.uas.sfootball.ViewModelFactory
import com.uas.sfootball.adapter.MatchClubAdapter
import com.uas.sfootball.databinding.FragmentHomeBinding
import com.uas.sfootball.models.MatchesWithDate
import java.util.Calendar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels {
        val repository = (requireActivity().application as SFootballApplication).repository
        ViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupDatePicker()
        setupObserver()
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar
        toolbar.isTitleCentered = true
    }

    private fun setupDatePicker() {
        val datePicker = binding.btnPickDate

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                    val selectedDayName = when (get(Calendar.DAY_OF_WEEK)) {
                        Calendar.MONDAY -> "Senin"
                        Calendar.TUESDAY -> "Selasa"
                        Calendar.WEDNESDAY -> "Rabu"
                        Calendar.THURSDAY -> "Kamis"
                        Calendar.FRIDAY -> "Jumat"
                        Calendar.SATURDAY -> "Sabtu"
                        Calendar.SUNDAY -> "Minggu"
                        else -> ""
                    }
                    val selectedDateWithDay = "$selectedDayName, $selectedYear"
                    showSnackbar(selectedDateWithDay)
                }
            },
            year, month, day
        )

        datePicker.setOnClickListener {
            datePickerDialog.show()
        }
    }

    private fun showSnackbar(errorMessage: String, color: Int = R.color.light_yellow) {
        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT).apply {
            setBackgroundTint(ContextCompat.getColor(requireContext(), color))
            setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            setActionTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            anchorView = requireActivity().findViewById(R.id.bottom_navigation)
            setAction(getString(R.string.ok)) {
                dismiss()
            }
        }.show()
    }

    private fun setupObserver() {
        viewModel.getDatesWithMatches().observe(viewLifecycleOwner) { datesWithMatches ->
            setupRecyclerView(datesWithMatches)
        }
    }

    private fun setupRecyclerView(matches: List<MatchesWithDate>) {
        val adapterMatch = MatchClubAdapter(matches)
        with(binding.rvMatch) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            this.adapter = adapterMatch
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}