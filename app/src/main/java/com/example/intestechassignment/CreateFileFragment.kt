package com.example.intestechassignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.intestechassignment.databinding.CreateFileBinding
import com.example.intestechassignment.viewModel.FolderViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class CreateFileFragment: Fragment() {
    private var _binding:CreateFileBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel:FolderViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= CreateFileBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarCreateFile.title="Dosya Oluştur"
        val reference=FirebaseDatabase.getInstance().reference
        val auth=FirebaseAuth.getInstance().currentUser
        var diziler= arrayListOf<file>()
        viewModel=ViewModelProvider(this).get(FolderViewModel::class.java)
        binding.buttonOlustur.setOnClickListener {
            val file_name=binding.editTextDosyaAdi.text.toString().trim()
            if(!(file_name.isNullOrEmpty())){
                val request=reference.child("users")
                        .child(auth?.uid!!)
                request.addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val users_infor=snapshot.child("users_information").getValue(PasswordName::class.java)
                        var allData=snapshot.child("pictures").getValue(holding::class.java)
                        val random=UUID.randomUUID()
                        val temp=Tempclass(users_infor?.name.toString(), users_infor?.password.toString(), users_infor?.id.toString(),file_name, random.toString(),diziler)
                        if(allData?.data?.dizi==null){

                            val a=ArrayList<Tempclass>()
                            a.add(temp)
                            val x=HoldingArray(a)
                            val z=holding(x)
                            reference.child("users")
                                    .child(auth.uid)
                                    .child("pictures")
                                    .setValue(z)
                        }else{
                            val new=Tempclass(users_infor?.name.toString(),users_infor?.password.toString(),users_infor?.id.toString(),file_name,random.toString(),diziler)

                            val new_dizi=ArrayList<Tempclass>()
                            new_dizi.addAll(allData.data.dizi)
                            new_dizi.add(new)
                            val z=HoldingArray(new_dizi)
                            val x=holding(z)
                           reference.child("users")
                                    .child(auth.uid)
                                    .child("pictures")
                                    .setValue(x)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }



        }
    }
}
