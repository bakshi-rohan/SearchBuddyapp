package com.searchbuddy.searchbuddy.Adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.searchbuddy.R
import com.kofigyan.stateprogressbar.StateProgressBar
import com.searchbuddy.searchbuddy.model.aa.PayLoad

class PreAppliedJobsAdapter( private val mList: List<PayLoad>) :
    RecyclerView.Adapter<PreAppliedJobsAdapter.ViewHolder>(), Filterable {
    lateinit var descriptionData:Array<String>
    var filterProductList = mList
    var datalist = mList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        descriptionData= arrayOf("Applied", "Education", "Experience", "CV","Preferences")

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.nav_pre_applied_recyler_layout, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = filterProductList.get(position)
        descriptionData= arrayOf("Applied", "Screening", "Interview","Offered")

        // sets the image to the imageview from our itemHolder class
        holder.company_image_field.setImageResource(R.drawable.prebuilding)
        if (ItemsViewModel.level!=null){
            holder.level.setText(ItemsViewModel.level)
        }
        else if (ItemsViewModel.level==null){
            holder.lvl_img.visibility=View.GONE
        }
        // sets the text to the textview from our itemHolder class
        holder.job_name_field.text = ItemsViewModel.positionName
        holder.company_name_field.text = ItemsViewModel.clientName
        holder.progress_bar.setStateDescriptionData(descriptionData)
        if (ItemsViewModel.stage=="Round 2"){
            holder.progress_bar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
        }
        else if (ItemsViewModel.stage=="Round 3"){
            holder.progress_bar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)

        }
        else if (ItemsViewModel.stage=="Round 4"){
            holder.progress_bar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)

        }
        else{
            holder.progress_bar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)

        }
        if (ItemsViewModel.status!=null){
            holder.status.setText(ItemsViewModel.status)
        }
//        if (ItemsViewModel.logo!=null){
//            var picname=ItemsViewModel.logo
//            val executor = Executors.newSingleThreadExecutor()
//            var image: Bitmap? = null
//            val handler = Handler(Looper.getMainLooper())
//            executor.execute {
//                val imageUrl =
//                    "https://www.searchbuddy.in/api/get-picture/organisation/"+picname
//                try {
//                    val `in` = java.net.URL(imageUrl).openStream()
//                    image = BitmapFactory.decodeStream(`in`)
//                    if (image != null) {
//
//                        handler.post {
//                            holder.company_image_field.setImageBitmap(image)
//                        }
//                    }
//
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return filterProductList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val company_image_field: ImageView = itemView.findViewById(R.id.company_image_field)
        val lvl_img: ImageView = itemView.findViewById(R.id.lvl_img)
        val level: TextView = itemView.findViewById(R.id.lvl)
        val company_name_field: TextView = itemView.findViewById(R.id.company_name_field)
        val job_name_field: TextView = itemView.findViewById(R.id.job_name_field)
        val status: TextView = itemView.findViewById(R.id.status_pre)
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
                    val resultList = ArrayList<PayLoad>()
                    for (row in datalist) {
                        if (row.positionName.uppercase().contains(charSearch.uppercase())) {
                            resultList.add(row)
                        }
                        else if (row.clientName.uppercase().contains(charSearch.uppercase())){
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

                filterProductList = results?.values as List<PayLoad>
                notifyDataSetChanged()


            }
        }
    }
}