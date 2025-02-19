package com.example.reelygoodmovies.ui.filter_genre_movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reelygoodmovies.R
import com.example.reelygoodmovies.databinding.GenreMoodFilterLayoutBinding
import com.example.reelygoodmovies.ui.ActivityViewModel

class GenreMoodFragment : Fragment() {

    private var _binding: GenreMoodFilterLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModelGenre: GenreMoodViewModel by viewModels()
    private val viewModel: ActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GenreMoodFilterLayoutBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.movieList?.observe(viewLifecycleOwner) { movies ->
            if (movies.isNotEmpty()) {
                viewModelGenre.filterMoviesByGenre(movies)
            }
        }

        val comedyAdapter = GenreItemAdapter(emptyList(), object : GenreItemAdapter.ItemListener {
            override fun onItemClicked(index: Int) {
                val selectedMovie = viewModelGenre.comedyMovies.value?.get(index)
                if (selectedMovie != null) {
                    viewModel.setMovie(selectedMovie)
                    findNavController().navigate(R.id.action_genreMoodFragment_to_detailedItemFragment,
                        bundleOf("id" to selectedMovie.id)
                    )
                }
            }

            override fun onItemLongClicked(index: Int) {
                val selectedMovie = viewModelGenre.comedyMovies.value?.get(index)
                if (selectedMovie != null) {
                    Toast.makeText(requireContext(), selectedMovie.title, Toast.LENGTH_SHORT).show()
                }
            }
            })

        binding.recyclerFunny.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerFunny.adapter = comedyAdapter

        viewModelGenre.comedyMovies.observe(viewLifecycleOwner) { movies ->
            comedyAdapter.updateMovies(movies)
        }

        val thrillerAdapter  = GenreItemAdapter(emptyList(), object : GenreItemAdapter.ItemListener {
            override fun onItemClicked(index: Int) {
                val selectedMovie = viewModelGenre.thrillerMovies.value?.get(index)
                if (selectedMovie != null) {
                    viewModel.setMovie(selectedMovie)
                    findNavController().navigate(R.id.action_genreMoodFragment_to_detailedItemFragment,
                        bundleOf("id" to selectedMovie.id))
                }
            }

            override fun onItemLongClicked(index: Int) {
                val selectedMovie = viewModelGenre.thrillerMovies.value?.get(index)
                if (selectedMovie != null) {
                    Toast.makeText(requireContext(), selectedMovie.title, Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.recyclerThriller.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerThriller.adapter = thrillerAdapter

        viewModelGenre.thrillerMovies.observe(viewLifecycleOwner) { movies ->
            thrillerAdapter .updateMovies(movies)
        }

        val fantasyAdapter = GenreItemAdapter(emptyList(), object : GenreItemAdapter.ItemListener {
            override fun onItemClicked(index: Int) {
                val selectedMovie = viewModelGenre.fantasyMovies.value?.get(index)
                if (selectedMovie != null) {
                    viewModel.setMovie(selectedMovie)
                    findNavController().navigate(R.id.action_genreMoodFragment_to_detailedItemFragment,
                        bundleOf("id" to selectedMovie.id))
                }
            }

            override fun onItemLongClicked(index: Int) {
                val selectedMovie = viewModelGenre.fantasyMovies.value?.get(index)
                if (selectedMovie != null) {
                    Toast.makeText(requireContext(), selectedMovie.title, Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.recyclerFantasy.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerFantasy.adapter = fantasyAdapter

        viewModelGenre.fantasyMovies.observe(viewLifecycleOwner) { movies ->
            fantasyAdapter.updateMovies(movies)
        }

        val romanceAdapter = GenreItemAdapter(emptyList(), object : GenreItemAdapter.ItemListener {
            override fun onItemClicked(index: Int) {
                val selectedMovie = viewModelGenre.romanceMovies.value?.get(index)
                if (selectedMovie != null) {
                    viewModel.setMovie(selectedMovie)
                    findNavController().navigate(R.id.action_genreMoodFragment_to_detailedItemFragment,
                        bundleOf("id" to selectedMovie.id))
                }
            }

            override fun onItemLongClicked(index: Int) {
                val selectedMovie = viewModelGenre.romanceMovies.value?.get(index)
                if (selectedMovie != null) {
                    Toast.makeText(requireContext(), selectedMovie.title, Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.recyclerRomantic.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerRomantic.adapter = romanceAdapter

        viewModelGenre.romanceMovies.observe(viewLifecycleOwner) { movies ->
            romanceAdapter.updateMovies(movies)
        }

        val familyAdapter = GenreItemAdapter(emptyList(), object : GenreItemAdapter.ItemListener {
            override fun onItemClicked(index: Int) {
                val selectedMovie = viewModelGenre.familyMovies.value?.get(index)
                if (selectedMovie != null) {
                    viewModel.setMovie(selectedMovie)
                    findNavController().navigate(R.id.action_genreMoodFragment_to_detailedItemFragment,
                        bundleOf("id" to selectedMovie.id))
                }
            }

            override fun onItemLongClicked(index: Int) {
                val selectedMovie = viewModelGenre.familyMovies.value?.get(index)
                if (selectedMovie != null) {
                    Toast.makeText(requireContext(), selectedMovie.title, Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.recyclerRelax.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerRelax.adapter = familyAdapter

        viewModelGenre.familyMovies.observe(viewLifecycleOwner) { movies ->
            familyAdapter.updateMovies(movies)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


