package com.example.incrediblemovieinfoapp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ClipData.Item
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.incrediblemovieinfoapp.databinding.AddItemLayoutBinding
import java.util.Calendar
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class AddItemFragment : Fragment(){
    private var _binding : AddItemLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel : ActivityViewModel by activityViewModels()
    private var imageUri: Uri?= null
    private lateinit var currentMovie : Movie

    private val pickImageLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            binding.ivSelectedImage.setImageURI((it))
            requireActivity().contentResolver.takePersistableUriPermission(it!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            imageUri = it
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddItemLayoutBinding.inflate(inflater,container,false)
        currentMovie = Movie()
        binding.btnAddMovie.setOnClickListener{
            if (isFormValid()) {
                currentMovie.movieTitle = binding.tvItemTitle.text.toString()
                currentMovie.movieGenres = getSelectedGenres()
                currentMovie.movieRate = binding.rbMovieRating.rating
                currentMovie.movieImageUri = imageUri
                currentMovie.moviePlot = binding.etMoviePlot.text.toString()
                currentMovie.movieYear = binding.tvSelectedYear.text.toString().toIntOrNull() ?: 1900
                val hours = binding.npHoursPicker.value
                val minutes = binding.npMinutesPicker.value
                currentMovie.movieLength = (hours * 60 + minutes).toDuration(DurationUnit.MINUTES)


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
                binding.tvSelectedYear.text = numberPicker.value.toString()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun getSelectedGenres(): List<String> {
        val genres = mutableListOf<String>()
        if (binding.checkboxDoco.isChecked) genres.add(getString(R.string.doco_label))
        if (binding.checkboxWar.isChecked) genres.add(getString(R.string.war_label))
        if (binding.checkboxDrama.isChecked) genres.add(getString(R.string.drama_label))
        if (binding.checkboxAction.isChecked) genres.add(getString(R.string.action_label))
        if (binding.checkboxFamily.isChecked) genres.add(getString(R.string.family_label))
        if (binding.checkboxRomance.isChecked) genres.add(getString(R.string.romance_label))
        if (binding.checkboxAdventure.isChecked) genres.add(getString(R.string.adventure_label))
        if (binding.checkboxAnimation.isChecked) genres.add(getString(R.string.animation_label))
        if (binding.checkboxScienceFiction.isChecked) genres.add(getString(R.string.science_fiction_label))
        if (binding.checkboxHorror.isChecked) genres.add(getString(R.string.horror_label))
        if (binding.checkboxThriller.isChecked) genres.add(getString(R.string.thriller_label))

        return genres
    }

    private fun isFormValid(): Boolean {
        val movieName = binding.tvItemTitle.text.toString()
        return movieName.isNotEmpty()
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