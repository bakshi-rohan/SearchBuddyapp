package com.example.searchbuddy.Dashboard

//import com.example.searchbuddy.settin
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.iterator
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.searchbuddy.*
import com.example.searchbuddy.Adapter.FieldSalesAdapter
import com.example.searchbuddy.Forms.Form_Two
import com.example.searchbuddy.Login.Login
import com.example.searchbuddy.Utils.Constant
import com.example.searchbuddy.Utils.LocalSessionManager
import com.example.searchbuddy.databinding.ActivityDashboardBinding
import com.example.searchbuddy.model.LogOutRequest
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.concurrent.Executors

//import java.lang.reflect.Array.newInstance

class Dashboard : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var bottomNavView: BottomNavigationView
    lateinit var drawerNav: NavigationView
    private lateinit var binding: ActivityDashboardBinding
    lateinit var header: RelativeLayout
    lateinit var viewModel: DashBoardViewModel
    private var pressedTime: Long = 0
    lateinit var analytics:FirebaseAnalytics
    lateinit var Jobadapter: FieldSalesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerNav = binding.navDrawerView
        bottomNavView = binding.navView
        analytics=FirebaseAnalytics.getInstance(this)
        val menu: Menu = drawerNav.menu
        val nav: Menu = bottomNavView.menu
        var settings = menu.findItem(R.id.settings)
        var faq = menu.findItem(R.id.Faq)
        var contact_us = menu.findItem(R.id.contact_us)
        val drawer: DrawerLayout = binding.drawerView
        var toolbar: LinearLayout = findViewById(R.id.toolbar)
        var arrow_back: ShapeableImageView = toolbar.findViewById(R.id.back)
        arrow_back.visibility = View.GONE
//        var bar:SearchView=toolbar.findViewById(R.id.search_bar_home)
        var drawer_profile:ShapeableImageView=toolbar.findViewById(R.id.drawer_icon)
        val logout = menu.findItem(R.id.log_out)
        var tnc = menu.findItem(R.id.tnc)
        var appliedJobs = menu.findItem(R.id.applied_job)
        var search_jobs = menu.findItem(R.id.search_jobs)
        var about_us = menu.findItem(R.id.abou_us)
        var setting = menu.findItem(R.id.settings)
        var jobs=nav.findItem(R.id.nav_sales)



        val headerview: View = drawerNav.inflateHeaderView(R.layout.nav_header_navigation)
        val drawericon: ShapeableImageView = toolbar.findViewById(R.id.drawer_icon)
        var profile_pic=headerview.findViewById<ShapeableImageView>(R.id.iv_profile_photo)
        var user=headerview.findViewById<TextView>(R.id.tv_name)
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        bottomNavView.setupWithNavController(navController)
        drawerNav.setupWithNavController(navController)

        viewModel = ViewModelProvider(this).get(DashBoardViewModel::class.java)
        LocalSessionManager.removeValue(Constant.SliderStartValue, this)
        LocalSessionManager.removeValue(Constant.SliderEndValue, this)
        LocalSessionManager.removeValue(Constant.SalarySliderStartValue, this)
        LocalSessionManager.removeValue(Constant.SalarySliderEndValue, this)
        LocalSessionManager.removeValue(Constant.FilterLocation, this)
        LocalSessionManager.removeValue(Constant.DatePosted, this)
        LocalSessionManager.removeValue(Constant.DatePost, this)

        var username=LocalSessionManager.getStringValue("userName","",this)
        if (username!=null)
        {
            user.setText(username)
        }

        var profilepicName=LocalSessionManager.getStringValue("profilepic","",this)
        if (profilepicName!=null) {
            val executor = Executors.newSingleThreadExecutor()
            var image: Bitmap? = null
            val handler = Handler(Looper.getMainLooper())
            executor.execute {
                val imageUrl =
                    "https://testingsales.searchbuddy.in/api/get-picture/profilepicture/" + profilepicName
                try {
                    val `in` = java.net.URL(imageUrl).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    if (image != null) {

                        handler.post {
                          profile_pic.setImageBitmap(image)
                            drawer_profile.setImageBitmap(image)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
            if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.orange)
        }

        drawericon.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }
        fun AlertLogout() {
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle("Alert!")
            //set message for alert dialog
            builder.setMessage("Are you sure, you want to logout?")
            builder.setPositiveButton("Yes") { dialogInterface, which ->
//                Logout()

//                Logout()
                LocalSessionManager.deleteAll(this)
                LocalSessionManager.saveValue(Constant.IS_LOGED_IN, false, this)
                var intent = Intent(this, Login::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton("No") { dialogInterface, which ->
            }
            val alertDialog: androidx.appcompat.app.AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        fun goTnc() {
            var download= this?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            var url = "https://searchbuddy-assets.s3.ap-south-1.amazonaws.com/document/Terms%20of%20Use%20Agreement%20Search%20Buddy%20v.3.pdf"
            var uri=Uri.parse(url)
            var getPdf = DownloadManager.Request(uri)
            getPdf.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            download.enqueue(getPdf)
            getPdf.setMimeType("application/pdf")
            Toast.makeText(this,"Download Started", Toast.LENGTH_LONG).show()
//            val openURL = Intent(Intent.ACTION_VIEW)
//            openURL.data =
//                Uri.parse()
//            startActivity(openURL)
        }
        fun goJobs(){
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            navController.navigate(R.id.action_navigation_home_to_nav_sales)
            drawer.closeDrawer(GravityCompat.START)

        }

        fun goFaq(){
            var intent=Intent(this,Faq::class.java)
            startActivity(intent)
        }
        fun goAbout_us(){
            var intent=Intent(this,AboutUs::class.java)
            startActivity(intent)
        }
        fun goContactUs(){
            var intent=Intent(this,ContactUs::class.java)
            startActivity(intent)
        }
        fun goSetting(){
            var intent=Intent(this,ProfileSetting::class.java)
            startActivity(intent)
        }
        faq.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                goFaq()

                drawer.closeDrawer(GravityCompat.START)

                return true
            }
        })
        setting.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                goSetting()

                drawer.closeDrawer(GravityCompat.START)

                return true
            }
        })
        contact_us.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                goContactUs()

                drawer.closeDrawer(GravityCompat.START)

                return true
            }
        })


        fun searchJobs() {
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            navController.navigate(R.id.action_navigation_home_to_nav_sales)
            drawer.closeDrawer(GravityCompat.START)
        }
        fun goJob() {
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            navController.navigate(R.id.action_navigation_home_to_nav_sales)
            drawer.closeDrawer(GravityCompat.START)
        }
        fun goappliedJobs(){
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            navController.navigate(R.id.action_navigation_home_to_applied_jobs)
            drawer.closeDrawer(GravityCompat.START)
        }


        logout.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                AlertLogout()


                drawer.closeDrawer(GravityCompat.START)

                return true
            }
        })

        tnc.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                goTnc()


                drawer.closeDrawer(GravityCompat.START)

                return true
            }
        })

        appliedJobs.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                goappliedJobs()


                drawer.closeDrawer(GravityCompat.START)

                return true
            }
        })


        search_jobs.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                goJobs()

