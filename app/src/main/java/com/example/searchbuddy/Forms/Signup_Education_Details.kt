package com.example.searchbuddy.Forms

import UpdateEducationDetailRequest
import android.R
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.searchbuddy.Utils.Constant
import com.example.searchbuddy.Utils.LocalSessionManager
import com.example.searchbuddy.databinding.ActivitySignupFormTwoBinding
import com.example.searchbuddy.model.CollegeListItem
import com.example.searchbuddy.model.PgDegreeListItem
import com.example.searchbuddy.model.UgDegreesListItem
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import java.io.IOException


class Signup_Education_Details : AppCompatActivity() {
    var exampleCollegeList: ArrayList<String> = ArrayList()
    var ugDegreeList: ArrayList<String> = ArrayList()
    var pgDegreeList: ArrayList<String> = ArrayList()
    var pgSpecList: ArrayList<Any> = ArrayList()
    var pSpecList: ArrayList<Any> = ArrayList()
    var board: ArrayList<String> = ArrayList()
    lateinit var viewModel: Signup_Education_ViewModel
    lateinit var UserID: String
    lateinit var descriptionData: Array<String>
    lateinit var binding: ActivitySignupFormTwoBinding
    lateinit var BoardList: java.util.ArrayList<String>
    lateinit var StartYearList: Array<Any>
    lateinit var EndYearList: Array<Any>
    var clickcount: Int = 0
     var GradStartYear:Int=0
     var Twe_ENDYEAR:Int=0
     var GradPassingYear:Int=0
     var PostGradStartYear:Int=0
     var PostGradPassingYear:Int=0

    var specializationList: ArrayList<Any> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupFormTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(Signup_Education_ViewModel::class.java)

//        binding.cancel.setOnClickListener {
//            val intent = Intent(this, Dashboard::class.java)
//            startActivity(intent)
//            finish()
//        }
//        binding.save.setOnClickListener {
//            updateDetails()
//        }
        BoardList = arrayListOf(
            "Board",
            "CBSE",
            "ICSE",
            "State Board",
            "Open Board",
            "ISE",
            "International",
        )

