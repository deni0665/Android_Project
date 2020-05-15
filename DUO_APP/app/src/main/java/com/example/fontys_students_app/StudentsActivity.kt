package com.example.fontys_students_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.os.DropBoxManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import java.util.*
import kotlin.collections.ArrayList
import com.google.common.graph.ElementOrder.sorted
import com.google.firebase.storage.FirebaseStorage
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference

class StudentsActivity : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()

    var students: ArrayList<Student>? = null;
    private var recycler: RecyclerView? = null;
    private var layout: RecyclerView.LayoutManager? = null;
    private var adapt: AdapterStudent? = null;
    private var position =0;
    private var subject :Subject ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //to get the back button
        val i = intent
         subject = i.getParcelableExtra<Subject>("Example name")
        position = i.getIntExtra("Example position",5)
        recycler = findViewById(R.id.recyclerView)
        recycler?.setHasFixedSize(true)
        layout = LinearLayoutManager(this)
        students = ArrayList<Student>()
        LoadStudents(subject!!)

        var mySpinner: Spinner = findViewById<Spinner>(R.id.SpinnerSorting)
        var stringAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.sortBy)
        )
        stringAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinner.adapter = stringAdapter
        mySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent!!.getItemAtPosition(position).toString()
                sortBy(selectedItem)
            }
        }
    }
        private fun LoadStudents(s: Subject) {
            students = ArrayList<Student>()

            db.collection("Students")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            val list: ArrayList<String> =
                                document.get("Knowledge") as ArrayList<String>
                            val listL: ArrayList<String> =
                                document.get("Level") as ArrayList<String>
                            val listFinal: ArrayList<Knowledge> = ArrayList<Knowledge>()
                            for (i in 0..(list.size - 1)) {
                                if (listL[i].equals(Level.MED.toString())) {
                                    listFinal.add(Knowledge(list[i], Level.MED))
                                }
                                if (listL[i].equals(Level.PRO.toString())) {
                                    listFinal.add(Knowledge(list[i], Level.PRO))
                                }
                            }
                            listFinal.sortWith(compareBy({ it.getSubjectOfKn() }))

                            if (document.get("Knowledge").toString().contains(s.getName()!!.toRegex())) {
                               var s:Student ?=null
                                   s = Student(
                                        document.getString("Image")!!,
                                        document.getString("Student_Number")!!,
                                        document.getString("Student_FName")!!,
                                        document.getString("Student_LName")!!,
                                        document.getString("Interests")!!,
                                        document.getString("Phone_Number")!!,
//                                        document.get("Rating") as Long
                                        document.getLong("Rating")!!,
                                       document.getBoolean("Availability")!!
                                )
                                for (i in 0..(listFinal.size - 1)) {
                                    if(listFinal[i].getSubjectOfKn().contains(subject!!.getName().toString())) {
                                        s.MainLevel(listFinal[i].getLevelOfKn())
                                    }
                                }
                                s.setKnowledge(listFinal)
                                students!!.add(s)
                            }
                        }
                        setAdapter()
                        adapt!!.setOnItemClickListener(object : AdapterStudent.OnItemClickListener {
                            @Override
                            override fun onItemClick(position: Int) {
                                openStudentMessageActivity(position)
                            }
                        })
                    }
                }
        }

    private fun sortBy(item:String)
    {
        if (item.equals("Last Name")) {
            if (students!!.size != 0) {
                students!!.sortBy { it.getLastName() }
            }
        }
             if(item.equals("First Name")) {
                if (students!!.size != 0)
                {
                    students!!.sortBy { it.getFirstName() }
                }
            }
         if(item.equals("Level")) {
            if (students!!.size != 0)
            {
//                students!!.sortWith (compareBy({ it.getKnowledge()[position].getLevelOfKn()}))
                students!!.sortBy{it.getMainLevel()}
                }
            }
        setAdapter()
        adapt!!.setOnItemClickListener(object : AdapterStudent.OnItemClickListener {
            @Override
            override fun onItemClick(position: Int) {
                openStudentMessageActivity(position)
            }
        })        }

    private fun setAdapter()
    {
        adapt = AdapterStudent(students!!);
        recycler?.layoutManager = layout;
        recycler?.adapter = adapt;
    }
    private fun openStudentMessageActivity(positionNow:Int)
    {
        intent = Intent(this,ConnectStudent::class.java)
        intent.putExtra("student", students!!.get(positionNow))
        startActivity(intent)
    }

    public override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id :Int = item.itemId;

        if(id == android.R.id.home) {
            this.finish()
        }
        return super.onOptionsItemSelected(item)
        }
    }
