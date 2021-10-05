package com.example.intestechassignment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.intestechassignment.GuestFragmentDirections
import com.example.intestechassignment.Tempclass
import com.example.intestechassignment.databinding.RecylerPictureRowBinding
import com.example.intestechassignment.file
import com.squareup.picasso.Picasso

class FileAdapterGuest:RecyclerView.Adapter<FileAdapterGuest.GuestVH>() {
    var emptyList= emptyList<file>()
    class GuestVH(val binding:RecylerPictureRowBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestVH {
        return GuestVH(RecylerPictureRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: GuestVH, position: Int) {
            Picasso.get().load(emptyList.get(position).toString()).into(holder.binding.imageViewPictureAdapter)
        holder.itemView.setOnClickListener {
            val action=GuestFragmentDirections.actionGuestFragmentToGuestPictureFragment(emptyList.get(position))
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
       return emptyList.size
    }
    fun refreshData(list: ArrayList<file>){
        this.emptyList=list
        notifyDataSetChanged()
    }
}