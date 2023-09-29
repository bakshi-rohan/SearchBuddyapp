package com.searchbuddy.searchbuddy.Forms

import android.R
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.searchbuddy.databinding.ActivitySignupWorkHistoryBinding
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.Constant.Companion.UserID
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager
import com.searchbuddy.searchbuddy.model.CityList
import com.searchbuddy.searchbuddy.model.FunctionList
import com.searchbuddy.searchbuddy.model.SaveCompanyRequest
import java.io.IOException
import java.util.Calendar


class Signup_Work_History : AppCompatActivity() {
    lateinit var binding: ActivitySignupWorkHistoryBinding
    var exampleCityList: ArrayList<String> = ArrayList()
    lateinit var noticePeriodList: ArrayList<String>
    lateinit var descriptionData: Array<String>
    lateinit var viewModel: SignupWorkHistoryViewModel
    var is_Prsent:String="null"
    var exampleIndustryList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupWorkHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(SignupWorkHistoryViewModel::class.java)

        binding.experienced.setBackgroundResource(com.bumptech.searchbuddy.R.drawable.work_status_selected_border)
        binding.tiCompanyName.isEnabled=true
        binding.tiDesignation.isEnabled=true
        binding.tiNotice.isEnabled=true
        binding.tiFunctionForm.isEnabled=true
        binding.startFrom.isEnabled=true
        binding.tiEnddateWork.isEnabled=true
        binding.tiEnddateWork.setVisibility(View.GONE)
        binding.internship.visibility=View.GONE
        binding.tiTotalExp.visibility=View.VISIBLE
        binding.experienced.setTextColor(Color.parseColor("#ffffff"))
        binding.radioNoWork.isClickable=true
        binding.radioYesWork.isClickable=true

