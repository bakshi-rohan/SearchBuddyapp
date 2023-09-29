package com.searchbuddy.searchbuddy.Dashboard

import android.app.DownloadManager
import android.content.Intent
import android.graphics.Bitmap
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.iterator
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.searchbuddy.R
import com.bumptech.searchbuddy.databinding.ActivityDashboardBinding
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.analytics.FirebaseAnalytics
import com.searchbuddy.searchbuddy.AboutUs
import com.searchbuddy.searchbuddy.Adapter.FieldSalesAdapter
import com.searchbuddy.searchbuddy.Blogs
import com.searchbuddy.searchbuddy.ContactUs
import com.searchbuddy.searchbuddy.Faq
import com.searchbuddy.searchbuddy.FirstScreen
import com.searchbuddy.searchbuddy.ProfileSetting
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager
import com.searchbuddy.searchbuddy.model.LogOutRequest
import java.util.concurrent.Executors

//import java.lang.reflect.Array.newInstance

@ExperimentalBadgeUtils class Dashboard : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var bottomNavView: BottomNavigationView
    lateinit var drawerNav: NavigationView
    private lateinit var binding: ActivityDashboardBinding
    lateinit var header: RelativeLayout
    lateinit var viewModel: DashBoardViewModel
    lateinit var analytics: FirebaseAnalytics
    lateinit var Jobadapter: FieldSalesAdapter
    lateinit var profile_pic: ShapeableImageView
    lateinit var drawer_profile: ShapeableImageView
var MY_REQUEST_CODE=100
lateinit var appUpdateManager:AppUpdateManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerNav = binding.navDrawerView
        bottomNavView = binding.navView
        analytics = FirebaseAnalytics.getInstance(this)

        val menu: Menu = drawerNav.menu
        val nav: Menu = bottomNavView.menu
        var settings = menu.findItem(R.id.settings)
        var faq = menu.findItem(R.id.Faq)
        var contact_us = menu.findItem(R.id.contact_us)
        val drawer: DrawerLayout = binding.drawerView
        var toolbar: LinearLayout = findViewById(R.id.toolbar)

        drawer_profile = toolbar.findViewById(R.id.drawer_icon)
        val logout = menu.findItem(R.id.log_out)
        var tnc = menu.findItem(R.id.tnc)
//        var appliedJobs = menu.findItem(R.id.applied_job)
        var savedJobs = menu.findItem(R.id.saved_jobs)
        var search_jobs = menu.findItem(R.id.search_jobs)
        var about_us = menu.findItem(R.id.abou_us)
        var setting = menu.findItem(R.id.settings)
        var blogs = menu.findItem(R.id.blogs)
        var jobs = nav.findItem(R.id.nav_sales)

        val headerview: View = drawerNav.inflateHeaderView(R.layout.nav_header_navigation)
        val drawericon: ShapeableImageView = toolbar.findViewById(R.id.drawer_icon)
        val header_name: TextView = toolbar.findViewById(R.id.header_name)
        profile_pic = headerview.findViewById<ShapeableImageView>(R.id.iv_profile_photo)
        var user = headerview.findViewById<TextView>(R.id.tv_name)
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
        var username = LocalSessionManager.getStringValue("userName", "", this)
        if (username != null) {
            user.setText(username)
            header_name.setText("Hello "+ username)
        }

// Creates instance of the manager.
         appUpdateManager = AppUpdateManagerFactory.create(this)

// Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // For a flexible update, use AppUpdateType.FLEXIBLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                // Request the update.
            }

            appUpdateManager.startUpdateFlowForResult(
                // Pass the intent that is returned by 'getAppUpdateInfo()'.
                appUpdateInfo,
                // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                AppUpdateType.FLEXIBLE,
                // The current activity making the update request.
                this,
                // Include a request code to later monitor this update request.
                MY_REQUEST_CODE)
        }

        fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent) {
            if (requestCode == MY_REQUEST_CODE){
                if (requestCode != RESULT_OK){
                    Log.i("kk","Update flow failed! Result code: " + resultCode)
                    // If the update is cancelled or fails,
                    // you can request to start the update again.
                }
            }
        }
binding.fb.setOnClickListener {
    val openURL = Intent(Intent.ACTION_VIEW)
    openURL.data =
        Uri.parse("https://www.facebook.com/people/SearchBuddy/100089299568762/")
    startActivity(openURL)
}


        binding.insta.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data =
                Uri.parse("https://www.instagram.com/searchbuddy_/")
            startActivity(openURL)
        }
