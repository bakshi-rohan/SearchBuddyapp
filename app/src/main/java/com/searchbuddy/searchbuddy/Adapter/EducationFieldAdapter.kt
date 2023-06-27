package com.searchbuddy.searchbuddy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.searchbuddy.R
import com.google.android.material.textfield.TextInputEditText
import com.searchbuddy.searchbuddy.model.EducationFieldModel


class EducationFieldAdapter (private val mList: List<EducationFieldModel>, context: Context) :
    RecyclerView.Adapter<EducationFieldAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.edu_field, parent, false)

    return ViewHolder(view)
}

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.university.setText(ItemsViewModel.university)

        // sets the text to the textview from our itemHolder class
        holder.course.setText(ItemsViewModel.course)
        holder.board.setText(ItemsViewModel.board)

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val university: TextInputEditText = itemView.findViewById(R.id.university)
        val course: TextInputEditText = itemView.findViewById(R.id.course)
        val board: TextInputEditText = itemView.findViewById(R.id.board)
    }



}