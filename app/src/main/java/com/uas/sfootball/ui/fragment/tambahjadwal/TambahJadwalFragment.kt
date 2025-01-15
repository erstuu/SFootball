package com.uas.sfootball.ui.fragment.tambahjadwal

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import com.uas.sfootball.R
import com.uas.sfootball.SFootballApplication
import com.uas.sfootball.ViewModelFactory
import com.uas.sfootball.databinding.FragmentTambahJadwalBinding
import com.uas.sfootball.helper.StorageHelper
import com.uas.sfootball.models.Dates
import com.uas.sfootball.models.Match
import com.uas.sfootball.models.MatchesWithDate
import com.uas.sfootball.models.Month
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TambahJadwalFragment : Fragment() {

    private var _binding: FragmentTambahJadwalBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TambahJadwalViewModel by viewModels {
        val repository = (requireActivity().application as SFootballApplication).repository
        ViewModelFactory(repository)
    }

    enum class CurrentInput {
        CLUB1, CLUB2
    }

    private lateinit var currentInput: CurrentInput
    private lateinit var logoClub1Uri: Uri
    private lateinit var logoClub2Uri: Uri

    private var selectedDay: String = ""
    private var selectedMonth: Month = Month.Januari
    private var selectedYear: String = ""
    private var selectedHour: String = ""
    private var selectedMinute: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTambahJadwalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupDatePicker()
        setupClockPicker()
        setupPhotoPicker()
        setupAction()
    }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            Log.d("URI", "$uri")
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
        }?: Log.d("URI", "Uri not found")
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar
        toolbar.isTitleCentered = true
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

    private fun setupPhotoPicker() {
        binding.tiLogoClub1.setOnClickListener {
            currentInput = CurrentInput.CLUB1
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.tiLogoClub2.setOnClickListener {
            currentInput = CurrentInput.CLUB2
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun setupAction() {
        binding.btnSave.setOnClickListener {
            val club1 = binding.tiNameClub1.text.toString()
            val club2 = binding.tiNameClub2.text.toString()
            val tanggal = binding.tiTanggal.text.toString()
            val waktu = binding.tiWaktu.text.toString()
            val logoClub1 = binding.tiLogoClub1.text.toString()
            val logoClub2 = binding.tiLogoClub2.text.toString()
            val stadium = binding.tiStadium.text.toString()

            when {
                club1.isEmpty() -> showSnackbar(getString(R.string.club1_name_required))
                club2.isEmpty() -> showSnackbar(getString(R.string.club2_name_required))
                tanggal.isEmpty() -> showSnackbar(getString(R.string.date_required))
                waktu.isEmpty() -> showSnackbar(getString(R.string.time_required))
                logoClub1.isEmpty() -> showSnackbar(getString(R.string.club1_logo_required))
                logoClub2.isEmpty() -> showSnackbar(getString(R.string.club2_logo_required))
                stadium.isEmpty() -> showSnackbar(getString(R.string.stadium_wajib_diisi))
                else -> {
                    val dates = Dates(
                        day = selectedDay,
                        month = selectedMonth,
                        year = selectedYear,
                        hour = selectedHour,
                        minute = selectedMinute
                    )

                    val match = Match(
                        nameHomeTeam = club1,
                        nameAwayTeam = club2,
                        logoHomeTeam = logoClub1Uri.toString(),
                        logoAwayTeam = logoClub2Uri.toString(),
                        dateId = 0,
                        stadium = stadium
                    )

                    val matchesWithDate = MatchesWithDate(dates, listOf(match))
                    viewModel.addMatch(matchesWithDate)
                    showSnackbar(getString(R.string.match_added_successfully), R.color.light_yellow)

                    clearForm()
                }
            }
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

        selectedDay = ""
        selectedMonth = Month.Januari
        selectedYear = ""
        selectedHour = ""
        selectedMinute = ""

        if (::logoClub1Uri.isInitialized) {
            logoClub1Uri = Uri.EMPTY
        }
        if (::logoClub2Uri.isInitialized) {
            logoClub2Uri = Uri.EMPTY
        }
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