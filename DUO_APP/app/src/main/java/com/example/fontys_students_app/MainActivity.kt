package com.example.fontys_students_app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.FieldPosition
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import java.lang.Exception


var STUDENT :String = "Anna"

class MainActivity : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()

    private val TAG = MainActivity::class.java.simpleName
    var subjects: ArrayList<Subject>? = null;
    private var recycler: RecyclerView? = null;
    private var layout: RecyclerView.LayoutManager? = null;
    private var adapt: AdapterSubject? = null;
    var iv:ImageView ?=null;

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        var selectedFragment:Fragment?=null
        when(it.itemId)
        {
            R.id.nav_home->{
                return@OnNavigationItemSelectedListener true}
            R.id.nav_change->{
                intent = Intent(this,UpdateStudentsDetails::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true}
            R.id.nav_account->{
                intent = Intent(this,UserAccount::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var intentM = Intent(this,NoConnection::class.java)

        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        this.registerReceiver(INTERNET_CONNECTION(null,intentM), intentFilter)

        var menu = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.menu)
        menu.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        subjects = ArrayList<Subject>()
        recycler = findViewById(R.id.recyclerView)
        recycler?.setHasFixedSize(true)
        layout = LinearLayoutManager(this);
        LoadDataFromFirestore();

        var editTextB = findViewById<EditText>(R.id.editText)
        editTextB.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                filter(s.toString()); }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int) {}

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int) {} })
        val i = intent
        var message = i.getStringExtra("refreshRating")
        if(message!=null) {
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
        }
    }
//    private fun replaceFragment(fragment: Fragment)
//    {
//        val fragmentTrans = supportFragmentManager.beginTransaction()
//        fragmentTrans.replace(R.id.fragment,fragment)
//        fragmentTrans.commit()
//    }


    private fun LoadDataFromFirestore() {
        db.collection("Subjects")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        subjects!!.add(
                            Subject(
                                R.drawable.ic_android,
                                document.getString("Name"),
                                document.getString("Tool"))
                        ); }
                    adapt = AdapterSubject(subjects!!);
                    recycler?.layoutManager = layout;
                    recycler?.adapter = adapt;

                    adapt!!.setOnItemClickListener(object : AdapterSubject.OnItemClickListener {
                        @Override
                        override fun onItemClick(position: Int) {
//                            subjects!!.get(position).ChangeTextTesting("Clicked!!!!");
                          openActivityStudentsList(position)
                            }

                    })
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }


    private fun openActivityStudentsList(position: Int)
    {
        intent = Intent(this,StudentsActivity::class.java)
        intent.putExtra("Example name",subjects!!.get(position))
        intent.putExtra("Example position",position)
        startActivity(intent)
    }
    private fun openUpdateActivity()
    {
        intent = Intent(this,UpdateStudentsDetails::class.java)
        startActivity(intent)
    }
    private fun filter(text:String)
    {
        var filteredsubjects = ArrayList<Subject>()
        if(subjects!=null) {
            for (item: Subject in subjects!!) {
                if(item.getName()!!.toLowerCase().contains(text.toLowerCase()))
                {
                    filteredsubjects.add(item);
                }
            }
            adapt!!.filterList(filteredsubjects);
        }
    }
    }