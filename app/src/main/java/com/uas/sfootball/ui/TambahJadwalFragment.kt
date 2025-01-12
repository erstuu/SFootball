package com.uas.sfootball.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.uas.sfootball.R
import com.uas.sfootball.databinding.FragmentTambahJadwalBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TambahJadwalFragment : Fragment() {

    private var _binding: FragmentTambahJadwalBinding? = null
    private val binding get() = _binding!!
    private var currentInput: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTambahJadwalBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupDatePicker()
        setupClockPicker()
        setupPhotoPicker()
        setupAction()
    }

    private val photoPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                when (currentInput) {
                    "club1" -> binding.tiLogoClub1.setText(selectedImageUri.toString())
                    "club2" -> binding.tiLogoClub2.setText(selectedImageUri.toString())
                }
            }
        }
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
                Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    binding.tiTanggal.setText(selectedDate)
                }
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
            currentInput = "club1"
            setPhotoPicker()
        }

        binding.tiLogoClub2.setOnClickListener {
            currentInput = "club2"
            setPhotoPicker()
        }
    }

    private fun setPhotoPicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        photoPickerLauncher.launch(intent)
    }

    private fun setupAction() {
        binding.btnSave.setOnClickListener {
            val club1 = binding.tiNameClub1.text.toString()
            val club2 = binding.tiNameClub2.text.toString()
            val tanggal = binding.tiTanggal.text.toString()
            val waktu = binding.tiWaktu.text.toString()
            val logoClub1 = binding.tiLogoClub1.text.toString()
            val logoClub2 = binding.tiLogoClub2.text.toString()

            if (club1.isEmpty() || club2.isEmpty() || tanggal.isEmpty() || waktu.isEmpty() || logoClub1.isEmpty() || logoClub2.isEmpty()) {
                showSnackbar(getString(R.string.form_harus_diisi_semua))
            } else {
                showSnackbar(getString(R.string.data_berhasil_ditambahkan), R.color.light_yellow)
            }
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