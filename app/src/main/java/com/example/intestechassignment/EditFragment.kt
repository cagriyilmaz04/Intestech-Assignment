package com.example.intestechassignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.intestechassignment.databinding.FragmentEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class EditFragment : Fragment() {
    private var _binding :FragmentEditBinding?=null
    private val binding get()=_binding!!
    private var job:Job?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentEditBinding.inflate(inflater,container,false)
        val user=FirebaseAuth.getInstance().currentUser?.uid
        val reference=FirebaseDatabase.getInstance().reference
        val sorgu=reference.child("users")
                .child(user!!)
                .child("pictures")
                .child("data")
                .child("dizi")
                .addValueEventListener(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                    //  Toast.makeText(requireContext(),snapshot.childrenCount.toString(),Toast.LENGTH_LONG).show()
                                for(i in 0 until snapshot.childrenCount){
                                    val data=snapshot.child("$i").getValue(Tempclass::class.java)
                                    Toast.makeText(requireContext(),data?.Picture?.size.toString()+data?.filename,Toast.LENGTH_LONG).show()
                                }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        binding.buttonKaydetEdit.setOnClickListener {
            val newUserName=binding.editTextKullanici.text.toString()
            val password=binding.editTextSifreEdit.text.toString()
            if(newUserName.isNotEmpty()&&password.isNotEmpty()){
                job=GlobalScope.launch(Dispatchers.Main) {
                    val file=PasswordName(newUserName,password)
                    //val data=unifyClass(file)
                    val auth=FirebaseAuth.getInstance().currentUser
                    if(auth!=null){
                        auth.updatePassword(password).addOnSuccessListener {
                            Toast.makeText(requireContext(),"Şifreniz Güncellendi",Toast.LENGTH_LONG).show()
                        }
                    }
                    val request=reference.child("users")
                            .child(auth?.uid.toString())
                            request.addValueEventListener(object :ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val data=snapshot.child("users_information").getValue(PasswordName::class.java)
                                    val updateInfo=HashMap<String,Any>()
                                    updateInfo["name"]=newUserName
                                    updateInfo["password"]=password
                                    reference.child("users")
                                            .child(auth?.uid.toString())
                                            .child("users_information")
                                            .updateChildren(updateInfo)
                                            .addOnSuccessListener {
                                                Toast.makeText(requireContext(),"Güncellemeler Yapıldı",Toast.LENGTH_LONG).show()
                                            }
                                    val pictureData=snapshot.child("pictures").getValue(holding::class.java)

                                    if(pictureData?.data?.dizi!=null){
                                        for(i in 0 until pictureData.data.dizi.size){
                                            val updateInfo=HashMap<String,Any>()
                                            updateInfo["name"]=newUserName
                                            updateInfo["password"]=password
                                            val picture= reference.child("users")
                                                    .child(auth?.uid.toString()).child("pictures").child("data").child("dizi").child(i.toString())
                                                    .updateChildren(updateInfo).addOnSuccessListener {
                                                        Toast.makeText(requireContext(),"Adamın Anasını Sikerler",Toast.LENGTH_LONG).show()
                                                    }
                                        }
                                    }


                                }

                                override fun onCancelled(error: DatabaseError) {

                                }

                            })
                   /* val reference=FirebaseDatabase.getInstance().reference.child("users")
                        .child(auth?.uid!!)
                            .child("users_information")
                            .setValue(PasswordName(newUserName,password,auth.uid))
                            .addOnCompleteListener {
                            }
                    */
                   // reference.setValue(data).addOnSuccessListener {

                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.toolbarEdit.title="Düzenle"
        super.onViewCreated(view, savedInstanceState)
    }



}