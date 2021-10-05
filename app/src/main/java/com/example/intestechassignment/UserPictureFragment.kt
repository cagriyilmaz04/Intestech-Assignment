package com.example.intestechassignment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.example.intestechassignment.databinding.FragmentUserPictureBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserPictureFragment : Fragment() {
    private var _binding : FragmentUserPictureBinding?=null
    private val binding get()=_binding!!
    var sayi=0
    val args by navArgs<UserPictureFragmentArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentUserPictureBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbarUserPicture)
        val image=args.userPicture?.image?.Picture
        Picasso.get().load(image).into(binding.imageViewUserPicture)
        binding.editTextSituation.setText(args.userPicture?.image?.situation)
        binding.buttonKaydetUserPicture.setOnClickListener {
            val text=binding.editTextSituation.text.toString()
            if(args.userPicture?.image?.situation==text){
                Toast.makeText(requireContext(),requireContext().resources.getString(R.string.bir_degisiklik_yapmadınız),Toast.LENGTH_LONG).show()
            }else if(text.isNullOrEmpty()){
                Toast.makeText(requireContext(),requireContext().resources.getString(R.string.bos_birakamazsiniz),Toast.LENGTH_LONG).show()
            }else{
                    updateData(text)
            }

        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.pictures_delete_user,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete_picture->{
                ++sayi
                Toast.makeText(requireContext(),sayi.toString(),Toast.LENGTH_LONG).show()
            val alertImage=AlertDialog.Builder(requireContext())
                    alertImage.setTitle(requireContext().resources.getString(R.string.uyari))
                alertImage.setMessage(requireContext().resources.getString(R.string.silmek_istediginize))
                alertImage.setPositiveButton(requireContext().resources.getString(R.string.evet)){dialog,i->
                deleteImage()

                }
                alertImage.setNegativeButton(requireContext().resources.getString(R.string.hayir)){dialog,i->
                }
                alertImage.create().show()
                return true
            }
        }
        return false
    }
fun deleteImage(){
    val reference=FirebaseDatabase.getInstance().reference
    val auth=args.userPicture?.id
    val uid=args.userPicture?.uid
    val request= reference.child("users")
        .child(auth!!)
        .child("pictures")
        .child("data")
    request.addValueEventListener(object :ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val data=snapshot.getValue(HoldingArray::class.java)
            val size=data?.dizi?.size
            if(data!=null&&size!!>0){
                for(i in 0 until size){
                    if(uid==data.dizi.get(i).uid){
                        for(j in 0 until data.dizi.get(i).Picture.size){
                            if(data.dizi.get(i).Picture.get(j)==args.userPicture?.image){
                                Toast.makeText(requireContext(),j.toString(),Toast.LENGTH_LONG).show()
                                FirebaseDatabase.getInstance().reference.
                                child("users")
                                    .child(auth)
                                    .child("pictures")
                                    .child("data")
                                    .child("dizi")
                                    .child(i.toString())
                                    .child("picture")
                                    .child(j.toString())
                                    .removeValue().addOnSuccessListener {
                                        Toast.makeText(requireContext(),requireContext().resources.getString(R.string.gorsel_silindi),Toast.LENGTH_LONG).show()
                                           GlobalScope.launch(Dispatchers.IO) {
                                               val requesting=reference.child("users")
                                                       .child(args.userPicture?.id.toString())
                                                       .child("pictures")
                                                       .child("data")
                                               requesting.addValueEventListener(object :ValueEventListener{
                                                   override fun onDataChange(snapshot: DataSnapshot) {
                                                       val new_array=ArrayList<file>()
                                                       var index=-1
                                                       val data=snapshot.getValue(HoldingArray::class.java)
                                                       if(data!=null){
                                                           if(data?.dizi.size!=null){
                                                               for(i in 0 until data.dizi.size){
                                                                   if(data.dizi.get(i).filename==args.userPicture?.filename){
                                                                       if(data.dizi.get(i).Picture!=null){
                                                                           index=i
                                                                           for(j in 0 until data.dizi.get(i).Picture.size){
                                                                               if(data.dizi.get(i).Picture.get(j)!=null){
                                                                                   new_array.add(data.dizi.get(i).Picture.get(j))
                                                                               }
                                                                           }
                                                                       }
                                                                   }
                                                               }
                                                               reference.child("users")
                                                                       .child(args.userPicture?.id.toString())
                                                                       .child("pictures")
                                                                       .child("data")
                                                                       .child("dizi")
                                                                       .child(index.toString())
                                                                       .child("picture")
                                                                       .setValue(new_array)
                                                           }
                                                       }
                                                   }
                                                   override fun onCancelled(error: DatabaseError) {
                                                   }
                                               })
                                           }
                                    }

                            }
                        }
                    }
                }
            }
        }
        override fun onCancelled(error: DatabaseError) {

        }

    })

}

fun updateData(situation:String){
    val reference=FirebaseDatabase.getInstance().reference
    val auth=args.userPicture?.id
   val request= reference.child("users")
            .child(auth.toString())
            .child("pictures")
           request.addValueEventListener(object :ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                   val data=snapshot.getValue(holding::class.java)
                   for(i in 0 until data?.data?.dizi?.size!!){
                       if(data.data.dizi.get(i).filename==args.userPicture?.filename){
                           Toast.makeText(requireContext(),i.toString(),Toast.LENGTH_LONG).show()
                           for(j in 0 until data.data?.dizi.get(i).Picture.size){
                               if(data.data.dizi.get(i).Picture.get(j).Picture==args.userPicture?.image?.Picture&&data.data.dizi.get(i).Picture.get(j).situation==args.userPicture?.image?.situation){
                                   val update=HashMap<String,Any>()
                                   update["situation"]=situation
                                   reference.child("users")
                                           .child(auth.toString())
                                           .child("pictures")
                                           .child("data").child("dizi")
                                           .child(i.toString())
                                           .child("picture")
                                           .child(j.toString())
                                           .updateChildren(update)
                                           .addOnSuccessListener {
                                               Toast.makeText(requireContext(),requireContext().resources.getString(R.string.basariyla_guncellendi),Toast.LENGTH_LONG).show()
                                           }
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