package com.example.fontys_students_app

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

class NoConnection : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_connection)
        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        var intentM = Intent(this,MainActivity::class.java)
        this.registerReceiver(INTERNET_CONNECTION(intentM,null), intentFilter)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
    public override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id :Int = item.itemId;

        if(id == android.R.id.home) {
            var i = Intent(this,MainActivity::class.java)
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item)
    }
}
