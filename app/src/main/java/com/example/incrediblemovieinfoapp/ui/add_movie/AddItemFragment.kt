package com.example.incrediblemovieinfoapp.ui.add_movie


import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.incrediblemovieinfoapp.data.models.Movie
import com.example.incrediblemovieinfoapp.R
import com.example.incrediblemovieinfoapp.databinding.AddItemLayoutBinding
import com.example.incrediblemovieinfoapp.ui.ActivityViewModel
import java.util.Calendar
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class AddItemFragment : Fragment(){
    private var _binding : AddItemLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel : ActivityViewModel by activityViewModels()
    private var _genres : String?=null
    private val genres get () = _genres!!

    private val pickImageLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            viewModel.setSelectedImageURI(it.toString())
            if (it!= null)
            requireActivity().contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddItemLayoutBinding.inflate(inflater,container,false)
        binding.btnAddMovie.setOnClickListener{
            if (isFormValid()) {
                val currentMovie = Movie(
                    binding.tvItemTitle.text.toString(),
                    binding.etMoviePlot.text.toString(),
                    (viewModel.selectedRuntimeHours.value!! * 60 + viewModel.selectedRuntimeMinutes.value!!),
                    viewModel.selectedYear.value!!,
                    binding.rbMovieRating.rating,
                    genres,
                    viewModel.selectedImageURI.value)
                viewModel.addMovie(currentMovie)
                findNavController().navigate(R.id.action_addItemFragment_to_allItemsFragment2)
            } else {
                showError("Please fill in all the required fields.")
            }
        }

        setupNumberPickers()

        binding.btnSelectYear.setOnClickListener {
            showYearPickerDialog()
        }

        binding.btnAddPhoto.setOnClickListener{
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        binding.npHoursPicker.setOnValueChangedListener { _, _, value ->
            viewModel.setSelectedRuntimeHours(value)
        }
        binding.npMinutesPicker.setOnValueChangedListener { _, _, value ->
            viewModel.setSelectedRuntimeMinutes(value)
        }

        viewModel.selectedYear.observe(requireActivity()){
            if (_binding != null) {
                if (binding.tvSelectedYear.text != null) {
                    binding.tvSelectedYear.text = viewModel.selectedYear.value.toString()
                }
            }
        }
        viewModel.selectedRuntimeHours.observe(requireActivity()) {
            if (_binding != null) {
                if (viewModel.selectedRuntimeHours.value == null) {
                    binding.npHoursPicker.value = 0
                } else
                    binding.npHoursPicker.value = viewModel.selectedRuntimeHours.value!!
            }
        }
        viewModel.selectedRuntimeMinutes.observe(requireActivity()){
            if (_binding != null) {
                if (viewModel.selectedRuntimeMinutes.value == null) {
                    binding.npMinutesPicker.value = 0
                } else
                    binding.npMinutesPicker.value = viewModel.selectedRuntimeMinutes.value!!
            }
        }

        viewModel.selectedImageURI.observe(requireActivity()){
            binding.ivSelectedImage.setImageURI(viewModel.selectedImageURI.value?.toUri())
        }

        return binding.root
    }


    private fun setupNumberPickers() {
        binding.npHoursPicker.minValue = 0
        binding.npHoursPicker.maxValue = 4
        binding.npHoursPicker.wrapSelectorWheel = true

        binding.npMinutesPicker.minValue = 0
        binding.npMinutesPicker.maxValue = 59
        binding.npMinutesPicker.wrapSelectorWheel = true
    }

    private fun showYearPickerDialog() {
        val numberPicker = NumberPicker(requireContext())
        numberPicker.minValue = 1900
        numberPicker.maxValue = Calendar.getInstance().get(Calendar.YEAR)
        numberPicker.value = Calendar.getInstance().get(Calendar.YEAR)

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.choose_year)
            .setView(numberPicker)
            .setPositiveButton(R.string.ok) { _, _ ->
                viewModel.setSelectedYear(numberPicker.value.toString().toInt())
                binding.tvSelectedYear.text = numberPicker.value.toString()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun getSelectedGenres(): String {
        val checkboxesToLabels = listOf(
            binding.checkboxDoco to getString(R.string.doco_label),
            binding.checkboxWar to getString(R.string.war_label),
            binding.checkboxDrama to getString(R.string.drama_label),
            binding.checkboxAction to getString(R.string.action_label),
            binding.checkboxFamily to getString(R.string.family_label),
            binding.checkboxRomance to getString(R.string.romance_label),
            binding.checkboxAdventure to getString(R.string.adventure_label),
            binding.checkboxAnimation to getString(R.string.animation_label),
            binding.checkboxScienceFiction to getString(R.string.science_fiction_label),
            binding.checkboxHorror to getString(R.string.horror_label),
            binding.checkboxThriller to getString(R.string.thriller_label)
        )

        return checkboxesToLabels
            .filter { it.first.isChecked }
            .joinToString(", ") { it.second }
    }



    private fun isFormValid(): Boolean {
        _genres = getSelectedGenres()
        return binding.tvItemTitle.text.toString().isNotEmpty() &&
                binding.etMoviePlot.text.toString().isNotEmpty() &&
                viewModel.selectedYear.value != null &&
                viewModel.selectedYear.value?.toString()!!.isNotEmpty() &&
                viewModel.selectedRuntimeHours.value  != null &&
                viewModel.selectedRuntimeHours.value!! >= 0 &&
                viewModel.selectedRuntimeMinutes.value != null &&
                viewModel.selectedRuntimeMinutes.value!! >= 0 &&
                binding.rbMovieRating.rating > 0 &&
                genres.isNotEmpty()
    }



    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}