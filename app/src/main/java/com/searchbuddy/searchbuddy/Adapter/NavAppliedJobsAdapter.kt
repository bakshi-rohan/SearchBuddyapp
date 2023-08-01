package com.searchbuddy.searchbuddy.Adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.searchbuddy.R
import com.kofigyan.stateprogressbar.StateProgressBar
import com.searchbuddy.searchbuddy.model.Positio
import java.util.concurrent.Executors

class NavAppliedJobsAdapter( private val mList: List<Positio>) :
    RecyclerView.Adapter<NavAppliedJobsAdapter.ViewHolder>(), Filterable {
    lateinit var descriptionData:Array<String>
    var filterProductList = mList
    var datalist = mList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        descriptionData= arrayOf("Applied", "Education", "Experience", "CV","Preferences")

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.nav_applied_jobs_layout, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = filterProductList.get(position)
        descriptionData= arrayOf("Applied", "Screening", "Interview","Offered")

        // sets the image to the imageview from our itemHolder class
        holder.company_image_field.setImageResource(R.drawable.city)

        // sets the text to the textview from our itemHolder class
        holder.job_name_field.text = ItemsViewModel.positionName
//        holder.company_name_field.text = ItemsViewModel.createdBy
        holder.progress_bar.setStateDescriptionData(descriptionData)
//        holder.progress_bar.setCurrentStateNumber()
        holder.itemView.setOnClickListener {

            var id= ItemsViewModel.positionId
            var bundle = Bundle()
            bundle.putString("position", ItemsViewModel.positionName)
            bundle.putInt("position_id", id!!)
            Navigation.findNavController(it).navigate(R.id.action_nav_progress_to_appliedjobs_desc,bundle)
        }
        if (ItemsViewModel.stage=="Screening"){
            holder.progress_bar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
        }
        else if (ItemsViewModel.stage=="Interview"){
            holder.progress_bar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)

        }
        else if (ItemsViewModel.stage=="Offered"){
            holder.progress_bar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)

        }
        else{
            holder.progress_bar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)

        }
        if (ItemsViewModel.location!=null) {
            if (ItemsViewModel.location.size>2) {
                var loc_one = ItemsViewModel.location.get(0)
                var loc_two = ItemsViewModel.location.get(1)
                holder.location.text = loc_one+" , "+loc_two+" and more"
            }
            else if (ItemsViewModel.location.size<2){
                var loc = ItemsViewModel.location.toString()
                var locres = loc.substring(1, loc.length - 1)
                holder.location.text = locres
            }
            else if (ItemsViewModel.location.isEmpty()){
                holder.location.visibility=View.GONE
                holder.loc_icon.visibility=View.GONE
            }
        }


        if (ItemsViewModel.logo!=null){
            var picname=ItemsViewModel.logo
            val executor = Executors.newSingleThreadExecutor()
            var image: Bitmap? = null
            val handler = Handler(Looper.getMainLooper())
            executor.execute {
                val imageUrl =
                    "https://www.searchbuddy.in/api/get-picture/organisation/"+picname
                try {
                    val `in` = java.net.URL(imageUrl).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    if (image != null) {

                        handler.post {
                            holder.company_image_field.setImageBitmap(image)
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return filterProductList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val company_image_field: ImageView = itemView.findViewById(R.id.company_image_field)
        val loc_icon: ImageView = itemView.findViewById(R.id.loc_icon)
        val company: TextView = itemView.findViewById(R.id.company_name_field_apply)
        val location: TextView = itemView.findViewById(R.id.location_field_apply)
//        val salary_field: TextView = itemView.findViewById(R.id.salary_field_apply)
//        val apply: TextView = itemView.findViewById(R.id.apply_positions)
//        val company_name_field: TextView = itemView.findViewById(R.id.company_name_field)
        val job_name_field: TextView = itemView.findViewById(R.id.job_name_field)
        val progress_bar: StateProgressBar = itemView.findViewById(R.id.your_state_progress_bar_id)
//        val rupee: ImageView = itemView.findViewById(R.id.rupee)
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterProductList = datalist


                } else {
                    val resultList = ArrayList<Positio>()
                    for (row in datalist) {
                        if (row.positionName.uppercase().contains(charSearch.uppercase())) {
                            resultList.add(row)
                        }
                        else if (row.createdBy.uppercase().contains(charSearch.uppercase())){
                            resultList.add(row)
                        }
                    }


                    filterProductList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterProductList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filterProductList = results?.values as List<Positio>
                notifyDataSetChanged()


            }
        }
    }
}
