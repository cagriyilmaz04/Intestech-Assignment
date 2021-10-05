package com.example.intestechassignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.intestechassignment.databinding.FragmentRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class RegisterFragment : Fragment() {

    private var _binding:FragmentRegisterBinding?=null
    private val binding get()=_binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding= FragmentRegisterBinding.inflate(inflater,container,false)
        binding.buttonDevamEt.setOnClickListener {
            val email=binding.textMail.text.toString()
            val password=binding.editPasswordRegister.text.toString()
            val password_again=binding.editPasswordRegisterAgain.text.toString()
            val name=binding.editTextKisiAdi.text.toString()
            if((email.isNotEmpty()&&password.isNotEmpty()&&password_again.isNotEmpty())&&password==password_again&&name.isNotEmpty()){
                val auth=FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(requireContext(),"Kaydınız Yapıldı",Toast.LENGTH_LONG).show()
                                val auth_user=FirebaseAuth.getInstance().currentUser
                            if(auth_user!=null){
                                val passData=PasswordName(name,password,email = email)
                                val action=RegisterFragmentDirections.actionRegisterFragmentToLogInFragment(passData)
                                auth_user.sendEmailVerification().addOnSuccessListener {
                                    Toast.makeText(requireContext(),"Onay Maili Gönderildi",Toast.LENGTH_LONG).show()
                                    findNavController().navigate(action)
                                }.addOnFailureListener {
                                    Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                                }
                            }
                            }
                        }
                }
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.toolbarKayitOl.title="Kayıt Ol"
        super.onViewCreated(view, savedInstanceState)
    }

}