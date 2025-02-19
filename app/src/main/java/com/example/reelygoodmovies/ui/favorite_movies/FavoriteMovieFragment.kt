package com.example.reelygoodmovies.ui.favorite_movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reelygoodmovies.R
import com.example.reelygoodmovies.databinding.FavoriteLayoutBinding
import com.example.reelygoodmovies.ui.ActivityViewModel
import com.example.reelygoodmovies.ui.add_edit_movie.EditViewModel
import com.example.reelygoodmovies.ui.all_movies.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoriteMovieFragment : Fragment() {
    private var _binding: FavoriteLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActivityViewModel by activityViewModels()
    private val editViewModel: EditViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // מאזינים לרשימת הסרטים המועדפים מה-ViewModel
        viewModel.favoriteMovies?.observe(viewLifecycleOwner) { favoriteMovies ->
            if (favoriteMovies.isEmpty()) {
                binding.recycler.visibility = View.GONE
            } else {
                binding.recycler.visibility = View.VISIBLE
                binding.recycler.adapter = ItemAdapter(favoriteMovies, object : ItemAdapter.ItemListener {
                    override fun onItemClicked(index: Int) {
                        viewModel.setMovie(favoriteMovies[index])
                        findNavController().navigate(R.id.action_favoriteMovieFragment_to_detailedItemFragment)
                    }

                    override fun onItemLongClicked(index: Int) {
                        Toast.makeText(requireContext(), favoriteMovies[index].title, Toast.LENGTH_SHORT).show()
                    }

                    override fun onEditButtonClick(index: Int) {
                        viewModel.setMovie(favoriteMovies[index])
                        editViewModel.setEditMode(true)
                        findNavController().navigate(R.id.action_favoriteMovieFragment_to_addOrEditItemFragment)
                    }

                    override fun onFavButtonClick(index: Int) {
                        viewModel.setMovie(favoriteMovies[index])
                        viewModel.chosenMovie.value!!.favorite = !viewModel.chosenMovie.value!!.favorite // To update the movie in the movies list
                        viewModel.updateMovie(viewModel.chosenMovie.value!!) // To update the movie in the DB
                        binding.recycler.adapter?.notifyItemChanged(index)


                    }
                })
            }
            binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