//                drawer.closeDrawer(GravityCompat.START)

                return true
            }
        })
        about_us.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                goAbout_us()

//                drawer.closeDrawer(GravityCompat.START)

                return true
            }
        })
//        jobs.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
//            override fun onMenuItemClick(item: MenuItem?): Boolean {
//                searchJobs()
//
//                drawer.closeDrawer(GravityCompat.START)
//
//                return true
//            }
//        })


//        settings.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
//            override fun onMenuItemClick(item: MenuItem?): Boolean {
//                val intent = Intent(this@Dashboard, Settings::class.java)
//                startActivity(intent)
//                drawer.closeDrawer(GravityCompat.START)
//
//                return true
//            }
//        })
        headerview.setOnClickListener {
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            navController.navigate(R.id.action_navigation_home_to_navigation_mycattle)
            drawer.closeDrawer(GravityCompat.START)

        }


    }
//    override fun onBackPressed()
//    {
//        super.onBackPressed()
//        val a = Intent(Intent.ACTION_MAIN)
//        a.addCategory(Intent.CATEGORY_HOME)
//        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        startActivity(a)
//    }
override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment_activity_main)
    return navController.navigateUp() || super.onSupportNavigateUp()
}
    override fun onResume() {
        super.onResume()
        val menu: Menu = drawerNav.menu
        menu.iterator().forEach {
            //to make item menu unselected after resuming
            it.isChecked = false
        }
        LocalSessionManager.removeValue(Constant.SliderStartValue, this)
        LocalSessionManager.removeValue(Constant.SliderEndValue, this)
        LocalSessionManager.removeValue(Constant.SalarySliderStartValue, this)
        LocalSessionManager.removeValue(Constant.SalarySliderEndValue, this)
        LocalSessionManager.removeValue(Constant.FilterLocation, this)
        LocalSessionManager.removeValue(Constant.DatePosted, this)
        LocalSessionManager.removeValue(Constant.DatePost, this)

//    var bar:SearchView=toolbar.findViewById(R.id.search_bar_home)
//bar.visibility=View.VISIBLE
}

    override fun onDestroy() {
        super.onDestroy()
        LocalSessionManager.removeValue(Constant.DatePost, this)

    }

    private fun Logout() {
        var tokeno = LocalSessionManager.getStringValue(Constant.TOKEN, "", this)
        Log.i("tokennnnn", tokeno.toString())
        var logoutParams = LogOutRequest(tokeno.toString())
        viewModel.requestLogout(this, binding.progress).observe(this, {
            LocalSessionManager.deleteAll(this)
            LocalSessionManager.saveValue(Constant.IS_LOGED_IN, false, this)
            Log.i("kkkkkkkkkkkk", tokeno.toString())

            OnLogout()


        })
    }

    private fun OnLogout() {
        var intent = Intent(this, Form_Two::class.java)
        startActivity(intent)
    }
//    private fun setCurrentFragment(fragment: Fragment)=
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.ss,fragment)
//            commit()
//        }
}


