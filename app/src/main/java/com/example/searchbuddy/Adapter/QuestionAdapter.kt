package com.example.searchbuddy.Adapter

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.searchbuddy.R
import com.example.searchbuddy.model.Question

lateinit var context:Context
class QuestionAdapter (private val mList: List<Question>): RecyclerView.Adapter<QuestionAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.question_recyler_layout, parent, false)
context=parent.context
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

holder.question.text=ItemsViewModel.question
var optilonlist =ItemsViewModel.options

        optilonlist.forEachIndexed({ i, e -> Log.i("kk","fruits[$i] = $e")
            if (ItemsViewModel.multiple==false) {
                var btn = RadioButton(context)
                btn.text = e
                btn.buttonTintList = getRadioButtonColor()
                var rprms: RadioGroup.LayoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                )
                holder.radiogroup.addView(btn, rprms)

            }
            else if (ItemsViewModel.multiple==true){
                var checkBox: CheckBox = CheckBox(context)
                checkBox.setText(e)
                checkBox.buttonTintList =getRadioButtonColor()
                var cprms:ViewGroup.LayoutParams= ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                holder.container.addView(checkBox,cprms)
            }
        })

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val question: TextView = itemView.findViewById(R.id.question)
        val checkBox: TextView = itemView.findViewById(R.id.question_checkBox)
        var radiogroup:RadioGroup=itemView.findViewById(R.id.question_group_radio)
        var container: LinearLayout=itemView.findViewById(R.id.rootContainer)

    }
    private fun getRadioButtonColor(): ColorStateList {
        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked))

        val colors = intArrayOf(
            ContextCompat.getColor(context, R.color.choclate),
            ContextCompat.getColor(context, R.color.choclate)
        )

        return ColorStateList(states, colors)
    }
}