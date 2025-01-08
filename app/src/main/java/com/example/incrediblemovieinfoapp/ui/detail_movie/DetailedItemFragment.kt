package com.example.incrediblemovieinfoapp.ui.detail_movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.incrediblemovieinfoapp.R
import com.example.incrediblemovieinfoapp.databinding.DetailedItemLayoutBinding
import com.example.incrediblemovieinfoapp.ui.ActivityViewModel


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
            binding.tvMovieTitle.text = it.title
            binding.tvMovieGenres.text = viewModel.getGeneresAsLocalizedString(requireContext(),it)
            binding.tvMoviePlot.text = it.plot
            binding.rbMovieRating.rating = it.rate
            binding.tvMovieYear.text = it.year.toString()
            binding.tvMovieLength.text =it.length.toString()
            Glide.with(requireContext())
                .load(it.photo.takeIf { !it.isNullOrEmpty() } ?: R.drawable.movie_picture)
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