package com.searchbuddy.searchbuddy.Adapter

//import com.example.searchbuddy.model.Position
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.auth0.android.jwt.JWT
import com.bumptech.glide.Glide
import com.bumptech.searchbuddy.R
import com.searchbuddy.searchbuddy.model.Positions
import java.util.concurrent.Executors

class FieldSalesAdapter(private val mList: List<Positions>, var context: Context, val onClickSave:(Int)->Unit, val onClickDelete:(Int)->Unit) :
    RecyclerView.Adapter<FieldSalesAdapter.ViewHolder>(), Filterable {
    var filterProductList = mList
    var datalist = mList
var saved:Boolean=false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.field_sale_recycler_layout, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = filterProductList.get(position)
        holder.cardView.visibility=View.GONE
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
            Navigation.findNavController(it).navigate(R.id.action_nav_sales_to_nav_job_desc,bundle)
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
            var uri= Uri.parse("https://www.searchbuddy.in/api/get-picture/organisation/"+picname)
            Glide.with(context).load(uri).into(holder.company_image_field);
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
        if (ItemsViewModel.expFrm!=null){

        }
if (ItemsViewModel.postedDay!=null){
    if (ItemsViewModel.postedDay==0){
        holder.days.text="Just now"

    }
    else {
        holder.days.text = ItemsViewModel.postedDay.toString() + " days ago"
    }
}

            if (ItemsViewModel.salary.isEmpty()==true) {
                holder.salary_field.text = "Not Disclosed"
                holder.rupee.visibility = View.VISIBLE
            }
        else if (ItemsViewModel.salary!=null){
            if (ItemsViewModel.salary.isEmpty()!=true) {
                var salfrom = ItemsViewModel.salary.get(0).toString()
                var salTo = ItemsViewModel.salary.get(1).toString()
                holder.salary_field.text = salfrom+"-"+salTo+" PA"
            }
        }
        if (ItemsViewModel.location!=null) {
            Log.i("dddd",ItemsViewModel.location.size.toString())
            if (ItemsViewModel.location.size>2) {
                var loc_one = ItemsViewModel.location.get(0)
                var loc_two = ItemsViewModel.location.get(1)

               val fullText= loc_one+" , "+loc_two+" and more..."
                val spannableBuilder :SpannableStringBuilder= SpannableStringBuilder(fullText)
                val firstWord = "and more..."
                val firstWordColor = Color.BLUE
                val startIndex = fullText.indexOf(firstWord)
                val endIndex = startIndex + firstWord.length
                spannableBuilder.setSpan(ForegroundColorSpan(firstWordColor), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                holder.location.text=spannableBuilder
            }
            else if (ItemsViewModel.location.size<2){
                val loc = ItemsViewModel.location.toString()
                var locres = loc.substring(1, loc.length - 1)
                holder.location.text = locres
            }
        }
//        if (holder.experience_field!=null) {
//            holder.experience_field.text = ItemsViewModel.industry
//        }
        if (ItemsViewModel.expFrm!=null&&ItemsViewModel.expTo!=null){
            val expfrm=ItemsViewModel.expFrm.toString()
            val expto=ItemsViewModel.expTo.toString()
            holder.exp_field.text=expfrm+ " - "+expto+" Year"
        }
        if (holder.company_name_field!=null) {
            holder.company_name_field.text = ItemsViewModel.organisationName
        }
        if (holder.job_name_field!=null) {
            holder.job_name_field.text = ItemsViewModel.positionName
        }
        holder.save.setOnClickListener {
            val token =
                ItemsViewModel.id
            val jwt = JWT(token)

//            val issuer = jwt.issuer //get registered claims

            val claim = jwt.getClaim("id").asString() //get custom claims
            val id= claim?.toInt()
            if (id != null) {
                onClickSave(id)
                holder.save.setImageResource(R.drawable.bookmark_saved)
                saved=true
            }
            notifyItemChanged(holder.adapterPosition)
            Toast.makeText(context,"Job saved successfully",Toast.LENGTH_SHORT).show()

        }
        if (ItemsViewModel.positionSaved==true){
            holder.save.setImageResource(R.drawable.bookmark_saved)
            holder.save.setOnClickListener {
                val token =
                    ItemsViewModel.id
                val jwt = JWT(token)

                val issuer = jwt.issuer //get registered claims

                val claim = jwt.getClaim("id").asString() //get custom claims
                var id= claim?.toInt()
                if (id != null) {
                    onClickDelete(id)
                    holder.save.setImageResource(R.drawable.bookmark)
                    saved=false
                    Toast.makeText(context,"Job unsaved successfully",Toast.LENGTH_SHORT).show()

                }
            }

        }
        else if (saved==true){
            holder.save.setImageResource(R.drawable.bookmark_saved)
            holder.save.setOnClickListener {
                val token =
                    ItemsViewModel.id
                val jwt = JWT(token)

                val issuer = jwt.issuer //get registered claims

                val claim = jwt.getClaim("id").asString() //get custom claims
                var id= claim?.toInt()
                if (id != null) {
                    onClickDelete(id)
                    holder.save.setImageResource(R.drawable.bookmark)
                    saved=false
                    Toast.makeText(context,"Job unsaved successfully",Toast.LENGTH_SHORT).show()

                }
            }
        }
        else{
            holder.save.setImageResource(R.drawable.bookmark)
//            notifyItemChanged(position)

        }
        holder.apply.setOnClickListener {
            val token =
                ItemsViewModel.id
            val jwt = JWT(token)

            val issuer = jwt.issuer //get registered claims

            val claim = jwt.getClaim("id").asString() //get custom claims
            var id= claim?.toInt()
            var bundle:Bundle=Bundle()
            bundle.putString("position_id", id!!.toString())
            Navigation.findNavController(it).navigate(R.id.action_nav_sales_to_question_fragment,bundle)
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
        val days: TextView = itemView.findViewById(R.id.days)
        val save: ImageView = itemView.findViewById(R.id.save_job_button)
        val rupee: ImageView = itemView.findViewById(R.id.rupee)
        val exp_field:TextView=itemView.findViewById(R.id.exp_field)
        val cardView: LinearLayout =itemView.findViewById(R.id.card)

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterProductList = datalist


                } else {
                    val resultList = ArrayList<Positions>()
                    var resultlength=resultList.size
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