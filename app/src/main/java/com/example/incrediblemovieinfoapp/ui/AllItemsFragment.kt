package com.example.incrediblemovieinfoapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.incrediblemovieinfoapp.R
import com.example.incrediblemovieinfoapp.databinding.AllItemsLayoutBinding

class AllItemsFragment : Fragment(){
    private var _binding : AllItemsLayoutBinding? = null
    private val binding get () = _binding!!
    private val viewModel : ActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AllItemsLayoutBinding.inflate(inflater,container,false)
        binding.fabAddItem.setOnClickListener{
            findNavController().navigate(R.id.action_allItemsFragment2_to_addItemFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.adapter = ItemAdapter(viewModel.movieList,object : ItemAdapter.ItemListener {
            override fun onItemClicked(index: Int) {
                viewModel.setSelectedMovieIndex(index)
                findNavController().navigate(R.id.action_allItemsFragment2_to_detailedItemFragment)
            }
            override fun onItemLongClicked(index: Int) {
                Toast.makeText(requireContext(),viewModel.getMovieAt(index)?.movieTitle,Toast.LENGTH_SHORT).show()
            }
        })
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        ItemTouchHelper(object : ItemTouchHelper.Callback(){
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) = makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.removeMovie(viewHolder.adapterPosition)
                binding.recycler.adapter!!.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(binding.recycler)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}