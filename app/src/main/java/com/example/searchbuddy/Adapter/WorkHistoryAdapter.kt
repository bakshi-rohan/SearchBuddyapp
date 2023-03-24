package com.example.searchbuddy.Adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.searchbuddy.Profile.AddProfessionalDetail
import com.example.searchbuddy.R
import com.example.searchbuddy.model.GetAllWorkHistoryModelItem

class WorkHistoryAdapter (private val mList: List<GetAllWorkHistoryModelItem>,val onClickDelete:(Int)->Unit) :
    RecyclerView.Adapter<WorkHistoryAdapter.ViewHolder>() {
    private var listData=mList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.work_history_recyler_layout, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
        var context=itemCount
        holder.itemView.setOnClickListener {
            var bundle=Bundle()
            bundle.putString("designation",ItemsViewModel.designation)
            bundle.putString("company_name",ItemsViewModel.companyName)
            bundle.putString("location",ItemsViewModel.location)
            bundle.putString("start_date",ItemsViewModel.startDate)
            bundle.putString("end_date",ItemsViewModel.endDate)
            bundle.putString("is_present",ItemsViewModel.isPresent)
            var intent= Intent(holder.itemView.context,AddProfessionalDetail::class.java)
            intent.putExtras(bundle)
            holder.itemView.context.startActivity(intent)

        }
        holder.delete.setOnClickListener{
onClickDelete(position)
        }
        // sets the image to the imageview from our itemHolder class
//        holder.company_image.setImageResource(ItemsViewModel.company_image)

        // sets the text to the textview from our itemHolder class
        holder.designation.text = ItemsViewModel.designation
        holder.company_name.text = ItemsViewModel.companyName
        holder.location.text = ItemsViewModel.location


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val company_name: TextView = itemView.findViewById(R.id.company_name_work_history)
        val designation: TextView = itemView.findViewById(R.id.designation_work_history)
        val location: TextView = itemView.findViewById(R.id.location_work_history)
        val delete: ImageView = itemView.findViewById(R.id.delete)
    }
public fun setItems(items:List<GetAllWorkHistoryModelItem>){
    listData=items
    notifyDataSetChanged()
}
}