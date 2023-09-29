package com.searchbuddy.searchbuddy.Profile

import UpdateEducationDetailRequest
import android.content.Context
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
import com.bumptech.searchbuddy.R
import com.bumptech.searchbuddy.databinding.ActivityEducationDetailsBinding
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager
import com.searchbuddy.searchbuddy.model.CollegeListItem
import com.searchbuddy.searchbuddy.model.PgDegreeListItem
import com.searchbuddy.searchbuddy.model.UgDegreesListItem
import java.io.IOException


class Education_details : AppCompatActivity() {
    lateinit var binding: ActivityEducationDetailsBinding
    var exampleCollegeList: ArrayList<String> = ArrayList()
    var ugDegreeList: ArrayList<String> = ArrayList()
    var pgDegreeList: ArrayList<String> = ArrayList()
    var pgSpecList: ArrayList<Any> = ArrayList()
    var pSpecList: ArrayList<Any> = ArrayList()
    var board: ArrayList<String> = ArrayList()
    lateinit var viewModel: EducationDetailViewModel
    lateinit var UserID: String
    lateinit var BoardList: java.util.ArrayList<String>
    lateinit var EndYearList: Array<Any>
    lateinit var StartYearList: Array<Any>
    var GradStartYear:Int=0
    var Twe_ENDYEAR:Int=0
    var GradPassingYear:Int=0
    var PostGradStartYear:Int=0
    var PostGradPassingYear:Int=0
    var specializationList :ArrayList<Any> = ArrayList()
    var yo:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEducationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(EducationDetailViewModel::class.java)


        binding.cancel.setOnClickListener {
         onBackPressed()
        }
        BoardList = arrayListOf(
            "Board",
            "CBSE",
            "ICSE",
            "State Board",
            "Open Board",
            "ISE",
            "International",
        )
        EndYearList = arrayOf(
            "Select completion year",
            "2028", "2027", "2026", "2025", "2024", "2023", "2022", "2021", "2020",
            "2019", "2018", "2017", "2016", "2015", "2014", "2013", "2012", "2011", "2010",
            "2009", "2008", "2007", "2006", "2005", "2004", "2003", "2002", "2001", "2000"
        )
        StartYearList = arrayOf(
            "Select Start year",
            "2028", "2027", "2026", "2025", "2024", "2023", "2022", "2021", "2020",
            "2019", "2018", "2017", "2016", "2015", "2014", "2013", "2012", "2011", "2010",
            "2009", "2008", "2007", "2006", "2005", "2004", "2003", "2002", "2001", "2000"
        )
        binding.tenthLlc.visibility=View.GONE
        binding.gradLl.visibility=View.GONE
        binding.postLl.visibility=View.GONE
        binding.tweLl.visibility=View.VISIBLE
        binding.twelve.setBackgroundResource(R.drawable.work_status_selected_border)
        binding.tenth.setBackgroundResource(R.drawable.work_status_border)
        binding.graduate.setBackgroundResource(R.drawable.work_status_border)
        binding.postGraduate.setBackgroundResource(R.drawable.work_status_border)
        binding.save.setOnClickListener {
updateDetails()
        }

        val DateAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, StartYearList)
        DateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val ENDDateAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, EndYearList)
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
//                Log.i("spinner", item.toString())


//                LocationList.add(item.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        val noticeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, BoardList)
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
        var persons: List<CollegeListItem> = gson.fromJson(jsonFileString, listCityType)
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
            ArrayAdapter(this, android.R.layout.simple_spinner_item, exampleCollegeList)

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
                var  location_string = item.toString()
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
                var  location_string = item.toString()
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
            val specArray:JsonArray=JsonArray()
            val dataa: String = deg.UG
            val ugspecialList:ArrayList<Any> =deg.specialisation

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
                Log.i("zzzz",specx)

            }
        }
        binding.tenth.setBackgroundResource(R.drawable.work_status_selected_border)
        binding.save.setOnClickListener{
            updateDetails()
        }
        val UGadapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, ugDegreeList)

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
                var index=position

                Log.i("ooo",pgSpecList.toString())
                val item = parent.getItemAtPosition(position)
                Log.i("spinner", item.toString())