binding.linkdn.setOnClickListener{
    val openURL = Intent(Intent.ACTION_VIEW)
    openURL.data =
        Uri.parse("https://www.linkedin.com/company/searchbuddy/?viewAsMember=true")
    startActivity(openURL)
}
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.grey)
        }

        drawericon.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }

        fun AlertLogout() {
            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle("Alert!")
            //set message for alert dialog
            builder.setMessage("Are you sure, you want to logout?")
            builder.setPositiveButton("Yes") { dialogInterface, which ->
//                Logout()

//                Logout()
                LocalSessionManager.deleteAll(this)
                LocalSessionManager.saveValue(Constant.IS_LOGED_IN, false, this)
                var intent = Intent(this, FirstScreen::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton("No") { dialogInterface, which ->
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        fun goTnc() {
            var download = this?.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            var url =
                "https://searchbuddy-assets.s3.ap-south-1.amazonaws.com/document/Terms%20of%20Use%20Agreement%20Search%20Buddy%20v.3.pdf"
            var uri = Uri.parse(url)
            var getPdf = DownloadManager.Request(uri)
            getPdf.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            download.enqueue(getPdf)
            getPdf.setMimeType("application/pdf")
            Toast.makeText(this, "Download Started", Toast.LENGTH_LONG).show()
//            val openURL = Intent(Intent.ACTION_VIEW)
//            openURL.data =
//                Uri.parse()
//            startActivity(openURL)
        }

        fun goJobs() {
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            navController.navigate(R.id.action_navigation_home_to_nav_sales)
            drawer.closeDrawer(GravityCompat.START)

        }

        fun goFaq() {
            var intent = Intent(this, Faq::class.java)
            startActivity(intent)
        }

        fun goAbout_us() {
            var intent = Intent(this, AboutUs::class.java)
            startActivity(intent)
        }

        fun goContactUs() {
            var intent = Intent(this, ContactUs::class.java)
            startActivity(intent)
        }

        fun goSetting() {
            var intent = Intent(this, ProfileSetting::class.java)
            startActivity(intent)
        }
        fun goBlogs(){
            var intent = Intent(this, Blogs::class.java)
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

        blogs.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                goBlogs()

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

        fun goappliedJobs() {
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            navController.navigate(R.id.action_navigation_home_to_nav_progress)
            drawer.closeDrawer(GravityCompat.START)
        }

        fun gosavedJobs() {
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            navController.navigate(R.id.action_navigation_home_to_saved_jobs)
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

//        appliedJobs.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
//            override fun onMenuItemClick(item: MenuItem): Boolean {
//                goappliedJobs()
//
//
//                drawer.closeDrawer(GravityCompat.START)
//
//                return true
//            }
//        })
        savedJobs.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                gosavedJobs()


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
        headerview.setOnClickListener {
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            navController.navigate(R.id.action_navigation_home_to_navigation_mycattle)
            drawer.closeDrawer(GravityCompat.START)

        }


    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        var toolbar: LinearLayout = findViewById(R.id.toolbar)

        val header_name: TextView = toolbar.findViewById(R.id.header_name)

        var username = LocalSessionManager.getStringValue("userName", "", this)
        if (username != null) {
            header_name.text="Hello "+ username
        }

        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    // If an in-app update is already running, resume the update.
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        IMMEDIATE,
                        this,
                        MY_REQUEST_CODE
                    );
                }
            }
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
        getBasicDetails()
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

            OnLogout()


        })
    }

    private fun OnLogout() {
        var intent = Intent(this, FirstScreen::class.java)
        startActivity(intent)
    }

    private fun getBasicDetails() {
       var UserId = LocalSessionManager.getStringValue(Constant.UserID, "", this).toString()
        var userIdINT =UserId.toInt()
        //        var profile_pic=headerview.findViewById<ShapeableImageView>(R.id.iv_profile_photo)
        Log.i("ppppp", userIdINT.toString())

        viewModel.requestPersonalDetail(this, userIdINT, binding.progress).observe(this, {
            if (it != null) {
                if (it.userDTO.profilePicName != null) {
                    val executor = Executors.newSingleThreadExecutor()
                    var image: Bitmap? = null
                    val handler = Handler(Looper.getMainLooper())
                    var uri= Uri.parse("https://www.searchbuddy.in/api/get-picture/profilepicture/" + it.userDTO.profilePicName)
                    Glide.with(this).load(uri).into(drawer_profile);
                    Glide.with(this).load(uri).into(profile_pic);
                    var toolbar: LinearLayout = findViewById(R.id.toolbar)
//
                    val header_name: TextView = toolbar.findViewById(R.id.header_name)
                    header_name.text="Hello "+ it.userDTO.name.toString()



                }
                if (it.userDTO.email!=null){
                    LocalSessionManager.saveValue(Constant.EMAIL,it.userDTO.email.toString(),this)

                }
            }
        })
    }

}


