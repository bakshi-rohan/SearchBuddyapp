package com.searchbuddy.searchbuddy.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.searchbuddy.R
import com.searchbuddy.searchbuddy.BlogDescription
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager
import com.searchbuddy.searchbuddy.model.BlogPageModel
import kotlin.math.log

class BlogAdapter (private val data: List<BlogPageModel>,var context: Context) : RecyclerView.Adapter<BlogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.blogs_recylerview_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.text
        holder.image.setImageResource(item.image)

        holder.itemView.setOnClickListener{
            var intent=Intent(context,BlogDescription::class.java)
            context.startActivity(intent)
            var pos=holder.absoluteAdapterPosition
            LocalSessionManager.saveValue(Constant.blogpos,pos.toString(),context)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView_blogs)
        val image: ImageView = itemView.findViewById(R.id.imageView_blogs)
    }
}
