package com.example.searchbuddy.Utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.widget.TextView
import com.example.searchbuddy.R

class CustomProgressDialog {
    lateinit var dialog: CustomDialog
    lateinit var titleView: TextView

    fun show(context: Context): Dialog {
        return show(context, null)
    }

    fun show(context: Context, title: CharSequence?): Dialog {
        val inflater = (context as Activity).layoutInflater
        val view = inflater.inflate(R.layout.progress_dialog_view, null)
        dialog = CustomDialog(context)
        dialog.setContentView(view)
        dialog.getWindow()!!.setLayout(480, 520);
        dialog.setCancelable(false)
        dialog.show()
        // set dynamic title
        titleView = dialog.findViewById(R.id.cp_title)
        titleView.text = title
        return dialog
    }

    class CustomDialog(context: Context) :
        Dialog(context, R.style.Base_ThemeOverlay_AppCompat_Dialog_Alert) {
        init {
            // Set Semi-Transparent Color for Dialog Background
            window?.decorView?.rootView?.setBackgroundResource(R.color.transparent)
//            window?.decorView?.setOnApplyWindowInsetsListener { _, insets ->
//                insets.consumeSystemWindowInsets()
//            }
        }
    }
}