package com.example.incrediblemovieinfoapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.incrediblemovieinfoapp.databinding.DetailedItemLayoutBinding

class DetailedItemFragment : Fragment(){
    private var _binding : DetailedItemLayoutBinding? = null
    private val binding get () = _binding!!
    private val viewModel : ActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailedItemLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.chosenMovie.observe(viewLifecycleOwner){
            binding.tvMovieTitle.text = it.movieTitle
            binding.tvMovieGenres.text = it.movieGenres.joinToString(", ")
            binding.tvMoviePlot.text = it.moviePlot
            binding.rbMovieRating.rating = it.movieRate
            binding.tvMovieYear.text = it.movieYear.toString()
            binding.tvMovieLength.text =it.movieLength.toString()
            Glide.with(requireContext()).load(it.movieImageUri).circleCrop().into(binding.ivMoviePoster)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}