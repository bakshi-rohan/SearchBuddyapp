package com.searchbuddy.searchbuddy.Utils

import android.R
import android.content.Context
import android.content.DialogInterface
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog


class Utils {
    companion object {

        fun hasNetwork(locationManager: LocationManager): Boolean {
            return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }


    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val cap = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
            return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val networks = cm.allNetworks
            for (n in networks) {
                val nInfo = cm.getNetworkInfo(n)
                if (nInfo != null && nInfo.isConnected) return true
            }
        } else {
            val networks = cm.allNetworkInfo
            for (nInfo in networks) {
                if (nInfo != null && nInfo.isConnected) return true
            }
        }
        return false
    }



    fun notNetwork(context: Context) {
        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(context)
        alertDialog.setTitle("Please check your internet!")


        alertDialog.setPositiveButton("OK") { dialogInterface, which ->
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()

            }
        }

        alertDialog.show()

    }


        fun showDialouge(context: Context,message: String) {
            val alertDialog = androidx.appcompat.app.AlertDialog.Builder(context)
            alertDialog.setTitle(message)


            alertDialog.setPositiveButton("OK") { dialogInterface, which ->
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()

                }
            }

            alertDialog.show()

        }



        fun showDialougeList(context: Context, gaushlaList: ArrayList<String>): AlertDialog.Builder {
            val alertDialog = androidx.appcompat.app.AlertDialog.Builder(context)
            alertDialog.setTitle("Please Select Gaushala!")
            val arrayAdapter =
                ArrayAdapter<String>(context, R.layout.simple_list_item_activated_1)
            arrayAdapter.addAll(gaushlaList)


            alertDialog.setAdapter(arrayAdapter,
                DialogInterface.OnClickListener { dialog, which ->
                    val strName = arrayAdapter.getItem(which)

                })

            return alertDialog

        }
    }
}
