package com.example.searchbuddy

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.searchbuddy.Adapter.*
import com.example.searchbuddy.Dashboard.Dashboard
import com.example.searchbuddy.Login.Login
import com.example.searchbuddy.Utils.Constant
import com.example.searchbuddy.Utils.LocalSessionManager
import com.example.searchbuddy.databinding.FragmentHomeFragmentBinding
import com.example.searchbuddy.model.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors


class Homefragment : Fragment() {
    lateinit var binding: FragmentHomeFragmentBinding
    lateinit var JobRecyclerView: RecyclerView
    lateinit var Reccomended_recyler: RecyclerView
    lateinit var Top_recyler: RecyclerView
    lateinit var newArrayList: ArrayList<JobCategoryModel>
    lateinit var recommList: ArrayList<RecommendedJob>
    lateinit var Top_List: ArrayList<TopCompaniesModel>
    lateinit var image: Array<Int>
    lateinit var description: Array<String>
    lateinit var comapny_name: Array<String>
    lateinit var post_name: Array<String>
    lateinit var job_location: Array<String>
    lateinit var experience: Array<String>
    lateinit var company_image: Array<Int>
    lateinit var comapny_name_top: Array<String>
    lateinit var post_name_top: Array<String>
    lateinit var experience_top: Array<String>
    lateinit var company_image_top: Array<Int>
    lateinit var myadapter: JobCategoryAdapter
    lateinit var my_adapter: RecommendedJobRecyclerAdapter
    lateinit var home_viewmodel : HomeFragmentViewModel
    lateinit var top_adapter: TopCompaniesAdapter
    lateinit var header: RelativeLayout
    lateinit var bottomNavView: BottomNavigationView
    lateinit var Jobadapter: FieldSalesAdapter
    lateinit var currentDate:String
    lateinit var date: SimpleDateFormat
    lateinit var appliedJobsAdapter :AppliedJobsAdapter
    lateinit var Applied_recyler: RecyclerView


    //    lateinit var profileIcon: ShapeableImageView
    lateinit var notificationIcon: ShapeableImageView
    lateinit var profileIcon: ShapeableImageView
    lateinit var UserID: String
    var yo: Int = 0
//    lateinit var bar: androidx.appcompat.widget.SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        home_viewmodel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
        LocalSessionManager.removeValue(Constant.DatePost,requireContext())
        LocalSessionManager.removeValue(Constant.FillLocation,requireContext())

        binding = FragmentHomeFragmentBinding.inflate(inflater, container, false)
        RecommendedJobs(binding.progress)
        Applied_recyler=binding.appliedRecyler
requestAppliedJobs(binding.progress)
        image = arrayOf(
            R.drawable.field_sales,
            R.drawable.product_sales,
            R.drawable.statistics,
            R.drawable.letter,
            R.drawable.accounting,
            R.drawable.dollar,
            R.drawable.sales_marketing,
            R.drawable.add
        )
        description = arrayOf(
            "Field Sales",
            "Product/Service Sales",
            "Sales Strategy",
            "Channel Sales",
            "Management",
            "Sales Process and Enablement",
            "Sales and Marketing Operation",
            "Others"
        )
        binding.viewAll.setOnClickListener{
      Navigation.findNavController(it).navigate(R.id.action_navigation_home_to_applied_jobs)
        }
binding.viewalljobs.setOnClickListener {
    LocalSessionManager.removeValue(Constant.cat_name,requireContext())
    Navigation.findNavController(it).navigate(R.id.action_navigation_home_to_nav_sales)
}
        binding.appliedJobs.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_navigation_home_to_applied_jobs)
        }
        company_image = arrayOf(R.drawable.bulb, R.drawable.bulb, R.drawable.bulb, R.drawable.bulb)
        company_image_top =
            arrayOf(R.drawable.creative_bulb, R.drawable.creative_bulb, R.drawable.creative_bulb, R.drawable.creative_bulb)
        comapny_name =
            arrayOf("XYZ technologies", "XYZ technologies", "XYZ technologies", "XYZ technologies")
        comapny_name_top =
            arrayOf("XYZ technologies", "XYZ technologies", "XYZ technologies", "XYZ technologies")
        post_name = arrayOf("Graphic Designer", "Android Developer", "Hello", "Hello")
//        post_name_top = arrayOf("Graphic Designer", "Android Developer", "Hello", "Hello")
        job_location = arrayOf("Haridwar", "Jaipur", "New Delhi", "Bangalore")
        experience = arrayOf("2yrs", "2yrs", "2yrs", "2yrs")
