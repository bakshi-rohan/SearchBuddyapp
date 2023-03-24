package com.example.searchbuddy.Categories

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.searchbuddy.Dashboard.Dashboard
import com.example.searchbuddy.R
import com.example.searchbuddy.databinding.FragmentAppliedJobsDescriptionBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class AppliedJobsDescription : Fragment() {
    lateinit var binding: FragmentAppliedJobsDescriptionBinding
    var position_name = ""
    var company_name = ""
    var url:String? = ""
    lateinit var header: RelativeLayout
    lateinit var bottomNavView: BottomNavigationView
    lateinit var position_id: String
    lateinit var status: String
    lateinit var viewModel: JobdescriptionViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(JobdescriptionViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAppliedJobsDescriptionBinding.inflate(inflater, container, false)
        header = (activity as Dashboard).findViewById(R.id.header)
        header.visibility = View.GONE
        bottomNavView = (activity as Dashboard)!!.findViewById(R.id.nav_view)
        bottomNavView.visibility = View.GONE
        binding.aboutCompanyLayout.visibility = View.GONE
        val text: String = "Job description"
        var bundle = Bundle()
        if (arguments != null) {
            position_id = requireArguments().getInt("position_id").toString()
            status = requireArguments().getString("status").toString()
            position_name = requireArguments().getString("position").toString()
            company_name = requireArguments().getString("company_name").toString()
            url = requireArguments().getString("url").toString()
//            Log.i("url",url.toString())
        }

//        Log.i("xxxxx", position_id)
        val id = position_id.toInt()
//        Log.i("xxxxx", id.toString())

        val content: SpannableString = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, text.length, 0)
        binding.jobNameDescription.text = position_name
        binding.companyNameDescription.text = company_name
        binding.jobDescriptionBtn.setText(content)

        binding.backBtnDesc.setOnClickListener {
            activity?.onBackPressed()
        }

//    if (status!=null){
//        binding.apply.setText(status)
//    }




//        if (url.isNullOrEmpty()) {
//            binding.apply.setOnClickListener {
//                Toast.makeText(
//                    requireContext(),
//                    "You've already applied for this job",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }


        // Inflate the layout for this fragment
        binding.jobDescriptionBtn.setOnClickListener {
            binding.jobDescLayout.visibility = View.VISIBLE
//            binding.jobDescIndusLayout.visibility = View.VISIBLE
//            binding.recruiterDetailLayout.visibility = View.VISIBLE
            binding.aboutCompanyLayout.visibility = View.GONE
            val text: String = "Job description"
            val content: SpannableString = SpannableString(text)
            content.setSpan(UnderlineSpan(), 0, text.length, 0)

            binding.jobDescriptionBtn.setText(content)
            binding.aboutCompanyBtn.setText("About company")

        }
        binding.aboutCompanyBtn.setOnClickListener {
            binding.jobDescLayout.visibility = View.GONE
//            binding.jobDescIndusLayout.visibility = View.GONE
//            binding.recruiterDetailLayout.visibility = View.GONE
            binding.aboutCompanyLayout.visibility = View.VISIBLE

            val text: String = "About Company"
            val content: SpannableString = SpannableString(text)
            content.setSpan(UnderlineSpan(), 0, text.length, 0)
            binding.aboutCompanyBtn.setText(content)
            binding.jobDescriptionBtn.setText("Job description")


        }
//        viewModel.errorMessage()?.observe(viewLifecycleOwner, {
//            showToast(it.toString())
//        })

        fun getHtml(htmlBody: String): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                Html.fromHtml(htmlBody, Html.FROM_HTML_MODE_LEGACY).toString()
            else
                Html.fromHtml(htmlBody).toString()
        }
        viewModel.requestJobs(requireContext(), position_id.toInt(), binding.progress)
            .observe(viewLifecycleOwner, {
//                Log.i("qqq", it.expTo)
                if (it == null) {
                    binding.positionOverviewDetail.setText("No Data Found")
                } else {
                    if (it!=null) {
                        if (it.name!=null) {
                            binding.jobNameDescription.setText(it.name)
                        }
                        if (it.client!=null) {
                            binding.companyNameDescription.setText(it.client)
                        }
                        if (it.expTo!=null&&it.expFrom!=null) {
                            binding.experienceDesc.setText(it.expFrom + "-" + it.expTo + "years")
                        }
                        if (it.roleDesc!=null) {
                            binding.positionOverviewDetail.setText(getHtml(it.roleDesc))
                        }
                        if (it.aboutOrg!=null) {
                            binding.aboutComp.setText(getHtml(it.aboutOrg))
                        }
                        if (it.kra!=null) {
                            binding.tiKra.setText(getHtml(it.kra))
                        }
                        if (it.industry!=null) {
                            binding.industryTypes.setText(it.industry)
                        }
                        if (it.name!=null) {
                            binding.function.setText(it.function)
                        }
                        if (it.location!=null) {
                            val some = it.location.toString()
                            var somstr = some.substring(1, some.length - 1)
                            binding.locationDesc.setText(somstr)
                        }
                        if (it.skills!=null) {
                            var getSkills = it.skills.toString()
                            var skillSubString = getSkills.substring(1, getSkills.length - 1)
                            binding.skills.setText(skillSubString)
                        }
                        if (it.level!=null) {
                            binding.level.setText(it.level)
                        }
                    }
                }
            })
        return binding.root
        // Inflate the layout for this fragment
    }
    fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}