package com.example.fontys_students_app

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import java.lang.System.out

    data class Subject(var picIn:Int,var nameIn:String?,var data:String?):Parcelable {

      private var pic: Int = picIn;
      private var name: String? = nameIn;
      private var explain: String? = data;


    constructor(parcel: Parcel) : this(
          parcel.readInt(),
          parcel.readString(),
          parcel.readString()
    )
           {
          pic = parcel.readInt()
          name = parcel.readString()
          explain = parcel.readString()
           }


      fun getPicture(): Int {
          return pic;
      }

      fun getName(): String? {
          return name;
      }

      fun getExplanation(): String? {
          return explain;
      }

      override fun writeToParcel(parcel: Parcel, flags: Int) {
          parcel.writeInt(picIn)
          parcel.writeString(nameIn)
          parcel.writeString(data)
          parcel.writeInt(pic)
          parcel.writeString(name)
          parcel.writeString(explain)
      }

      override fun describeContents(): Int {
          return 0
      }

      companion object CREATOR : Parcelable.Creator<Subject> {
          override fun createFromParcel(parcel: Parcel): Subject {
              return Subject(parcel)
          }

          override fun newArray(size: Int): Array<Subject?> {
              return arrayOfNulls(size)
          }
      }
  }
