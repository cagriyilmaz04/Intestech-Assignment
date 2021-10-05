package com.example.intestechassignment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.intestechassignment.databinding.RecylerPictureRowBinding
import com.example.intestechassignment.file
import com.squareup.picasso.Picasso

class PictureAdapter(private val Listener:listener):RecyclerView.Adapter<PictureAdapter.PictureVH>() {
    interface listener{
        fun onItemClick(position: Int)
    }
    var emptlyList=ArrayList<file>()
    class PictureVH(val binding:RecylerPictureRowBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureVH {
        return PictureVH(RecylerPictureRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PictureVH, position: Int) {
        Picasso.get().load(emptlyList.get(position).Picture).into(holder.binding.imageViewPictureAdapter)
        holder.itemView.setOnClickListener {
            Listener.onItemClick(position)

        }
    }

    override fun getItemCount(): Int {
        return emptlyList.size
    }
    fun refreshData(list:ArrayList<file>){
        this.emptlyList=list
        notifyDataSetChanged()
    }

}