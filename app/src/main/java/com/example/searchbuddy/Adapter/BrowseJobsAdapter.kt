package com.example.searchbuddy.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.auth0.android.jwt.JWT
import com.example.searchbuddy.Login.BrowseJobsDescription
import com.example.searchbuddy.Login.Login
import com.example.searchbuddy.R
import com.example.searchbuddy.model.Positions


class BrowseJobsAdapter(private val mList: List<Positions>) :
    RecyclerView.Adapter<BrowseJobsAdapter.ViewHolder>(), Filterable {
    var filterProductList = mList
    var datalist = mList
    lateinit var context:Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.field_sale_recycler_layout, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = filterProductList.get(position)
        holder.itemView.setOnClickListener { view ->
            val token =
                ItemsViewModel.id
            val jwt = JWT(token)

            val issuer = jwt.issuer //get registered claims

            val claim = jwt.getClaim("id").asString() //get custom claims
            var id= claim?.toInt()
//            var bundle = Bundle()
//            bundle.putString("position", ItemsViewModel.positionName)
//            bundle.putInt("position_id", id!!)
//            bundle.putString("company_name", ItemsViewModel.organisationName)
//            bundle.putString("url", ItemsViewModel.url)
//            Navigation.findNavController(it).navigate(R.id.action_nav_sales_to_nav_job_desc,bundle)
            var intent=Intent(view.context,BrowseJobsDescription::class.java)
            intent.putExtra("position",ItemsViewModel.positionName)
            intent.putExtra("position_id", id!!)
            intent.putExtra("company_name", ItemsViewModel.organisationName)
            intent.putExtra("url", ItemsViewModel.url)
            view.context.startActivity(intent)
        }
        // sets the image to the imageview from our itemHolder class

        holder.company_image_field.setImageResource(R.drawable.city)

        // sets the text to the textview from our itemHolder class
        if (ItemsViewModel.skills!=null) {
            var sk = ItemsViewModel.skills.toString()
            var skres = sk.substring(1, sk.length - 1)
            holder.description.text = skres
        }

        if (ItemsViewModel.salary==null){
            holder.salary_field.text=""
            holder.rupee.visibility= View.GONE
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

        holder.apply.setOnClickListener { view ->
            val builder = AlertDialog.Builder(view.context,)
            builder.setTitle("Alert")
            builder.setMessage("Please log in to apply")
            builder.setPositiveButton("OK") { dialog, which ->
                // handle OK button click
                val intent = Intent(view.context, Login::class.java)
                view.context.startActivity(intent)

            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                // handle Cancel button click
            }
            val dialog = builder.create()
            dialog.show()


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
        val salary_field: TextView = itemView.findViewById(R.id.salary_field)
        val apply: TextView = itemView.findViewById(R.id.apply_positions)
        val company_name_field: TextView = itemView.findViewById(R.id.company_name_field)
        val job_name_field: TextView = itemView.findViewById(R.id.job_name_field)
        val rupee: ImageView = itemView.findViewById(R.id.rupee)

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