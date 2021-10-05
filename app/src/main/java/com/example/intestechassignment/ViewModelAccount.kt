package com.example.intestechassignment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ViewModelAccount:ViewModel() {
    val private_data = MutableLiveData<PasswordName>()

    fun getData(reference:DatabaseReference,kullanici:FirebaseUser?){
        //val reference=FirebaseDatabase.getInstance().reference

        val sorgu=reference.child("users")
                .child(kullanici?.uid.toString())
                .child("users_information")
        sorgu.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                    val users:PasswordName=snapshot.getValue(PasswordName::class.java)!!
                    private_data.value=users

            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}