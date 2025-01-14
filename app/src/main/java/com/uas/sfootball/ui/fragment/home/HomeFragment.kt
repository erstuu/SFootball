package com.uas.sfootball.ui.fragment.home

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.uas.sfootball.R
import com.uas.sfootball.SFootballApplication
import com.uas.sfootball.ViewModelFactory
import com.uas.sfootball.adapter.DateOfMatchAdapter
import com.uas.sfootball.adapter.MatchClubAdapter
import com.uas.sfootball.databinding.FragmentHomeBinding
import com.uas.sfootball.models.MatchesWithDate
import java.util.Calendar
import com.uas.sfootball.helper.DatePickerHelper
import com.uas.sfootball.helper.SmoothScrollHelper
import com.uas.sfootball.models.MDate
import com.uas.sfootball.ui.fragment.detail.DetailMatchFragment

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupDatePicker()
        setupMatchSchedule()
        setupCurrentMonthDates()
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar
        toolbar.isTitleCentered = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedMonthName = DatePickerHelper.getMonthName(selectedMonth)
                val allDatesInMonth = DatePickerHelper.getAllDatesInMonth(selectedYear, selectedMonth)
                val selectedMonthEnum = DatePickerHelper.getMonthByNum(selectedMonth)

                val date = MDate(
                    days = allDatesInMonth.map { it.day },
                    dayNumbers = allDatesInMonth.map { it.dayNumber },
                    month = selectedMonth,
                    year = selectedYear.toString()
                )

                setupDateRecyclerView(date)

                viewModel.getMatchesByDate(
                    selectedDay.toString(),
                    selectedMonthEnum,
                    selectedYear.toString()
                ).observe(viewLifecycleOwner) { matches ->
                    if (matches.isEmpty()) {
                        binding.tvCompe.visibility = View.GONE
                        showSnackbar(getString(R.string.belum_ada_jadwal), R.color.light_yellow)
                    }
                    binding.tvMonth.text = selectedMonthName
                    binding.tvYear.text = selectedYear.toString()
                    setupMatchesRecyclerView(matches)
                }

                val selectedPosition = date.dayNumbers.indexOf(selectedDay)
                if (selectedPosition != -1) {
                    (binding.rvDate.adapter as DateOfMatchAdapter).setSelectedPosition(selectedPosition)

                    val itemWidth = resources.getDimensionPixelSize(R.dimen.item_width)
                    val offset = SmoothScrollHelper.calculateOffset(requireContext(), itemWidth)
                    (binding.rvDate.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(selectedPosition, offset)
                }
            },
            year, month, day
        )

        setupAction(datePickerDialog)
    }

    private fun setupAction(datePickerDialog: DatePickerDialog) {
        val datePicker = binding.btnPickDate
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

    private fun setupMatchSchedule() {
        val dateNow = Calendar.getInstance()
        val dayNow = dateNow.get(Calendar.DAY_OF_MONTH)
        val monthNow = dateNow.get(Calendar.MONTH)
        val yearNow = dateNow.get(Calendar.YEAR)

        val day = dayNow.toString()
        val month = DatePickerHelper.getMonthByNum(monthNow)
        val year = yearNow.toString()

        viewModel.getMatchesByDate(day, month, year).observe(viewLifecycleOwner) { matches ->
            if (matches.isEmpty()) {
                binding.tvCompe.visibility = View.GONE
                showSnackbar(getString(R.string.belum_ada_jadwal), R.color.light_yellow)
            }
            setupMatchesRecyclerView(matches)
        }
    }

    private fun setupMatchesRecyclerView(matches: MutableList<MatchesWithDate>) {
        val adapterMatch = MatchClubAdapter()
        with(binding.rvMatch) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            this.adapter = adapterMatch
        }
        adapterMatch.updateList(matches)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupCurrentMonthDates() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)

        val allDatesInMonth = DatePickerHelper.getAllDatesInMonth(year, month)

        val date = MDate(
            days = allDatesInMonth.map { it.day },
            dayNumbers = allDatesInMonth.map { it.dayNumber },
            month = month,
            year = year.toString()
        )

        setupDateRecyclerView(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupDateRecyclerView(date: MDate) {
        val adapterDate = DateOfMatchAdapter(date.days, date.dayNumbers, date) { selectedDate, selectedMonth, selectedYear ->
            val monthName = DatePickerHelper.getMonthByNum(selectedMonth)
            viewModel.getMatchesByDate(
                selectedDate,
                monthName,
                selectedYear
            ).observe(viewLifecycleOwner) { matches ->
                if (matches.isEmpty()) {
                    binding.tvCompe.visibility = View.GONE
                    showSnackbar(getString(R.string.belum_ada_jadwal), R.color.light_yellow)
                }
                setupMatchesRecyclerView(matches)
            }
        }
        with(binding.rvDate) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            this.adapter = adapterDate

            val selectedPosition = adapterDate.getSelectedPosition()
            if (selectedPosition != -1) {
                val itemWidth = resources.getDimensionPixelSize(R.dimen.item_width)
                val offset = SmoothScrollHelper.calculateOffset(requireContext(), itemWidth)
                (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(selectedPosition, offset)

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}