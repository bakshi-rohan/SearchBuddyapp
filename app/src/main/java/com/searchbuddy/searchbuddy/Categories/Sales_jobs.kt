package com.searchbuddy.searchbuddy.Categories

import android.content.Context
import android.content.res.ColorStateList
import android.icu.text.Transliterator
import android.os.Bundle
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
import com.rm.rmswitch.RMSwitch.RMSwitchObserver
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
    lateinit var company:Array<String>
    lateinit var keyword:Array<String>
    lateinit var function_name:String
     var company_name:String=""
     var level:Int=0
     lateinit var level_string:Array<Int>
    var isPremium:Boolean=true

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

        var home=nav.findItem(R.id.navigation_home)
        home.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {

                activity?.onBackPressed()

                return true
            }
        })
        binding.chip3.visibility=View.GONE
        binding.chip2.visibility=View.GONE
        binding.chip5.visibility=View.GONE
//        binding.chip6.visibility=View.VISIBLE
        binding.yourId.isChecked=true
        keyword= arrayOf<String>()
        if (arguments!=null) {
            var searchText = requireArguments().getString("Search","")
            binding.searchBar.setQuery(searchText,true)
            keyword= arrayOf(searchText)
        }

        FieldJobRecycler = binding.fieldSaleRecycler
        PremiumJobRecycler = binding.premiumJobRecyler
        FieldJobRecycler.layoutManager = LinearLayoutManager(requireContext())
        PremiumJobRecycler.layoutManager = LinearLayoutManager(requireContext())



        activitySalesViewModel.errorMessage()?.observe(requireActivity(), {
            showToast(it.toString())
        })
        field_sale_list = arrayListOf<FieldSalesModel>()

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.jobLength.visibility=View.GONE
                if (::myadapter.isInitialized) {

                    myadapter.filter.filter(query)
                }
                if (::preadapter.isInitialized) {

                    preadapter.filter.filter(query)
                }
                    return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.jobLength.visibility=View.GONE

                if (::myadapter.isInitialized) {

                    myadapter.filter.filter(newText)
                }
                if (::preadapter.isInitialized) {

                    preadapter.filter.filter(newText)
                }

                return true
            }
        })

        if (isPremium==false){
            requestData(binding.progress)

        }
        else if (isPremium==true){
            requestPreData(binding.progress)
        }
        binding.jobLength.visibility=View.VISIBLE
        binding.yourId.addSwitchObserver(RMSwitchObserver { switchView, isChecked ->
            if (isChecked==false){
                isPremium=false
                requestData(binding.progress)
                binding.fieldSaleRecycler.visibility=View.VISIBLE
                binding.premiumJobRecyler.visibility=View.GONE
                binding.chip3.visibility=View.VISIBLE
                binding.chip5.visibility=View.VISIBLE
                binding.chip2.visibility=View.VISIBLE
                binding.chip6.visibility=View.GONE


            }
            else{
                isPremium=true
                requestPreData(binding.progress)
                binding.fieldSaleRecycler.visibility=View.GONE
                binding.premiumJobRecyler.visibility=View.VISIBLE
                binding.chip3.visibility=View.GONE
                binding.chip5.visibility=View.GONE
                binding.chip2.visibility=View.GONE
//                binding.chip6.visibility=View.VISIBLE

            }
        })
        binding.filters.setOnClickListener {
            var bundle:Bundle= Bundle()
            bundle.putBoolean("isPremium",isPremium)
            Navigation.findNavController(it).navigate(R.id.action_nav_sales_to_filter,bundle)
        }
        binding.chip1.setOnClickListener {
            var bundle:Bundle=Bundle()
            bundle.putBoolean("isPremium",isPremium)
            Navigation.findNavController(it).navigate(R.id.action_nav_sales_to_filter,bundle)
        }
        binding.chip2.setOnClickListener {
            var bundle:Bundle=Bundle()
            bundle.putBoolean("isPremium",isPremium)
            Navigation.findNavController(it).navigate(R.id.action_nav_sales_to_filter,bundle)
        }
        binding.chip3.setOnClickListener {
            var bundle:Bundle=Bundle()
            bundle.putBoolean("isPremium",isPremium)
            Navigation.findNavController(it).navigate(R.id.action_nav_sales_to_filter,bundle)
        }
        binding.chip4.setOnClickListener {
            var bundle:Bundle=Bundle()
            bundle.putBoolean("isPremium",isPremium)
            Navigation.findNavController(it).navigate(R.id.action_nav_sales_to_filter,bundle)
        }
        binding.chip5.setOnClickListener {
            var bundle:Bundle=Bundle()
            bundle.putBoolean("isPremium",isPremium)
            Navigation.findNavController(it).navigate(R.id.action_nav_sales_to_filter,bundle)
        }
        binding.chip6.setOnClickListener {
            var bundle:Bundle=Bundle()
            bundle.putBoolean("isPremium",isPremium)
            Navigation.findNavController(it).navigate(R.id.action_nav_sales_to_filter,bundle)
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
          binding.chip2.setTextColor(resources.getColor(R.color.white))

      }
        if (exp_to != "") {
            exp_to_int = exp_to.toInt()
        }
        if (exp_from_int>0&&exp_to_int<=30){
            binding.chip2.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))
            binding.chip2.setTextColor(resources.getColor(R.color.white))
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
            binding.chip3.setTextColor(resources.getColor(R.color.white))

        }
        if (salary_to != "") {
            sal_to_int = salary_to.toInt()
        }
        if (sal_from_int>0&&sal_to_int<=30){
            binding.chip3.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))
            binding.chip3.setTextColor(resources.getColor(R.color.white))

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
            company,
            keyword,
