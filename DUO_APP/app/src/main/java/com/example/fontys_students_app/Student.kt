package com.example.fontys_students_app
import  java.io.Serializable;
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
data class Student (var img:String,var Student_number:String,var Fname:String, var Lname:String, var interests:String,var phoneNum:String,var rating1:Long,var availability:Boolean) :
    Parcelable {

    private var first_name:String=Fname;
    private var image:String=img;
    private var student_number:String=Student_number;
    private var last_name:String=Lname;
    private var p_interests:String=interests;
    private var p_knowledge: ArrayList<Knowledge>?=null
    private var p_number:String = phoneNum
    private var p_rating:Long = rating1
    private var p_availability : Boolean = availability
    private var mainLevel:Level ?=null;

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readByte() != 0.toByte()
    ) {
        first_name = parcel.readString()!!
        image = parcel.readString()!!
        student_number = parcel.readString()!!
        last_name = parcel.readString()!!
        p_interests = parcel.readString()!!
        p_number = parcel.readString()!!
        p_rating = parcel.readLong()
        p_availability = parcel.readByte() != 0.toByte()
    }


    fun getFirstName():String
    {
        return  first_name;
    }
    fun getPhoneNumber():String
    {
        return  p_number
    }
    fun getRating():Long
    {
        return  p_rating
    }
    fun getImage():String
    {
        return  image;
    }
    fun getLastName():String
    {
        return  last_name;
    }
    fun getStudentNumber():String
    {
        return  student_number;
    }
    fun getInterestsStudent():String
    {
        return  p_interests;
    }
    fun setKnowledge(arrayKnowledge: ArrayList<Knowledge>)
    {
        p_knowledge = arrayKnowledge
    }
    fun getCurrentAvailability():Boolean
    {
        return p_availability
    }
    fun getKnowledge():ArrayList<Knowledge>
    {
       return p_knowledge!!
    }

    fun getKnowledgeOf():String
    {
        var k :String = "";
       for(i in 0..(p_knowledge!!.size-1)) {
           k=k+ " "+ p_knowledge!![i].toString()
       }
        return  k;
    }
    fun MainLevel(l:Level)
    {
        mainLevel = l
    }
    fun getMainLevel():Level
    {
        return mainLevel!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(img)
        parcel.writeString(Student_number)
        parcel.writeString(Fname)
        parcel.writeString(Lname)
        parcel.writeString(interests)
        parcel.writeString(phoneNum)
        parcel.writeLong(rating1)
        parcel.writeByte(if (availability) 1 else 0)
        parcel.writeString(first_name)
        parcel.writeString(image)
        parcel.writeString(student_number)
        parcel.writeString(last_name)
        parcel.writeString(p_interests)
        parcel.writeString(p_number)
        parcel.writeLong(p_rating)
        parcel.writeByte(if (p_availability) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Student> {
        override fun createFromParcel(parcel: Parcel): Student {
            return Student(parcel)
        }

        override fun newArray(size: Int): Array<Student?> {
            return arrayOfNulls(size)
        }
    }

}
