package com.searchbuddy.searchbuddy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.searchbuddy.R
import com.searchbuddy.searchbuddy.model.JobCategoryModel

class JobCategoryAdapter(private val mList: List<JobCategoryModel>, private val onClickListener: OnClickListener, context: Context) :
    RecyclerView.Adapter<JobCategoryAdapter.ViewHolder>() {
    class OnClickListener(val clickListener: (meme: JobCategoryModel) -> Unit) {
        fun onClick(meme: JobCategoryModel) = clickListener(meme)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.job_category_layout, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]
//        holder.itemView.setOnClickListener {
//            Navigation.findNavController(it).navigate(R.id.action_navigation_home_to_nav_sales)
//        }
        holder.itemView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_navigation_home_to_nav_sales)
            onClickListener.onClick(ItemsViewModel)
        }


        // sets the image to the imageview from our itemHolder class
        holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.textView.text = ItemsViewModel.text

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textView: TextView = itemView.findViewById(R.id.textView)
    }
}