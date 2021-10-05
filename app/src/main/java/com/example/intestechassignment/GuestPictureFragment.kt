package com.example.intestechassignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.intestechassignment.databinding.FragmentGuestBinding
import com.example.intestechassignment.databinding.FragmentGuestPictureBinding
import com.squareup.picasso.Picasso


class GuestPictureFragment : Fragment() {
    private var _binding:FragmentGuestPictureBinding?=null
    private val binding get()=_binding!!
    private val args by navArgs<GuestPictureFragmentArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding= FragmentGuestPictureBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Picasso.get().load(args.imageClass.Picture).into(binding.imageViewPicturesFragment)
        binding.textViewGuest.text=args.imageClass.situation
    }

}