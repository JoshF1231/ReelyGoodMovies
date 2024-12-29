package com.example.incrediblemovieinfoapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.incrediblemovieinfoapp.data.model.Movie
import com.example.incrediblemovieinfoapp.databinding.ItemLayoutBinding

class ItemAdapter(val items: LiveData<List<Movie>>, val callBack: ItemListener) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    interface ItemListener {
        fun onItemClicked(index : Int)
        fun onItemLongClicked(index : Int)
    }

    inner class ItemViewHolder(private val binding : ItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root) ,OnClickListener,OnLongClickListener{
            init {
                binding.root.setOnClickListener (this)
                binding.root.setOnLongClickListener(this)
            }
        override fun onClick(p0: View?) {
            callBack.onItemClicked(adapterPosition)
        }

        override fun onLongClick(p0: View?): Boolean {
            callBack.onItemLongClicked(adapterPosition)
            return true
        }

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