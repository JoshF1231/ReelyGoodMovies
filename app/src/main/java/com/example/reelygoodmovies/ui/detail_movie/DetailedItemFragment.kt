    package com.example.reelygoodmovies.ui.detail_movie

    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Toast
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.activityViewModels
    import androidx.fragment.app.viewModels
    import androidx.navigation.fragment.findNavController
    import com.bumptech.glide.Glide
    import com.example.reelygoodmovies.R
    import com.example.reelygoodmovies.data.models.GenreConverter
    import com.example.reelygoodmovies.data.models.Movie
    import com.example.reelygoodmovies.databinding.DetailedItemLayoutBinding
    import com.example.reelygoodmovies.ui.ActivityViewModel
    import com.example.reelygoodmovies.utils.Error
    import com.example.reelygoodmovies.utils.Loading
    import com.example.reelygoodmovies.utils.Success
    import dagger.hilt.android.AndroidEntryPoint


    @AndroidEntryPoint
    class DetailedItemFragment : Fragment() {
        private var _binding: DetailedItemLayoutBinding? = null
        private val binding get() = _binding!!
        private val viewModel: ActivityViewModel by activityViewModels()
        private val detailedMovieViewModel : DetailedMovieViewModel by viewModels()

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


            detailedMovieViewModel.movie.observe(viewLifecycleOwner){ movie ->
                when (movie.status){
                    is Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), movie.status.message, Toast.LENGTH_SHORT).show()
                    }
                    is Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Success -> {
                        binding.progressBar.visibility = View.GONE
                        updateMovie(movie.status.data!!)
                    }
                }
            }

            arguments?.getInt("id")?.let{
                detailedMovieViewModel.setId(it)
            }


            binding.ibMovieEdit.setOnClickListener {
                viewModel.setEditMode(true)
                findNavController().navigate(R.id.action_detailedItemFragment_to_addOrEditItemFragment)

            }
        }

        private fun updateMovie(movie : Movie){
            val genreIds = movie.genre

            val genreNames = genreIds.joinToString(", ") { genreId ->
                binding.root.context.getString(genreId)
            }
            binding.tvMovieTitle.text = movie.title
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

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }