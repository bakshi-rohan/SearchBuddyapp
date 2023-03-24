package com.example.searchbuddy.Categories

import android.content.Context
import android.content.res.ColorStateList
import android.icu.text.Transliterator
import android.os.Bundle
import android.util.Log
import android.view.*
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
import com.example.searchbuddy.Adapter.FieldSalesAdapter
import com.example.searchbuddy.Dashboard.Dashboard
import com.example.searchbuddy.R
import com.example.searchbuddy.Utils.Constant
import com.example.searchbuddy.Utils.LocalSessionManager
import com.example.searchbuddy.databinding.FragmentSalesJobsBinding
import com.example.searchbuddy.model.FieldSalesModel
import com.example.searchbuddy.model.JobRequestModel
import com.example.searchbuddy.model.Positions
import com.google.android.material.bottomnavigation.BottomNavigationView


class Sales_jobs : Fragment() {
    lateinit var FieldJobRecycler: RecyclerView
    lateinit var binding: FragmentSalesJobsBinding
    lateinit var description: Array<String>
    lateinit var post_name: Array<String>
    lateinit var job_location: Array<String>
    lateinit var experience: Array<String>
    lateinit var salary: Array<String>
    lateinit var company_image: Array<Int>
    lateinit var header: RelativeLayout
    lateinit var field_sale_list: ArrayList<FieldSalesModel>
    lateinit var newArrayList: ArrayList<Transliterator.Position>
    lateinit var myadapter: FieldSalesAdapter
    lateinit var bottomNavView: BottomNavigationView
    lateinit var activitySalesViewModel: SalesJobsViewModel
    lateinit var scroll: NestedScrollView
    lateinit var layoutManager: LinearLayoutManager
    private var isLoading: Boolean = false
    var temp: Int = 1
    lateinit var position_list: ArrayList<Positions>
    lateinit var exp_from: String
    lateinit var exp_to: String
    lateinit var salary_from: String
    lateinit var salary_to: String
    lateinit var date_posted: String
//    lateinit var date_post: String
    lateinit var LocationList: ArrayList<String>
    var exp_from_int: Int = 0
    var exp_to_int: Int = 0
    var sal_from_int: Int = 0
    var sal_to_int: Int = 0
    lateinit var functionList:ArrayList<String>
    lateinit var company:Array<String>
    lateinit var function_name:String
     var company_name:String=""
     var searchText:String=""

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
        header = (activity as Dashboard)!!.findViewById(R.id.header)
        header.visibility = View.GONE
        bottomNavView = (activity as Dashboard)!!.findViewById(R.id.nav_view)
        bottomNavView.visibility = View.VISIBLE
        newArrayList = arrayListOf<Transliterator.Position>()
        activitySalesViewModel = ViewModelProvider(this).get(SalesJobsViewModel::class.java)
        layoutManager = LinearLayoutManager(requireContext())
        val nav: Menu = bottomNavView.menu
//        var jobs=nav.findItem(R.id.jobs_nav)

        var home=nav.findItem(R.id.navigation_home)
        home.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {

                activity?.onBackPressed()

                return true
            }
        })
        FieldJobRecycler = binding.fieldSaleRecycler
        FieldJobRecycler.layoutManager = LinearLayoutManager(requireContext())
        requestData(binding.progress)
        binding.filters.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_nav_sales_to_filter)
        }
        binding.chip1.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_nav_sales_to_filter)
        }
        binding.chip2.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_nav_sales_to_filter)
        }
        binding.chip3.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_nav_sales_to_filter)
        }
        binding.chip4.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_nav_sales_to_filter)
        }
        binding.chip5.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_nav_sales_to_filter)
        }
        binding.chip6.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_nav_sales_to_filter)
        }
        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        activitySalesViewModel.errorMessage()?.observe(requireActivity(), {
            showToast(it.toString())
        })
        field_sale_list = arrayListOf<FieldSalesModel>()

//        binding.idPBLoading.setOnClickListener {
//            temp++
//            Log.i("yoooooo", temp.toString())
//            requestData(binding.progress)
//
//        }
//        binding.idNestedSV.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            // on scroll change we are checking when users scroll as bottom.
//            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
//                // in this method we are incrementing page number,
//                // making progress bar visible and calling get data method.
////                temp++
//                binding.progress.setVisibility(View.VISIBLE)
//
//                var handler :Handler =Handler()
//                Handler().postDelayed({
//                    binding.progress.setVisibility(View.GONE)
//
//                }, 1800) // 2000 is the delayed time in milliseconds.
//
////                requestData(binding.progress)
//            }
//        })

        if (arguments!=null) {
           var searchText = requireArguments().getString("Search","")
            binding.searchBar.setQuery(searchText,true)

        }
//        activity?.onBackPressedDispatcher?.addCallback(requireActivity(), object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//
//                LocalSessionManager.removeValue("DatePost",requireContext())
//                // in here you can do logic when backPress is clicked
//            }
//        })
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.jobLength.visibility=View.GONE
                if (::myadapter.isInitialized) {

                    myadapter.filter.filter(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.jobLength.visibility=View.GONE

                if (::myadapter.isInitialized) {

                    myadapter.filter.filter(newText)
                }

                return true
            }
        })


        binding.jobLength.visibility=View.VISIBLE

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
        if (arguments != null){
            company_name=requireArguments().getString("company").toString()
        }

        if (company_name==""){
            company= arrayOf()
        }

        else {
            company = arrayOf(company_name)
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
        ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
}
        if (exp_to=="0"){
            binding.chip2.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        }
        if (salary_from=="0"){
            binding.chip3.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        }
        if (salary_to=="0"){
            binding.chip2.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        }

        if (exp_from != "") {
            exp_from_int = exp_from.toInt()
        }
      if (exp_from_int>0){
          binding.chip2.chipBackgroundColor=
              ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))

      }
        if (exp_to != "") {
            exp_to_int = exp_to.toInt()
        }
        if (exp_from_int>0&&exp_to_int<=30){
            binding.chip2.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))
        }
        else if (exp_to_int==30&&exp_from_int==0){
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        }
        if (salary_from != "") {
            sal_from_int = salary_from.toInt()
        }
        if (sal_from_int>0){
            binding.chip3.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))

        }
        if (salary_to != "") {
            sal_to_int = salary_to.toInt()
        }
        if (sal_from_int>0&&sal_to_int<=30){
            binding.chip3.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))
        }
        else if (sal_to_int==30&&sal_from_int==0){
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        }
        var location=LocalSessionManager.getStringValue(Constant.FilterLocation,"",requireContext())
        Log.i("xxxx",location.toString())
        LocationList=ArrayList()
        if (location==""){
            LocationList.remove(location)
            binding.chip4.chipBackgroundColor= ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        }else {
            LocationList.add(location.toString())
            binding.chip4.chipBackgroundColor= ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))

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
        Log.i("cccc",date_posted)
        if (date_posted==""){
//            Log.i("pppp",date_post)

            binding.chip5.chipBackgroundColor= ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))

        }
        else if (date_posted=="All Jobs"){
            binding.chip5.chipBackgroundColor= ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))

        }
        else {
//            date_post = date_posted
            binding.chip5.chipBackgroundColor= ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))

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
            company

        )
        fun checkIfFragmentAttached(operation: Context.() -> Unit) {
            if (isAdded && context != null) {
                operation(requireContext())
            }
        }
        activitySalesViewModel.requestJobs(requireContext(), RequestParams, progress)
            .observe(requireActivity(), {
                Log.i("kkkkkkk", it.toString())
                checkIfFragmentAttached {
                    if (it.positions == null) {
//                    binding.txtNoData.visibility=View.VISIBLE
                        Toast.makeText(requireContext(), "No Positions Found", Toast.LENGTH_SHORT).show()
                        binding.jobLength.setText(it.length.toString() + " results found")
                        binding.idPBLoading.visibility=View.GONE
                        binding.txtNoData.visibility=View.VISIBLE
                        binding.fieldSaleRecycler.visibility=View.GONE
                    } else if (it.positions != null) {
                        binding.jobLength.setText(it.length.toString() + " results found")
                        if (it.length<10){
                            binding.idPBLoading.visibility=View.GONE
                        }
                        position_list = (it.positions as ArrayList<Positions>)
                        myadapter = FieldSalesAdapter(position_list)
                        FieldJobRecycler.adapter = myadapter
                        binding.txtNoData.visibility=View.GONE
                        binding.fieldSaleRecycler.visibility=View.VISIBLE

                        if (temp > 3) {
                            binding.idPBLoading.visibility = View.GONE

                        }
                    }
                }
            })

    }


}