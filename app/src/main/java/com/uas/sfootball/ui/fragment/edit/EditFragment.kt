package com.uas.sfootball.ui.fragment.edit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.uas.sfootball.R
import com.uas.sfootball.SFootballApplication
import com.uas.sfootball.ViewModelFactory
import com.uas.sfootball.databinding.FragmentEditBinding
import com.uas.sfootball.helper.StorageHelper
import com.uas.sfootball.models.Dates
import com.uas.sfootball.models.Match
import com.uas.sfootball.models.MatchesWithDate
import com.uas.sfootball.models.Month
import com.uas.sfootball.ui.fragment.home.HomeViewModel
import com.uas.sfootball.ui.fragment.tambahjadwal.TambahJadwalFragment.CurrentInput
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditFragment : Fragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private val args: EditFragmentArgs by navArgs()

    private val viewModel: HomeViewModel by viewModels {
        val repository = (requireActivity().application as SFootballApplication).repository
        ViewModelFactory(repository)
    }

    private lateinit var currentInput: CurrentInput
    private var logoClub1Uri: Uri? = null
    private var logoClub2Uri: Uri? = null

    private var selectedDay: String? = null
    private var selectedMonth: Month? = null
    private var selectedYear: String? = null
    private var selectedHour: String? = null
    private var selectedMinute: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupDatePicker()
        setupClockPicker()
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

    private fun setupObserver() {
        viewModel.getMatchById(args.matchId).observe(viewLifecycleOwner) {
            setupView(it)
            setupAction(it)
        }
    }

    private fun setupView(matchesWithDate: MatchesWithDate) {
        val tanggal = "${matchesWithDate.date.day}/${matchesWithDate.date.month}/${matchesWithDate.date.year}"
        val waktu = "${matchesWithDate.date.hour}:${matchesWithDate.date.minute}"
        binding.tiNameClub1.setText(matchesWithDate.matches[0].nameHomeTeam)
        binding.tiNameClub2.setText(matchesWithDate.matches[0].nameAwayTeam)
        binding.tiTanggal.setText(tanggal)
        binding.tiWaktu.setText(waktu)
        binding.tiLogoClub1.setText(matchesWithDate.matches[0].logoHomeTeam)
        binding.tiLogoClub2.setText(matchesWithDate.matches[0].logoAwayTeam)
        binding.tiStadium.setText(matchesWithDate.matches[0].stadium)
        matchesWithDate.matches[0].score?.let { itScore ->
            binding.tiScore.setText(itScore)
        }
    }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            try {
                val savedImageUri = StorageHelper.saveImageToStorage(requireContext(), it)
                val fileName = savedImageUri?.lastPathSegment?.substringAfterLast('/')
                if (savedImageUri != null) {
                    when (currentInput) {
                        CurrentInput.CLUB1 -> {
                            binding.tiLogoClub1.setText(fileName)
                            logoClub1Uri = savedImageUri
                        }
                        CurrentInput.CLUB2 -> {
                            binding.tiLogoClub2.setText(fileName)
                            logoClub2Uri = savedImageUri
                        }
                    }
                } else {
                    showSnackbar(getString(R.string.failed_to_save_image))
                }
            } catch (e: Exception) {
                showSnackbar(getString(R.string.error_saving_image))
            }
        }
    }

    private fun setupDatePicker() {
        val datePicker = binding.tiTanggal

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                this.selectedYear = selectedYear.toString()
                this.selectedMonth = Month.entries.toTypedArray()[selectedMonth]
                this.selectedDay = selectedDay.toString()
                binding.tiTanggal.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
            },
            year, month, day
        )

        datePicker.setOnClickListener {
            datePickerDialog.show()
        }
    }

    private fun setupClockPicker() {
        val clockPicker = binding.tiWaktu

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                this.selectedHour = selectedHour.toString()
                this.selectedMinute = selectedMinute.toString()
                Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                    val selectedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(time)
                    binding.tiWaktu.setText(selectedTime)
                }
            },
            hour, minute, true
        )

        clockPicker.setOnClickListener {
            timePickerDialog.show()
        }
    }

    private fun setupAction(matchWithDate: MatchesWithDate) {
        binding.btnSave.setOnClickListener {
            val club1 = binding.tiNameClub1.text.toString()
            val club2 = binding.tiNameClub2.text.toString()
            val tanggal = binding.tiTanggal.text.toString()
            val waktu = binding.tiWaktu.text.toString()
            val logoClub1 = binding.tiLogoClub1.text.toString()
            val logoClub2 = binding.tiLogoClub2.text.toString()
            val stadium = binding.tiStadium.text.toString()
            val score = binding.tiScore.text.toString()

            when {
                club1.isBlank() -> showSnackbar(getString(R.string.club1_name_required))
                club2.isBlank() -> showSnackbar(getString(R.string.club2_name_required))
                tanggal.isBlank() -> showSnackbar(getString(R.string.date_required))
                waktu.isBlank() -> showSnackbar(getString(R.string.time_required))
                logoClub1.isBlank() -> showSnackbar(getString(R.string.club1_logo_required))
                logoClub2.isBlank() -> showSnackbar(getString(R.string.club2_logo_required))
                stadium.isBlank() -> showSnackbar(getString(R.string.stadium_wajib_diisi))
                else -> {
                    val dates = Dates(
                        id = matchWithDate.date.id,
                        day = selectedDay ?: matchWithDate.date.day,
                        month = selectedMonth ?: matchWithDate.date.month,
                        year = selectedYear ?: matchWithDate.date.year,
                        hour = selectedHour ?: matchWithDate.date.hour,
                        minute = selectedMinute ?: matchWithDate.date.minute
                    )

                    val match = Match(
                        nameHomeTeam = club1,
                        nameAwayTeam = club2,
                        logoHomeTeam = logoClub1Uri?.toString() ?: matchWithDate.matches[0].logoHomeTeam,
                        logoAwayTeam = logoClub2Uri?.toString() ?: matchWithDate.matches[0].logoAwayTeam,
                        dateId = matchWithDate.matches[0].dateId,
                        stadium = stadium,
                        score = score.ifBlank { null }
                    )

                    val matchesWithDate = MatchesWithDate(dates, listOf(match))
                    viewModel.updateMatchesAndDateById(matchWithDate.matches[0].id, matchesWithDate)
                    showSnackbar(getString(R.string.match_added_successfully), R.color.light_yellow)

                    clearForm()
                    findNavController().navigateUp()
                }
            }
        }

        binding.tiLogoClub1.setOnClickListener {
            currentInput = CurrentInput.CLUB1
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.tiLogoClub2.setOnClickListener {
            currentInput = CurrentInput.CLUB2
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnDeleteMatch.setOnClickListener {
            viewModel.deleteMatch(matchWithDate)
            val toHomeFragment = EditFragmentDirections.actionEditFragmentToHomeFragment()
            findNavController().navigate(toHomeFragment)
        }
    }

    private fun clearForm() {
        binding.tiNameClub1.text?.clear()
        binding.tiNameClub2.text?.clear()
        binding.tiTanggal.text?.clear()
        binding.tiWaktu.text?.clear()
        binding.tiLogoClub1.text?.clear()
        binding.tiLogoClub2.text?.clear()
        binding.tiStadium.text?.clear()
        binding.tiScore.text?.clear()

        selectedDay = ""
        selectedMonth = Month.Januari
        selectedYear = ""
        selectedHour = ""
        selectedMinute = ""
    }

    private fun showSnackbar(message: String, color: Int = R.color.red) {
        val textColor = ContextCompat.getColor(requireContext(), if (color == R.color.red) R.color.white else R.color.black)

        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).apply {
            setBackgroundTint(ContextCompat.getColor(requireContext(), color))
            setTextColor(textColor)
            setActionTextColor(textColor)
            anchorView = binding.btnSave
            setAction(getString(R.string.ok)) {
                dismiss()
            }
        }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}