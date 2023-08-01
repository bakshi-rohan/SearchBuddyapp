package com.searchbuddy.searchbuddy.AppliedJobs

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.searchbuddy.R
import com.bumptech.searchbuddy.databinding.FragmentAppliedJobsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.imageview.ShapeableImageView
import com.searchbuddy.searchbuddy.Adapter.NavAppliedJobsAdapter
import com.searchbuddy.searchbuddy.Adapter.PreAppliedJobsAdapter
import com.searchbuddy.searchbuddy.Dashboard.Dashboard
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager
import com.searchbuddy.searchbuddy.model.Positio




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [Applied_Jobs.newInstance] factory method to
 * create an instance of this fragment.
 */
class Applied_Jobs : Fragment() {
    lateinit var binding: FragmentAppliedJobsBinding
    lateinit var Applied_recyler: RecyclerView
    lateinit var PreApplied_recyler: RecyclerView
    lateinit var appliedJobsAdapter : NavAppliedJobsAdapter
    lateinit var PreappliedJobsAdapter : PreAppliedJobsAdapter
    lateinit var AppliedJobsViewMode : AppliedJobsViewModel
    lateinit var UserID: String
    lateinit var profileIcon: ShapeableImageView
    lateinit var header: RelativeLayout
    var yo: Int = 0
    lateinit var descriptionData:Array<String>
    lateinit var bottomNavView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomNavView = (activity as Dashboard)!!.findViewById(R.id.nav_view)
        bottomNavView.visibility = View.VISIBLE
        val nav: Menu = bottomNavView.menu
        descriptionData= arrayOf("Basic\nDetails", "Education", "Experience", "CV","Preferences")
        if (Build.VERSION.SDK_INT >= 21) {
            val window = requireActivity().window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.grey)

    }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAppliedJobsBinding.inflate(inflater, container, false)

        AppliedJobsViewMode = ViewModelProvider(this).get(AppliedJobsViewModel::class.java)
        bottomNavView = (activity as Dashboard)!!.findViewById(R.id.nav_view)
        bottomNavView.visibility = View.VISIBLE
        header = (activity as Dashboard)!!.findViewById(R.id.header)
        header.visibility = View.GONE
        bottomNavView=  (activity as Dashboard?)!!.findViewById(R.id.nav_view)
        val nav: Menu = bottomNavView.menu
        var home=nav.findItem(R.id.navigation_home)
        home.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {

                activity?.onBackPressed()

                return true
            }
        })

Applied_recyler = binding.appliedJobRecycler
        PreApplied_recyler=binding.appliedPreJobRecycler
        Applied_recyler.layoutManager = LinearLayoutManager(requireContext())

        requestAppliedJobs(binding.progress)
//        binding.backBtn.setOnClickListener {
//            activity?.onBackPressed()
//        }
        binding.shimmerView.setVisibility(View.VISIBLE);
        binding.shimmerView.startShimmer();
        profileIcon = (activity as Dashboard)!!.findViewById(com.bumptech.searchbuddy.R.id.drawer_icon)
        profileIcon.visibility=View.GONE
       var UserID = LocalSessionManager.getStringValue(Constant.UserID, "", requireContext()).toString()
         yo = UserID.toInt()
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                binding.jobLength.visibility=View.GONE
                if (::appliedJobsAdapter.isInitialized) {

                    appliedJobsAdapter.filter.filter(query)
                }
                if (::PreappliedJobsAdapter.isInitialized) {

                    PreappliedJobsAdapter.filter.filter(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                binding.jobLength.visibility=View.GONE

                if (::appliedJobsAdapter.isInitialized) {

                    appliedJobsAdapter.filter.filter(newText)
                }
                if (::PreappliedJobsAdapter.isInitialized) {

                    PreappliedJobsAdapter.filter.filter(newText)
                }

                return true
            }
        })
//        binding.yourId.addSwitchObserver(RMSwitch.RMSwitchObserver { switchView, isChecked ->
//            if (isChecked) {
//                requestPreAppliedJobs(binding.progress)
//                binding.appliedJobRecycler.visibility = View.GONE
//                binding.appliedPreJobRecycler.visibility = View.VISIBLE
//                requestPreAppliedJobs(binding.progress)
////           binding.txtNopreData.visibility=View.VISIBLE
////           binding.txtNoData.visibility=View.GONE
//
//            } else {
//                requestAppliedJobs(binding.progress)
//                binding.appliedJobRecycler.visibility = View.VISIBLE
//                binding.appliedPreJobRecycler.visibility = View.GONE
//
//            }
//        })
        AppliedJobsViewMode.errorMessage()?.observe(requireActivity(), {
            binding.txtNoData.visibility=View.VISIBLE
            binding.imgNodata.visibility=View.VISIBLE
        })
        return binding.root

    }
    private fun requestAppliedJobs(progress: ProgressBar) {
//        UserID = LocalSessionManager.getStringValue(Constant.UserID, "", requireContext()).toString()
            var index: Int = 1
            var pagesize: Int = 200

        var UserID = LocalSessionManager.getStringValue(Constant.UserID, "", requireContext()).toString()
        yo = UserID.toInt()
//        var RequestParams = AppliedJobsRequestModel(condition)

        AppliedJobsViewMode.requestAppliedJobs(requireContext(),yo,pagesize,index,progress )
            .observe(requireActivity(), {

                    var position_list: ArrayList<Positio> = ArrayList()
                    position_list = it.positions as ArrayList<Positio>
                    appliedJobsAdapter = NavAppliedJobsAdapter(position_list)

                    Applied_recyler.adapter = appliedJobsAdapter
                binding.shimmerView.setVisibility(View.GONE);
                binding.shimmerView.stopShimmer();
              if (position_list.isEmpty()){
                  binding.txtNoData.visibility=View.VISIBLE
                  binding.imgNodata.visibility=View.VISIBLE
                  binding.searchBar.visibility=View.GONE
              }
                else{
                    binding.txtNoData.visibility=View.GONE
                    binding.imgNodata.visibility=View.GONE
                  binding.searchBar.visibility=View.VISIBLE

              }
            })
    }

//    private fun requestPreAppliedJobs(progress: ProgressBar) {
////        UserID = LocalSessionManager.getStringValue(Constant.UserID, "", requireContext()).toString()
//        var index: Int = 1
//        var pagesize: Int = 10
//var Mail=LocalSessionManager.getStringValue("Email","",requireContext())
//        var mail=Mail.toString()
////        var encodedEmail=URLEncoder.encode(mail)
//
//        var email=Mail.toString()
//        var UserID = LocalSessionManager.getStringValue(Constant.UserID, "", requireContext()).toString()
//        yo = UserID.toInt()
////        var RequestParams = AppliedJobsRequestModel(condition)
//
//        AppliedJobsViewMode.requestPreAppliedJobs(requireContext(),pagesize,index,email.toString(),binding.progress )
//            .observe(requireActivity(), {
//
//                var position_list: ArrayList<PayLoad> = ArrayList()
//                position_list = it.payLoad as ArrayList<PayLoad>
//                PreappliedJobsAdapter = PreAppliedJobsAdapter(position_list)
//
//                PreApplied_recyler.adapter = PreappliedJobsAdapter
//                if (position_list.isEmpty()){
//                    binding.txtNoData.visibility=View.VISIBLE
//                    binding.imgNodata.visibility=View.VISIBLE
//                    binding.searchBar.visibility=View.GONE
//                }
//                else{
//                    binding.txtNoData.visibility=View.GONE
//                    binding.imgNodata.visibility=View.GONE
//                    binding.searchBar.visibility=View.VISIBLE
//
//                }
//            })
//    }
}