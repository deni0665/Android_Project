package com.example.fontys_students_app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import android.net.wifi.WifiInfo
import androidx.core.content.ContextCompat.getSystemService
import android.net.wifi.WifiManager




 class INTERNET_CONNECTION (intentMain:Intent?,intentNoInternet:Intent?) : BroadcastReceiver() {

    var intentMainMain: Intent? = intentMain
    var intentNoInternetInternet: Intent? = intentNoInternet

    override fun onReceive(context: Context?, intent: Intent?) {

        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork != null) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {

                val wifiManager = context!!.getSystemService(Context.WIFI_SERVICE) as WifiManager?
                val info = wifiManager!!.connectionInfo
                val ssid = info.ssid

                Toast.makeText(context, ssid, Toast.LENGTH_LONG).show()

                if(ssid.toLowerCase().contains("EDUROAM".toLowerCase())) {
                    studentRef!!.document(STUDENT)
                        .update("Availability", true)
                }
                }

                if (intentMainMain != null) {
                    context.startActivity(intentMainMain);
                }

            } else if (activeNetwork?.type == ConnectivityManager.TYPE_MOBILE) {
                Toast.makeText(context, "Mobile data enabled", Toast.LENGTH_LONG).show()
                if (intentMainMain != null) {
                    context.startActivity(intentMainMain);
                }
            }
         else {
            Toast.makeText(context, "No internet is available", Toast.LENGTH_LONG).show()
            if (intentNoInternetInternet != null) {
                context.startActivity(intentNoInternetInternet);
            }
        }
    }

    }
//    AsyncTask<Void, Void, String>() {

//     companion object {
//         public fun checkConnection(context: Context): Boolean {
//             var connMgr: ConnectivityManager =
//                 context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//             if (connMgr != null) {
//
//                 if (connMgr.getActiveNetworkInfo() != null) { // connected to the internet
//                    return  true;
//                     }
//             }
//             return false;
//         }
//     }
// }


//    var context: Context = contextMain
//
//        override fun doInBackground(vararg voids: Void): String {
//            var connMgr: ConnectivityManager =
//                context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//            if (connMgr != null) {
//
//                if (connMgr.getActiveNetworkInfo() != null) { // connected to the internet
//                    if (connMgr.getActiveNetworkInfo().type == ConnectivityManager.TYPE_WIFI) {
//                        return "Device connected to the internet!"
//                    } else {
//                        return "There is no internet connection!"
//                    }
//                }
//            }
//            return "Could not get information!"
//        }
//
//        override fun onPostExecute(result: String) {
//            Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
//            Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
//        }
//    }

