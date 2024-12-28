package com.example.incrediblemovieinfoapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
        binding.tvMovieTitle.text = viewModel.getMovieAt(viewModel.getSelectedMovieIndex())?.movieTitle
        binding.tvMovieGenres.text = viewModel.getMovieAt(viewModel.getSelectedMovieIndex())?.movieGenres?.joinToString(", ")
        binding.tvMoviePlot.text = viewModel.getMovieAt(viewModel.getSelectedMovieIndex())?.moviePlot
        binding.rbMovieRating.rating = viewModel.getMovieAt(viewModel.getSelectedMovieIndex())!!.movieRate
        binding.tvMovieYear.text = viewModel.getMovieAt(viewModel.getSelectedMovieIndex())?.movieYear.toString()
        binding.tvMovieLength.text = viewModel.getMovieAt(viewModel.getSelectedMovieIndex())?.movieLength.toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}