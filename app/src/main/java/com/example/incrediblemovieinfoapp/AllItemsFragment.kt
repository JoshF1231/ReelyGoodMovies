package com.example.incrediblemovieinfoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding.recycler.adapter = ItemAdapter(viewModel.movieList)
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}