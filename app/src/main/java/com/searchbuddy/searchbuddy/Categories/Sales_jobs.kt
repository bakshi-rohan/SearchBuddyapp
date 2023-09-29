package com.searchbuddy.searchbuddy.Categories

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.icu.text.Transliterator
import android.os.Bundle
import android.os.Handler
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.searchbuddy.R
import com.bumptech.searchbuddy.databinding.FragmentSalesJobsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.searchbuddy.searchbuddy.Adapter.FieldSalesAdapter
import com.searchbuddy.searchbuddy.Adapter.PremiumJobsAdapter
import com.searchbuddy.searchbuddy.Dashboard.Dashboard
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager
import com.searchbuddy.searchbuddy.model.FieldSalesModel
import com.searchbuddy.searchbuddy.model.JobRequestModel
import com.searchbuddy.searchbuddy.model.Positions
import com.searchbuddy.searchbuddy.model.aa.PrePosition


class Sales_jobs : Fragment() {
    lateinit var FieldJobRecycler: RecyclerView
    lateinit var PremiumJobRecycler: RecyclerView
    lateinit var binding: FragmentSalesJobsBinding
    lateinit var description: Array<String>
    lateinit var experience: Array<String>
    lateinit var salary: Array<String>
    lateinit var company_image: Array<Int>
    lateinit var header: RelativeLayout
    lateinit var field_sale_list: ArrayList<FieldSalesModel>
    lateinit var newArrayList: ArrayList<Transliterator.Position>
    lateinit var myadapter: FieldSalesAdapter
    lateinit var preadapter: PremiumJobsAdapter
    lateinit var bottomNavView: BottomNavigationView
    lateinit var activitySalesViewModel: SalesJobsViewModel
    lateinit var scroll: NestedScrollView
    lateinit var layoutManager: LinearLayoutManager
    private var isLoading: Boolean = false
    var temp: Int = 1
    lateinit var position_list: ArrayList<Positions>
    lateinit var Preposition_list: ArrayList<PrePosition>
    lateinit var exp_from: String
    lateinit var exp_to: String
    lateinit var salary_from: String
    lateinit var salary_to: String
    lateinit var date_posted: String
    lateinit var LocationList: ArrayList<String>
    var exp_from_int: Int = 0
    var exp_to_int: Int = 0
    var sal_from_int: Int = 0
    var sal_to_int: Int = 0
    lateinit var functionList:ArrayList<String>
//    lateinit var company:Array<String>
    lateinit var keyword:Array<String>
    lateinit var function_name:String
     var company_name:String=""
     var level:Int=0
     lateinit var level_string:Array<Int>
     lateinit var job_Type:ArrayList<String>
    var isPremium:Boolean=true
    private lateinit var speech: SpeechRecognizer
    private lateinit var recognizerIntent: Intent
    private lateinit var searchText: String
    private  var isSearched: Boolean=false
    private val handler = Handler()
    var string="Companies"
    var jobtitle="Job Title"
    var keywor="Keywords"
    private val hintTexts = listOf("Search by \"$string\"", "Search by \"$jobtitle\"", "Search by \"$keywor\"")
    private var currentHintIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalSessionManager.removeValue(Constant.SalarySliderStartValue,requireContext())
        LocalSessionManager.removeValue(Constant.SalarySliderEndValue,requireContext())
        LocalSessionManager.removeValue(Constant.FilterLocation,requireContext())
        LocalSessionManager.removeValue(Constant.DatePosted,requireContext())

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSalesJobsBinding.inflate(inflater, container, false)
        header = (activity as Dashboard)!!.findViewById(com.bumptech.searchbuddy.R.id.header)
        header.visibility = View.GONE
        bottomNavView = (activity as Dashboard)!!.findViewById(com.bumptech.searchbuddy.R.id.nav_view)
        bottomNavView.visibility = View.VISIBLE
        newArrayList = arrayListOf<Transliterator.Position>()
        activitySalesViewModel = ViewModelProvider(this).get(SalesJobsViewModel::class.java)
        layoutManager = LinearLayoutManager(requireContext())
        val nav: Menu = bottomNavView.menu

