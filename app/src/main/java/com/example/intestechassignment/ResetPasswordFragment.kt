package com.example.intestechassignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.intestechassignment.databinding.DialogFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordFragment:DialogFragment(R.layout.dialog_fragment) {
    private var _binding:DialogFragmentBinding?=null
    private val binding get()=_binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= DialogFragmentBinding.inflate(inflater,container,false)
        binding.button.setOnClickListener {
            val text=binding.editTextTextPersonName.text.toString()
            if(text.isNotEmpty()){
                FirebaseAuth.getInstance().sendPasswordResetEmail(text).addOnSuccessListener {
                    Toast.makeText(requireContext(),"Gönderildi",Toast.LENGTH_LONG).show()
                    dismiss()
                }
            }
        }
        return binding.root
    }

}