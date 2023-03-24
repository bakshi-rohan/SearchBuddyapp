package com.example.searchbuddy.Adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.searchbuddy.R
import com.example.searchbuddy.model.Positio
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AppliedJobsAdapter ( private val mList: List<Positio>) :
RecyclerView.Adapter<AppliedJobsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_applied_jobs, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
        holder.itemView.setOnClickListener {
            var bundle=Bundle()
            bundle.putInt("position_id",ItemsViewModel.positionId)
            bundle.putString("status",ItemsViewModel.status)
            Navigation.findNavController(it).navigate(R.id.action_navigation_home_to_appliedjobs_desc,bundle)

        }
        // sets the image to the imageview from our itemHolder class
        holder.company_image.setImageResource(R.drawable.city)

        // sets the text to the textview from our itemHolder class
        holder.job_name.text = ItemsViewModel.positionName
        holder.company_name.text = ItemsViewModel.createdBy
//        var loc = ItemsViewModel.location.toString()
//        var locSubstring=loc.substring(1,loc.length-1)
//        holder.location.text = locSubstring
//        var applied=ItemsViewModel.appliedOn.substring(0,10)
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
        val inputDateStr = ItemsViewModel.appliedOn
        val date: Date = inputFormat.parse(inputDateStr)
        val outputDateStr: String = outputFormat.format(date)
        Log.i("date",outputDateStr)
        holder.applied_date.text = outputDateStr
        holder.applied_status.text = ItemsViewModel.status

//        var ski=ItemsViewModel.skills.toString()
//        var skillSub = ski.substring(1,ski.length-1)
//        holder.salary.text=skillSub
//        val date = SimpleDateFormat("yyyy-MM-dd").parse(ItemsViewModel.appliedOn)
//        Log.i("date",date.toString())
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val company_image: ImageView = itemView.findViewById(R.id.company_image_apply)
        val job_name: TextView = itemView.findViewById(R.id.job_name_apply)
        val company_name: TextView = itemView.findViewById(R.id.company_name_apply)
//        val location: TextView = itemView.findViewById(R.id.location_field_applied)
//        val salary: TextView = itemView.findViewById(R.id.skill_field)
        val applied_date: TextView = itemView.findViewById(R.id.applied_date)
        val applied_status: TextView = itemView.findViewById(R.id.status)
    }
}