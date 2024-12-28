package com.example.incrediblemovieinfoapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        viewModel.movieList?.observe(viewLifecycleOwner) {
            binding.recycler.adapter = ItemAdapter(viewModel.movieList,object : ItemAdapter.ItemListener{
                override fun onItemLongClicked(index: Int) {
                    dialogCreator()
                }

                override fun onItemClicked(index: Int) {
                    TODO("Not yet implemented")
                }
            })
        }
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun dialogCreator(): Unit{
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.delete_item_dialog, null)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}