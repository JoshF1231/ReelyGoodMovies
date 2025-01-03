package com.example.incrediblemovieinfoapp.ui.edit_movie

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
import com.example.incrediblemovieinfoapp.R
import com.example.incrediblemovieinfoapp.data.models.Movie
import com.example.incrediblemovieinfoapp.databinding.AddItemLayoutBinding
import com.example.incrediblemovieinfoapp.ui.ActivityViewModel
import java.util.Calendar
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class EditItemFragment : Fragment() {
    private var _binding: AddItemLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActivityViewModel by activityViewModels()
    private var imageUri: Uri? = null

    private val pickImageLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            binding.ivSelectedImage.setImageURI((it))
            requireActivity().contentResolver.takePersistableUriPermission(
                it!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            imageUri = it
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddItemLayoutBinding.inflate(inflater, container, false)


        setParameters(viewModel.chosenMovie.value!!)


        binding.ivSelectedImage.setImageURI(imageUri)
        binding.btnAddMovie.text = getString(R.string.edit_movie_bt)
        binding.btnAddMovie.setOnClickListener {
            if (isFormValid()) {
                val currentMovie = Movie(
                    binding.tvItemTitle.text.toString(),
                    binding.etMoviePlot.text.toString(),
                    (binding.npHoursPicker.value * 60 + binding.npMinutesPicker.value).toDuration(
                        DurationUnit.MINUTES
                    ).toInt(DurationUnit.MINUTES),
                    binding.tvSelectedYear.text.toString().toIntOrNull() ?: 1900,
                    binding.rbMovieRating.rating,
                    getSelectedGenres().joinToString(", "),
                    imageUri.toString(),
                )
                currentMovie.id = viewModel.chosenMovie.value!!.id

                viewModel.updateMovie(currentMovie)

                findNavController().navigate(R.id.action_editItemFragment_to_allItemsFragment2)
            } else {
                showError(getString(R.string.please_fill_in_all_the_required_fields))
            }
        }

        setupNumberPickers()

        binding.btnSelectYear.setOnClickListener {
            showYearPickerDialog()
        }

        binding.btnAddPhoto.setOnClickListener {
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
        if (binding.checkboxComedy.isChecked) genres.add(getString(R.string.comedy_label))
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
        return movieName.isNotEmpty() && binding.etMoviePlot.text.isNotEmpty()
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


    private fun setParameters(movie: Movie) {
        binding.tvItemTitle.setText(movie.title)
        binding.etMoviePlot.setText(movie.plot)
        setNumberPickers(movie)
        binding.tvSelectedYear.text = movie.year.toString()
        binding.rbMovieRating.rating = movie.rate
        imageUri = movie.photo?.toUri()
        showGenres(movie)
    }

    private fun showGenres(movie: Movie): Unit {
        val list: List<String> = movie.genre.split(", ").toList<String>()
        for (gen in list) {
            when (gen) {
                getString(R.string.horror_label) -> binding.checkboxHorror.isChecked = true
                getString(R.string.family_label) -> binding.checkboxFamily.isChecked = true
                getString(R.string.comedy_label) -> binding.checkboxComedy.isChecked = true
                getString(R.string.drama_label) -> binding.checkboxDrama.isChecked = true
                getString(R.string.action_label) -> binding.checkboxAction.isChecked = true
                getString(R.string.thriller_label) -> binding.checkboxThriller.isChecked = true
                getString(R.string.science_fiction_label) -> binding.checkboxScienceFiction.isChecked =
                    true

                getString(R.string.romance_label) -> binding.checkboxRomance.isChecked = true
                getString(R.string.adventure_label) -> binding.checkboxAdventure.isChecked = true
                getString(R.string.war_label) -> binding.checkboxWar.isChecked = true
                getString(R.string.animation_label) -> binding.checkboxAnimation.isChecked = true
                getString(R.string.doco_label) -> binding.checkboxDoco.isChecked = true
            }
        }
    }

    private fun setNumberPickers(movie: Movie): Unit {
        binding.npHoursPicker.minValue = 0
        binding.npHoursPicker.maxValue = 4
        binding.npMinutesPicker.minValue = 0
        binding.npMinutesPicker.maxValue = 59
        binding.npHoursPicker.value = movie.length.div(60)
        binding.npMinutesPicker.value = (movie.length - binding.npHoursPicker.value.times(60))
    }
}