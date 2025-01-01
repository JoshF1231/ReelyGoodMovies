package com.example.incrediblemovieinfoapp.ui.all_movies


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.incrediblemovieinfoapp.R
import com.example.incrediblemovieinfoapp.databinding.AllItemsLayoutBinding
import com.example.incrediblemovieinfoapp.ui.ActivityViewModel


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
            clearAllData()
            findNavController().navigate(R.id.action_allItemsFragment2_to_addItemFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.movieList?.observe(viewLifecycleOwner) { movies ->
            if (!movies.isNullOrEmpty()) {
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
                        findNavController().navigate(R.id.action_allItemsFragment2_to_editItemFragment)
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
                dialogCreator(viewHolder)
            }
        }).attachToRecyclerView(binding.recycler)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun dialogCreator(viewHolder: RecyclerView.ViewHolder) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.delete_item_dialog, null)

        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.setCancelable(false)

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            binding.recycler.adapter?.notifyItemChanged(viewHolder.adapterPosition)
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnDelete).setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.item_deletion), Toast.LENGTH_SHORT)
                .show()
            val movie = (binding.recycler.adapter as ItemAdapter).itemAt(viewHolder.adapterPosition)
            viewModel.deleteMovie(movie)
            binding.recycler.adapter?.notifyItemRemoved(viewHolder.adapterPosition)

            dialog.dismiss()
        }
        dialog.show()
    }

    private fun clearAllData() {
        viewModel.setSelectedImageURI(null)
        viewModel.setSelectedRuntimeHours(0)
        viewModel.setSelectedRuntimeMinutes(0)
        viewModel.setSelectedYear(1900)
    }
}
