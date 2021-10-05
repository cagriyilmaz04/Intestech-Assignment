package com.example.intestechassignment

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.intestechassignment.adapter.PictureAdapter
import com.example.intestechassignment.databinding.FragmentUserFileBinding
import com.google.android.material.transition.Hold
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.vmadalin.easypermissions.EasyPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap


class UserFile : Fragment(),EasyPermissions.PermissionCallbacks {
    private var _binding:FragmentUserFileBinding?=null
    private val binding get()=_binding!!
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>
    private lateinit var activity:ActivityResultLauncher<Intent>
    private val args by navArgs<UserFileArgs>()
    var newString=""
    var newStringBitmap=""
    var reference=FirebaseStorage.getInstance().reference
    var images=reference.child("images")
    val auth=FirebaseAuth.getInstance().currentUser
    lateinit var Adapter:PictureAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
          activityResultLauncher=registerForActivityResult(ActivityResultContracts.GetContent(),
                  ActivityResultCallback {
                      if(it!=null){
                          binding.imageViewCamera.setImageURI(it)
                          val file=it
                          val a=UUID.randomUUID()
                          val identify=images.child(args.sendingDatas.uid)
                                  .child(a.toString()+".jpg")
                          identify.putFile(file)
                                  .addOnFailureListener {
                                      Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                                  }.addOnSuccessListener {
                                      val newa=  FirebaseStorage.getInstance().reference.child("images").child(args.sendingDatas.uid).child(a.toString()+".jpg")
                                      newa.downloadUrl.addOnSuccessListener {
                                            newString=it.toString()
                                      }
                                  }
                      }
                  })
       activity=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->

