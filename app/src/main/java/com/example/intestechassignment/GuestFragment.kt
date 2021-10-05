package com.example.intestechassignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.intestechassignment.adapter.FileAdapterGuest
import com.example.intestechassignment.adapter.PictureAdapter
import com.example.intestechassignment.databinding.FragmentGuestBinding
import com.google.android.material.transition.Hold
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class GuestFragment : Fragment() {
    private var _binding:FragmentGuestBinding?=null
    private val binding get()=_binding!!
    val args by navArgs<GuestFragmentArgs>()
    private lateinit var Adapter:FileAdapterGuest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
      _binding= FragmentGuestBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarGuest.title="Detaylar"
        Adapter= FileAdapterGuest()
        binding.recyclerViewGuest.layoutManager=GridLayoutManager(requireContext(),3)
        AdapterFunction()
    }

    private fun AdapterFunction() {
        val reference=FirebaseDatabase.getInstance().reference
        val auth=args.sendingTempClass.id
        val uid=args.sendingTempClass.uid
        val request=reference.child("users")
            .child(auth)
            .child("pictures")
            .child("data")
            request.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data=snapshot.getValue(HoldingArray::class.java)
                    if(data?.dizi!=null){
                        for(i in 0 until data.dizi.size){
                            if(data.dizi.get(i).uid==uid){
                                if(data.dizi.get(i).Picture!= null){
                                    val a=data.dizi.get(i).Picture
                                   Adapter.refreshData(a)
                                    binding.recyclerViewGuest.adapter=Adapter
                                    binding.recyclerViewGuest.adapter?.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })

    }


}