//        experience_top = arrayOf("2-4yrs", "2-3yrs", "1-2yrs", "2-4yrs")

        JobRecyclerView = binding.recycler
        JobRecyclerView.layoutManager = GridLayoutManager(context, 3)
        newArrayList = arrayListOf<JobCategoryModel>()
        getdata()
        Topcomapnies(binding.progress)
        Reccomended_recyler = binding.recommRecycler
        recommList = arrayListOf<RecommendedJob>()
        Top_recyler = binding.topCompaniesRecycler
        Top_List = arrayListOf<TopCompaniesModel>()

        home_viewmodel.errorMessage()?.observe(requireActivity(), {

            val builder = AlertDialog.Builder(requireContext())

            // set title
            builder.setTitle("You've been logged out. Please login again")

            // set dialog non cancelable
            builder.setCancelable(false)
            builder.setPositiveButton("Ok"){dialog,id ->
                var intent= Intent(requireContext(), Login::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
           builder.show()

//            showToast("Please login to see recommendations")

        })
        date = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        currentDate = date.format(Date())

        binding.searchBarHom.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                var bundle=Bundle()
                bundle.putString("Search",query)
                bundle.putString("company","")

                Navigation.findNavController(binding.searchBarHom).navigate(R.id.action_navigation_home_to_nav_sales,bundle)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (::Jobadapter.isInitialized) {

                    Jobadapter.filter.filter(newText)
                }

                return true
            }
        })
        var profilepicName=LocalSessionManager.getStringValue("profilepic","",requireContext())
        if (profilepicName!=null){
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
                            binding.imProfilePic.setImageBitmap(image)
                        }
                    }
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
            }


        }
        // Inflate the layout for this fragment
        return binding.root

    }
    fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
    override fun onResume() {
        super.onResume()
        header = (activity as Dashboard)!!.findViewById(R.id.header)
        header.visibility = View.VISIBLE
        bottomNavView = (activity as Dashboard)!!.findViewById(R.id.nav_view)
        bottomNavView.visibility = View.VISIBLE
        profileIcon = (activity as Dashboard)!!.findViewById(R.id.drawer_icon)
        notificationIcon = (activity as Dashboard)!!.findViewById(R.id.notification_icon)

        profileIcon.visibility = View.VISIBLE
//        bar.visibility=View.VISIBLE
        notificationIcon.visibility = View.GONE
        binding.profileName.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_navigation_home_to_navigation_mycattle)
        }
        header = (activity as Dashboard)!!.findViewById(R.id.header)
        header.setBackgroundDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.color.orange
            )
        )


    }

    private fun getdata() {
        for (i in 0..image.size - 1) {
            val listdata = JobCategoryModel(image[i], description[i])
            newArrayList.add(listdata)

        }
        myadapter = JobCategoryAdapter(newArrayList, JobCategoryAdapter.OnClickListener{
              text->

//            Toast.makeText(requireContext(), text.text, Toast.LENGTH_SHORT).show()
            LocalSessionManager.saveValue(Constant.cat_name,text.text,requireContext())

        },requireContext())
        JobRecyclerView.adapter = myadapter
    }

    private fun RecommendedJobs(progress: ProgressBar) {

        home_viewmodel.requestRecommendedJobs(requireContext(), progress).observe(requireActivity(), {
                var position_list: ArrayList<RecommendedJob> = ArrayList()
            if (it.recommendedJobs!=null) {
                position_list = it.recommendedJobs as ArrayList<RecommendedJob>
                Log.i("yooooooooooo", position_list.toString())
                Log.i("Size", it.recommendedJobs.size.toString())
                my_adapter = RecommendedJobRecyclerAdapter(position_list)
                Reccomended_recyler.adapter = my_adapter
            }
            else{
                binding.txtNoData.visibility=View.VISIBLE
            }
//            binding.appliedJobs.setText(it.totalScore.toString())

            })
    }
private fun Topcomapnies(progress: ProgressBar){
    home_viewmodel.requestTopcompanies(requireContext(),progress).observe(requireActivity(),{
        var companies_list:ArrayList<TopcomapniesResponseItem> = ArrayList()
            companies_list=it
           top_adapter=TopCompaniesAdapter(companies_list)
            Top_recyler.adapter=top_adapter

    })

}
    private fun requestAppliedJobs(progress: ProgressBar) {
//        UserID = LocalSessionManager.getStringValue(Constant.UserID, "", requireContext()).toString()
        var index: Int = 1
        var pagesize: Int = 10

        UserID = LocalSessionManager.getStringValue(Constant.UserID, "", requireContext()).toString()
        yo = UserID.toInt()
//        var RequestParams = AppliedJobsRequestModel(condition)

        home_viewmodel.requestAppliedJobs(requireContext(),yo,pagesize,index,progress )
            .observe(requireActivity(), {

                var position_list: ArrayList<Positio> = ArrayList()
                position_list = it.positions as ArrayList<Positio>
                appliedJobsAdapter = AppliedJobsAdapter(position_list)

                Applied_recyler.adapter = appliedJobsAdapter
                if (position_list.isEmpty()){
                    binding.txtNData.visibility=View.VISIBLE
                }
            })
    }
}