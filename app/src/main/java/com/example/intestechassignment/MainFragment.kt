package com.example.intestechassignment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intestechassignment.adapter.FileAdapter
import com.example.intestechassignment.databinding.FragmentMainBinding
import com.example.intestechassignment.viewModel.FolderViewModel
import com.google.android.material.transition.Hold
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainFragment : Fragment(){
  private var _binding:FragmentMainBinding?=null
    private val binding get()=_binding!!
    private val args by navArgs<MainFragmentArgs>()
    lateinit var viewModel:FolderViewModel
   lateinit var Adapter:FileAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        val activity=(requireActivity() as AppCompatActivity)
        activity.setSupportActionBar(binding.toolbarMainFragment)
        binding.recyclerView.layoutManager=GridLayoutManager(requireContext(),3)
        viewModel=ViewModelProvider(this).get(FolderViewModel::class.java)
        val reference=FirebaseDatabase.getInstance().reference
        val auth=FirebaseAuth.getInstance().currentUser
        val request=reference.child("users")
                .addValueEventListener(object :ValueEventListener{
                    var dizi=ArrayList<Tempclass>()
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(i in snapshot.children){
                            val data=i.child("pictures").getValue(holding::class.java)
                              if(data?.data?.dizi!=null){
                                    dizi.addAll(data.data.dizi)
                              }
                        }
                        val anonymous=object :FileAdapter.Listener{
                            override fun onItemClick(value: Int) {
                                if(auth?.uid==dizi.get(value).id){
                                    val intent_Data=dizi.get(value)
                                    val action=MainFragmentDirections.actionMainFragmentToUserFile(intent_Data)
                                    findNavController().navigate(action)
                                }else{
                                    val data=dizi.get(value)
                                    val action=MainFragmentDirections.actionMainFragmentToGuestFragment(data)
                                    findNavController().navigate(action)
                                }
                            }
                        }
                        Adapter= FileAdapter(requireContext(),anonymous)
                        Adapter.refreshDataForId(dizi)
                        binding.recyclerView.adapter=Adapter
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_toolbar_menu,menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.new_folder->{
                findNavController().navigate(R.id.action_mainFragment_to_createFileFragment)
                return true
            }
            R.id.account->{
                val action=R.id.action_mainFragment_to_accountFragment
                findNavController().navigate(action)
                return true
            }
        }
        return false
    }


}