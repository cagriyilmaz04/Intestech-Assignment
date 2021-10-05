package com.example.intestechassignment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.intestechassignment.databinding.FragmentLogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LogInFragment : Fragment() {

    var _binding:FragmentLogInBinding?=null
    val binding get()=_binding!!
    private val args by navArgs<LogInFragmentArgs>()
    lateinit var sharedPreferences: SharedPreferences
    var Sharedemail:String?=null
    var Sharedpassword:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      sharedPreferences=(requireActivity() as AppCompatActivity).getSharedPreferences("com.example.intestechassignment",Context.MODE_PRIVATE)
        val mail=sharedPreferences.getString("mail","")
        val password=sharedPreferences.getString("password","")
        if(mail!=""&&password!=""){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(mail.toString(), password.toString()).addOnSuccessListener {
                val action=R.id.action_logInFragment_to_mainFragment
                        findNavController().navigate(action)
            }

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentLogInBinding.inflate(inflater,container,false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonGiris.setOnClickListener {
            val reference=FirebaseDatabase.getInstance().reference
            val auth=FirebaseAuth.getInstance().currentUser
            val email=binding.editTextMail.text.toString()
            val password=binding.editTextSifre.text.toString()
            if(email.isNotEmpty()&&password.isNotEmpty()){
                    val auth=FirebaseAuth.getInstance()
                    auth.signInWithEmailAndPassword("m.cagri0205@gmail.com","a30943094-").addOnSuccessListener {
                        if(it.user?.isEmailVerified==true){
                            GlobalScope.launch(Dispatchers.IO) {
                                val request=reference.child("users")
                                        .child(auth.uid!!)
                                        .child("users_information")
                                request.addListenerForSingleValueEvent(object :ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val data=snapshot.getValue(PasswordName::class.java)
                                        if(data==null){
                                            val new_data=PasswordName(name =args.PassData?.name,password =args.PassData?.password,id =auth.uid,email=args.PassData?.email.toString())
                                            reference.child("users")
                                                    .child(auth.uid!!)
                                                    .child("users_information")
                                                    .setValue(new_data).addOnCompleteListener {

                                                    }

                                            findNavController().navigate(R.id.action_logInFragment_to_mainFragment)
                                        }else{
                                            findNavController().navigate(R.id.action_logInFragment_to_mainFragment)
                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
                            }
                        }
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                    }
            }else{
                Toast.makeText(requireContext(),requireContext().resources.getString(R.string.bos_yerleri_doldur),Toast.LENGTH_LONG).show()
            }
        }
        binding.textHesabYokMu.setOnClickListener {
            val action=R.id.action_logInFragment_to_registerFragment
            findNavController().navigate(action)
        }
        binding.textViewSifreUnuttum.setOnClickListener {
            val dialog=ResetPasswordFragment()
            val activity=(requireActivity() as AppCompatActivity)
            dialog.show(activity.supportFragmentManager,"Show")
        }
        binding.textViewBeniHatirla.setOnClickListener {
            val textMail=binding.editTextMail.text.toString()
            val password=binding.editTextSifre.text.toString()
            if(textMail!=null&&password!= null){
                Sharedpassword= sharedPreferences.edit().putString("mail",textMail).apply().toString()
                Sharedemail= sharedPreferences.edit().putString("password",password).apply().toString()
            }else{
                Toast.makeText(requireContext(),requireContext().resources.getString(R.string.sonra_tiklayin),Toast.LENGTH_LONG).show()
            }

        }

        super.onViewCreated(view, savedInstanceState)
    }
    override fun onDestroyView() {
       _binding=null
        super.onDestroyView()
    }
}