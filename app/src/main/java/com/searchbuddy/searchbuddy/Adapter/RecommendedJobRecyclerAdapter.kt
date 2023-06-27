package com.searchbuddy.searchbuddy.Adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.auth0.android.jwt.JWT
import com.bumptech.searchbuddy.R
import com.searchbuddy.searchbuddy.model.RecommendedJob
import java.util.concurrent.Executors

class RecommendedJobRecyclerAdapter(private val mList: List<RecommendedJob>, val onClickSave:(Int)->Unit, val onClickDelete:(Int)->Unit) :
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
        if (ItemsViewModel.logo!=null){
            var picname=ItemsViewModel.logo
            val executor = Executors.newSingleThreadExecutor()
            var image: Bitmap? = null
            val handler = Handler(Looper.getMainLooper())
            executor.execute {
                val imageUrl =
                    "https://www.searchbuddy.in/api/get-picture/organisation/" + picname
                try {
                    val `in` = java.net.URL(imageUrl).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    if (image != null) {

                        handler.post {
                            holder.company_image.setImageBitmap(image)
                        }
                    }
                    else{
                        handler.post{
                            holder.company_image.setImageResource(R.drawable.city)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        holder.save.setOnClickListener {
            val token =
                ItemsViewModel.encryptedId
            val jwt = JWT(token)

            val issuer = jwt.issuer //get registered claims

            val claim = jwt.getClaim("id").asString() //get custom claims
            var id= claim?.toInt()
            if (id != null) {
                onClickSave(id)
            }
            holder.save.setImageResource(R.drawable.bookmark_saved)

        }
//        if (ItemsViewModel.positionSaved==true){
//            holder.save.setImageResource(R.drawable.bookmark_saved)
//            holder.save.setOnClickListener {
//                val token =
//                    ItemsViewModel.encryptedId
//                val jwt = JWT(token)
//
//                val issuer = jwt.issuer //get registered claims
//
//                val claim = jwt.getClaim("id").asString() //get custom claims
//                var id= claim?.toInt()
//                if (id != null) {
//                    onClickDelete(id)
//                }
//                holder.save.setImageResource(R.drawable.bookmark)
//            }
//        }
//        else{
//            holder.save.setImageResource(R.drawable.bookmark)
//
//        }
        // sets the text to the textview from our itemHolder class
        if (ItemsViewModel.positionName!=null) {
            holder.job_name.text = ItemsViewModel.positionName
        }
//        if (ItemsViewModel.postedDay!=null) {
//            holder.days.text = ItemsViewModel.postedDay.toString() + " days ago"
//        }
        if (ItemsViewModel.companyName!=null) {
            holder.company_name.text = ItemsViewModel.companyName
        }
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
//        val days: TextView = itemView.findViewById(R.id.days)
        val save: ImageView = itemView.findViewById(R.id.save_job_button)

    }
}