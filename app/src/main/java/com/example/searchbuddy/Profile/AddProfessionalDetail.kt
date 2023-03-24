package com.example.searchbuddy.Profile

import android.R
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.searchbuddy.Utils.Constant
import com.example.searchbuddy.Utils.LocalSessionManager
import com.example.searchbuddy.databinding.ActivityAddProfessionalDetailBinding
import com.example.searchbuddy.model.CityList
import com.example.searchbuddy.model.SaveCompanyRequest
import com.example.searchbuddy.model.WorkHistory
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.util.*


class AddProfessionalDetail : AppCompatActivity() {
    lateinit var binding: ActivityAddProfessionalDetailBinding
    var exampleCityList: ArrayList<String> = ArrayList()
    lateinit var designation: String
    var comapanyName: String = ""
    lateinit var list: ArrayList<WorkHistory>
    lateinit var UserID: String
    var is_Prsent:String="null"
    lateinit var viewModel: AddProfesionalDetailViewModel


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddProfesionalDetailViewModel::class.java)

        binding = ActivityAddProfessionalDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.extras != null) {
            var bundle = intent.extras!!
            if (bundle != null) {
                comapanyName = bundle.getString("company_name", "")
                requestCompany()
                binding.save.setOnClickListener {
                    Updatecompany(
                        binding.etCompName!!.text.toString(),
                        binding.tiLocationForm!!.text.toString(),
                        binding.etDateWork!!.text.toString() + "T18:30:00.000Z",
                        is_Prsent,
                        binding.etEnddateWork!!.text.toString() + "T18:30:00.000Z",
                        binding.etDesignation!!.text.toString()
                    )
                }
            }
        }

        //            startDate = bundle!!.getString("start_date", "")
//            endDate = bundle!!.getString("end_date", "")
//            is_prsent = bundle!!.getString("is_present", "")
//            if (designation != null) {
//                binding.etDesignation.setText(designation)
//                binding.etCompName.setText(comapanyName)
//
//            }
//            if (startDate != null) {
//                var output = startDate
//                var outputdate = output.substring(0, 10)
//                binding.etDateWork.setText(outputdate)
//            }
//        }
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
                    val dat = (year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
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
                    val date = (year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
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
        binding.radioYesWork.isChecked = true
        binding.save.setOnClickListener {
            if (binding.radioYesWork.isChecked==true){
                 is_Prsent="true"
            }
    Savecompany(
        binding.etCompName!!.text.toString(),
        binding.tiLocationForm!!.text!!.toString(),
        binding.etDateWork!!.text.toString() + "T18:30:00.000Z",
        is_Prsent,
        binding.etEnddateWork!!.text.toString() + "T18:30:00.000Z",
        binding.etDesignation!!.text.toString()
    )


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
        viewModel.requestCompany(this, comapanyName, binding.progress).observe(this, {
//            Log.i("comapany", it.companyName.toString())
            if (it.companyName != null) {
                binding.etCompName.setText(it.companyName)
            }
            if (it.designation != null) {

                binding.etDesignation.setText(it.designation)
            }
            if (it.location != null) {
                var spinnerText: String = it.location
                var spinnerPosition: Int = adapter.getPosition(spinnerText)
                binding.tiLocationForm.setText(it.location.toString())
            }
            if (it.isPresent != null) {
                binding.radioYesWork.isChecked == true
            } else {
                binding.radioNoWork.isChecked == true
            }
            if (it.startDate != null) {
                var startDate = it.startDate
//                Log.i("Date",startDate)
                var OutputStartDate=startDate.substring(0,10)
                binding.etDateWork.setText(OutputStartDate)
            }


//            if (it.endDate != null) {
//                var EndDate = it.endDate.toString()
//                var OutputEndDate = EndDate.substring(0, 10)
//                binding.etEnddateWork.setText(OutputEndDate)
//            }
        })
    }


    private fun Savecompany(
        companyName: String,
        location:String,
        startDate:String,
        isPresent:String,
        endDate:String,
        designation:String) {
        var saveParams=SaveCompanyRequest(companyName,location,startDate,isPresent,endDate,designation)
        viewModel.saveCompany(this,saveParams,binding.progress).observe(this,{
            Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()

        })
    }

    private fun Updatecompany(
        companyName: String,
        location:String,
        startDate:String,
        isPresent:String,
        endDate:String,
        designation:String) {
        var saveParams=SaveCompanyRequest(companyName,location,startDate,isPresent,endDate,designation)
        viewModel.UpdateCompany(this,comapanyName,saveParams,binding.progress).observe(this,{
            Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
        })
    }


}