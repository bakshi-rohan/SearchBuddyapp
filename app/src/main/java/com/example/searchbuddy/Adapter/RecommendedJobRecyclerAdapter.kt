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
import com.example.searchbuddy.model.RecommendedJob

class RecommendedJobRecyclerAdapter(private val mList: List<RecommendedJob>) :
    RecyclerView.Adapter<RecommendedJobRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recommended_recyler_layout, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
        holder.itemView.setOnClickListener {
            var bundle = Bundle()
            bundle.putInt("position_id",ItemsViewModel.positionId)
            bundle.putString("url", ItemsViewModel.url)
            Navigation.findNavController(it).navigate(R.id.action_navigation_home_to_nav_job_desc,bundle)
        }
        // sets the image to the imageview from our itemHolder class
        holder.company_image.setImageResource(R.drawable.city)

        // sets the text to the textview from our itemHolder class
        holder.job_name.text = ItemsViewModel.positionName
        holder.company_name.text = ItemsViewModel.companyName
        var tmpStr :String = ItemsViewModel.location.toString()
        var newStr = tmpStr.substring(1, tmpStr.length-1);
        Log.i("mmmmmmm",ItemsViewModel.positionId.toString())
        holder.location.text = newStr
         var exp_from =  ItemsViewModel.expFrm.toString()+"-"
        var exp_to=ItemsViewModel.expTo.toString()
        var plus=exp_from+exp_to
        holder.experience.text = plus+ " years"

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val company_image: ImageView = itemView.findViewById(R.id.company_image)
        val job_name: TextView = itemView.findViewById(R.id.job_name)
        val company_name: TextView = itemView.findViewById(R.id.company_name)
        val location: TextView = itemView.findViewById(R.id.location)
        val experience: TextView = itemView.findViewById(R.id.experience)
    }
}