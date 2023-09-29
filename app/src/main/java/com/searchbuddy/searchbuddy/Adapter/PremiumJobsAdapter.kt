package com.searchbuddy.searchbuddy.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.searchbuddy.R
import com.searchbuddy.searchbuddy.model.aa.PrePosition


class PremiumJobsAdapter(private val mList: List<PrePosition>, var context: Context, val onClickSave:(Int)->Unit, val onClickDelete:(Int)->Unit) :
    RecyclerView.Adapter<PremiumJobsAdapter.ViewHolder>(), Filterable {
    var filterProductList = mList
    var datalist = mList
    var saved:Boolean=false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.premium_recyler_layout, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = filterProductList.get(position)

//        }
        // sets the text to the textview from our itemHolder class
        if (ItemsViewModel.skills!=null) {
            var sk = ItemsViewModel.skills.toString()
            var skres = sk.substring(1, sk.length - 1)
            holder.description.text = skres
        }
        if (ItemsViewModel.positionAge!=null){
            holder.days.text=ItemsViewModel.positionAge.toString()+" days ago"
        }
        if (ItemsViewModel.location!=null) {
            var loc = ItemsViewModel.location.toString()
            var locres = loc.substring(1, loc.length - 1)
            holder.location.text = locres
        }
//        if (holder.experience_field!=null) {
//            holder.experience_field.text = ItemsViewModel.industry
//        }
        if (holder.company_name_field!=null) {
            holder.company_name_field.text = ItemsViewModel.clientName
        }
        if (holder.job_name_field!=null) {
            holder.job_name_field.text = ItemsViewModel.positionName
        }
        if (ItemsViewModel.level!=null){
            holder.level.setText(ItemsViewModel.level)
        }

        holder.apply.setOnClickListener {
            var url = ItemsViewModel.url
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(browserIntent);
        }
        holder.itemView.setOnClickListener {
            var url = ItemsViewModel.url
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(browserIntent);
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return filterProductList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val company_image_field: ImageView = itemView.findViewById(R.id.company_image_field)
        val description: TextView = itemView.findViewById(R.id.description_field)
        val location: TextView = itemView.findViewById(R.id.location_field)
        val level: TextView = itemView.findViewById(R.id.level)
        val salary_field: TextView = itemView.findViewById(R.id.salary_field)
        val apply: TextView = itemView.findViewById(R.id.apply_positions)
        val company_name_field: TextView = itemView.findViewById(R.id.company_name_field)
        val job_name_field: TextView = itemView.findViewById(R.id.job_name_field)
        val days: TextView = itemView.findViewById(R.id.days)
        val save: ImageView = itemView.findViewById(R.id.save_job_button)
        val rupee: ImageView = itemView.findViewById(R.id.rupee)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterProductList = datalist


                } else {
                    val resultList = ArrayList<PrePosition>()
                    for (row in datalist) {
                        if (row.positionName.uppercase().contains(charSearch.uppercase())) {
                            resultList.add(row)
                        }
//                        else if (row.clientName.uppercase().contains(charSearch.uppercase())){
//                            resultList.add(row)
//                        }
                    }


                    filterProductList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterProductList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filterProductList = results?.values as List<PrePosition>
                notifyDataSetChanged()


            }
        }
    }
}