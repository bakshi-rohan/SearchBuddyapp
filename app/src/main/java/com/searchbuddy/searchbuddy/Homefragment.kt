package com.searchbuddy.searchbuddy

import CarouselAdapter
import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.searchbuddy.R
import com.bumptech.searchbuddy.databinding.FragmentHomeFragmentBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.imageview.ShapeableImageView
import com.searchbuddy.searchbuddy.Adapter.AppliedJobsAdapter
import com.searchbuddy.searchbuddy.Adapter.FieldSalesAdapter
import com.searchbuddy.searchbuddy.Adapter.JobCategoryAdapter
import com.searchbuddy.searchbuddy.Adapter.RecommendedJobRecyclerAdapter
import com.searchbuddy.searchbuddy.Adapter.TopCompaniesAdapter
import com.searchbuddy.searchbuddy.Dashboard.Dashboard
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager
import com.searchbuddy.searchbuddy.model.CarouselItem
import com.searchbuddy.searchbuddy.model.JobCategoryModel
import com.searchbuddy.searchbuddy.model.Positio
import com.searchbuddy.searchbuddy.model.RecommendedJob
import com.searchbuddy.searchbuddy.model.TopCompaniesModel
import com.searchbuddy.searchbuddy.model.TopcomapniesResponseItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.Executors


@ExperimentalBadgeUtils class Homefragment : Fragment() {
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
    lateinit var home_viewmodel: HomeFragmentViewModel
    lateinit var top_adapter: TopCompaniesAdapter
    lateinit var header: RelativeLayout
    lateinit var bottomNavView: BottomNavigationView
    lateinit var Jobadapter: FieldSalesAdapter
    lateinit var currentDate: String
    lateinit var date: SimpleDateFormat
    lateinit var appliedJobsAdapter: AppliedJobsAdapter
    lateinit var Applied_recyler: RecyclerView


    //    lateinit var profileIcon: ShapeableImageView
    lateinit var notificationIcon: ShapeableImageView
    lateinit var profileIcon: ShapeableImageView
    lateinit var UserID: String
    var yo: Int = 0
    private lateinit var viewPager: ViewPager2
    private lateinit var carouselAdapter: CarouselAdapter
    private  var isSearched: Boolean=false

    //    lateinit var bar: androidx.appcompat.widget.SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        home_viewmodel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
        LocalSessionManager.removeValue(Constant.DatePost, requireContext())
        LocalSessionManager.removeValue(Constant.FillLocation, requireContext())

        binding = FragmentHomeFragmentBinding.inflate(inflater, container, false)
        binding.shimmerView.setVisibility(View.VISIBLE);
        binding.shimmerView.startShimmer();
        binding.shimmerViewRecommended.setVisibility(View.VISIBLE);
        binding.shimmerViewRecommended.startShimmer();
        RecommendedJobs(binding.progress)
        getBasicDetails()

        Applied_recyler = binding.appliedRecyler
//requestAppliedJobs(binding.progress)
        viewPager = binding.viewPager
        carouselAdapter = CarouselAdapter(getSampleData())
        viewPager.adapter = carouselAdapter
        image = arrayOf(
            R.drawable.field_sales,
            R.drawable.direct_sale,
            R.drawable.buisness_dev,
            R.drawable.channel_sa,
            R.drawable.accounting,
            R.drawable.b_to_c,
            R.drawable.govt_sales,
            R.drawable.institutional
        )
        description = arrayOf(
            "Field Sales",
            "Direct Sales",
            "Business Development",
            "Channel Sales",
            "Inside Sales",
            "Sales (B2C)",
            "Govt. Sales",
            "Institutional Sales"
        )
        binding.viewAll.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_navigation_home_to_nav_progress)
        }
        binding.viewalljobs.setOnClickListener {
            LocalSessionManager.removeValue(Constant.cat_name, requireContext())
            Navigation.findNavController(it).navigate(R.id.action_navigation_home_to_nav_sales)
        }
        binding.appliedJobs.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_navigation_home_to_nav_progress)
        }
        company_image = arrayOf(R.drawable.bulb, R.drawable.bulb, R.drawable.bulb, R.drawable.bulb)
        company_image_top =
            arrayOf(
                R.drawable.creative_bulb,
                R.drawable.creative_bulb,
                R.drawable.creative_bulb,
                R.drawable.creative_bulb
            )
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
//        JobRecyclerView.layoutManager = LinearLayoutManager(context)
        newArrayList = arrayListOf<JobCategoryModel>()
        getdata()

        binding.hl.visibility=View.GONE
        Reccomended_recyler = binding.recommRecycler
        recommList = arrayListOf<RecommendedJob>()
        Top_recyler = binding.topCompaniesRecycler

        Top_List = arrayListOf<TopCompaniesModel>()
        var username = LocalSessionManager.getStringValue("userName", "", requireContext())
        if (username != null) {
            binding.usernameProfile.setText(username)
        }
        home_viewmodel.errorMessage()?.observe(requireActivity(), {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        })
        date = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        currentDate = date.format(Date())



        binding.searchBarHom.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                isSearched=true
                var bundle = Bundle()
                bundle.putString("Search", query)
                bundle.putString("company", "")
                bundle.putBoolean("isSearched", isSearched)

                Navigation.findNavController(binding.searchBarHom)
                    .navigate(R.id.action_navigation_home_to_nav_sales, bundle)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (::Jobadapter.isInitialized) {

                    Jobadapter.filter.filter(newText)
                }

                return true
            }
        })
