package com.searchbuddy.searchbuddy.Categories

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.searchbuddy.R
import com.bumptech.searchbuddy.databinding.FragmentAppliedJobsDescriptionBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.searchbuddy.searchbuddy.Dashboard.Dashboard
import java.util.concurrent.Executors

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
    var position_id_int: Int=0
    lateinit var logo: String

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
        val text: String = "Job Description"
        var bundle = Bundle()
        if (arguments != null) {
            position_id = requireArguments().getInt("position_id").toString()
            status = requireArguments().getString("status").toString()
            position_name = requireArguments().getString("position").toString()
            company_name = requireArguments().getString("company_name").toString()
            url = requireArguments().getString("url").toString()
            position_id_int = requireArguments().getInt("position_id")

        }
        binding.jobDescriptionBtn.setBackgroundResource(R.drawable.job_sdesc_selected_border)

        val id = position_id.toInt()

        val content: SpannableString = SpannableString(text)
//        content.setSpan(UnderlineSpan(), 0, text.length, 0)
        binding.jobNameDescription.text = position_name
        binding.companyNameDescription.text = company_name
        binding.jobDescriptionBtn.setText(content)

        binding.backBtnDesc.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.jobDescriptionBtn.setOnClickListener {
            binding.jobDescLayout.visibility = View.VISIBLE
            binding.jobDescriptionBtn.setTypeface(null, Typeface.BOLD)
            binding.aboutCompanyBtn.setTypeface(null, Typeface.NORMAL)
            binding.aboutCompanyLayout.visibility = View.GONE
            binding.jobDescriptionBtn.setBackgroundResource(R.drawable.job_sdesc_selected_border)
            binding.jobDescriptionBtn.setTextColor(Color.WHITE)
            binding.aboutCompanyBtn.setTextColor(Color.BLACK)
            binding.aboutCompanyBtn.setBackgroundResource(R.drawable.job_desc_border)
            binding.aboutCompanyBtn.setText("About Company")

        }
        binding.aboutCompanyBtn.setOnClickListener {
            binding.jobDescLayout.visibility = View.GONE
            binding.aboutCompanyBtn.setTypeface(null, Typeface.BOLD)
            binding.jobDescriptionBtn.setTypeface(null, Typeface.NORMAL)
            binding.aboutCompanyLayout.visibility = View.VISIBLE
            binding.aboutCompanyBtn.setBackgroundResource(R.drawable.job_sdesc_selected_border)
            binding.jobDescriptionBtn.setBackgroundResource(R.drawable.job_desc_border)
            binding.aboutCompanyBtn.setTextColor(Color.WHITE)
            binding.jobDescriptionBtn.setTextColor(Color.BLACK)
            binding.jobDescriptionBtn.setText("Job Description")


        }


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
                            binding.experienceDesc.setText(it.expFrom + "-" + it.expTo + " years")
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
                        if (it.logo != null) {
                            logo = it.logo
                            val executor = Executors.newSingleThreadExecutor()
                            var image: Bitmap? = null
                            val handler = Handler(Looper.getMainLooper())
                            executor.execute {
                                val imageUrl =
                                    "https://www.searchbuddy.in/api/get-picture/organisation/" + logo
                                try {
                                    val `in` = java.net.URL(imageUrl).openStream()
                                    image = BitmapFactory.decodeStream(`in`)
                                    if (image != null) {

                                        handler.post {
                                            binding.build.setImageBitmap(image)
                                            binding.build.setImageBitmap(image)
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                        if (it.location!=null) {
                            val some = it.location.toString()
                            var somstr = some.substring(1, some.length - 1)
                            binding.locationDesc.setText(somstr)
                        }
                        if (it.positionSaved!=null) {
                            if (it.positionSaved == true) {
                                binding.saveJob.setText("Saved")
                                binding.saveJob.setOnClickListener {
                                    deleteJobs()
                                }
                            } else {
                                binding.saveJob.setText("Save")
                                binding.saveJob.setOnClickListener {
                                    saveJobs()
                                }
                            }
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
    private fun saveJobs(){

        var jobList= arrayListOf<Any>()
        var kk=object {
            var id=position_id_int
        }
        jobList.add(kk)
        viewModel.saveJobs(requireContext(),jobList,binding.progress).observe(viewLifecycleOwner,{
            Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
        })
    }
    private fun deleteJobs(){


        var id=position_id_int

        viewModel.deleteJobs(requireContext(),id,binding.progress).observe(viewLifecycleOwner,{
            Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
            binding.saveJob.setText("Save")

        })
    }
}