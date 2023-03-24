package com.example.searchbuddy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.searchbuddy.R
import com.example.searchbuddy.model.ExperienceFieldModel
import com.google.android.material.textfield.TextInputEditText

class ExperienceFieldAdapter  (private val mList: List<ExperienceFieldModel>, context: Context) :
    RecyclerView.Adapter<ExperienceFieldAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.work_field, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.university.setText(ItemsViewModel.company_name)

        // sets the text to the textview from our itemHolder class
        holder.course.setText(ItemsViewModel.location)
        holder.board.setText(ItemsViewModel.designation)

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val university: TextInputEditText = itemView.findViewById(R.id.et_comp_name)
        val course: TextInputEditText = itemView.findViewById(R.id.et_designation)
        val board: TextInputEditText = itemView.findViewById(R.id.et_location_work)
    }
}