//        var profilepicName=LocalSessionManager.getStringValue("profilepic","",requireContext())
//        if (profilepicName!=null){
//            val executor = Executors.newSingleThreadExecutor()
//            var image: Bitmap? = null
//            val handler = Handler(Looper.getMainLooper())
//            executor.execute {
//                val imageUrl =
//                    "https://testingsales.searchbuddy.in/api/get-picture/profilepicture/" + profilepicName
//                try {
//                    val `in` = java.net.URL(imageUrl).openStream()
//                    image = BitmapFactory.decodeStream(`in`)
//                    if (image != null) {
//
//                        handler.post {
//                            binding.imProfilePic.setImageBitmap(image)
//                            binding.dp.setImageBitmap(image)
//                        }
//                    }
//                }
//                catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }


//        }
        // Inflate the layout for this fragment
        Topcomapnies(binding.progress)

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
        notificationIcon.visibility = View.GONE
        val badge = BadgeDrawable.create(requireContext())
        badge.number = 4
        badge.backgroundColor=Color.RED
        badge.setVerticalOffset(21);
        badge.setHorizontalOffset(42);
        BadgeUtils.attachBadgeDrawable(
            badge,
            notificationIcon,
        )
        binding.profileName.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_navigation_home_to_navigation_mycattle)
        }
        binding.updateProfile.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_navigation_home_to_navigation_mycattle)
        }
        binding.shimmerViewRecommended.setVisibility(View.GONE);
        binding.shimmerViewRecommended.stopShimmer();
        header = (activity as Dashboard)!!.findViewById(R.id.header)
        header.setBackgroundDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.color.grey
            )
        )
        getBasicDetails()
