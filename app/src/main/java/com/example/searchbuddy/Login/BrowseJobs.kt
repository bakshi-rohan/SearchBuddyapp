package com.example.searchbuddy.Login

import android.content.Intent
import android.content.res.ColorStateList
import android.icu.text.Transliterator
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.searchbuddy.Adapter.BrowseJobsAdapter
import com.example.searchbuddy.Categories.SalesJobsViewModel
import com.example.searchbuddy.R
import com.example.searchbuddy.Utils.Constant
import com.example.searchbuddy.Utils.LocalSessionManager
import com.example.searchbuddy.databinding.ActivityBrowseJobsBinding
import com.example.searchbuddy.model.FieldSalesModel
import com.example.searchbuddy.model.JobRequestModel
import com.example.searchbuddy.model.Positions

class BrowseJobs : AppCompatActivity() {
    lateinit var binding:ActivityBrowseJobsBinding
    lateinit var FieldJobRecycler: RecyclerView
    lateinit var description: Array<String>
    lateinit var post_name: Array<String>
    lateinit var job_location: Array<String>
    lateinit var experience: Array<String>
    lateinit var salary: Array<String>
    lateinit var company_image: Array<Int>
    lateinit var header: RelativeLayout
    lateinit var field_sale_list: ArrayList<FieldSalesModel>
    lateinit var newArrayList: ArrayList<Transliterator.Position>
    lateinit var myadapter: BrowseJobsAdapter
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
        binding = ActivityBrowseJobsBinding.inflate(layoutInflater)
        newArrayList = arrayListOf<Transliterator.Position>()
        activitySalesViewModel = ViewModelProvider(this).get(SalesJobsViewModel::class.java)
        layoutManager = LinearLayoutManager(this)
        FieldJobRecycler = binding.fieldSaleRecycler
        FieldJobRecycler.layoutManager = LinearLayoutManager(this)
        requestData(binding.progress)
        binding.filters.setOnClickListener {
            var intent=Intent(this,FragmentActivity::class.java)
            startActivity(intent)
        }
        binding.chip1.setOnClickListener {
            var intent=Intent(this,FragmentActivity::class.java)
            startActivity(intent)
        }
        binding.chip2.setOnClickListener {
            var intent=Intent(this,FragmentActivity::class.java)
            startActivity(intent)
        }
        binding.chip3.setOnClickListener {
            var intent=Intent(this,FragmentActivity::class.java)
            startActivity(intent)
        }
        binding.chip4.setOnClickListener {
            var intent=Intent(this,FragmentActivity::class.java)
            startActivity(intent)
        }
        binding.chip5.setOnClickListener {
            var intent=Intent(this,FragmentActivity::class.java)
            startActivity(intent)
        }
        binding.chip6.setOnClickListener {
            var intent=Intent(this,FragmentActivity::class.java)
            startActivity(intent)
        }
        binding.backBtn.setOnClickListener {
           onBackPressed()
        }
//        activitySalesViewModel.errorMessage()?.observe(this, {
//            showToast(it.toString())
//        })
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(com.example.searchbuddy.R.color.orange)
        }
        field_sale_list = arrayListOf<FieldSalesModel>()
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.jobLength.visibility= View.GONE
                if (::myadapter.isInitialized) {

                    myadapter.filter.filter(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.jobLength.visibility= View.GONE

                if (::myadapter.isInitialized) {

                    myadapter.filter.filter(newText)
                }

                return true
            }
        })


        binding.jobLength.visibility= View.VISIBLE
        setContentView(binding.root)


    }

//    override fun onPause() {
//        super.onPause()
//        LocalSessionManager.removeValue(Constant.SliderStartValue,this)
//        LocalSessionManager.removeValue(Constant.SliderEndValue,this)
//        LocalSessionManager.removeValue(Constant.SalarySliderStartValue,this)
//        LocalSessionManager.removeValue(Constant.SalarySliderEndValue,this)
//        LocalSessionManager.removeValue(Constant.FilterLocation,this)
//        LocalSessionManager.removeValue(Constant.DatePosted,this)
//        LocalSessionManager.removeValue(Constant.cat_name,this)
//    }

    //    override fun onDestroy() {
