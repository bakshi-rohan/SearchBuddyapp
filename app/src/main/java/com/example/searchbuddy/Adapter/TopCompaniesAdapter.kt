package com.example.searchbuddy.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.searchbuddy.R
import com.example.searchbuddy.model.TopcomapniesResponseItem

class TopCompaniesAdapter(private val mList: List<TopcomapniesResponseItem>) :
    RecyclerView.Adapter<TopCompaniesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.top_companies_recyler_layout, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
        holder.itemView.setOnClickListener {
            var bundle=Bundle()
            bundle.putString("company",ItemsViewModel.companyName)
            Navigation.findNavController(it).navigate(R.id.action_navigation_home_to_nav_sales,bundle)

        }
        // sets the image to the imageview from our itemHolder class
//        holder.company_image.setImageResource(ItemsViewModel)

        // sets the text to the textview from our itemHolder class
        holder.job_name.text = "("+ItemsViewModel.total.toString()+")"
        holder.company_name.text = ItemsViewModel.companyName
//        if (ItemsViewModel.companyName.length==)
//        holder.experience.text = ItemsViewModel.experience

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val company_image: ImageView = itemView.findViewById(R.id.company_image_top)
        val job_name: TextView = itemView.findViewById(R.id.job_name_top)
        val company_name: TextView = itemView.findViewById(R.id.company_name_top)
//        val experience: TextView = itemView.findViewById(R.id.experience_top)
    }
}