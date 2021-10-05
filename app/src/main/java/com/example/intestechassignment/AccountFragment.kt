package com.example.intestechassignment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.intestechassignment.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class AccountFragment : Fragment() {
    private var _binding:FragmentAccountBinding ?=null
    private val binding get()=_binding!!
    private val TAG="TEST"
    private lateinit var viewModel:ViewModelAccount
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentAccountBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel=ViewModelProvider(this).get(ViewModelAccount::class.java)
        observeData()
        binding.toolbar.title="Hesabım"
        binding.buttonDuzenle.setOnClickListener {
            val action=R.id.action_accountFragment_to_editFragment
            findNavController().navigate(action)
        }
        super.onViewCreated(view, savedInstanceState)
    }
    private fun observeData() {
        val reference=FirebaseDatabase.getInstance().reference
        val kullanici=FirebaseAuth.getInstance().currentUser
        viewModel.getData(reference,kullanici)
        viewModel.private_data.observe(viewLifecycleOwner){
           // Log.e(TAG,it.password.toString())
                binding.textKullaniciAdiAccount.text=  requireContext().resources.getString(R.string.kullanici_adiniz)+it.name.toString()
                binding.textSifreAccount.text= requireContext().resources.getString(R.string.sifreniz_account_fragment)+it.password.toString()

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}