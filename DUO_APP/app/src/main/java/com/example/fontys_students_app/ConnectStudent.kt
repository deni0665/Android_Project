package com.example.fontys_students_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import android.telephony.PhoneNumberUtils
import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_user_account.*
import java.util.*


class ConnectStudent : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()

    var studentRef: CollectionReference? = db.collection("Students")

    var student: Student? = null
    var image: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_student)

        val i = intent
        student = i.getParcelableExtra("student")
//        var link = i.getStringExtra("pic")
//        var phone_number = i.getStringExtra("phoneNum")
//        var rating_student = i.getLongExtra("rating",0)
        var link = student!!.getImage()
        var phone_number = student!!.getPhoneNumber()
        var rating_student = student!!.getRating()
        image = findViewById<ImageView>(R.id.imageStudentBigger)

        Glide.with(this)
            .load(link)
            .placeholder(R.drawable.ic_android)
            .into(image!!);
        var whatsApp = findViewById<Button>(R.id.btnConnectWhatsAPP)
        whatsApp.setOnClickListener(
            {
                openWhatsApp(phone_number)
            }
        )
        var rate = findViewById<RatingBar>(R.id.ratingStudent)
        rate.setRating(rating_student.toFloat());
        rate.setOnRatingBarChangeListener(OnRatingBarChangeListener { ratingBar, rating, fromUser ->
            val builder = AlertDialog.Builder(this)
            val view = LayoutInflater.from(this).inflate(R.layout.alert_box, null)
            val data = view.findViewById<TextView>(R.id.textInfoWhatWentWrong)

            data.setText("You send rating for " + student!!.getFirstName() + " " + student!!.getLastName() + ". " + "Are you sure?")

            builder.setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    if (rating == 1.toFloat()) {
                        studentRef!!.document(student!!.getFirstName())
                            .update("star_1", FieldValue.increment(1))
                    }
                    if (rating == 2.toFloat()) {
                        studentRef!!.document(student!!.getFirstName())
                            .update("star_2", FieldValue.increment(1))
                    }
                    if (rating == 3.toFloat()) {
                        studentRef!!.document(student!!.getFirstName())
                            .update("star_3", FieldValue.increment(1))
                    }
                    if (rating == 4.toFloat()) {
                        studentRef!!.document(student!!.getFirstName())
                            .update("star_4", FieldValue.increment(1))
                    }
                    if (rating == 5.toFloat()) {
                        studentRef!!.document(student!!.getFirstName())
                            .update("star_5", FieldValue.increment(1))
                    }
                    var total: Long? = 0
                    db.collection("Students")
                        .get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                for (document in task.result!!) {
                                    if (document.get("Student_FName").toString().contains(student!!.getFirstName()!!.toRegex())) {
                                        total =
                                            (1 * document.getLong("star_1")!! + 2 * document.getLong(
                                                "star_2"
                                            )!! +
                                                    3 * document.getLong("star_3")!! + 4 * document.getLong(
                                                "star_4"
                                            )!! +
                                                    5 * document.getLong("star_5")!!) / (document.getLong(
                                                "star_1"
                                            )!! + document.getLong("star_2")!!
                                                    + document.getLong("star_3")!! + document.getLong(
                                                "star_4"
                                            )!! + document.getLong("star_5")!!)

                                        studentRef!!.document(student!!.getFirstName())
                                            .update("Rating", total)
                                    }
                                }
                            }

                        }
                    intent = Intent(getBaseContext(), MainActivity::class.java)
                    intent.putExtra(
                        "refreshRating",
                        "Your rating was send to" + " " + student!!.getLastName() + " " + student!!.getLastName() + "!"
                    )
                    navigateUpTo(intent)
                })
            builder.setNegativeButton("No",
                DialogInterface.OnClickListener { dialogInterface, i ->
                })
            builder.setView(view)
            builder.show()
        })


    }

    private fun openWhatsApp(number: String) {
        var number = number
        Log.d("aha", student!!.getCurrentAvailability().toString())
        if (student!!.getCurrentAvailability() == true && student!!.getFirstName() != STUDENT) {
            try {
                number = number.replace(" ", "").replace("+", "")

                val sendIntent = Intent("android.intent.action.MAIN")
                sendIntent.component = ComponentName("com.whatsapp", "com.whatsapp.Conversation")
                sendIntent.putExtra(
                    "jid",
                    PhoneNumberUtils.stripSeparators(number) + "@s.whatsapp.net"
                )
                this.startActivity(sendIntent)
            } catch (e: Exception) {
                Log.e("WhatsApp", "ERROR_OPEN_WHATSAPP")
            }
        } else {
            if (student!!.getFirstName() == STUDENT) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "You can't contact yourself!",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "This person is currently not available!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}