           try{
               val selectedPhotoUri = result.data?.extras?.get("data") as Bitmap
               val baos=ByteArrayOutputStream()
               selectedPhotoUri.compress(Bitmap.CompressFormat.JPEG,100,baos)
               val data=baos.toByteArray()
               val az=UUID.randomUUID()
               var uploadTask=FirebaseStorage.getInstance().reference.child("images")
                       .child(args.sendingDatas.uid)
                       .child(az.toString())
                       .putBytes(data)
                       .addOnSuccessListener {
                           val newa= FirebaseStorage.getInstance().reference.child("images")
                                   .child(args.sendingDatas.uid)
                                   .child(az.toString()).downloadUrl.addOnSuccessListener {
                                       newStringBitmap=it.toString()
                                   }
                       }
           }catch (e:Exception){
           }

       }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding= FragmentUserFileBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbarUserFile)
        binding.toolbarUserFile.title=args.sendingDatas.name+ args.sendingDatas.filename
        binding.imageViewCamera.setOnClickListener {
                if(hasCameraPermission()){
                    val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    activity.launch(intent)
                }else{
                    requestCameraPermission()
                }
        }
        binding.imageViewFile.setOnClickListener {
                if(hasReadExternalPermission()){
                 val intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                  val a=  activityResultLauncher.launch("image/*")
                }else{
                  requestReadExternalPermission()
                }
        }
        binding.buttonKaydetFragmentUser.setOnClickListener {
            val situation=binding.editTextAciklama.text.toString()
            if(situation.isNullOrEmpty()){
                Toast.makeText(requireContext(),requireContext().resources.getString(R.string.bos_birakamazsiniz),Toast.LENGTH_LONG).show()
            }
            if(newString!=""&&(situation.isNullOrEmpty()==false)){
                val File=file(newString,situation)
                AddData(File)
                binding.editTextAciklama.setText("")
            }
            if(newStringBitmap!=""&&(situation.isNullOrEmpty()==false)){
                val File=file(newStringBitmap,situation)
                AddData(File)
                binding.editTextAciklama.setText("")
            }

        }
        binding.recyclerViewPictures.layoutManager=GridLayoutManager(requireContext(),3)
        val auth=FirebaseAuth.getInstance().currentUser?.uid
        val reference=FirebaseDatabase.getInstance().reference.child("users")
        binding.buttonDuzenleUserFile.setOnClickListener {
            val text=binding.editTextKlasorAdi.text.toString()
            if(!(text.isNullOrEmpty())){
                val request=FirebaseDatabase.getInstance().reference.child("users")
                    .child(auth.toString())
                    .child("pictures")
                    .child("data")
                    .addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val data=snapshot.getValue(HoldingArray::class.java)
                            for(i in 0 until data?.dizi?.size!!){
                                if(data.dizi.get(i).uid==args.sendingDatas.uid){
                                    val updateInfo=HashMap<String,Any>()
                                    updateInfo["filename"]=text
                                    args.sendingDatas.filename=text
                                    FirebaseDatabase.getInstance().reference.child("users")
                                        .child(auth.toString())
                                        .child("pictures")
                                        .child("data").child("dizi").child(i.toString()).updateChildren(updateInfo)
                                }
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }

                    })


            }else{
                Toast.makeText(requireContext(),requireContext().resources.getString(R.string.bos_birakamazsiniz),Toast.LENGTH_LONG).show()
            }

        }

        GlobalScope.launch {
            val sorgu=   reference.child(auth!!)
                    .child("pictures")
                    .child("data")
                    .addValueEventListener(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            try {
                                val data=snapshot.getValue(HoldingArray::class.java)

                                if(data?.dizi?.size!=null|| data?.dizi?.size!! >0){
                                    for(i in 0 until data.dizi.size){
                                        if(data.dizi.get(i)!= null&&data.dizi.get(i).uid==args.sendingDatas.uid){

                                            val listener=object :PictureAdapter.listener{
                                                override fun onItemClick(position: Int) {
                                                    val temp=args.sendingDatas
                                                    val image=data.dizi.get(i).Picture.get(position)
                                                    val data=SendingImageClass(temp.name,temp.password,temp.id,temp.filename,temp.uid,image)
                                                    val action=UserFileDirections.actionUserFileToUserPictureFragment(data)
                                                    findNavController().navigate(action)
                                                }
                                            }
                                            Adapter= PictureAdapter(listener)
                                            recyclerFunction(data.dizi.get(i).Picture)
                                        }
                                    }
                                }
                            }catch (e:Exception){
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
        }

    }

    private fun recyclerFunction(picture: ArrayList<file>) {
        Adapter.refreshData(picture)
        binding.recyclerViewPictures.adapter=Adapter
        binding.recyclerViewPictures.adapter?.notifyDataSetChanged()
    }

    private fun hasCameraPermission()=EasyPermissions.hasPermissions(requireContext(),Manifest.permission.CAMERA)
    private fun requestCameraPermission(){
        if(!hasCameraPermission()){
            EasyPermissions.requestPermissions(this,"Bu İzni Vermeden Kamerayı Kullanamazsınız",
                CAMERA_CODE,Manifest.permission.CAMERA)
        }
    }
    private fun hasReadExternalPermission()=EasyPermissions.hasPermissions(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
    private fun requestReadExternalPermission(){
        if(!hasReadExternalPermission()){
            EasyPermissions.requestPermissions(this,"Bu İzni Vermeden Dosyalara Erişemezsiniz",
                READ_EXTERNAL_CODE,Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
    companion object{
        val CAMERA_CODE=2
        val READ_EXTERNAL_CODE=3
        val CAMERA_REQUEST_CODE=200
    }
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if(EasyPermissions.somePermissionDenied(this,Manifest.permission.CAMERA)&&requestCode== CAMERA_CODE){
        }else{
            requestCameraPermission()
        }
        if(EasyPermissions.somePermissionDenied(this,Manifest.permission.READ_EXTERNAL_STORAGE)&&requestCode== READ_EXTERNAL_CODE){
        }else{
            requestReadExternalPermission()
        }
    }
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {}
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.user_file_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when(item.itemId){
                R.id.user_sil->{
                    delete()
                    return true
                }
                R.id.user_duzenle->{
                    return true
                }
            }

        return false
    }
    private fun delete(){
        val current=  FirebaseAuth.getInstance().currentUser
       val db= FirebaseDatabase.getInstance().reference.child("users")
                .child(current?.uid!!)
               .child("pictures")
                .child("data").child("dizi")
               db.addValueEventListener(object :ValueEventListener{
                   override fun onDataChange(snapshot: DataSnapshot) {
                       for(i in 0 until  snapshot.childrenCount){
                           val data=snapshot.child(i.toString()).getValue(Tempclass::class.java)
                           if(data?.uid==args.sendingDatas.uid){

                                    val deletedData=FirebaseDatabase.getInstance().reference.child("users")
                                            .child(current.uid)
                                            .child("pictures")
                                            .child("data").child("dizi").child(i.toString()).removeValue().addOnSuccessListener {
                                                Toast.makeText(requireContext(),requireContext().resources.getString(R.string.basarili),Toast.LENGTH_LONG).show()
                                                findNavController().navigate(R.id.action_userFile_to_mainFragment)
                                                    GlobalScope.launch {
                                                        val sorgu=FirebaseDatabase.getInstance().reference.child("users")
                                                            .child(auth?.uid!!)
                                                            .child("pictures")
                                                            .child("data")
                                                        sorgu.addValueEventListener(object :ValueEventListener{
                                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                                val data=snapshot.getValue(HoldingArray::class.java)

                                                                var dizi=ArrayList<Tempclass>()
                                                                if(data?.dizi?.size!=null){
                                                                    for(i in 0 until data.dizi.size){
                                                                        if(data.dizi.get(i).filename==null){

                                                                        }else{
                                                                              dizi.add(data.dizi.get(i))
                                                                        }
                                                                    }
                                                                }
                                                                val x=HoldingArray(dizi)
                                                                val y=holding(x)
                                                                FirebaseDatabase.getInstance().reference.child("users")
                                                                    .child(auth.uid)
                                                                    .child("pictures")
                                                                    .child("data")
                                                                        .setValue(x)
                                                            }
                                                            override fun onCancelled(error: DatabaseError) {
                                                            }
                                                        })
                                                    }

                                            }.addOnSuccessListener {
                                            }
                                }
                       }
                   }
                   override fun onCancelled(error: DatabaseError) {
                   }
               })
    }
    private fun AddData(image:file){

        val referans=FirebaseDatabase.getInstance().reference.child("users").child(auth?.uid.toString()).child("pictures")
        val sorgu=referans.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data=snapshot.getValue(holding::class.java)
                for(i in 0 until data?.data?.dizi?.size!!){
                    if(data.data.dizi.get(i).uid==args.sendingDatas.uid){
                        val ntry=snapshot.child("data")
                                .child("dizi")
                                .child(i.toString()).getValue(Tempclass::class.java)
                        val new_data= arrayListOf<file>(image)

                        GlobalScope.launch {
                            val size=ntry?.Picture?.size
                            if(size==0){
                                val a=Tempclass(args.sendingDatas.name,args.sendingDatas.password,args.sendingDatas.id,args.sendingDatas.filename,args.sendingDatas.uid,new_data)
                                FirebaseDatabase.getInstance().reference.child("users").child(auth?.uid!!)
                                        .child("pictures")
                                        .child("data")
                                        .child("dizi").child(i.toString())
                                        .setValue(a).addOnSuccessListener {
                                        }
                            }else{
                                val new_list=ArrayList<file>()
                                new_list.addAll(ntry?.Picture!!)
                                new_list.add(image)
                                val a=Tempclass(args.sendingDatas.name,args.sendingDatas.password,args.sendingDatas.id,args.sendingDatas.filename,args.sendingDatas.uid,new_list)
                                FirebaseDatabase.getInstance().reference.child("users").child(auth?.uid!!)
                                        .child("pictures")
                                        .child("data")
                                        .child("dizi").child(i.toString())
                                        .setValue(a).addOnSuccessListener {
                                        }
                            }
                        }
                        break
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })



    }

}