package com.searchbuddy.searchbuddy.Profile

import android.R
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.searchbuddy.databinding.ActivityAddProfessionalDetailBinding
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager
import com.searchbuddy.searchbuddy.model.CityList
import com.searchbuddy.searchbuddy.model.FunctionList
import com.searchbuddy.searchbuddy.model.GetAllWorkHistoryModelItem
import com.searchbuddy.searchbuddy.model.SaveCompanyRequest
import com.searchbuddy.searchbuddy.model.WorkHistory
import java.io.IOException
import java.util.*


class AddProfessionalDetail : AppCompatActivity() {
    lateinit var binding: ActivityAddProfessionalDetailBinding
    var exampleCityList: ArrayList<String> = ArrayList()
    lateinit var designation: String
    var comapanyName: String = ""
    lateinit var list: ArrayList<WorkHistory>
    lateinit var UserID: String
    var is_Prsent:Boolean = false
    lateinit var position_list: ArrayList<GetAllWorkHistoryModelItem>
    lateinit var viewModel: AddProfesionalDetailViewModel
    lateinit var noticePeriodList: ArrayList<String>
    var exampleIndustryList: ArrayList<String> = ArrayList()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddProfesionalDetailViewModel::class.java)

        binding = ActivityAddProfessionalDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        position_list=ArrayList()

        noticePeriodList = arrayListOf(
            "Select Notice Period",
            "Immediate",
            "15 days",
            "30 days",
            "45 days",
            "60 days",
            "90 days"
        )
        requestCompany()

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(com.bumptech.searchbuddy.R.color.grey)
        }
        binding.tiEnddateWork.setVisibility(View.GONE)

        var radioYes = binding.radioYesWork
        var radioNo = binding.radioNoWork
        var radioGroup = binding.radioLayout
//        radioYes.isChecked = true
        radioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                // TODO Auto-generated method stub
                if (radioNo.isChecked() === true) {
                    binding.tiEnddateWork.setVisibility(View.VISIBLE)
                    binding.noticeRl.setVisibility(View.GONE)
                } else {
                    binding.tiEnddateWork.setVisibility(View.GONE)
                    binding.noticeRl.setVisibility(View.VISIBLE)

                }
            }
        })


        val noticeAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, noticePeriodList)
        noticeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tiNotice.setAdapter(noticeAdapter)
        binding.tiNotice.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    (view as TextView).setTextColor(Color.parseColor("#808080"))
                }
                val item = parent.getItemAtPosition(position)
                var location_string = item.toString()
//                Log.i("spinner", item.toString())


//                LocationList.add(item.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        fun getJsonDataFromAsset(context: Context, fileName: String): String? {
            val jsonString: String
            try {
                jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                return null
            }
            return jsonString
        }
        val jsonFileStri = getJsonDataFromAsset(this, "functions.json")
        val gs = Gson()
        val listCityTyp = object : TypeToken<List<FunctionList>>() {}.type
        var person: List<FunctionList> = gs.fromJson(jsonFileStri, listCityTyp)
        person.forEachIndexed { idx, func ->
//            Log.i("dataaaaa", "> Item $idx:\n${func.Value}")
//             someList = person.Districtname
            val CityArray: JsonArray = JsonArray()
            val dataa: String = func.Value
            CityArray.add(dataa)

//            Log.i("kkkkkkkkk", city.CombinedName)
            for (i in 0 until CityArray.size()) {
//                val he = CityList("","",person.CombinedName,"","")
//                exampleList.add(exampleArray.toString())
                var kuch = CityArray.get(i).toString()
                var result = kuch.substring(1, kuch.length - 1)
                exampleIndustryList.add(result)

//                Log.i("Arrraaaaay", exampleCityList.toString())

            }
        }
        val adapte =
            ArrayAdapter(this, R.layout.simple_spinner_item, exampleIndustryList)

        adapte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tiFunctionForm.setAdapter(adapte)
        binding.tiFunctionForm.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    (view as TextView).setTextColor(Color.parseColor("#808080"))
                }
                val item = parent.getItemAtPosition(position)
                var location_string = item.toString()
//                Log.i("spinner", item.toString())


