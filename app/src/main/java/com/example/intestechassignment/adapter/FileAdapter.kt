package com.example.intestechassignment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.intestechassignment.*
import com.example.intestechassignment.databinding.RecyclerRowBinding
import java.lang.Exception

class FileAdapter(val context:Context,private val listener:Listener):RecyclerView.Adapter<FileAdapter.TutucuVH>() {
    var list= emptyList<Tempclass>()
    interface Listener {
        fun onItemClick(value:Int)
    }
    class TutucuVH(val binding:RecyclerRowBinding):RecyclerView.ViewHolder(binding.root) {
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutucuVH {
        return TutucuVH(RecyclerRowBinding.inflate(LayoutInflater.from(parent?.context),parent,false))
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TutucuVH, position: Int) {
            if(list.get(position)!=null){
                holder.binding.textKisiAdi.text= context.resources.getString(R.string.olusturan)+" "+list.get(position).name
                holder.binding.textKlasorAdi.text= context.resources.getString(R.string.dosya_adi)+" "+list.get(position).filename
                holder.itemView.setOnClickListener {
                    listener.onItemClick(position)
                }
            }
    }
    override fun getItemCount(): Int {
        return list.size
    }

    fun refreshDataForId(newList:List<Tempclass>){
        this.list=newList
        notifyDataSetChanged()
    }

}