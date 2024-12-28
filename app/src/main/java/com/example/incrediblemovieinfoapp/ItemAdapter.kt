package com.example.incrediblemovieinfoapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.incrediblemovieinfoapp.databinding.ItemLayoutBinding

class ItemAdapter(val items: LiveData<List<Movie>>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val binding : ItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root){
            fun bind(movie: Movie){
                binding.tvItemMovieTitle.text = movie.movieTitle
                binding.ivItemMovieImage.setImageURI(movie.movieImageUri)
                binding.rbItemMovieRating.rating = movie.movieRate
                binding.tvItemMovieGenre.text = movie.movieGenres.joinToString(", ")
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items.value!![position])
    }

    override fun getItemCount(): Int {
        return items.value!!.size
    }
}