//                LocationList.add(item.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        val jsonFileString = getJsonDataFromAsset(this, "cities.json")
        val gson = Gson()
        val listCityType = object : TypeToken<List<CityList>>() {}.type
        var persons: List<CityList> = gson.fromJson(jsonFileString, listCityType)

        val adapter =
            ArrayAdapter(this, R.layout.simple_list_item_1, exampleCityList)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.etDateWork.setOnClickListener {
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
//                    val dat = (year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year.toString())
                    binding.etDateWork.setText(dat)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.show()
            datePickerDialog.datePicker.setMaxDate(System.currentTimeMillis())
        }

        binding.etEnddateWork.setOnClickListener {
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val date = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year.toString())

                    binding.etEnddateWork.setText(date)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.show()
            datePickerDialog.datePicker.setMaxDate(System.currentTimeMillis())
        }

//        var locationValue=bundle.getString("location","")
//        binding.radioYesWork.isChecked = true



                binding.save.setOnClickListener {
                    if (binding.radioYesWork.isChecked){
                        is_Prsent=true
                    }
                    else if (binding.radioNoWork.isChecked){
                        is_Prsent=false
                    }
                    binding.tiFunctionForm.selectedItem=="Select Function"
                    var id=10
                    if (binding.tiFunctionForm.selectedItem.toString()=="Select Function"){
                        id=10
                    }else if (binding.tiFunctionForm.selectedItem.toString()=="Channel Sales and P&L"){
                        id=13
                    }
                    else if(binding.tiFunctionForm.selectedItem.toString()=="Product/ Service Sale"){
                        id=12
                    }
                    else if (binding.tiFunctionForm.selectedItem.toString()=="Sales and Marketing Operations"){
                        id=14
                    }
                    else if (binding.tiFunctionForm.selectedItem.toString()=="Sales Process and Enablemen"){
                        id=15
                    }
                    else if (binding.tiFunctionForm.selectedItem.toString()=="Sales Strategy"){
                        id=11
                    }
                    else if (binding.tiFunctionForm.selectedItem.toString()=="Other"){
                        id=4005
                    }
                    var function = object {
                        var id = id
                        var codeValueType = object {
                            var id = 3
                            var name = "function"
                        }
                        var value = binding.tiFunctionForm!!.selectedItem.toString()
                    }
                    var noticePeriod = object {
                        var id = 110
                        var value = binding.tiNotice!!.selectedItem.toString()
                    }

                    UpdateProfDetails()

                }



        persons.forEachIndexed { idx, city ->
//            Log.i("dataaaaa", "> Item $idx:\n${city.Districtname}")
//             someList = person.Districtname
            val CityArray: JsonArray = JsonArray()
            val dataa: String = city.CombinedName
            CityArray.add(dataa)

//            Log.i("kkkkkkkkk", city.CombinedName)
            for (i in 0 until CityArray.size()) {
//                val he = CityList("","",person.CombinedName,"","")
//                exampleList.add(exampleArray.toString())
                var kuch = CityArray.get(i).toString()
                var result = kuch.substring(1, kuch.length - 1)
                exampleCityList.add(result)

//                Log.i("Arrraaaaay", exampleCityList.toString())

            }
        }
        UserID = LocalSessionManager.getStringValue(Constant.UserID, "", this).toString()

        var yo = UserID.toInt()
        binding.tiLocationForm.setAdapter(adapter)





        binding.tiLocationForm.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val item = parent.getItemAtPosition(position)
                var location_string = item.toString()
//                Log.i("spinner", item.toString())


