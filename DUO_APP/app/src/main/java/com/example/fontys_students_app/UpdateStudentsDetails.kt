package com.example.fontys_students_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import kotlinx.android.synthetic.main.activity_update_students_details.*
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import android.content.DialogInterface
import android.content.Intent
import android.widget.ImageButton
import android.widget.TextView
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.FieldPosition

var db = FirebaseFirestore.getInstance()
var studentRef: CollectionReference? =  db.collection("Students")

class UpdateStudentsDetails : AppCompatActivity() {


    var etInterests:EditText ?=null;

    private var recycler: RecyclerView? = null;
    private var layout: RecyclerView.LayoutManager? = null;
    private var adapt: AdapterSkill? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_students_details)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        var spinnerSkill: Spinner = findViewById<Spinner>(R.id.spinnerSkills)
        var stringAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.subjects)
        )
        recycler = findViewById(R.id.recycleViewSkills)
        recycler?.setHasFixedSize(true)
        layout = LinearLayoutManager(this)


        stringAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSkill.adapter = stringAdapter

        var spinnerLevel: Spinner = findViewById(R.id.spinnerLevels)
        spinnerLevel.setAdapter(
            ArrayAdapter<Level>(
                this,
                android.R.layout.simple_list_item_1,
                Level.values()
            )
        )

        var skill: String = ""
        var level: Level = Level.PRO
        spinnerSkill?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val selectedItem = parent!!.getItemAtPosition(position).toString()
                if (selectedItem.equals("Choose subject")) {
                } else {
                    skill = selectedItem;
                }
            }
        }

        spinnerLevel?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val selectedItem = parent!!.getItemAtPosition(position).toString()
                if (selectedItem.equals("PRO")) {
                    level = Level.PRO;
                } else {
                    level = Level.MED
                }
            }
        }
        etInterests = findViewById<EditText>(R.id.et)
        LoadInterests(STUDENT);


        var btnUpdateNow = findViewById<Button>(R.id.btnUpdate)
        btnUpdateNow.setOnClickListener()
        {
            updateArray(skill, level, STUDENT)
        }
    }

    private fun updateArray(skill: String, level: Level,name: String) {
        var msg: String = ""
        db.collection("Students")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        if (document.getString("Student_FName").equals(name)) {
                            val list: ArrayList<String> =
                                document.get("Knowledge") as ArrayList<String>
                            val listL: ArrayList<String> =
                                document.get("Level") as ArrayList<String>

                                if (etInterests!!.hasFocus() != true) {
                                    msg += "Interests field was not updated!\n"
                                }
                                else {
                                    studentRef!!.document(name)
                                        .update("Interests", etInterests!!.text.toString())
                                    msg += "Interests field was updated!\n"
                                }
                                if (list.contains(skill) || skill == "") {
                                    msg += "Your skills were not updated!\n"
                                }
                                else {
                                    studentRef!!.document(name)
                                        .update("Knowledge", FieldValue.arrayUnion(skill))
                                    listL.add(level.toString())
                                    studentRef!!.document(name)
                                        .update("Level", listL)
                                    msg += "New skill was successfully added!"

                            } }}}
                LoadInterests(STUDENT)
                expanView.collapse()
                val builder = AlertDialog.Builder(this)
                val view = LayoutInflater.from(this).inflate(R.layout.alert_box, null)
                val imageButton = view.findViewById(R.id.image) as ImageButton
                val data = view.findViewById<TextView>(R.id.textInfoWhatWentWrong)

                data.setText(msg)

                builder.setPositiveButton("OK",
                    DialogInterface.OnClickListener { dialogInterface, i ->

                    })
                builder.setView(view)
                builder.show()
            }
    }

    private fun LoadInterests(name:String) {
        db.collection("Students")
            .get()
            .addOnCompleteListener { task ->
                var listFinal:ArrayList<Knowledge>?=null;
                if (task.isSuccessful) {
                    for (document in task.result!!) {

                        if (document.getString("Student_FName").equals(name))
                        {
                             etInterests!!.setText(document.getString("Interests")!!)

                            val list:ArrayList<String> = document.get("Knowledge") as ArrayList<String>
                            val listL: ArrayList<String> =document.get("Level") as ArrayList<String>
                            listFinal = ArrayList<Knowledge>()
                            for(i in 0..(list.size-1))
                            {
                                if(listL[i].equals(Level.MED.toString())){
                                    listFinal.add(Knowledge(list[i],Level.MED))}
                                if(listL[i].equals(Level.PRO.toString())){
                                    listFinal.add(Knowledge(list[i],Level.PRO))}
                            }
                        }
                    }
                    setAdapter(listFinal!!)
                    adapt!!.setOnItemClickListener(object : AdapterSkill.OnItemClickListener {
                        @Override
                        override fun onItemClick(position: Int) {
                            UpdateData(listFinal,STUDENT,position) }})
                }
                }
                }
    private fun setAdapter(arr:ArrayList<Knowledge>)
    {
        adapt = AdapterSkill(arr!!);
        recycler?.layoutManager = layout;
        recycler?.adapter = adapt;
    }
    private fun UpdateData(list: ArrayList<Knowledge>,name:String,position: Int)
    {
        if(list.get(position).level==Level.MED) {
//
            val builder = AlertDialog.Builder(this)
            val view = LayoutInflater.from(this).inflate(R.layout.alert_box, null)
            val data = view.findViewById<TextView>(R.id.textInfoWhatWentWrong)

            data.setText("You are going to change your skill"+" "+ list.get(position).getSubjectOfKn() +" to PRO. Are you sure?")

            builder.setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    list.get(position).changeLevelToPro(list.get(position).level)

                    db.collection("Students")
                        .get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                for (document in task.result!!) {
                                    if (document.getString("Student_FName").equals(name)) {

                                        val listL: ArrayList<String> =
                                            document.get("Level") as ArrayList<String>

                                        listL[position] = Level.PRO.toString()
                                        Log.d("sdf",listL[position])
                                        studentRef!!.document(name)
                                            .update("Level", listL)
                                    }}
                                adapt!!.filterList(list)
                            }}})
            builder.setNegativeButton("No",
                DialogInterface.OnClickListener { dialogInterface, i ->
                })
            builder.setView(view)
            builder.show()
        }
    }
    public override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id :Int = item.itemId;

        if(id == android.R.id.home) {
            this.finish()
        }
        return super.onOptionsItemSelected(item)
    }
    }

//            .update("Knowledge",FieldValue.arrayRemove("FF"))




