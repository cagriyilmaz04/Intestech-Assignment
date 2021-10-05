package com.example.intestechassignment.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intestechassignment.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FolderViewModel():ViewModel() {
    val fileDatas= MutableLiveData<ArrayList<Tempclass>>()
    fun getData(reference: DatabaseReference){

        //val reference=FirebaseDatabase.getInstance().reference
      viewModelScope.launch(Dispatchers.IO) {
            val sorgu=reference.child("users")
            sorgu.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(i in snapshot.children){
                        val pictures=i.child("pictures").getValue(holding::class.java)
                       val a= pictures?.data?.dizi
                        if(a!=null){
                            fileDatas.value?.addAll(a)
                        }

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

    }

}