        StartYearList = arrayOf(
            "Select Start year",
            "2028", "2027", "2026", "2025", "2024", "2023", "2022", "2021", "2020",
            "2019", "2018", "2017", "2016", "2015", "2014", "2013", "2012", "2011", "2010",
            "2009", "2008", "2007", "2006", "2005", "2004", "2003", "2002", "2001", "2000"
        )
        EndYearList = arrayOf(
            "Select completion year",
            "2028", "2027", "2026", "2025", "2024", "2023", "2022", "2021", "2020",
            "2019", "2018", "2017", "2016", "2015", "2014", "2013", "2012", "2011", "2010",
            "2009", "2008", "2007", "2006", "2005", "2004", "2003", "2002", "2001", "2000"
        )
//YearList.add(0,"Select Year of completion")
        Log.i("mmmm", StartYearList.toString())
        binding.skipBtn.setOnClickListener {
            var intent = Intent(this, Signup_Work_History::class.java)
            startActivity(intent)
        }
        val DateAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, StartYearList)
        DateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val ENDDateAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, EndYearList)
        ENDDateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tweEndYr.setAdapter(ENDDateAdapter)
        binding.tweEndYr.setOnItemSelectedListener(object :
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

        binding.startGrad.setAdapter(DateAdapter)
        binding.startGrad.setOnItemSelectedListener(object :
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

        binding.endGrad.setAdapter(ENDDateAdapter)
        binding.endGrad.setOnItemSelectedListener(object :
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

        binding.startGrad.setAdapter(DateAdapter)
        binding.startGrad.setOnItemSelectedListener(object :
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
        binding.startPostgrad.setAdapter(DateAdapter)
        binding.startPostgrad.setOnItemSelectedListener(object :
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
        binding.endPostgrad.setAdapter(ENDDateAdapter)
        binding.endPostgrad.setOnItemSelectedListener(object :
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
        val noticeAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, BoardList)
        noticeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.etBoard.setAdapter(noticeAdapter)
        binding.etBoard.setOnItemSelectedListener(object :
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
        descriptionData =
            arrayOf("Basic\nDetails", "Education", "Experience", "CV","Preferences")
        binding.yourStateProgressBarId.setStateDescriptionData(descriptionData)
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

        val jsonFileString = getJsonDataFromAsset(this, "colleges.json")
        val gson = Gson()
        val listCityType = object : TypeToken<List<CollegeListItem>>() {}.type
        val persons: List<CollegeListItem> = gson.fromJson(jsonFileString, listCityType)
        persons.forEachIndexed { idx, clg ->
//            Log.i("dataaaaa", "> Item $idx:\n${clg.college}")
//             someList = person.Districtname
            val CityArray: JsonArray = JsonArray()
            val dataa: String = clg.college
            CityArray.add(dataa)

//            Log.i("kkkkkkkkk", clg.college)
            for (i in 0 until CityArray.size()) {
//                val he = CityList("","",person.CombinedName,"","")
//                exampleList.add(exampleArray.toString())
                var kuch = CityArray.get(i).toString()
                var result = kuch.substring(1, kuch.length - 1)
                exampleCollegeList.add(result)

//                Log.i("Arrraaaaay", exampleCollegeList.toString())

            }
        }
        UserID = LocalSessionManager.getStringValue(Constant.UserID, "", this).toString()

        val adapter =
            ArrayAdapter(this, R.layout.simple_spinner_item, exampleCollegeList)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tiGradUni.setAdapter(adapter)
        binding.tiPostgradUni.setAdapter(adapter)
        binding.tiGradUni.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val item = parent.getItemAtPosition(position)
                var location_string = item.toString()
                Log.i("spinner", item.toString())

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        binding.tiPostgradUni.setOnItemSelectedListener(object :
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
        val UgFileString = getJsonDataFromAsset(this, "ugdegrees.json")
        val gsonn = Gson()
        val listDegreeType = object : TypeToken<List<UgDegreesListItem>>() {}.type
        var degree: List<UgDegreesListItem> = gsonn.fromJson(UgFileString, listDegreeType)
        degree.forEachIndexed { idx, deg ->
//            Log.i("dataaaaa", "> Item $idx:\n${deg.UG}")
//             someList = person.Districtname
            val CityArray: JsonArray = JsonArray()
            val specArray: JsonArray = JsonArray()
            val dataa: String = deg.UG
            val ugspecialList: ArrayList<Any> = deg.specialisation

            CityArray.add(dataa)
            specArray.add(ugspecialList.toString())
            for (i in 0 until CityArray.size()) {
                var kuch = CityArray.get(i).toString()
                var result = kuch.substring(1, kuch.length - 1)
                ugDegreeList.add(result)

            }
            for (i in 0 until specArray.size()) {
                var kuch = specArray.get(i).toString()
                var specx = kuch.substring(2, kuch.length - 2)
                specializationList.add(specx)
                Log.i("zzzz", specx)

            }
        }
//        binding.save.setOnClickListener{
//            updateDetails()
//        }
        val UGadapter =
            ArrayAdapter(this, R.layout.simple_spinner_item, ugDegreeList)

        UGadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tiGradDeg.setAdapter(UGadapter)


        binding.tiGradDeg.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                var index = position

                Log.i("ooo", pgSpecList.toString())
                val item = parent.getItemAtPosition(position)
                Log.i("spinner", item.toString())
                pgSpecList.clear()
                var specs = specializationList.elementAt(position)
//        var specsub=specs.substring(1,specs.length-1)
                pgSpecList.add(specs)
                pSpecList.clear()
                val it: MutableListIterator<Any> = pgSpecList.listIterator()
                while (it.hasNext()) {
                    val e = it.next()
                    pSpecList.add(e)
                }
                Log.i("nana", pSpecList.toString())
                Log.i("nanaa", pgSpecList.toString())

//                LocationList.add(item.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        })

        Log.i("mama", pgSpecList.toString())
        val SpecAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, pSpecList)
        SpecAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        SpecAdapter.notifyDataSetChanged()

        val PgFileString = getJsonDataFromAsset(this, "pgdegrees.json")
        val gsonnn = Gson()
        val listPgDegreeType = object : TypeToken<List<PgDegreeListItem>>() {}.type
        var Pgdegree: List<PgDegreeListItem> = gsonnn.fromJson(PgFileString, listPgDegreeType)
        Pgdegree.forEachIndexed { idx, deg ->

            val CityArray: JsonArray = JsonArray()
            val dataa: String = deg.PG
            CityArray.add(dataa)

            for (i in 0 until CityArray.size()) {
                var kuch = CityArray.get(i).toString()
                var result = kuch.substring(1, kuch.length - 1)
                pgDegreeList.add(result)

            }
        }
        val PGadapter =
            ArrayAdapter(this, R.layout.simple_spinner_item, pgDegreeList)

        UGadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tiPostgradDeg.setAdapter(PGadapter)
        binding.tiPostgradDeg.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val item = parent.getItemAtPosition(position)
                var location_string = item.toString()
                Log.i("pg", item.toString())


//                LocationList.add(item.toString())
            }


            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

binding.btnSbmt.setOnClickListener {
    updateDetails()
}
//        binding.btnSbmt.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View?) {
//
//                clickcount = clickcount + 1
//                if (clickcount == 1) {
//                    //first time clicked to do this
//                    val builder =
//                        androidx.appcompat.app.AlertDialog.Builder(this@Signup_Education_Details)
//                    //set title for alert dialog
//                    builder.setTitle("SearchBuddy is asking!")
//                    //set message for alert dialog
//                    builder.setMessage("Are you a graduate?")
//                    builder.setPositiveButton("Yes") { dialogInterface, which ->
//                        binding.tweLl.visibility = View.GONE
//                        binding.tenthLlc.visibility = View.GONE
//                        binding.postLl.visibility = View.GONE
//                        binding.gradLl.visibility = View.VISIBLE
//                        binding.graduate.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_selected_border)
//                        binding.tenth.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_border)
//                        binding.twelve.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_selected_border)
//                        binding.postGraduate.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_border)
//                    }
//                    builder.setNegativeButton("No") { dialogInterface, which ->
//                        val builder =
//                            androidx.appcompat.app.AlertDialog.Builder(this@Signup_Education_Details)
//                        //set title for alert dialog
//                        builder.setTitle("SearchBuddy says!")
//                        //set message for alert dialog
//                        builder.setMessage("Don't worry we got your back")
//                        builder.setPositiveButton("OK") { dialogInterface, which ->
//                            updateDetails()
//                            var intent = Intent(
//                                this@Signup_Education_Details,
//                                Signup_Work_History::class.java
//                            )
//                            startActivity(intent)
//                            finish()
//                        }
//
//                        val alertDialog: androidx.appcompat.app.AlertDialog = builder.create()
//                        alertDialog.setCancelable(false)
//                        alertDialog.show()
//                    }
//                    val alertDialog: androidx.appcompat.app.AlertDialog = builder.create()
//                    alertDialog.setCancelable(false)
//                    alertDialog.show()
//
//
//                } else if (clickcount == 2) {
//                    //check how many times clicked and so on
//                    val builder =
//                        androidx.appcompat.app.AlertDialog.Builder(this@Signup_Education_Details)
//                    //set title for alert dialog
//                    builder.setTitle("SearchBuddy is asking!")
//                    //set message for alert dialog
//                    builder.setMessage("Are you a Postgraduate?")
//                    builder.setPositiveButton("Yes") { dialogInterface, which ->
//                        binding.tweLl.visibility = View.GONE
//                        binding.gradLl.visibility = View.GONE
//                        binding.tenthLlc.visibility = View.GONE
//                        binding.postLl.visibility = View.VISIBLE
//                        binding.postGraduate.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_selected_border)
//                        binding.tenth.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_border)
//                        binding.twelve.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_selected_border)
//                        binding.graduate.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_selected_border)
//                    }
//                    builder.setNegativeButton("No") { dialogInterface, which ->
//                        val builder =
//                            androidx.appcompat.app.AlertDialog.Builder(this@Signup_Education_Details)
//                        //set title for alert dialog
//                        builder.setTitle("SearchBuddy says!")
//                        //set message for alert dialog
//                        builder.setMessage("Don't worry we got your back")
//                        builder.setPositiveButton("OK") { dialogInterface, which ->
//                            updateDetails()
//
//                            var intent = Intent(
//                                this@Signup_Education_Details,
//                                Signup_Work_History::class.java
//                            )
//                            startActivity(intent)
//                            finish()
//                        }
//
//                        val alertDialog: androidx.appcompat.app.AlertDialog = builder.create()
//                        alertDialog.setCancelable(false)
//                        alertDialog.show()
//                    }
//                    val alertDialog: androidx.appcompat.app.AlertDialog = builder.create()
//                    alertDialog.setCancelable(false)
//                    alertDialog.show()
//                } else if (clickcount == 3) {
//                    updateDetails()
//                    Toast.makeText(
//                        applicationContext,
//                        "Education Details Saved",
//                        Toast.LENGTH_LONG
//                   ).show()
//                }
//                else if(clickcount==4){
//                    updateDetails()
//                    Toast.makeText(
//                        applicationContext,
//                        "Education Details Saved",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
//        })
//        yo = UserID.toInt()


        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(com.example.searchbuddy.R.color.orange)
        }
        binding.twelve.setOnClickListener {
            binding.tenthLlc.visibility=View.GONE
            binding.gradLl.visibility=View.GONE
            binding.postLl.visibility=View.GONE
            binding.tweLl.visibility=View.VISIBLE
//            binding.graduate.visibility=View.GONE
//            binding.twelve.visibility=View.GONE
//            binding.postGraduate.visibility=View.GONE
//            binding.highestQual.visibility=View.GONE
            binding.twelve.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_selected_border)
            binding.tenth.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_border)
            binding.graduate.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_border)
            binding.postGraduate.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_border)


        }
        binding.graduate.setOnClickListener {
            binding.tweLl.visibility=View.GONE
            binding.tenthLlc.visibility=View.GONE
            binding.postLl.visibility=View.GONE
            binding.gradLl.visibility=View.VISIBLE
//            binding.graduate.visibility=View.GONE
//            binding.twelve.visibility=View.GONE
//            binding.postGraduate.visibility=View.GONE
//            binding.highestQual.visibility=View.GONE
            binding.graduate.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_selected_border)
            binding.tenth.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_border)
            binding.twelve.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_border)
            binding.postGraduate.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_border)

        }
        binding.postGraduate.setOnClickListener{
            binding.tweLl.visibility=View.GONE
            binding.gradLl.visibility=View.GONE
            binding.tenthLlc.visibility=View.GONE
            binding.postLl.visibility=View.VISIBLE
//            binding.graduate.visibility=View.GONE
//            binding.twelve.visibility=View.GONE
//            binding.postGraduate.visibility=View.GONE
//            binding.highestQual.visibility=View.GONE
            binding.postGraduate.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_selected_border)
            binding.tenth.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_border)
            binding.twelve.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_border)
            binding.graduate.setBackgroundResource(com.example.searchbuddy.R.drawable.work_status_border)

        }



    }
    private fun updateDetails() {
        if (binding.startGrad.selectedItem=="Select Start year"){
            GradStartYear=0
        }
        else{
            var gs=binding.startGrad.selectedItem.toString()
            GradStartYear=gs.toInt()
        }
        if (binding.tweEndYr.selectedItem=="Select completion year"){
            Twe_ENDYEAR=0
        }
        else{
            var te=binding.tweEndYr.selectedItem.toString()
            Twe_ENDYEAR=te.toInt()
        }
        if (binding.endGrad.selectedItem=="Select completion year"){
            GradPassingYear=0
        }
        else{
            var ge=binding.endGrad.selectedItem.toString()
            GradPassingYear=ge.toInt()
        }
        if (binding.startPostgrad.selectedItem=="Select Start year"){
            PostGradStartYear=0
        }
        else{
            var pgs=binding.startPostgrad.selectedItem.toString()
            PostGradPassingYear=pgs.toInt()
        }
        if (binding.endPostgrad.selectedItem=="Select completion year"){
            PostGradPassingYear=0
        }
        else{
            var pge=binding.endPostgrad.selectedItem.toString()
            PostGradPassingYear=pge.toInt()
        }
        var id = 4
        var professionalDetails = object {
            var educationDetails = object {
                var metric = object {
                    var board = object {
                        var id = 4253
                        var value = binding.etBoardTw!!.text.toString()
                    }
                    var school = binding.etSchoolTw!!.text.toString()
                    var percentage = binding.cgpaTw!!.text.toString()
                    var startYear = binding.etTenStartYr!!.text.toString()
                    var passingYear = binding.etTenEndYr!!.text.toString()
                }
                var intermediate = object {
                    var board = object {
                        var id = 4253
                        var value = binding.etBoard!!.selectedItem.toString()
                    }
                    var school = binding.etSchoolTwe!!.text.toString()
                    var percentage = binding.etPercTwe!!.text.toString()
                    var startYear = binding.etTweStartYr!!.text.toString()
//                    var passingYear = binding.tweEndYr!!.selectedItem.toString()
                    var passingYear = Twe_ENDYEAR
                }
                var graduation = object {
                    var university = binding.tiGradUni!!.selectedItem.toString()
                    var degree = binding.tiGradDeg!!.selectedItem.toString()
                    var percentage = binding.cgpaGrad!!.text.toString()
//                    var startYear = binding.startGrad!!.selectedItem.toString()
                    var startYear=GradStartYear
//                    var passingYear = binding.endGrad!!.selectedItem.toString()
                    var passingYear = GradPassingYear
                    var specialization = binding.specGrad!!.text.toString()
                }
                var postGraduation = object {
                    var university = binding.tiPostgradUni!!.selectedItem.toString()
                    var degree = binding.tiPostgradDeg!!.selectedItem.toString()
                    var percentage = binding.cgpaPostgrad!!.text.toString()
//                    var startYear = binding.startPostgrad!!.selectedItem.toString()
                    var startYear = PostGradStartYear
//                    var passingYear = binding.endPostgrad!!.selectedItem.toString()
                    var passingYear = PostGradPassingYear
                    var specialization = binding.specPostgrad!!.text.toString()
                }
            }
        }
        var RequestParams = UpdateEducationDetailRequest(id, professionalDetails)
        viewModel.UpdateEducationDetail(this, RequestParams, binding.progress).observe(this, {
//            Log.i("Educaaa", it.message)
            Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            var intent=Intent(this,Signup_Work_History::class.java)
            startActivity(intent)
        })
    }
}