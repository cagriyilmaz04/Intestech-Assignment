package com.example.intestechassignment

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize
@IgnoreExtraProperties
@Parcelize
data class PasswordName (val name:String?=null, var password:String?=null, var id:String?=null,var email:String=""):Parcelable{}

@Parcelize
@IgnoreExtraProperties
data class file(var Picture:String="",var situation:String=""):Parcelable


@IgnoreExtraProperties
class Data(var image: ArrayList<String> =ArrayList<String>())

@IgnoreExtraProperties
@Parcelize
class Tempclass(var name: String="",var password:String="",var id:String="",var filename:String="",var uid:String="",var Picture:ArrayList<file> = ArrayList()):Parcelable

@IgnoreExtraProperties
class HoldingArray(var dizi:ArrayList<Tempclass> = ArrayList<Tempclass>())

@IgnoreExtraProperties
class holding(var data:HoldingArray=HoldingArray())

@Parcelize
data class SendingImageClass(var name: String="",var password:String="",var id:String="",var filename:String="",var uid:String="",var image:file=file()):Parcelable