pgSpecList.clear()
                var specs=specializationList.elementAt(position)
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
        val SpecAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,pSpecList)
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
            ArrayAdapter(this, android.R.layout.simple_spinner_item, pgDegreeList)

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
                var  location_string = item.toString()
                Log.i("pg", item.toString())


//                LocationList.add(item.toString())
            }


            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
         yo = UserID.toInt()
        viewModel.requestEducationDetail(this,yo,binding.progress).observe(this,{
            if(it.professionalDetails!=null) {
                if (it.professionalDetails.educationDetails != null) {

                    if (it.professionalDetails.educationDetails.metric!=null) {
                        if (it.professionalDetails.educationDetails.metric.school!=null) {
                            binding.etSchoolTw.setText(it.professionalDetails.educationDetails.metric.school.toString())
                        }
                        if (it.professionalDetails.educationDetails.metric.startYear!=null) {
                            var year:String=it.professionalDetails.educationDetails.metric.startYear.toString()
                            var yearsuv=year
                            binding.etTenStartYr.setText(yearsuv)
                        }
                        if (it.professionalDetails.educationDetails.metric.percentage!=null) {
                            binding.cgpaTw.setText(it.professionalDetails.educationDetails.metric.percentage.toString())
                        }
                        if (it.professionalDetails.educationDetails.metric.passingYear!=null) {
                            binding.etTenEndYr.setText(it.professionalDetails.educationDetails.metric.passingYear.toString())
                        }
                        if (it.professionalDetails.educationDetails.metric.board!=null){
                            if (it.professionalDetails.educationDetails.metric.board.value!=null) {
                                binding.etBoardTw.setText(it.professionalDetails.educationDetails.metric.board.value.toString())
                            }
                        }
                    }

                    if (it.professionalDetails.educationDetails.intermediate!=null){
                        if (it.professionalDetails.educationDetails.intermediate.school!=null) {
                            binding.etSchoolTwe.setText(it.professionalDetails.educationDetails.intermediate.school.toString())
                        }
                        if (it.professionalDetails.educationDetails.intermediate.startYear!=null) {
                            binding.etTweStartYr.setText(it.professionalDetails.educationDetails.intermediate.startYear.toString())
                        }
                        if (it.professionalDetails.educationDetails.intermediate.percentage!=null) {
                            binding.etPercTwe.setText(it.professionalDetails.educationDetails.intermediate.percentage.toString())
                        }
                        if (it.professionalDetails.educationDetails.intermediate.board!=null){
                            if (it.professionalDetails.educationDetails.intermediate.board.value!=null) {
                                var twe_board=  it.professionalDetails.educationDetails.intermediate.board.value.toString()
                                var twe_adater=noticeAdapter.getPosition(twe_board)
                                binding.etBoard.setSelection(twe_adater)
                            }
                        }
                        if (it.professionalDetails.educationDetails.intermediate.passingYear!=null){
                            var twe_passing_year= it.professionalDetails.educationDetails.intermediate.passingYear.toDouble().toString()
                            var twe_substring=twe_passing_year.substring(0,4)
                            var twe_pass_adapter=ENDDateAdapter.getPosition(twe_substring)
                            binding.tweEndYr.setSelection(twe_pass_adapter)
                        }
                    }
                    if (it.professionalDetails.educationDetails.graduation!=null){
                        if (it.professionalDetails.educationDetails.graduation.percentage!=null){
                             binding.cgpaGrad.setText(it.professionalDetails.educationDetails.graduation.percentage.toString())
                        }
                        if (it.professionalDetails.educationDetails.graduation.startYear!=null){
                            var gradstart=it.professionalDetails.educationDetails.graduation.startYear.toString()

                        }
                        if (it.professionalDetails.educationDetails.graduation.passingYear!=null){
                            var gradend=it.professionalDetails.educationDetails.graduation.passingYear.toString()
                            var grad_end_adapter=ENDDateAdapter.getPosition(gradend)
                            binding.endGrad.setSelection(grad_end_adapter)
                        }
                        if (it.professionalDetails.educationDetails.graduation.specialization!=null){
                            binding.specGrad.setText(it.professionalDetails.educationDetails.graduation.specialization.toString())
                        }
                        if (it.professionalDetails.educationDetails.graduation.degree!=null) {
                            var gradDegree=it.professionalDetails.educationDetails.graduation.degree.toString()
                            if (gradDegree!=null){
                                var degreespinner:Int=UGadapter.getPosition(gradDegree)
                                binding.tiGradDeg.setSelection(degreespinner,true)
                            }
                        }
                        if (it.professionalDetails.educationDetails.graduation.university!=null){
                            var gradUni=it.professionalDetails.educationDetails.graduation.university.toString()
                            if (gradUni!=null){
                                binding.tiGradUni.setText(it.professionalDetails.educationDetails.graduation.university.toString())
                            }
                        }
                    }
                    if (it.professionalDetails.educationDetails.postGraduation!=null){
                        if (it.professionalDetails.educationDetails.postGraduation.percentage!=null){
                            binding.cgpaPostgrad.setText(it.professionalDetails.educationDetails.postGraduation.percentage.toString())
                        }
                        if (it.professionalDetails.educationDetails.postGraduation.startYear!=null){
                            if (it.professionalDetails.educationDetails.postGraduation.startYear!=0) {
                                var startpostgrad =
                                    it.professionalDetails.educationDetails.postGraduation.startYear.toString()
                                var startpost_sybstring = startpostgrad.substring(0, 3)
                                var startpostgradadapter =
                                    DateAdapter.getPosition(startpost_sybstring)
                                binding.startPostgrad.setSelection(startpostgradadapter)
                            }
                        }
                        if (it.professionalDetails.educationDetails.postGraduation.passingYear!=null){
                            if (it.professionalDetails.educationDetails.postGraduation.passingYear!=0) {
                                var endpostgrad =
                                    it.professionalDetails.educationDetails.postGraduation.passingYear.toString()
                                var endpost_substring = endpostgrad.substring(0, 3)
                                var endpostgraadapter =
                                    ENDDateAdapter.getPosition(endpost_substring)
                                binding.endPostgrad.setSelection(endpostgraadapter)
                            }
                        }
                        if (it.professionalDetails.educationDetails.postGraduation.specialization!=null){
                            binding.specPostgrad.setText(it.professionalDetails.educationDetails.postGraduation.specialization.toString())
                        }
                        if (it.professionalDetails.educationDetails.postGraduation.degree!=null){
                            var PostgradDegree=it.professionalDetails.educationDetails.postGraduation.degree
                            if (PostgradDegree!=null){
                                var PostDegreeSpinner:Int=PGadapter.getPosition(PostgradDegree)
                                binding.tiPostgradDeg.setSelection(PostDegreeSpinner,true)
                            }
                        }
                        if (it.professionalDetails.educationDetails.postGraduation.university!=null){
                            var PostgradUni=it.professionalDetails.educationDetails.postGraduation.university
                            if (PostgradUni!=null){
                                binding.tiPostgradUni.setText(it.professionalDetails.educationDetails.postGraduation.university)
                            }
                        }
                    }
                }
            }

            })

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.grey)
        }
        binding.tenth.setOnClickListener {
            binding.tweLl.visibility=View.GONE
            binding.gradLl.visibility=View.GONE
            binding.postLl.visibility=View.GONE
            binding.tenthLlc.visibility=View.VISIBLE
            binding.tenth.setBackgroundResource(R.drawable.work_status_selected_border)
            binding.postGraduate.setBackgroundResource(R.drawable.work_status_border)
            binding.graduate.setBackgroundResource(R.drawable.work_status_border)
            binding.twelve.setBackgroundResource(R.drawable.work_status_border)

        }

        binding.twelve.setOnClickListener {
            binding.tenthLlc.visibility=View.GONE
            binding.gradLl.visibility=View.GONE
            binding.postLl.visibility=View.GONE
            binding.tweLl.visibility=View.VISIBLE
            binding.twelve.setBackgroundResource(R.drawable.work_status_selected_border)
            binding.tenth.setBackgroundResource(R.drawable.work_status_border)
            binding.graduate.setBackgroundResource(R.drawable.work_status_border)
            binding.postGraduate.setBackgroundResource(R.drawable.work_status_border)


        }
        binding.graduate.setOnClickListener {
            binding.tweLl.visibility=View.GONE
            binding.tenthLlc.visibility=View.GONE
            binding.postLl.visibility=View.GONE
            binding.gradLl.visibility=View.VISIBLE
            binding.graduate.setBackgroundResource(R.drawable.work_status_selected_border)
            binding.tenth.setBackgroundResource(R.drawable.work_status_border)
            binding.twelve.setBackgroundResource(R.drawable.work_status_border)
            binding.postGraduate.setBackgroundResource(R.drawable.work_status_border)

        }
        binding.postGraduate.setOnClickListener{
            binding.tweLl.visibility=View.GONE
            binding.gradLl.visibility=View.GONE
            binding.tenthLlc.visibility=View.GONE
            binding.postLl.visibility=View.VISIBLE
            binding.postGraduate.setBackgroundResource(R.drawable.work_status_selected_border)
            binding.tenth.setBackgroundResource(R.drawable.work_status_border)
            binding.twelve.setBackgroundResource(R.drawable.work_status_border)
            binding.graduate.setBackgroundResource(R.drawable.work_status_border)

        }




    }



    private fun updateDetails() {
        if (binding.tweEndYr.selectedItem=="Select completion year"){
            Twe_ENDYEAR=0
        }
        else{
            var te=binding.tweEndYr.selectedItem.toString()
            Log.i("endyr",te)
            Twe_ENDYEAR=te.toInt()
        }
        if (binding.startGrad.selectedItem=="Select Start year"){
            GradStartYear=0
        }
        else{
            var gs=binding.startGrad.selectedItem.toString()
            GradStartYear=gs.toInt()
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
//        if (binding.endPostgrad.selectedItem=="Select completion year"){
//            PostGradPassingYear=0
//        }
//        else{
//            var pge=binding.endPostgrad.selectedItem.toString()
//            PostGradPassingYear=pge.toInt()
//        }
        var id = yo
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
//                    var passingYear = binding.tweEndYr!!.text.toString()
                    var passingYear = Twe_ENDYEAR
                }
                var graduation = object {
                    var university = binding.tiGradUni!!.text.toString()
                    var degree = binding.tiGradDeg!!.selectedItem.toString()
                    var percentage = binding.cgpaGrad!!.text.toString()
                    var startYear = GradStartYear
                    var passingYear = GradPassingYear
                    var specialization = binding.specGrad!!.text.toString()
                }
                var postGraduation = object {
                    var university = binding.tiPostgradUni!!.text.toString()
                    var degree = binding.tiPostgradDeg!!.selectedItem.toString()
                    var percentage = binding.cgpaPostgrad!!.text.toString()
                    var startYear =PostGradStartYear
                    var passingYear = PostGradPassingYear
                    var specialization = binding.specPostgrad!!.text.toString()
                }
            }
        }
        var RequestParams = UpdateEducationDetailRequest(id, professionalDetails)
        viewModel.UpdateEducationDetail(this, RequestParams, binding.progress).observe(this, {
//            Log.i("Educaaa", it.message)
            Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
//            onBackPressed()
        })
    }
}