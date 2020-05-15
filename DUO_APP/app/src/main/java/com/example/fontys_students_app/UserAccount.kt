package com.example.fontys_students_app

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FieldValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_user_account.*
import java.lang.Exception
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.HashMap

class UserAccount : AppCompatActivity() {

    val PICK_FROM_GALLERY =1
    var Fname: TextView? = null
    var ImageURL: ImageView? = null
    var student_number :String = ""
    internal var storageRef: StorageReference?=null
    var NEWimageUri: Uri?=null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_navigation, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_changePic-> {
                hasPermissions()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_account)
        Fname = findViewById(R.id.tbFNameStudent)
        ImageURL = findViewById(R.id.imAccount)
        ImageURL = findViewById(R.id.imAccount)

        storageRef = FirebaseStorage.getInstance()!!.getReference("Photos")

        LoadData("Anna")
        switchAvailability.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                setAvailability(true, STUDENT)
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "You are now available! People can contact you!",
                    Snackbar.LENGTH_SHORT
                ).show()

            } else {
                setAvailability(false, "Anna")
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "You are not available any more! People can not contact you!",
                    Snackbar.LENGTH_SHORT
                ).show()

            }
        }
        testButton.setOnClickListener({
            val builder = AlertDialog.Builder(this)
            val view = LayoutInflater.from(this).inflate(R.layout.alert_box, null)
            val data = view.findViewById<TextView>(R.id.textInfoWhatWentWrong)
            data.setText("Are you sure you want to change your account picture?")

            builder.setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    uploadFile()
                    Snackbar.make(findViewById(android.R.id.content), "Your account picture was changed!", Snackbar.LENGTH_SHORT).show()

                })
            builder.setNegativeButton("No",
                DialogInterface.OnClickListener { dialogInterface, i ->

                    Snackbar.make(findViewById(android.R.id.content), "Your account picture was not changed!", Snackbar.LENGTH_SHORT).show()
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            this@UserAccount.finish()
                            Log.d("HELLOW","SAD")
                        }
                    }, 1100)
                    

                })
            builder.setView(view)
            builder.show()
        })
    }

    private fun LoadData(name: String) {
        db.collection("Students")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        if (document.getString("Student_FName").equals(name)) {
                            Fname!!.setText(document.getString("Student_FName"))
                            switchAvailability.isChecked = (document.getBoolean("Availability")!!)
                                Glide.with(this)
                                .load(document.getString("Image"))
                                .placeholder(R.drawable.ic_android)
                                .into(ImageURL!!);
                                 student_number = document.getString("Student_Number")!!
                        }
                    }
                }
            }
    }

    fun setAvailability(av: Boolean, name: String) {
        db.collection("Students")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        if (document.getString("Student_FName").equals(name)) {
                            studentRef!!.document(name)
                                .update("Availability", av)
                        }
                    }
                }
            }
    }
    private fun hasPermissions() {
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PICK_FROM_GALLERY)
            } else {
                openFileChooser()
            }
        } catch (e:Exception) {
            e.printStackTrace();
        }
    }
    private fun openFileChooser()
    {
        val openGallery = Intent()
        openGallery.setType("image/*")//only to see the images
        openGallery.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(openGallery, PICK_FROM_GALLERY)
    }

    private fun uploadFile()
    {
        if(NEWimageUri!=null)
        {
            val imageRef = storageRef!!.child(student_number + ".jpeg" )

            imageRef.putFile(NEWimageUri!!)
                .addOnSuccessListener {
                    it.getMetadata()!!.getReference()!!.getDownloadUrl().addOnSuccessListener {
                       var hashmap : HashMap<String,String> = HashMap()
                        hashmap.put("Image",it.toString())
                        studentRef!!.document("Anna")
                            .update("Image", hashmap.getValue("Image"))
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "did not work", Toast.LENGTH_SHORT).show()
                }
        }
        else {
            Toast.makeText(this, "not", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==PICK_FROM_GALLERY&& resultCode== Activity.RESULT_OK && data!=null)
        {
            NEWimageUri = data.data;
//            Log.d("jasdf",NEWimageUri!!.path!!.substring(NEWimageUri!!.path!!.lastIndexOf(".")))
            Glide.with(this)
                .load(NEWimageUri)
                .placeholder(R.drawable.ic_android)
                .into(imAccount);

        }
    }
}

