package com.searchbuddy.searchbuddy

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.searchbuddy.R
import com.searchbuddy.searchbuddy.Adapter.BlogAdapter
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager
import com.searchbuddy.searchbuddy.model.BlogPageModel

class Blogs : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    lateinit var newArrayList: ArrayList<BlogPageModel>
    lateinit var myadapter: BlogAdapter
    lateinit var image: Array<Int>
    lateinit var description: Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blogs)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.grey)
        }
        image = arrayOf(
            R.drawable.blog_one,
            R.drawable.blog_two,
            R.drawable.blog_three,
            R.drawable.blog_four,
            )
        description = arrayOf(
            "10 Proven Sales Techniques to Boost Your Closing Rate",
            "Recruitment Strategies to Hire Top Sales Talent",
            "Overcoming Sales Objections:Turn No into Yes",
            "How to Handle Sales Rejection: Turning Setbacks into Opportunities",

        )
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        newArrayList = arrayListOf<BlogPageModel>()
        getdata()

    }
    private fun getdata() {
        for (i in 0..image.size - 1) {
            val listdata = BlogPageModel(image[i], description[i])
            newArrayList.add(listdata)

        }
        myadapter=BlogAdapter(newArrayList,this)

        recyclerView.adapter = myadapter
    }
}