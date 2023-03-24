package com.example.searchbuddy.AppliedJobs

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.searchbuddy.Adapter.NavAppliedJobsAdapter
import com.example.searchbuddy.Dashboard.Dashboard
import com.example.searchbuddy.R
import com.example.searchbuddy.Utils.Constant
import com.example.searchbuddy.Utils.LocalSessionManager
import com.example.searchbuddy.databinding.FragmentAppliedJobsBinding
import com.example.searchbuddy.model.Positio
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.imageview.ShapeableImageView

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
    lateinit var appliedJobsAdapter :NavAppliedJobsAdapter
    lateinit var AppliedJobsViewMode : AppliedJobsViewModel
    lateinit var UserID: String
    lateinit var profileIcon: ShapeableImageView
    lateinit var header: RelativeLayout
    var yo: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.visibility=View.GONE
        if (Build.VERSION.SDK_INT >= 21) {
            val window = requireActivity().window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.orange)

    }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAppliedJobsBinding.inflate(inflater, container, false)
        AppliedJobsViewMode = ViewModelProvider(this).get(AppliedJobsViewModel::class.java)
        header = (activity as Dashboard)!!.findViewById(R.id.header)
        header.visibility = View.GONE
Applied_recyler = binding.appliedJobRecycler
        Applied_recyler.layoutManager = LinearLayoutManager(requireContext())

        requestAppliedJobs(binding.progress)
        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        profileIcon = (activity as Dashboard)!!.findViewById(com.example.searchbuddy.R.id.drawer_icon)
        profileIcon.visibility=View.GONE
        UserID = LocalSessionManager.getStringValue(Constant.UserID, "", requireContext()).toString()
         yo = UserID.toInt()
        return binding.root

    }
    private fun requestAppliedJobs(progress: ProgressBar) {
//        UserID = LocalSessionManager.getStringValue(Constant.UserID, "", requireContext()).toString()
            var index: Int = 1
            var pagesize: Int = 10

        UserID = LocalSessionManager.getStringValue(Constant.UserID, "", requireContext()).toString()
        yo = UserID.toInt()
//        var RequestParams = AppliedJobsRequestModel(condition)

        AppliedJobsViewMode.requestAppliedJobs(requireContext(),yo,pagesize,index,progress )
            .observe(requireActivity(), {

                    var position_list: ArrayList<Positio> = ArrayList()
                    position_list = it.positions as ArrayList<Positio>
                    appliedJobsAdapter = NavAppliedJobsAdapter(position_list)

                    Applied_recyler.adapter = appliedJobsAdapter
              if (position_list.isEmpty()){
                  binding.txtNoData.visibility=View.VISIBLE
              }
            })
    }
}