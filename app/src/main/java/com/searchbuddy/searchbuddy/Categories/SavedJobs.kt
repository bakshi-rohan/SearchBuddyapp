package com.searchbuddy.searchbuddy.Categories

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.searchbuddy.R
import com.bumptech.searchbuddy.databinding.FragmentSavedJobsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.imageview.ShapeableImageView
import com.searchbuddy.searchbuddy.Adapter.SavedJobsAdapter
import com.searchbuddy.searchbuddy.Dashboard.Dashboard
import com.searchbuddy.searchbuddy.model.Positions


class SavedJobs : Fragment() {
    lateinit var SaveJobRecycler: RecyclerView
    lateinit var binding: FragmentSavedJobsBinding
    lateinit var activitySavedJobsViewModel: SavedJobsViewModel
    lateinit var savedJobsAdapter : SavedJobsAdapter
    lateinit var header: RelativeLayout
    lateinit var profileIcon: ShapeableImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.visibility=View.GONE
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
        binding = FragmentSavedJobsBinding.inflate(inflater, container, false)
        activitySavedJobsViewModel = ViewModelProvider(this).get(SavedJobsViewModel::class.java)
        header = (activity as Dashboard)!!.findViewById(R.id.header)
        header.visibility = View.GONE
        SaveJobRecycler = binding.appliedJobRecycler
        SaveJobRecycler.layoutManager = LinearLayoutManager(requireContext())
        requestAppliedJobs(binding.progress)

//        binding.backBtn.setOnClickListener {
//            activity?.onBackPressed()
//        }
        profileIcon = (activity as Dashboard)!!.findViewById(com.bumptech.searchbuddy.R.id.drawer_icon)
        profileIcon.visibility=View.GONE
        // Inflate the layout for this fragment
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                binding.jobLength.visibility=View.GONE
                if (::savedJobsAdapter.isInitialized) {

                    savedJobsAdapter.filter.filter(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                binding.jobLength.visibility=View.GONE

                if (::savedJobsAdapter.isInitialized) {

                    savedJobsAdapter.filter.filter(newText)
                }

                return true
            }
        })
        return binding.root

    }
    private fun requestAppliedJobs(progress: ProgressBar) {

        var index: Int = 1
        var pagesize: Int = 10

        activitySavedJobsViewModel.requestAppliedJobs(requireContext(),pagesize,index,progress,binding.imgNodata,binding.txtNoData)
            .observe(requireActivity(), {

                var position_list: ArrayList<Positions> = ArrayList()
                if (it!=null) {
                    position_list = it.positions as ArrayList<Positions>
                    savedJobsAdapter = SavedJobsAdapter(position_list,requireContext())
                    binding.imgNodata.visibility=View.GONE

                    SaveJobRecycler.adapter = savedJobsAdapter
                }

                    if (it.positions.isEmpty()) {
                        binding.txtNoData.visibility = View.VISIBLE
                        binding.imgNodata.visibility = View.VISIBLE
                    }
            })
    }

}