Topcomapnies(binding.progress)

    }

    private fun getdata() {
        for (i in 0..image.size - 1) {
            val listdata = JobCategoryModel(image[i], description[i])
            newArrayList.add(listdata)

        }
        myadapter = JobCategoryAdapter(newArrayList, JobCategoryAdapter.OnClickListener { text ->

//            Toast.makeText(requireContext(), text.text, Toast.LENGTH_SHORT).show()
            LocalSessionManager.saveValue(Constant.cat_name, text.text, requireContext())

        }, requireContext())
        JobRecyclerView.adapter = myadapter
    }

    private fun RecommendedJobs(progress: ProgressBar) {

        home_viewmodel.requestRecommendedJobs(requireContext(), progress)
            .observe(requireActivity(), {
                var position_list: ArrayList<RecommendedJob> = ArrayList()
                if (it.profilePercentage!=null) {
                    if (it.profilePercentage == 100) {
                        binding.warn.visibility = View.VISIBLE
                        binding.warnImg.visibility = View.GONE
                        binding.warnText.setText("Congratulations!! Your profile is 100% completed.")
                    }
                        binding.profilePercent.setText(it.profilePercentage.toString() + "% completed")
                        binding.progressBar.progress = it.profilePercentage
//                    Log.i("pppp", it.profilePercentage.toString())
                        ObjectAnimator.ofInt(
                            binding.progressBar,
                            "progress",
                            0,
                            binding.progressBar.progress
                        )
                            .setDuration(1150)
                            .start();
binding.shimmerViewRecommended.visibility=View.GONE
                    binding.shimmerViewRecommended.stopShimmer();

                }
                if (it.recommendedJobs != null) {

                    position_list = it.recommendedJobs as ArrayList<RecommendedJob>
                    my_adapter = RecommendedJobRecyclerAdapter(position_list,requireContext(),
                        { index -> saveJobs(index) },
                        { index -> deleteJobs(index) })
                    Reccomended_recyler.adapter = my_adapter
                } else {
                    binding.txtNoData.visibility = View.VISIBLE
                }
//            binding.appliedJobs.setText(it.totalScore.toString())

            })
    }

    private fun getBasicDetails() {
        var UserId =
            LocalSessionManager.getStringValue(Constant.UserID, "", requireContext()).toString()
//        yo = UserID.toInt()
        yo = UserId.toInt()
        home_viewmodel.requestPersonalDetail(requireContext(), yo, binding.progress)
            .observe(requireActivity(), {

                if (it != null) {
                    if (it.userDTO.profilePicName != null) {
                        val executor = Executors.newSingleThreadExecutor()
                        var image: Bitmap? = null
                        val handler = Handler(Looper.getMainLooper())
                        var uri= Uri.parse("https://www.searchbuddy.in/api/get-picture/profilepicture/" + it.userDTO.profilePicName)
                        Glide.with(this).load(uri).into(binding.imProfilePic);
                        Glide.with(this).load(uri).into(binding.dp);
//                        executor.execute {
//                            val imageUrl =
//                                "https://www.searchbuddy.in/api/get-picture/profilepicture/" + it.userDTO.profilePicName
//                            try {
//                                val `in` = java.net.URL(imageUrl).openStream()
//                                image = BitmapFactory.decodeStream(`in`)
//                                if (image != null) {
//
//                                    handler.post {
//                                        binding.imProfilePic.setImageBitmap(image)
//                                        binding.dp.setImageBitmap(image)
//                                    }
//                                }
//                            } catch (e: Exception) {
//                                e.printStackTrace()
//                            }
//                        }

                    }
                    if (it.workExperiencePresent != null) {
                        if (it.workExperiencePresent == true) {
                            binding.homeWexp.visibility = View.GONE
                        } else {
                            binding.homeWexp.visibility = View.VISIBLE
                        }
                    }
                    if (it.educationDetailPresent != null) {
                        if (it.educationDetailPresent == true) {
                            binding.homeEd.visibility = View.GONE
                        } else {
                            binding.homeEd.visibility = View.VISIBLE
                        }
                    }
                    if (it.cvPresent != null) {
                        if (it.cvPresent == true) {
                            binding.homeAddCv.visibility = View.GONE
                        } else {
                            binding.homeAddCv.visibility = View.VISIBLE
                        }
                    }
                    if (it.videoPresent != null) {
                        if (it.videoPresent == true) {
                            binding.homeAddVideoCv.visibility = View.GONE
                        } else {
                            binding.homeAddVideoCv.visibility = View.VISIBLE
                        }
                    }
                }
            })
    }

    private fun Topcomapnies(progress: ProgressBar) {
        home_viewmodel.requestTopcompanies(requireContext(), progress).observe(requireActivity(), {
            binding.shimmerView.setVisibility(View.GONE);
            binding.shimmerView.stopShimmer();
            var companies_list: ArrayList<TopcomapniesResponseItem> = ArrayList()
            companies_list = it
            top_adapter = TopCompaniesAdapter(companies_list,requireContext())
            Top_recyler.adapter = top_adapter

//            if (it.isEmpty()){
//                binding.hl.visibility=View.VISIBLE
//            }
//            else{
//                binding.hl.visibility=View.GONE
//            }
        })

    }

    private fun requestAppliedJobs(progress: ProgressBar) {
//        UserID = LocalSessionManager.getStringValue(Constant.UserID, "", requireContext()).toString()
        var index: Int = 1
        var pagesize: Int = 10

        UserID =
            LocalSessionManager.getStringValue(Constant.UserID, "", requireContext()).toString()
//        yo = UserID.toInt()
        yo = 4
//        var RequestParams = AppliedJobsRequestModel(condition)

        home_viewmodel.requestAppliedJobs(requireContext(), yo, pagesize, index, progress)
            .observe(requireActivity(), {

                var position_list: ArrayList<Positio> = ArrayList()
                position_list = it.positions as ArrayList<Positio>
                appliedJobsAdapter =
                    AppliedJobsAdapter(
                        position_list
                    )

                Applied_recyler.adapter = appliedJobsAdapter
                if (position_list.isEmpty()) {
                    binding.txtNData.visibility = View.VISIBLE
                }
            })
    }

    private fun saveJobs(index: Int) {

        var jobList = arrayListOf<Any>()
        var kk = object {
            var id = index
        }
        jobList.add(kk)
        home_viewmodel.saveJobs(requireContext(), jobList, binding.progress)
            .observe(viewLifecycleOwner, {
            })
    }

    private fun deleteJobs(index: Int) {


        var id = index

//        jobList.add(kk)
        home_viewmodel.deleteJobs(requireContext(), id, binding.progress)
            .observe(viewLifecycleOwner, {
            })
    }
    private fun getSampleData(): List<CarouselItem> {
        // Replace this with your actual data source (list of CarouselItem objects)
        return listOf(
            CarouselItem(R.drawable.fleetx, "FleetX"),
            CarouselItem(R.drawable.motilal, "Image 2"),
            CarouselItem(R.drawable.sab_paisa, "Image 3")
        )
    }
}