        var home=nav.findItem(com.bumptech.searchbuddy.R.id.navigation_home)
        home.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {

                activity?.onBackPressed()

                return true
            }
        })
        binding.searchBar.queryHint = hintTexts[currentHintIndex]
        handler.postDelayed(updateHintTask, 5000)
        binding.shimmerView.setVisibility(View.VISIBLE);
        binding.shimmerView.startShimmer();

        keyword= arrayOf<String>()
        searchText=""
        if (arguments!=null) {
             searchText = requireArguments().getString("Search","")
             isSearched = requireArguments().getBoolean("isSearched")
            binding.searchBar.setQuery(searchText,true)


        }

        FieldJobRecycler = binding.fieldSaleRecycler
        PremiumJobRecycler = binding.premiumJobRecyler
        FieldJobRecycler.layoutManager = LinearLayoutManager(requireContext())
        PremiumJobRecycler.layoutManager = LinearLayoutManager(requireContext())



        activitySalesViewModel.errorMessage()?.observe(requireActivity(), {
            showToast(it.toString())
        })

        field_sale_list = arrayListOf<FieldSalesModel>()
        binding.jobLength.visibility=View.VISIBLE

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                binding.jobLength.visibility=View.GONE
                if (::myadapter.isInitialized) {

                    myadapter.filter.filter(query)
                }
                if (::preadapter.isInitialized) {

                    preadapter.filter.filter(query)
                }
                    return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                binding.jobLength.visibility=View.GONE

                if (::myadapter.isInitialized) {

                    myadapter.filter.filter(newText)

                }
                if (::preadapter.isInitialized) {

                    preadapter.filter.filter(newText)

                }

                return true
            }
        })

            requestData(binding.progress)


        binding.filters.setOnClickListener {
            var bundle:Bundle= Bundle()
            bundle.putBoolean("isPremium",isPremium)
            Navigation.findNavController(it).navigate(com.bumptech.searchbuddy.R.id.action_nav_sales_to_filter,bundle)
        }
        binding.chip1.setOnClickListener {
            var bundle:Bundle=Bundle()
            bundle.putBoolean("isPremium",isPremium)
            Navigation.findNavController(it).navigate(com.bumptech.searchbuddy.R.id.action_nav_sales_to_filter,bundle)
        }
        binding.chip2.setOnClickListener {
            var bundle:Bundle=Bundle()
            bundle.putBoolean("isPremium",isPremium)
            Navigation.findNavController(it).navigate(com.bumptech.searchbuddy.R.id.action_nav_sales_to_filter,bundle)
        }
        binding.chip3.setOnClickListener {
            var bundle:Bundle=Bundle()
            bundle.putBoolean("isPremium",isPremium)
            Navigation.findNavController(it).navigate(com.bumptech.searchbuddy.R.id.action_nav_sales_to_filter,bundle)
        }
        binding.chip4.setOnClickListener {
            var bundle:Bundle=Bundle()
            bundle.putBoolean("isPremium",isPremium)
            Navigation.findNavController(it).navigate(com.bumptech.searchbuddy.R.id.action_nav_sales_to_filter,bundle)
        }
        binding.chip5.setOnClickListener {
            var bundle:Bundle=Bundle()
            bundle.putBoolean("isPremium",isPremium)
            Navigation.findNavController(it).navigate(com.bumptech.searchbuddy.R.id.action_nav_sales_to_filter,bundle)
        }
        binding.chip6.setOnClickListener {
            var bundle:Bundle=Bundle()
            bundle.putBoolean("isPremium",isPremium)
            Navigation.findNavController(it).navigate(com.bumptech.searchbuddy.R.id.action_nav_sales_to_filter,bundle)
        }
        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        LocalSessionManager.removeValue(Constant.SliderStartValue,requireContext())
        LocalSessionManager.removeValue(Constant.SliderEndValue,requireContext())
        LocalSessionManager.removeValue(Constant.SalarySliderStartValue,requireContext())
        LocalSessionManager.removeValue(Constant.SalarySliderEndValue,requireContext())
        LocalSessionManager.removeValue(Constant.FilterLocation,requireContext())
        LocalSessionManager.removeValue(Constant.DatePosted,requireContext())
        LocalSessionManager.removeValue(Constant.cat_name,requireContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalSessionManager.removeValue(Constant.SliderStartValue,requireContext())
        LocalSessionManager.removeValue(Constant.SliderEndValue,requireContext())
        LocalSessionManager.removeValue(Constant.SalarySliderStartValue,requireContext())
        LocalSessionManager.removeValue(Constant.SalarySliderEndValue,requireContext())
        LocalSessionManager.removeValue(Constant.FilterLocation,requireContext())
        LocalSessionManager.removeValue(Constant.DatePosted,requireContext())
        LocalSessionManager.removeValue(Constant.cat_name,requireContext())
    }
    override fun onDetach() {
        super.onDetach()
        LocalSessionManager.removeValue(Constant.SliderStartValue,requireContext())
        LocalSessionManager.removeValue(Constant.SliderEndValue,requireContext())
        LocalSessionManager.removeValue(Constant.SalarySliderStartValue,requireContext())
        LocalSessionManager.removeValue(Constant.SalarySliderEndValue,requireContext())
        LocalSessionManager.removeValue(Constant.FilterLocation,requireContext())
        LocalSessionManager.removeValue(Constant.DatePosted,requireContext())
        LocalSessionManager.removeValue(Constant.cat_name,requireContext())
    }
    fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }


    private fun requestData(progress: ProgressBar) {
        var condition = object {
            var index: Int = temp
            var pagesize: Int = 200

        }
if (arguments!=null){
    isSearched=requireArguments().getBoolean("isSearched")
}
        if (isSearched==true){
            keyword= arrayOf(searchText)
        }

        else if (arguments != null) {
            company_name = requireArguments().getString("company").toString()


            if (company_name == "") {
                keyword = arrayOf()
            } else {
                keyword = arrayOf(company_name)
            }
        }
        exp_from = LocalSessionManager.getStringValue(
            Constant.SliderStartValue.toString(),
            "",
            requireContext()
        ).toString()

        exp_to = LocalSessionManager.getStringValue(
            Constant.SliderEndValue.toString(),
            "",
            requireContext()
        ).toString()

        salary_from = LocalSessionManager.getStringValue(
            Constant.SalarySliderStartValue.toString(),
            "",
            requireContext()
        ).toString()

        salary_to = LocalSessionManager.getStringValue(
            Constant.SalarySliderEndValue.toString(),
            "",
            requireContext()
        ).toString()

if (exp_from=="0"){
    binding.chip2.chipBackgroundColor=
        ColorStateList.valueOf(ContextCompat.getColor(requireContext(), com.bumptech.searchbuddy.R.color.white))
}
        if (exp_to=="0"){
            binding.chip2.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), com.bumptech.searchbuddy.R.color.white))
        }
        if (salary_from=="0"){
            binding.chip3.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), com.bumptech.searchbuddy.R.color.white))
        }
        if (salary_to=="0"){
            binding.chip2.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), com.bumptech.searchbuddy.R.color.white))
        }

        if (exp_from != "") {
            exp_from_int = exp_from.toInt()
        }
      if (exp_from_int>0){
          binding.chip2.chipBackgroundColor=
              ColorStateList.valueOf(ContextCompat.getColor(requireContext(), com.bumptech.searchbuddy.R.color.orange))
          binding.chip2.setTextColor(resources.getColor(com.bumptech.searchbuddy.R.color.white))

      }
        if (exp_to != "") {
            exp_to_int = exp_to.toInt()
        }
        if (exp_to_int>0){
            binding.chip2.chipBackgroundColor=ColorStateList.valueOf(ContextCompat.getColor(requireContext(), com.bumptech.searchbuddy.R.color.orange))
            binding.chip2.setTextColor(resources.getColor(com.bumptech.searchbuddy.R.color.white))
        }
        if (exp_from_int>0&&exp_to_int<=30){
            binding.chip2.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), com.bumptech.searchbuddy.R.color.orange))
            binding.chip2.setTextColor(resources.getColor(com.bumptech.searchbuddy.R.color.white))
        }
        else if (exp_to_int>=30){
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), com.bumptech.searchbuddy.R.color.orange))
        }
        if (salary_from != "") {
            sal_from_int = salary_from.toInt()
        }
        if (sal_from_int>0){
            binding.chip3.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), com.bumptech.searchbuddy.R.color.orange))
            binding.chip3.setTextColor(resources.getColor(com.bumptech.searchbuddy.R.color.white))

        }
        if (salary_to != "") {
            sal_to_int = salary_to.toInt()
        }
        if (sal_to_int>0){
            binding.chip3.chipBackgroundColor= ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))
            binding.chip3.setTextColor(resources.getColor(R.color.white))
        }
        if (sal_from_int>0&&sal_to_int<=30){
            binding.chip3.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))
            binding.chip3.setTextColor(resources.getColor(R.color.white))

        }
        else if (sal_to_int==30&&sal_from_int==0){
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        }
        var iscontract=LocalSessionManager.getStringValue(Constant.CheckContract,"",requireContext())
        Log.i("chomu",iscontract.toString())
        job_Type=ArrayList()
        if (iscontract!=null){
            job_Type.add(iscontract.toString())
        }
        else if (iscontract==""){
            job_Type.remove(iscontract)
        }
        var location=LocalSessionManager.getStringValue(Constant.FilterLocation,"",requireContext())
        LocationList=ArrayList()
        if (location==""){
            LocationList.remove(location)
            binding.chip4.chipBackgroundColor= ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        }else {
            LocationList.add(location.toString())
            binding.chip4.chipBackgroundColor= ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))
            binding.chip4.setTextColor(resources.getColor(R.color.white))


        }
        function_name= LocalSessionManager.getStringValue("cat_name","",requireContext())!!
        functionList= ArrayList()
        if (function_name==""){
            functionList.remove(function_name)
        }
        else{
            functionList.add(function_name)
        }

        date_posted= LocalSessionManager.getStringValue(Constant.DatePosted,"",requireContext()).toString()
        if (date_posted==""){

            binding.chip5.chipBackgroundColor= ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))

        }
        else if (date_posted=="All Jobs"){
            binding.chip5.chipBackgroundColor= ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))

        }
        else {
//            date_post = date_posted
            binding.chip5.chipBackgroundColor= ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))
            binding.chip5.setTextColor(resources.getColor(R.color.white))


        }
        var RequestParams = JobRequestModel(
            condition,
            exp_from_int,
            exp_to_int,
            sal_from_int * 100000,
            sal_to_int * 100000,
            LocationList,
            date_posted,
        functionList,
            keyword,


        )
        fun checkIfFragmentAttached(operation: Context.() -> Unit) {
            if (isAdded && context != null) {
                operation(requireContext())
            }
        }
        activitySalesViewModel.requestJobsLogged(requireContext(), RequestParams, progress)
            .observe(requireActivity(), {
                checkIfFragmentAttached {
                    if (it.positions == null) {
                        binding.jobLength.setText(it.length.toString() + " results found")
                        binding.idPBLoading.visibility=View.GONE
                        binding.txtNoData.visibility=View.VISIBLE
                        binding.imgNodata.visibility=View.VISIBLE
                        binding.fieldSaleRecycler.visibility=View.GONE
                    } else if (it.positions != null) {
                        binding.jobLength.setText(it.length.toString() + " results found")
                        if (it.length<10){
                            binding.idPBLoading.visibility=View.GONE
                            binding.jobLength.setText(it.length.toString() + " results found")

                        }
                        position_list = (it.positions as ArrayList<Positions>)
                        myadapter = FieldSalesAdapter(position_list,this,{index->saveJobs(index)},{index->deleteJobs(index)})
                        FieldJobRecycler.adapter = myadapter
                        binding.txtNoData.visibility=View.GONE
                        binding.imgNodata.visibility=View.GONE
                        binding.jobLength.setText(it.length.toString() + " results found")
                        binding.fieldSaleRecycler.visibility=View.VISIBLE
                        binding.shimmerView.setVisibility(View.GONE);
                        binding.shimmerView.stopShimmer();

                    }
                }
            })

    }



    private fun saveJobs(index:Int){

        var jobList= arrayListOf<Any>()
        var id=object {
            var id=index
        }
        jobList.add(id)
        activitySalesViewModel.saveJobs(requireContext(),jobList,binding.progress).observe(viewLifecycleOwner,{
            requestData(binding.progress)
        })
    }
    private val updateHintTask = object : Runnable {
        override fun run() {
            // Update the hint text to the next one in the list
            currentHintIndex = (currentHintIndex + 1) % hintTexts.size
            binding.searchBar.queryHint = hintTexts[currentHintIndex]

            // Schedule the task again after 2 seconds
            handler.postDelayed(this, 2500)
        }
    }
    private fun deleteJobs(index:Int){


            var id=index

        activitySalesViewModel.deleteJobs(requireContext(),id,binding.progress).observe(viewLifecycleOwner,{
            requestData(binding.progress)
        })
    }

}