//        super.onDestroy()
//        LocalSessionManager.removeValue(Constant.SliderStartValue,this)
//        LocalSessionManager.removeValue(Constant.SliderEndValue,this)
//        LocalSessionManager.removeValue(Constant.SalarySliderStartValue,this)
//        LocalSessionManager.removeValue(Constant.SalarySliderEndValue,this)
//        LocalSessionManager.removeValue(Constant.FilterLocation,this)
//        LocalSessionManager.removeValue(Constant.DatePosted,this)
//        LocalSessionManager.removeValue(Constant.cat_name,this)
//    }
    override fun onResume() {
        super.onResume()
        requestData(binding.progress)
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
    private fun requestData(progress: ProgressBar) {
        var condition = object {
            var index: Int = temp
            var pagesize: Int = 200

        }
//        if (arguments != null){
//            company_name=requireArguments().getString("company").toString()
//        }

        if (company_name==""){
            company= arrayOf()
        }

        else {
            company = arrayOf(company_name)
        }
        exp_from = LocalSessionManager.getStringValue(
            Constant.SliderStartValue.toString(),
            "",
          this
        ).toString()

        exp_to = LocalSessionManager.getStringValue(
            Constant.SliderEndValue.toString(),
            "",
            this
        ).toString()

        salary_from = LocalSessionManager.getStringValue(
            Constant.SalarySliderStartValue.toString(),
            "",
         this
        ).toString()

        salary_to = LocalSessionManager.getStringValue(
            Constant.SalarySliderEndValue.toString(),
            "",
           this
        ).toString()

        if (exp_from=="0"){
            binding.chip2.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        }
        if (exp_to=="0"){
            binding.chip2.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        }
        if (salary_from=="0"){
            binding.chip3.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        }
        if (salary_to=="0"){
            binding.chip2.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        }

        if (exp_from != "") {
            exp_from_int = exp_from.toInt()
        }
        if (exp_from_int>0){
            binding.chip2.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange))

        }
        if (exp_to != "") {
            exp_to_int = exp_to.toInt()
        }
        if (exp_from_int>0&&exp_to_int<=30){
            binding.chip2.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange))
        }
        else if (exp_to_int==30&&exp_from_int==0){
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        }
        if (salary_from != "") {
            sal_from_int = salary_from.toInt()
        }
        if (sal_from_int>0){
            binding.chip3.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange))

        }
        if (salary_to != "") {
            sal_to_int = salary_to.toInt()
        }
        if (sal_from_int>0&&sal_to_int<=30){
            binding.chip3.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange))
        }
        else if (sal_to_int==30&&sal_from_int==0){
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        }
        var location=LocalSessionManager.getStringValue(Constant.FilterLocation,"",this)
        Log.i("xxxx",location.toString())
        LocationList=ArrayList()
        if (location==""){
            LocationList.remove(location)
            binding.chip4.chipBackgroundColor= ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        }else {
            LocationList.add(location.toString())
            binding.chip4.chipBackgroundColor= ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange))

        }
        function_name= LocalSessionManager.getStringValue("cat_name","",this)!!
        functionList= ArrayList()
        if (function_name==""){
            functionList.remove(function_name)
        }
        else{
            functionList.add(function_name)
        }

        date_posted= LocalSessionManager.getStringValue(Constant.DatePosted,"",this).toString()
        Log.i("cccc",date_posted)
        if (date_posted==""){
//            Log.i("pppp",date_post)

            binding.chip5.chipBackgroundColor= ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))

        }
        else if (date_posted=="All Jobs"){
            binding.chip5.chipBackgroundColor= ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))

        }
        else {
//            date_post = date_posted
            binding.chip5.chipBackgroundColor= ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange))

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

        activitySalesViewModel.requestJobs(this, RequestParams, progress)
            .observe(this, {
                Log.i("kkkkkkk", it.toString())

                    if (it.positions == null) {
//                    binding.txtNoData.visibility=View.VISIBLE
//                        Toast.makeText(this, "No Positions Found", Toast.LENGTH_SHORT).show()
                        binding.jobLength.setText(it.length.toString() + " results found")
                        binding.idPBLoading.visibility= View.GONE
                        binding.txtNoData.visibility= View.VISIBLE
                        binding.fieldSaleRecycler.visibility= View.GONE
                    } else if (it.positions != null) {
                        binding.jobLength.setText(it.length.toString() + " results found")
                        if (it.length<10){
                            binding.idPBLoading.visibility= View.GONE
                        }
                        position_list = (it.positions as ArrayList<Positions>)
                        myadapter = BrowseJobsAdapter(position_list)
                        FieldJobRecycler.adapter = myadapter
                        binding.txtNoData.visibility= View.GONE
                        binding.fieldSaleRecycler.visibility= View.VISIBLE

                        if (temp > 3) {
                            binding.idPBLoading.visibility = View.GONE

                        }
                    }

            })

    }
}