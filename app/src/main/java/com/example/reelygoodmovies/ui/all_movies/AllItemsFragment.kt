package com.example.reelygoodmovies.ui.all_movies


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reelygoodmovies.R
import com.example.reelygoodmovies.databinding.AllItemsLayoutBinding
import com.example.reelygoodmovies.ui.ActivityViewModel

class AllItemsFragment : Fragment() {
    private var _binding: AllItemsLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AllItemsLayoutBinding.inflate(inflater, container, false)
        binding.fabAddItem.setOnClickListener {
            viewModel.setEditMode(false)
            viewModel.clearAllData()
            findNavController().navigate(R.id.action_allItemsFragment2_to_addOrEditItemFragment)
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.movieList?.observe(viewLifecycleOwner) { movies ->
            if (movies.isEmpty()) {
                binding.recycler.visibility = View.GONE
            } else {
                binding.recycler.visibility = View.VISIBLE
                binding.recycler.adapter = ItemAdapter(movies, object : ItemAdapter.ItemListener {

                    override fun onItemClicked(index: Int) {
                        viewModel.setMovie(movies[index])
                        findNavController().navigate(R.id.action_allItemsFragment2_to_detailedItemFragment)
                    }

                    override fun onItemLongClicked(index: Int) {
                        Toast.makeText(
                            requireContext(),
                            movies[index].title,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onButtonClick(index: Int) {
                        viewModel.setMovie(movies[index])
                        viewModel.setEditMode(true)
                        findNavController().navigate(R.id.action_allItemsFragment2_to_addOrEditItemFragment)
                    }
                })
            }
            binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        }

        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) = makeFlag(
                ItemTouchHelper.ACTION_STATE_SWIPE,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            )

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteMovieDialog(viewHolder)
            }
        }).attachToRecyclerView(binding.recycler)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun deleteMovieDialog(viewHolder: RecyclerView.ViewHolder) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.delete_item_dialog, null)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.setCancelable(false)
        val movie = (binding.recycler.adapter as ItemAdapter).itemAt(viewHolder.adapterPosition)

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            binding.recycler.adapter?.notifyItemChanged(viewHolder.adapterPosition)
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnDelete).setOnClickListener {
            val deleteMessage = getString(R.string.item_deletion, movie.title)
            Toast.makeText(requireContext(), deleteMessage, Toast.LENGTH_SHORT).show()
            viewModel.deleteMovie(movie)
            (binding.recycler.adapter as ItemAdapter).notifyItemRemoved(viewHolder.adapterPosition)

            dialog.dismiss()
        }

        if (!movie.photo.isNullOrEmpty()) {
            dialogView.findViewById<ImageView>(R.id.iv_warning_image_movie)
                .setImageURI(movie.photo?.toUri())
        }

        dialog.show()
    }

    private fun deleteAllMoviesDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.delete_item_dialog, null)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnDelete).setOnClickListener {
            viewModel.deleteAllMovies()
            Toast.makeText(
                requireActivity(),
                getString(R.string.all_movies_deleted_confirmation),
                Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
        }
        dialogView.findViewById<TextView>(R.id.tv_delete_message).text =
            dialogView.context.getText(R.string.delete_all_message)
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_delete) {
            deleteAllMoviesDialog()
        }
        return true
    }
}