        binding.btnSbmt.setOnClickListener {
      UpdateProfDetails()

        }
        binding.btnSkip.setOnClickListener {
            var intent = Intent(this, Signup_CV::class.java)
            startActivity(intent)
        }
        descriptionData =
            arrayOf("Basic\nDetails", "Educational\nDetails", "Experience", "CV", "Preferences")
        binding.yourStateProgressBarId.setStateDescriptionData(descriptionData)
        var radioYes = binding.radioYesWork
        var radioNo = binding.radioNoWork
        var radioGroup = binding.radioLayout
        radioYes.isChecked = true
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
        noticePeriodList = arrayListOf(
            "Select Notice Period",
            "Immediate",
            "15 days",
            "30 days",
            "45 days",
            "60 days",
            "90 days"
        )
//        binding.svWh.setOnTouchListener(object : OnSwipeTouchListener(this) {
//
//            override fun onSwipeRight() {
//                super.onSwipeRight()
//                binding.experienced.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_selected_border)
//                binding.fresher.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_border)
//                binding.tiCompanyName.isEnabled=true
//                binding.tiDesignation.isEnabled=true
//                binding.tiNotice.isEnabled=true
//                binding.tiFunctionForm.isEnabled=true
//                binding.tiLocationForm.isEnabled=true
//                binding.radioLayout.isEnabled=true
//                binding.startFrom.isEnabled=true
//                binding.tiEnddateWork.isEnabled=true
//                binding.tiTotalExp.visibility=View.VISIBLE
//                binding.experienced.setTextColor(Color.parseColor("#ffffff"))
//                binding.fresher.setTextColor(Color.parseColor("#8A414141"))
//                binding.experiecneLayout.visibility=View.VISIBLE
//                binding.internship.visibility=View.GONE
//                binding.radioNoWork.isClickable=true
//                binding.radioYesWork.isClickable=true
//                binding.btnSbmt.setOnClickListener {
//                    if (binding.radioYesWork.isChecked==true){
//                        is_Prsent="true"
//                    }
//                    binding.tiFunctionForm.selectedItem == "Select Function"
//                    var id = 10
//                    if (binding.tiFunctionForm.selectedItem.toString() == "Select Function") {
//                        id = 10
//                    } else if (binding.tiFunctionForm.selectedItem.toString() == "Channel Sales and P&L") {
//                        id = 13
//                    } else if (binding.tiFunctionForm.selectedItem.toString() == "Product/ Service Sale") {
//                        id = 12
//                    } else if (binding.tiFunctionForm.selectedItem.toString() == "Sales and Marketing Operations") {
//                        id = 14
//                    } else if (binding.tiFunctionForm.selectedItem.toString() == "Sales Process and Enablemen") {
//                        id = 15
//                    } else if (binding.tiFunctionForm.selectedItem.toString() == "Sales Strategy") {
//                        id = 11
//                    } else if (binding.tiFunctionForm.selectedItem.toString() == "Other") {
//                        id = 4005
//                    }
//                    var function = object {
//                        var id = id
//                        var codeValueType = object {
//                            var id = 3
//                            var name = "function"
//                        }
//                        var value = binding.tiFunctionForm!!.selectedItem.toString()
//                    }
//                    var noticePeriod = object {
//                        var id = 110
//                        var value = binding.tiNotice!!.selectedItem.toString()
//                    }
//                    if (binding.radioYesWork.isChecked == true) {
//                        is_Prsent = "true"
//                    }
//                    Savecompany(binding.etCompName!!.text.toString(),
//                        binding.tiLocationForm!!.text!!.toString(),
//                        binding.etDateWork!!.text!!.toString(),
//                        is_Prsent,
//                        binding.etEnddateWork!!.text!!.toString(),
//                        binding.etDesignation!!.text.toString(),
//                        function,
//                        noticePeriod
//                    )
//
//                }
//            }
//
//            override fun onSwipeLeft() {
//                super.onSwipeLeft()
//                binding.fresher.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_selected_border)
//                binding.experienced.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_border)
//                binding.fresher.setTextColor(Color.parseColor("#ffffff"))
//                binding.experienced.setTextColor(Color.parseColor("#8A414141"))
//                binding.internship.visibility=View.VISIBLE
//                binding.tiCompanyName.isEnabled=false
//                binding.tiDesignation.isEnabled=false
//                binding.tiNotice.isEnabled=false
//                binding.tiFunctionForm.isEnabled=false
//                binding.tiLocationForm.isEnabled=false
//                binding.radioLayout.isEnabled=false
//                binding.startFrom.isEnabled=false
//                binding.tiTotalExp.visibility=View.GONE
//                binding.tiEnddateWork.isEnabled=false
//                binding.radioNoWork.isClickable=false
//                binding.radioYesWork.isClickable=false
//            }
//
//
//        })
        binding.fresher.setOnClickListener {
            binding.fresher.setBackgroundResource(com.bumptech.searchbuddy.R.drawable.work_status_selected_border)
            binding.experienced.setBackgroundResource(com.bumptech.searchbuddy.R.drawable.work_status_border)
            binding.fresher.setTextColor(Color.parseColor("#ffffff"))
            binding.experienced.setTextColor(Color.parseColor("#8A414141"))
            binding.internship.visibility=View.VISIBLE
            binding.tiCompanyName.isEnabled=false
            binding.tiDesignation.isEnabled=false
            binding.tiNotice.isEnabled=false
            binding.tiFunctionForm.isEnabled=false
            binding.tiLocationForm.isEnabled=false
            binding.radioLayout.isEnabled=false
            binding.startFrom.isEnabled=false
            binding.tiTotalExp.visibility=View.GONE
            binding.tiEnddateWork.isEnabled=false
            binding.radioNoWork.isClickable=false
            binding.radioYesWork.isClickable=false
            binding.btnSbmt.setOnClickListener {

                var intent = Intent(this, Signup_CV::class.java)
                startActivity(intent)
                Toast.makeText(this,"Work History Saved",Toast.LENGTH_SHORT).show()
            }
        }
        binding.experienced.setOnClickListener {
            binding.experienced.setBackgroundResource(com.bumptech.searchbuddy.R.drawable.work_status_selected_border)
            binding.fresher.setBackgroundResource(com.bumptech.searchbuddy.R.drawable.work_status_border)
            binding.tiCompanyName.isEnabled=true
            binding.tiDesignation.isEnabled=true
            binding.tiNotice.isEnabled=true
            binding.tiFunctionForm.isEnabled=true
            binding.tiLocationForm.isEnabled=true
            binding.radioLayout.isEnabled=true
            binding.startFrom.isEnabled=true
            binding.tiEnddateWork.isEnabled=true
            binding.tiTotalExp.visibility=View.VISIBLE
            binding.experienced.setTextColor(Color.parseColor("#ffffff"))
            binding.fresher.setTextColor(Color.parseColor("#8A414141"))
            binding.experiecneLayout.visibility=View.VISIBLE
            binding.internship.visibility=View.GONE
            binding.radioNoWork.isClickable=true
            binding.radioYesWork.isClickable=true
            binding.btnSbmt.setOnClickListener {

        UpdateProfDetails()

            }
        }
         binding.radioLayoutIntern.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
             override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                 // TODO Auto-generated method stub
                 if (binding.radioYesIntern.isChecked() === true) {
                     binding.tiCompanyName.isEnabled=true
                     binding.tiDesignation.isEnabled=true
                     binding.tiNotice.isEnabled=true
                     binding.tiFunctionForm.isEnabled=true
                     binding.tiLocationForm.isEnabled=true
                     binding.radioLayout.isEnabled=true
                     binding.startFrom.isEnabled=true
                     binding.tiEnddateWork.isEnabled=true
//                     binding.experienced.setTextColor(Color.parseColor("#ffffff"))
//                     binding.fresher.setTextColor(Color.parseColor("#8A414141"))
                     binding.experiecneLayout.visibility=View.VISIBLE
                     binding.radioNoWork.isClickable=true
                     binding.radioYesWork.isClickable=true
                     binding.btnSbmt.setOnClickListener {
                       UpdateProfDetails()
                     }
                 } else {

                     binding.internship.visibility=View.VISIBLE
                     binding.tiCompanyName.isEnabled=false
                     binding.tiDesignation.isEnabled=false
                     binding.tiNotice.isEnabled=false
                     binding.tiFunctionForm.isEnabled=false
                     binding.tiLocationForm.isEnabled=false
                     binding.radioLayout.isEnabled=false
                     binding.startFrom.isEnabled=false
                     binding.tiEnddateWork.isEnabled=false
                     binding.radioNoWork.isClickable=false
                     binding.radioYesWork.isClickable=false

//                     binding.btnSbmt.setOnClickListener {
//
//                         var intent = Intent(this, Signup_CV::class.java)
//                         startActivity(intent)
//                     }
                 }
             }
         })



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
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(com.bumptech.searchbuddy.R.color.grey)
        }
        binding.etEnddateWork.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val date =
                        (year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
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
        val listCityTy = object : TypeToken<List<FunctionList>>() {}.type
        var pers: List<FunctionList> = gs.fromJson(jsonFileStri, listCityTy)
        pers.forEachIndexed { idx, func ->
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

        val jsonFileStrin = getJsonDataFromAsset(this, "cities.json")
        val gso = Gson()
        val listCityTyp = object : TypeToken<List<CityList>>() {}.type
        var person: List<CityList> = gso.fromJson(jsonFileStrin, listCityTyp)
        person.forEachIndexed { idx, city ->
//            Log.i("dataaaaa", "> Item $idx:\n${city.Districtname}")
//             someList = person.Districtname
            val CityArray: JsonArray = JsonArray()
            val dataa: String = city.District
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
        val adapter =
            ArrayAdapter(this, R.layout.simple_list_item_1, exampleCityList)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tiLocationForm.setAdapter(adapter)

        val jsonFileString = getJsonDataFromAsset(this, "functions.json")
        val gson = Gson()
        val listCityType = object : TypeToken<List<FunctionList>>() {}.type
        var persons: List<FunctionList> = gson.fromJson(jsonFileString, listCityType)
        persons.forEachIndexed { idx, func ->
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
                exampleCityList.add(result)

//                Log.i("Arrraaaaay", exampleCityList.toString())

            }


        }





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
        UserID = LocalSessionManager.getStringValue(Constant.UserID, "", this).toString()


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

    }

    fun validatefunction(): Boolean {
        if (binding.tiFunctionForm.selectedItem.toString() == "Select Category") {
            Toast.makeText(this, "Please select Category", Toast.LENGTH_SHORT)
            return false
        } else {
        }
        return true
    }

    private fun isValidate(): Boolean =
        validatefunction()

    private fun Savecompany(
        companyName: String,
        location:String,
        startDate:String,
        isPresent:String,
        endDate:String,
        designation:String,
        function:Any,
        noticePeriod: Any) {
        var saveParams=
            SaveCompanyRequest(companyName,location,startDate,isPresent,endDate,designation,function,noticePeriod)
        viewModel.saveCompany(this,saveParams,binding.progress).observe(this,{
            Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            var intent=Intent(this,Signup_CV::class.java)
            startActivity(intent)

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
        if (binding.tiFunctionForm.selectedItem.toString()=="Select Category"){
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
        else if (binding.tiFunctionForm.selectedItem.toString()=="Other"){
            id=4276
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
            Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
            var intent=Intent(this,Signup_CV::class.java)
            startActivity(intent)
        })
    }
}
