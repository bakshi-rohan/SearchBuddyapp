package com.searchbuddy.searchbuddy.Adapter

//import com.example.searchbuddy.model.Positions
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.auth0.android.jwt.JWT
import com.bumptech.glide.Glide
import com.bumptech.searchbuddy.R
import com.searchbuddy.searchbuddy.model.Positions
import java.util.concurrent.Executors

class SavedJobsAdapter( private val mList: List<Positions>,var context:Context) :
    RecyclerView.Adapter<SavedJobsAdapter.ViewHolder>(), Filterable {
    var filterProductList = mList
    var datalist = mList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedJobsAdapter.ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.nav_saved_jobs_layout, parent, false)
        return SavedJobsAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedJobsAdapter.ViewHolder, position: Int) {

        val ItemsViewModel = filterProductList.get(position)


        // sets the image to the imageview from our itemHolder class
        holder.itemView.setOnClickListener {
            val token =
                ItemsViewModel.id
            val jwt = JWT(token)

            val issuer = jwt.issuer //get registered claims

            val claim = jwt.getClaim("id").asString() //get custom claims
            var id= claim?.toInt()
            var bundle = Bundle()
            bundle.putString("position", ItemsViewModel.positionName)
            bundle.putInt("position_id", id!!)
            bundle.putString("company_name", ItemsViewModel.organisationName)
            bundle.putString("url", ItemsViewModel.url)
            Navigation.findNavController(it).navigate(R.id.action_saved_jobs_to_nav_job_desc,bundle)
        }
        // sets the image to the imageview from our itemHolder class

//        holder.company_image_field.setImageResource(R.drawable.city)
//        Glide.with(context).load(ItemsViewModel.logo).apply(
//            RequestOptions()
//                .placeholder(R.drawable.city)
//        ).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.company_image_field)
        if (ItemsViewModel.logo!=null){
            var picname=ItemsViewModel.logo
            val executor = Executors.newSingleThreadExecutor()
            var image: Bitmap? = null
            val handler = Handler(Looper.getMainLooper())
            var uri= Uri.parse(  "https://www.searchbuddy.in/api/get-picture/organisation/" + picname)
            Glide.with(context).load(uri).error(R.drawable.city).into(holder.company_image_field);
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
        }
        // sets the text to the textview from our itemHolder class
        if (ItemsViewModel.skills!=null) {
            var sk = ItemsViewModel.skills.toString()
            var skres = sk.substring(1, sk.length - 1)
            holder.description.text = skres
        }
        if (ItemsViewModel.applied==true)
        {
            holder.apply.setText("Applied")
        }
        if (ItemsViewModel.postedDay!=null){
            holder.days.text=ItemsViewModel.postedDay.toString()+" days ago"
        }
        if (ItemsViewModel.salary==null){
            holder.salary_field.text=""
            holder.rupee.visibility=View.GONE
        }else if (ItemsViewModel.salary!=null){
            var sa = ItemsViewModel.salary.toString()
            var rez=sa.substring(1,sa.length-1)
            holder.salary_field.text=rez.toString()
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
            holder.company_name_field.text = ItemsViewModel.organisationName
        }
        if (holder.job_name_field!=null) {
            holder.job_name_field.text = ItemsViewModel.positionName
        }
//        holder.apply.setOnClickListener {
//            val token =
//                ItemsViewModel.id
//            val jwt = JWT(token)
//
//            val issuer = jwt.issuer //get registered claims
//
//            val claim = jwt.getClaim("id").asString() //get custom claims
//            var id= claim?.toInt()
////            var bundle: Bundle = Bundle()
////            bundle.putString("position_id", id!!.toString())
////            Navigation.findNavController(it).navigate(R.id.action_saved_jobs_to_nav_job_desc,bundle)
//        }
    }
    override fun getItemCount(): Int {
        return filterProductList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val company_image_field: ImageView = itemView.findViewById(R.id.company_image_field)
        val description: TextView = itemView.findViewById(R.id.description_field)
        val location: TextView = itemView.findViewById(R.id.location_field)
        val salary_field: TextView = itemView.findViewById(R.id.salary_field)
        val apply: TextView = itemView.findViewById(R.id.apply_positions)
        val company_name_field: TextView = itemView.findViewById(R.id.company_name_field)
        val job_name_field: TextView = itemView.findViewById(R.id.job_name_field)
        val days: TextView = itemView.findViewById(R.id.days)
        val rupee: ImageView = itemView.findViewById(R.id.rupee)
//        val applied_status: TextView = itemView.findViewById(R.id.status)
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterProductList = datalist


                } else {
                    val resultList = ArrayList<Positions>()
                    for (row in datalist) {
                        if (row.positionName.uppercase().contains(charSearch.uppercase())) {
                            resultList.add(row)
                        }
                        else if (row.organisationName.uppercase().contains(charSearch.uppercase())){
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

                filterProductList = results?.values as List<Positions>
                notifyDataSetChanged()


            }
        }
    }
}