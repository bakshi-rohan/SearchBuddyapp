package com.searchbuddy.searchbuddy.Login

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
import com.bumptech.searchbuddy.R
import com.bumptech.searchbuddy.databinding.ActivityBrowseJobsBinding
import com.searchbuddy.searchbuddy.Adapter.BrowseJobsAdapter
import com.searchbuddy.searchbuddy.Adapter.PremiumJobsAdapter
import com.searchbuddy.searchbuddy.Categories.SalesJobsViewModel
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager
import com.searchbuddy.searchbuddy.model.FieldSalesModel
import com.searchbuddy.searchbuddy.model.JobRequestModel
import com.searchbuddy.searchbuddy.model.Positions
import com.searchbuddy.searchbuddy.model.aa.PrePosition
import koleton.api.loadSkeleton

class BrowseJobs : AppCompatActivity() {
    lateinit var binding: ActivityBrowseJobsBinding
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
    lateinit var Preposition_list: ArrayList<PrePosition>
    lateinit var PremiumJobRecycler: RecyclerView
    lateinit var position_list: ArrayList<Positions>
    lateinit var exp_from: String
    lateinit var exp_to: String
    lateinit var salary_from: String
    lateinit var salary_to: String
    lateinit var date_posted: String
    //    lateinit var date_post: String
    lateinit var preadapter: PremiumJobsAdapter

    lateinit var LocationList: ArrayList<String>
    var exp_from_int: Int = 0
    var exp_to_int: Int = 0
    var sal_from_int: Int = 0
    var sal_to_int: Int = 0
    lateinit var functionList:ArrayList<String>
    lateinit var company:Array<String>
    lateinit var function_name:String
    lateinit var emp_type:String
    var company_name:String=""
    lateinit var keyword:Array<String>
    var isPremium:Boolean=true
lateinit var level:Array<Int>
    private var isScrollingDown = false
    private var lastVisibleItemPosition = 0
    lateinit var job_Type:ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrowseJobsBinding.inflate(layoutInflater)
        newArrayList = arrayListOf<Transliterator.Position>()
        activitySalesViewModel = ViewModelProvider(this).get(SalesJobsViewModel::class.java)
        layoutManager = LinearLayoutManager(this)
        keyword= arrayOf("")
//        binding.yourId.isChecked=true

        FieldJobRecycler = binding.fieldSaleRecycler
        PremiumJobRecycler = binding.premiumJobRecyler

        FieldJobRecycler.layoutManager = LinearLayoutManager(this)
        PremiumJobRecycler.layoutManager = LinearLayoutManager(this)

        binding.backBtn.setOnClickListener {
           onBackPressed()
        }
    FieldJobRecycler.loadSkeleton(R.layout.field_sale_recycler_layout){
        shimmer(false)
    }



        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(com.bumptech.searchbuddy.R.color.grey)
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


            requestData(binding.progress)


        binding.jobLength.visibility= View.VISIBLE