//            level_string

        )
        fun checkIfFragmentAttached(operation: Context.() -> Unit) {
            if (isAdded && context != null) {
                operation(requireContext())
            }
        }
        activitySalesViewModel.requestJobsLogged(requireContext(), RequestParams, progress)
            .observe(requireActivity(), {
                Log.i("kkkkkkk", it.toString())
                checkIfFragmentAttached {
                    if (it.positions == null) {
//                    binding.txtNoData.visibility=View.VISIBLE
//                        Toast.makeText(requireContext(), "No Positions Found", Toast.LENGTH_SHORT).show()
                        binding.jobLength.setText(it.length.toString() + " results found")
                        binding.idPBLoading.visibility=View.GONE
                        binding.txtNoData.visibility=View.VISIBLE
                        binding.imgNodata.visibility=View.VISIBLE
                        binding.fieldSaleRecycler.visibility=View.GONE
                    } else if (it.positions != null) {
                        binding.jobLength.setText(it.length.toString() + " results found")
                        if (it.length<10){
                            binding.idPBLoading.visibility=View.GONE
                        }
                        position_list = (it.positions as ArrayList<Positions>)
                        myadapter = FieldSalesAdapter(position_list,this,{index->saveJobs(index)},{index->deleteJobs(index)})
                        FieldJobRecycler.adapter = myadapter
                        binding.txtNoData.visibility=View.GONE
                        binding.imgNodata.visibility=View.GONE
                        binding.fieldSaleRecycler.visibility=View.VISIBLE

                        if (temp > 3) {
                            binding.idPBLoading.visibility = View.GONE

                        }
                    }
                }
            })

    }

    private fun saveJobs(index:Int){

        var jobList= arrayListOf<Any>()
        var kk=object {
            var id=index
        }
        jobList.add(kk)
        activitySalesViewModel.saveJobs(requireContext(),jobList,binding.progress).observe(viewLifecycleOwner,{
//            Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
            requestData(binding.progress)
        })
//        requestData(binding.progress)
    }
    private fun deleteJobs(index:Int){


            var id=index

//        jobList.add(kk)
        activitySalesViewModel.deleteJobs(requireContext(),id,binding.progress).observe(viewLifecycleOwner,{
//            Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
            requestData(binding.progress)
        })
//        (binding.progress)
    }
    private fun requestPreData(progress: ProgressBar) {
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
level= LocalSessionManager.getIntValue(Constant.DatePosted,0,requireContext())!!
        if (level!=null){
            level_string= arrayOf(level)
        }

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
            binding.chip2.setTextColor(resources.getColor(R.color.white))

        }
        if (exp_to != "") {
            exp_to_int = exp_to.toInt()
        }
        if (exp_from_int>0&&exp_to_int<=30){
            binding.chip2.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))
            binding.chip2.setTextColor(resources.getColor(R.color.white))
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
            binding.chip3.setTextColor(resources.getColor(R.color.white))

        }
        if (salary_to != "") {
            sal_to_int = salary_to.toInt()
        }
        if (sal_from_int>0&&sal_to_int<=30){
            binding.chip3.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))
            binding.chip3.setTextColor(resources.getColor(R.color.white))

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
            company,
            keyword,
//            level_string

        )

        activitySalesViewModel.requestPremiumJobs(requireContext(), RequestParams, progress)
            .observe(requireActivity(), {
                Log.i("kkkkkkk", it.toString())

                if (it.positions == null) {
                    binding.jobLength.setText(it.length.toString() + " results found")
                    binding.idPBLoading.visibility= View.GONE
                    binding.txtNoData.visibility= View.VISIBLE
                    binding.imgNodata.visibility= View.VISIBLE
                    binding.premiumJobRecyler.visibility= View.GONE
                } else if (it.positions != null) {
                    binding.jobLength.setText(it.length.toString() + " results found")
                    if (it.length<10){
                        binding.idPBLoading.visibility= View.GONE
                    }
                    Preposition_list = (it.positions as ArrayList<PrePosition>)
                    preadapter = PremiumJobsAdapter(Preposition_list,requireContext(),{index->saveJobs(index)},{index->deleteJobs(index)})
                    PremiumJobRecycler.adapter = preadapter
                    binding.txtNoData.visibility= View.GONE
                    binding.imgNodata.visibility= View.GONE
                    binding.premiumJobRecyler.visibility= View.VISIBLE

                    if (temp > 3) {
                        binding.idPBLoading.visibility = View.GONE

                    }
                }

            })

    }
}