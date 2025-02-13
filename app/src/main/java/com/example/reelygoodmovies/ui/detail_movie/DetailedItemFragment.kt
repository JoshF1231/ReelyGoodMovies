package com.example.reelygoodmovies.ui.detail_movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.reelygoodmovies.R
import com.example.reelygoodmovies.databinding.DetailedItemLayoutBinding
import com.example.reelygoodmovies.ui.ActivityViewModel


class DetailedItemFragment : Fragment() {
    private var _binding: DetailedItemLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailedItemLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.chosenMovie.observe(viewLifecycleOwner) { movie ->
            binding.tvMovieTitle.text = movie.title
            val genreIds = movie.genre

            val genreNames = genreIds.joinToString(", ") { genreId ->
                requireContext().getString(genreId)
            }
            binding.tvMovieGenres.text = genreNames
            binding.tvMoviePlot.text = movie.plot
            binding.rbMovieRating.rating = movie.rate
            binding.tvMovieYear.text = movie.year.toString()
            binding.tvMovieLength.text = movie.length.toString()

            Glide.with(requireContext())
                .load(movie.photo.takeIf { !it.isNullOrEmpty() } ?: R.drawable.movie_picture)
                .circleCrop()
                .into(binding.ivMoviePoster)
        }

        binding.ibMovieEdit.setOnClickListener {
            viewModel.setEditMode(true)
            findNavController().navigate(R.id.action_detailedItemFragment_to_addOrEditItemFragment)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}