        binding.filters.setOnClickListener {
            var intent= Intent(this,FragmentActivity::class.java)
            intent.putExtra("premium",isPremium)
            startActivity(intent)
        }
        binding.chip1.setOnClickListener {
            var intent= Intent(this,FragmentActivity::class.java)
            intent.putExtra("premium",isPremium)
            startActivity(intent)
        }
        binding.chip2.setOnClickListener {
            var intent= Intent(this,FragmentActivity::class.java)
            intent.putExtra("premium",isPremium)

            startActivity(intent)
        }
        binding.chip3.setOnClickListener {
            var intent= Intent(this,FragmentActivity::class.java)
            intent.putExtra("premium",isPremium)
            startActivity(intent)
        }
        binding.chip4.setOnClickListener {
            var intent= Intent(this,FragmentActivity::class.java)
            intent.putExtra("premium",isPremium)
            startActivity(intent)
        }
        binding.chip5.setOnClickListener {
            var intent= Intent(this,FragmentActivity::class.java)
            intent.putExtra("premium",isPremium)
            startActivity(intent)
        }
        binding.chip6.setOnClickListener {
            var intent= Intent(this,FragmentActivity::class.java)
            intent.putExtra("premium",isPremium)
            startActivity(intent)
        }
        setContentView(binding.root)


    }



    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
    private fun requestData(progress: ProgressBar) {
        var condition = object {
            var index: Int = temp
            var pagesize: Int = 200

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
            binding.chip2.setTextColor(resources.getColor(R.color.orange))

        }
        if (exp_to=="0"){
            binding.chip2.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
            binding.chip2.setTextColor(resources.getColor(R.color.orange))

        }
        if (salary_from=="0"){
            binding.chip3.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
            binding.chip3.setTextColor(resources.getColor(R.color.orange))

        }
        if (salary_to=="0"){
            binding.chip3.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
            binding.chip3.setTextColor(resources.getColor(R.color.orange))

        }

        if (exp_from != "") {
            exp_from_int = exp_from.toInt()
        }
        if (exp_from_int>0){
            binding.chip2.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange))
            binding.chip2.setTextColor(resources.getColor(R.color.white))

        }
        if (exp_to != "") {
            exp_to_int = exp_to.toInt()
        }
        if (exp_from_int>0&&exp_to_int<=30){
            binding.chip2.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange))
            binding.chip2.setTextColor(resources.getColor(R.color.white))

        }
        else if (exp_to_int==30&&exp_from_int==0){
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
            binding.chip2.setTextColor(resources.getColor(R.color.orange))

        }
        if (salary_from != "") {
            sal_from_int = salary_from.toInt()
        }
        if (sal_from_int>0){
            binding.chip3.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange))
            binding.chip3.setTextColor(resources.getColor(R.color.white))

        }
        if (salary_to != "") {
            sal_to_int = salary_to.toInt()
        }
        if (sal_from_int>0&&sal_to_int<=30){
            binding.chip3.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange))
            binding.chip3.setTextColor(resources.getColor(R.color.white))

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
            binding.chip4.setTextColor(resources.getColor(R.color.white))

        }
        function_name= LocalSessionManager.getStringValue("cat_name","",this)!!
        functionList= ArrayList()
        if (function_name==""){
            functionList.remove(function_name)
        }
        else{
            functionList.add(function_name)
        }
        emp_type=LocalSessionManager.getStringValue("CheckContract","",this)!!
        job_Type= ArrayList()
        if (emp_type==""){
            job_Type.remove(emp_type)
        }
        else {
            job_Type.add(emp_type)
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
//            job_Type
//            level

        )

        activitySalesViewModel.requestJobs(this, RequestParams, progress)
            .observe(this, {
                Log.i("kkkkkkk", it.toString())

                    if (it.positions == null) {

                        binding.jobLength.setText(it.length.toString() + " results found")
                        binding.idPBLoading.visibility= View.GONE
                        binding.txtNoData.visibility= View.VISIBLE
                        binding.imgNodata.visibility= View.VISIBLE
                        binding.fieldSaleRecycler.visibility= View.GONE
                    } else if (it.positions != null) {
                        binding.jobLength.setText(it.length.toString() + " results found")
                        if (it.length<10){
                            binding.idPBLoading.visibility= View.GONE
                        }
                        position_list = (it.positions as ArrayList<Positions>)
                        myadapter = BrowseJobsAdapter(position_list,this)
                        FieldJobRecycler.adapter = myadapter
                        binding.txtNoData.visibility= View.GONE
                        binding.imgNodata.visibility= View.GONE
                        binding.fieldSaleRecycler.visibility= View.VISIBLE
//                        FieldJobRecycler.hideSkeleton()


                        if (temp > 3) {
                            binding.idPBLoading.visibility = View.GONE

                        }
                    }

            })

    }

    private fun requestPreData(progress: ProgressBar) {
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
            binding.chip2.setTextColor(resources.getColor(R.color.white))

        }
        if (exp_to != "") {
            exp_to_int = exp_to.toInt()
        }
        if (exp_from_int>0&&exp_to_int<=30){
            binding.chip2.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange))
            binding.chip2.setTextColor(resources.getColor(R.color.white))

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
            binding.chip3.setTextColor(resources.getColor(R.color.white))

        }
        if (salary_to != "") {
            sal_to_int = salary_to.toInt()
        }
        if (sal_from_int>0&&sal_to_int<=30){
            binding.chip3.chipBackgroundColor=
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange))
            binding.chip3.setTextColor(resources.getColor(R.color.white))

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
            binding.chip4.setTextColor(resources.getColor(R.color.white))

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
//            job_Type
//            level

        )

        activitySalesViewModel.requestPremiumJobs(this, RequestParams, progress)
            .observe(this, {
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
                    preadapter = PremiumJobsAdapter(Preposition_list,this,{index->saveJobs(index)},{index->deleteJobs(index)})
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

    private fun saveJobs(index:Int){

        var jobList= arrayListOf<Any>()
        var kk=object {
            var id=index
        }
        jobList.add(kk)
        activitySalesViewModel.saveJobs(this,jobList,binding.progress).observe(this,{
//            Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
            requestData(binding.progress)
        })
//        requestData(binding.progress)
    }
    private fun deleteJobs(index:Int){


        var id=index

//        jobList.add(kk)
        activitySalesViewModel.deleteJobs(this,id,binding.progress).observe(this,{
//            Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
            requestData(binding.progress)
        })
//        requestData(binding.progress)
    }
}