//                LocationList.add(item.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestCompany() {
        val adapter =
            ArrayAdapter(this, R.layout.simple_spinner_item, exampleCityList)
        val adapte =
            ArrayAdapter(this, R.layout.simple_spinner_item, exampleIndustryList)
        val noticeAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, noticePeriodList)

        viewModel.requestWorkHistory(this, binding.progress).observe(this, {
//            Log.i("comapany", it.companyName.toString())
            if (it.companyName != null) {
                binding.etCompName.setText(it.companyName)
                comapanyName=it.companyName
            }
            if (it.designation != null) {

                binding.etDesignation.setText(it.designation)
            }
            if (it.expMonths!=null){
                binding.etTotalExp.setText(it.expMonths.toString())
            }
            if (it.location != null) {
                var spinnerText: String = it.location
                var spinnerPosition: Int = adapter.getPosition(spinnerText)
                binding.tiLocationForm.setText(it.location.toString())
            }
            if (it.isPresent!=null) {
                if (it.isPresent == true) {
                    binding.radioYesWork.isChecked = true
                } else if (it.isPresent == false) {
                    binding.radioNoWork.isChecked = true

                }
            }
            if (it.startDate != null) {
                var startDate = it.startDate
//                Log.i("Date",startDate)
                var OutputStartDate=startDate.substring(0,9)
                binding.etDateWork.setText(OutputStartDate)
            }


            if (it.endDate != null) {
                var EndDate = it.endDate.toString()
                var OutputEndDate = EndDate.substring(0,8)
                binding.etEnddateWork.setText(OutputEndDate)
            }

            if (it.function!=null){
                if (it.function.value!=null&&it.function.value!=""){
                    var func=it.function.value.toString()
                    var spinnerfunct: Int = adapte.getPosition(func)
                    binding.tiFunctionForm.setSelection(spinnerfunct,true)

                }
            }
            if (it.noticePeriod!= null){
                var notice=it.noticePeriod.value.toString()
                var spinnernotice:Int=noticeAdapter.getPosition(notice)
                binding.tiNotice.setSelection(spinnernotice)
            }

        })
    }


    private fun Savecompany(
        companyName: String,
        location:String,
        startDate:String,
        isPresent:String,
        endDate:String,
        designation:String,
        function:Any,
        noticePeriod: Any) {
        var saveParams= SaveCompanyRequest(companyName,location,startDate,isPresent,endDate,designation,function,noticePeriod)
        viewModel.saveCompany(this,saveParams,binding.progress).observe(this,{
            Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            onBackPressed()

        })
    }

    private fun Updatecompany(
        companyName: String,
        location:String,
        startDate:String,
        isPresent:String,
        endDate:String,
        designation:String,
        function:Any,
        noticePeriod: Any
    ) {
        var saveParams=SaveCompanyRequest(companyName,location,startDate,isPresent,endDate,designation,function,noticePeriod)
        viewModel.UpdateCompany(this,comapanyName,saveParams,binding.progress).observe(this,{
            Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
        })
    }
    private fun UpdateProfDetails(){
        var exp=binding.etTotalExp!!.text.toString()
        var k =object {
            var companyName=binding.etCompName!!.text.toString()
            var location=binding.tiLocationForm!!.text.toString()
            var startDate=binding.etDateWork!!.text.toString()+"T18:30:00.000Z"
            var endDate=binding.etEnddateWork!!.text.toString()+"T18:30:00.000Z"
            var isPresnt=is_Prsent
            var designation=binding.etDesignation!!.text.toString()
        }
        var id=1
        if (binding.tiFunctionForm.selectedItem.toString()=="Select Function"){
            id=1
        }else if (binding.tiFunctionForm.selectedItem.toString()=="Business Development"){
            id=10
        }
        else if(binding.tiFunctionForm.selectedItem.toString()=="Channel Sales"){
            id=12
        }
        else if (binding.tiFunctionForm.selectedItem.toString()=="Field Sales"){
            id=14
        }
        else if (binding.tiFunctionForm.selectedItem.toString()=="Govt Sales"){
            id=4005
        }
        else if (binding.tiFunctionForm.selectedItem.toString()=="Inside Sales"){
            id=11
        }
        else if (binding.tiFunctionForm.selectedItem.toString()=="Institutional Sales"){
            id=4275
        }
        else if (binding.tiFunctionForm.selectedItem.toString()=="Sales (B2C)"){
            id=15
        }
//        var path: File? =null
//        var Salary = binding.etSalary!!.text.toString()
//        var expecSalary=binding.etExpecSal!!.text.toString()
//        var exp=binding.etExp!!.text.toString()

        var info=object {

            var id = UserID.toInt()
            var experienceMonths =exp.toInt()
            var function = object {
                var id = id
                var codeValueType = object {
                    var id = 3
                    var name = "function"
                }
                var value = binding.tiFunctionForm!!.selectedItem.toString()
            }
            var professionalDetails = object {
//                var annualSalary:Int=Salary.toInt()
//                var level = object {
//                    var id = 951
//                    var value = binding.tiLevelForm!!.selectedItem.toString()
//                }
//                var summary = binding.etSummary!!.text.toString()
//                var industry = binding.tiIndustryForm!!.selectedItem.toString()
//                var expectedSalary = expecSalary.toInt()
                var noticePeriod = object {
                    var id = 110
                    var value = binding.tiNotice!!.selectedItem.toString()
                }
                var primarySkills = arrayListOf<String>("java","sql")


//                var po_list= ArrayList()
                var workHistory = arrayListOf<Any>(k)

            }
        }
        var gson = Gson()
        var information = gson.toJson(info)
        var requestParams: String = information
        var infostr=information.replace("\\u003d",":")

        viewModel.UpdateProfessionalDetail(this,infostr, "",binding.progress).observe(this,{
            onBackPressed()
            Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
        })
    }

}