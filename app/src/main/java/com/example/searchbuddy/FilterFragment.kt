package com.example.searchbuddy

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.searchbuddy.Dashboard.Dashboard
import com.example.searchbuddy.Utils.Constant
import com.example.searchbuddy.Utils.LocalSessionManager
import com.example.searchbuddy.databinding.FilterFooterBinding
import com.example.searchbuddy.databinding.FragmentFilterBinding
import com.example.searchbuddy.model.CityList
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.RangeSlider
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import java.io.IOException
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 * Use the [FilterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FilterFragment : Fragment() {

    lateinit var typeList: LinkedHashMap<String, String>
    lateinit var salaryList: LinkedHashMap<String, String>
    lateinit var locationList: LinkedHashMap<String, String>
    lateinit var freshnessList: LinkedHashMap<String, String>
    lateinit var cattleStatusList: LinkedHashMap<String, String>
    lateinit var departmentList: LinkedHashMap<String, String>
    lateinit var WfhcheckBox: CheckBox
    lateinit var cattlecheckBox: CheckBox

    lateinit var DepartmentcheckBox: CheckBox
    lateinit var binding: FragmentFilterBinding
    lateinit var typeResult: ArrayList<String>
    lateinit var header: RelativeLayout
    lateinit var bottomNavView: BottomNavigationView
    lateinit var cattleResult: ArrayList<String>
    lateinit var location_string: String
    lateinit var date_posted: String
     var exp_start: Float = 0.0f
    var exp_End: Float = 0.0f
    var sal_start: Float = 0.0f
    var sal_End: Float = 0.0f
    var exampleCityList: ArrayList<String> = ArrayList()
    var DateList: ArrayList<String> = ArrayList()
    lateinit var LocationList: ArrayList<String>
    lateinit var Dateadapter:ArrayAdapter<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        typeResult = ArrayList()
        cattleResult = ArrayList()
        typeList = LinkedHashMap<String, String>()
        cattleStatusList = LinkedHashMap<String, String>()
        salaryList = LinkedHashMap<String, String>()
        locationList = LinkedHashMap<String, String>()
        departmentList = LinkedHashMap<String, String>()
        freshnessList = LinkedHashMap<String, String>()
        header = (activity as Dashboard).findViewById(R.id.header)
        header.visibility = View.GONE
        bottomNavView = (activity as Dashboard).findViewById(R.id.nav_view)
        bottomNavView.visibility = View.GONE
        var toolbar: FilterFooterBinding = binding.include2
        var apply: MaterialButton = toolbar.buttonDone
        var clearAll:MaterialButton=toolbar.ivRefresh
        apply.setOnClickListener {
            findNavController().navigateUp()

        }


        binding.buttonDone.setOnClickListener{
            findNavController().navigateUp()

        }
        location_string=""
        val rangeSlider: RangeSlider = binding.rangeSliderExp;
        rangeSlider.values.set(0,30F)
        val rangeSlidersalary: RangeSlider = binding.rangeSliderSalary;
        rangeSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                val values = rangeSlider.values

                val salaryValues = rangeSlidersalary.values
                //Those are the satrt and end values of sldier when user start dragging
                Log.i("SliderPreviousValueFro", values[0].toString())
                Log.i("SliderPreviousValue To", values[1].toString())
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                val values = rangeSlider.values
                val salaryValues = rangeSlidersalary.values
                var l: Int = values[0].roundToInt()
                var m: Int = values[1].roundToInt()
                //Those are the new updated values of sldier when user has finshed dragging
                Log.i("SliderNewValue From", values[0].toString())
                Log.i("SliderNewValue To", values[1].toString())
                binding.expValue.setText(l.toString()+ " years"+ " to " +m.toString()+ " years")

                Log.i("SalarySliderNewValuef", salaryValues[0].toString())
                Log.i("SalarySliderNewValue To", salaryValues[1].toString())

                var i: Int = values[0].roundToInt()
                var j: Int = values[1].roundToInt()
                LocalSessionManager.saveValue(
                    Constant.SliderStartValueFloat,
                    values[0],
                    requireContext()
                )
                LocalSessionManager.saveValue(
                    Constant.SliderEndValueFloat,
                    values[1],
                    requireContext()
                )
                LocalSessionManager.saveValue(
                    Constant.SliderStartValue,
                    i.toString(),
                    requireContext()
                )
                LocalSessionManager.saveValue(
                    Constant.SliderEndValue,
                    j.toString(),
                    requireContext()
                )
//                textView.setText("Start value: ${values[0]}, End value: ${values[1]}")
            }
        })
        exp_start= LocalSessionManager.getFloatValue("SliderStartValueFloat",0F,requireContext())!!
        exp_End= LocalSessionManager.getFloatValue("SliderEndValueFloat",0F,requireContext())!!
        if (exp_start!=null&&exp_End!=null) {
            var exp_start_int = exp_start.toInt()
            var exp_end_int = exp_End.toInt()

            rangeSlider.setValues(exp_start, exp_End)
            binding.expValue.setText(exp_start_int.toString() + " years" + " to " + exp_end_int.toString() + " years")
        }
        rangeSlidersalary.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                val values = rangeSlider.values
                val salaryValues = rangeSlidersalary.values
                //Those are the satrt and end values of sldier when user start dragging
                Log.i("SliderPreviousValueFro", values[0].toString())
                Log.i("SliderPreviousValue To", values[1].toString())
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                val salaryValues = rangeSlidersalary.values
                var q: Int = salaryValues[0].roundToInt()
                var a: Int = salaryValues[1].roundToInt()
                Log.i("SalarySliderNewValuef", salaryValues[0].toString())
                Log.i("SalarySliderNewValue To", salaryValues[1].toString())
                binding.salaryValue.setText(q.toString()+ " Lpa"+ " to " +a.toString()+ " Lpa")

                var k: Int = salaryValues[0].roundToInt()
                var l: Int = salaryValues[1].roundToInt()
                LocalSessionManager.saveValue(
                    Constant.SalarySliderStartValueFloat,
                    salaryValues[0],
                    requireContext()
                )
                LocalSessionManager.saveValue(
                    Constant.SalarySliderEndValueFloat,
                    salaryValues[1],
                    requireContext()
                )
                LocalSessionManager.saveValue(
                    Constant.SalarySliderStartValue,
                    k.toString(),
                    requireContext()
                )
                LocalSessionManager.saveValue(
                    Constant.SalarySliderEndValue,
                    l.toString(),
                    requireContext()
                )

            }
        })
        sal_start= LocalSessionManager.getFloatValue("SalarySliderStartValueFloat",0F,requireContext())!!
        sal_End= LocalSessionManager.getFloatValue("SalarySliderEndValueFloat",0F,requireContext())!!
        if (sal_start!=null&&sal_End!=null) {
            var sal_start_int = sal_start.toInt()
            var sal_end_int = sal_End.toInt()
            rangeSlidersalary.setValues(sal_start, sal_End)
            binding.salaryValue.setText(sal_start_int.toString() + " Lpa" + " to " + sal_end_int.toString() + " Lpa")
        }

        //If you only want the slider start and end value and don't care about the previous values
        rangeSlider.addOnChangeListener { slider, value, fromUser ->
            val values = rangeSlider.values
            Log.i("Slider", "Start value: ${values[0]}, End value: ${values[1]}")
//            textView2.text = "Start value: ${values[0]}, End value: ${values[1]}";
        }
        rangeSlidersalary.addOnChangeListener { slider, value, fromUser ->
            val salaryValues = rangeSlidersalary.values
            Log.i("SalarySlider", "Start value: ${salaryValues[0]}, End value: ${salaryValues[1]}")
//            textView2.text = "Start value: ${values[0]}, End value: ${values[1]}";
        }
        typeList.put("1", "Wfh during Covid")
        typeList.put("2", "Remote")
        typeList.put("3", "Onsite")
        typeList.put("4", "Hybrid")

        cattleStatusList.put("1", "Fresher")
        cattleStatusList.put("2", "0-1year")
        cattleStatusList.put("3", "2-4year")
        cattleStatusList.put("4", ">5 years")

        salaryList.put("1", "0-3 LPA")
        salaryList.put("2", "3-6 LPA")
        salaryList.put("3", "6-9 LPA")
        salaryList.put("4", "9-12 LPA")
        salaryList.put("5", "12-15 LPA")
        salaryList.put("6", "15-18 LPA")

        locationList.put("1", "Noida")
        locationList.put("2", "Delhi")
        locationList.put("3", "Gurugram")
        locationList.put("4", "Jaipur")
        locationList.put("5", "Lucknow")
        locationList.put("6", "Banglore")

        freshnessList.put("1", "3 days")
        freshnessList.put("2", "7 days")
        freshnessList.put("3", "1 month")
        freshnessList.put("4", "3 month")

        departmentList.put("1", "Field Sales")
        departmentList.put("2", "Lead Generation")
        departmentList.put("3", "Revenue Marketing")
        departmentList.put("4", "Service Sales")
        departmentList.put("5", "PreSales/Sales Support")
        departmentList.put("6", "Sales Strategy")
        binding.rangeSliderExp.setLabelFormatter { value: Float ->
            return@setLabelFormatter "" + value.roundToInt() + "years"
        }
        binding.rangeSliderSalary.setLabelFormatter { value: Float ->
            return@setLabelFormatter "" + value.roundToInt() + "LPa"
        }

//        createCattleStatusView()
        createTypeView()
        createSalaryStatusView()
        createLocationStatusView()
        createFreshnessStatusView()
        createDepartmentStatusView()
        binding.experLayout.setOnClickListener {

            binding.wfhArrow.setImageResource(R.drawable.down_arrow)
            binding.salArrow.setImageResource(R.drawable.down_arrow)
            binding.locArrow.setImageResource(R.drawable.down_arrow)
            binding.freshArrow.setImageResource(R.drawable.down_arrow)
            binding.departArrow.setImageResource(R.drawable.down_arrow)
            binding.exparrow.setImageResource(R.drawable.down_arrow_white)
            binding.experLayout.setBackgroundColor(resources.getColor(R.color.choclate))
            binding.cattleTypeHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.salaryHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.locationHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.departmentHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.experience.setTextColor(resources.getColor(R.color.white))
            binding.freshHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.Wfh.setTextColor(resources.getColor(R.color.black))
            binding.salary.setTextColor(resources.getColor(R.color.black))
            binding.freshness.setTextColor(resources.getColor(R.color.black))
            binding.location.setTextColor(resources.getColor(R.color.black))
            binding.department.setTextColor(resources.getColor(R.color.black))
            binding.layoutCattleStatus.visibility = View.VISIBLE
            binding.layoutCattleType.visibility = View.GONE
            binding.layoutSalaryType.visibility = View.GONE
            binding.layoutFreshnessType.visibility = View.GONE
            binding.layoutDepartmentType.visibility = View.GONE
            binding.layoutLocationType.visibility = View.GONE
        }
        binding.imageView2.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.cattleTypeHead.setOnClickListener {

            binding.wfhArrow.setImageResource(R.drawable.down_arrow_white)
            binding.exparrow.setImageResource(R.drawable.down_arrow)
            binding.salArrow.setImageResource(R.drawable.down_arrow)
            binding.locArrow.setImageResource(R.drawable.down_arrow)
            binding.freshArrow.setImageResource(R.drawable.down_arrow)
            binding.departArrow.setImageResource(R.drawable.down_arrow)
            binding.cattleTypeHead.setBackgroundColor(resources.getColor(R.color.choclate))
            binding.experLayout.setBackgroundColor(resources.getColor(R.color.white))
            binding.salaryHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.locationHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.departmentHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.Wfh.setTextColor(resources.getColor(R.color.white))
            binding.freshHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.experience.setTextColor(resources.getColor(R.color.black))
            binding.salary.setTextColor(resources.getColor(R.color.black))
            binding.location.setTextColor(resources.getColor(R.color.black))
            binding.freshness.setTextColor(resources.getColor(R.color.black))
            binding.department.setTextColor(resources.getColor(R.color.black))
            binding.layoutCattleStatus.visibility = View.GONE
            binding.layoutCattleType.visibility = View.VISIBLE
            binding.layoutSalaryType.visibility = View.GONE
            binding.layoutFreshnessType.visibility = View.GONE
        }
        binding.salaryHead.setOnClickListener {

            binding.exparrow.setImageResource(R.drawable.down_arrow)
            binding.wfhArrow.setImageResource(R.drawable.down_arrow)
            binding.locArrow.setImageResource(R.drawable.down_arrow)
            binding.freshArrow.setImageResource(R.drawable.down_arrow)
            binding.departArrow.setImageResource(R.drawable.down_arrow)
            binding.salArrow.setImageResource(R.drawable.down_arrow_white)
            binding.salaryHead.setBackgroundColor(resources.getColor(R.color.choclate))
            binding.experLayout.setBackgroundColor(resources.getColor(R.color.white))
            binding.cattleTypeHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.locationHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.freshHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.departmentHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.salary.setTextColor(resources.getColor(R.color.white))
            binding.experience.setTextColor(resources.getColor(R.color.black))
            binding.location.setTextColor(resources.getColor(R.color.black))
            binding.freshness.setTextColor(resources.getColor(R.color.black))
            binding.department.setTextColor(resources.getColor(R.color.black))
            binding.Wfh.setTextColor(resources.getColor(R.color.black))
            binding.layoutCattleStatus.visibility = View.GONE
            binding.layoutCattleType.visibility = View.GONE
            binding.layoutSalaryType.visibility = View.VISIBLE
            binding.layoutFreshnessType.visibility = View.GONE
        }
        binding.locationHead.setOnClickListener {

            binding.exparrow.setImageResource(R.drawable.down_arrow)
            binding.wfhArrow.setImageResource(R.drawable.down_arrow)
            binding.salArrow.setImageResource(R.drawable.down_arrow)
            binding.locArrow.setImageResource(R.drawable.down_arrow_white)
            binding.departArrow.setImageResource(R.drawable.down_arrow)
            binding.freshArrow.setImageResource(R.drawable.down_arrow)
            binding.locationHead.setBackgroundColor(resources.getColor(R.color.choclate))
            binding.experLayout.setBackgroundColor(resources.getColor(R.color.white))
            binding.cattleTypeHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.salaryHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.freshHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.departmentHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.location.setTextColor(resources.getColor(R.color.white))
            binding.experience.setTextColor(resources.getColor(R.color.black))
            binding.Wfh.setTextColor(resources.getColor(R.color.black))
            binding.salary.setTextColor(resources.getColor(R.color.black))
            binding.freshness.setTextColor(resources.getColor(R.color.black))
            binding.department.setTextColor(resources.getColor(R.color.black))
            binding.layoutCattleStatus.visibility = View.GONE
            binding.layoutCattleType.visibility = View.GONE
            binding.layoutSalaryType.visibility = View.GONE
            binding.layoutLocationType.visibility = View.VISIBLE
            binding.layoutFreshnessType.visibility = View.GONE
        }

        binding.freshHead.setOnClickListener {

            binding.exparrow.setImageResource(R.drawable.down_arrow)
            binding.wfhArrow.setImageResource(R.drawable.down_arrow)
            binding.salArrow.setImageResource(R.drawable.down_arrow)
            binding.locArrow.setImageResource(R.drawable.down_arrow)
            binding.freshArrow.setImageResource(R.drawable.down_arrow_white)
            binding.departArrow.setImageResource(R.drawable.down_arrow)
            binding.freshHead.setBackgroundColor(resources.getColor(R.color.choclate))
            binding.experLayout.setBackgroundColor(resources.getColor(R.color.white))
            binding.cattleTypeHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.salaryHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.locationHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.departmentHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.freshness.setTextColor(resources.getColor(R.color.white))
            binding.experience.setTextColor(resources.getColor(R.color.black))
            binding.Wfh.setTextColor(resources.getColor(R.color.black))
            binding.location.setTextColor(resources.getColor(R.color.black))
            binding.salary.setTextColor(resources.getColor(R.color.black))
            binding.department.setTextColor(resources.getColor(R.color.black))
            binding.layoutCattleStatus.visibility = View.GONE
            binding.layoutCattleType.visibility = View.GONE
            binding.layoutSalaryType.visibility = View.GONE
            binding.layoutLocationType.visibility = View.GONE
            binding.layoutFreshnessType.visibility = View.VISIBLE
        }
        binding.departmentHead.setOnClickListener {

            binding.exparrow.setImageResource(R.drawable.down_arrow)
            binding.wfhArrow.setImageResource(R.drawable.down_arrow)
            binding.salArrow.setImageResource(R.drawable.down_arrow)
            binding.locArrow.setImageResource(R.drawable.down_arrow)
            binding.freshArrow.setImageResource(R.drawable.down_arrow)
            binding.departArrow.setImageResource(R.drawable.down_arrow_white)
            binding.departmentHead.setBackgroundColor(resources.getColor(R.color.choclate))
            binding.experLayout.setBackgroundColor(resources.getColor(R.color.white))
            binding.cattleTypeHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.salaryHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.locationHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.freshHead.setBackgroundColor(resources.getColor(R.color.white))
            binding.department.setTextColor(resources.getColor(R.color.white))
            binding.experience.setTextColor(resources.getColor(R.color.black))
            binding.Wfh.setTextColor(resources.getColor(R.color.black))
            binding.location.setTextColor(resources.getColor(R.color.black))
            binding.salary.setTextColor(resources.getColor(R.color.black))
            binding.freshness.setTextColor(resources.getColor(R.color.black))
            binding.layoutCattleStatus.visibility = View.GONE
            binding.layoutCattleType.visibility = View.GONE
            binding.layoutSalaryType.visibility = View.GONE
            binding.layoutLocationType.visibility = View.GONE
            binding.layoutFreshnessType.visibility = View.GONE
            binding.layoutDepartmentType.visibility = View.VISIBLE
        }
//binding.ivRefresh.setOnClickListener {
//    LocalSessionManager.removeValue(Constant.SliderStartValue,requireContext())
//    LocalSessionManager.removeValue(Constant.SliderEndValue,requireContext())
//    LocalSessionManager.removeValue(Constant.SalarySliderStartValue,requireContext())
//    LocalSessionManager.removeValue(Constant.SalarySliderEndValue,requireContext())
//    LocalSessionManager.removeValue(Constant.FilterLocation,requireContext())
//    LocalSessionManager.removeValue(Constant.DatePosted,requireContext())
//    findNavController().navigateUp()
//}
        binding.buttonDone.setOnClickListener {

            LocalSessionManager.saveValue(Constant.FillLocation,"",requireContext())
            LocalSessionManager.saveValue(Constant.DatePost,"All Jobs",requireContext())
            LocalSessionManager.removeValue(Constant.FilterLocation,requireContext())
            LocalSessionManager.removeValue(Constant.DatePosted,requireContext())
            rangeSlidersalary.setValues(0F, 30F)
            binding.expValue.setText("0 years to 30 years")
            rangeSlider.setValues(0F, 30F)
            binding.salaryValue.setText("0 Lpa to 30 Lpa")
            LocalSessionManager.saveValue(Constant.SliderStartValue,"0",requireContext())
            LocalSessionManager.saveValue(Constant.SliderEndValue,"0",requireContext())
            LocalSessionManager.saveValue(Constant.SalarySliderStartValue,"0",requireContext())
            LocalSessionManager.saveValue(Constant.SalarySliderEndValue,"0",requireContext())
            binding.tiLocationForm.setText("")
            var reset_date= "All Jobs"
            var date_pos:Int=Dateadapter.getPosition(reset_date)
            binding.tiDatePosted.setSelection(date_pos,true)

        }
        return binding.root


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

    private fun createTypeView() {

        val set: Set<*> = typeList.entries
        // Get an iterator
        // Get an iterator
        val i = set.iterator()

        while (i.hasNext()) {
            val me = i.next() as Map.Entry<*, *>
            val states = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf())
            var colors: IntArray = intArrayOf(
                resources.getColor(R.color.app_blue),
                resources.getColor(R.color.app_grey)
            )
            val face = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requireContext().resources.getFont(R.font.seogee_ui)
            } else {
                TODO("VERSION.SDK_INT < O")
                // this is must please check this ashutosh purohit 2022-04025 23:38
            }
            WfhcheckBox = CheckBox(requireContext())

            WfhcheckBox.buttonTintList = ColorStateList(states, colors)
            WfhcheckBox.id = me.key.toString().toInt()
            WfhcheckBox.text = me.value.toString()
            WfhcheckBox.setTextColor(resources.getColor(R.color.app_grey))
            WfhcheckBox.typeface = face
//            typecheckBox.textSize =
            WfhcheckBox.setOnClickListener(getOnClickCattleType(WfhcheckBox))
            binding.layoutCattleType.addView(WfhcheckBox)
        }
    }

    private fun createCattleStatusView() {
        val set: Set<*> = cattleStatusList.entries
        // Get an iterator
        // Get an iterator
        val i = set.iterator()
        while (i.hasNext()) {
            val me = i.next() as Map.Entry<*, *>
            val states = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf())
            var colors: IntArray = intArrayOf(
                resources.getColor(R.color.app_blue),
                resources.getColor(R.color.app_grey)
            )
            val face = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requireContext().resources.getFont(R.font.seogee_ui)
            } else {
                TODO("VERSION.SDK_INT < O")
                // this is must please check this ashutosh purohit 2022-04025 23:38
            }
            cattlecheckBox = CheckBox(requireContext())
            cattlecheckBox.id = me.key.toString().toInt()
            cattlecheckBox.buttonTintList = ColorStateList(states, colors)
            cattlecheckBox.setTextColor(resources.getColor(R.color.app_grey))
            cattlecheckBox.typeface = face
            cattlecheckBox.text = me.value.toString()
            cattlecheckBox.setOnClickListener(getOnClickCattleStatus(cattlecheckBox))
            binding.layoutCattleStatus.addView(cattlecheckBox)
        }
    }

    private fun createSalaryStatusView() {
        val set: Set<*> = salaryList.entries
        // Get an iterator
        // Get an iterator
        val i = set.iterator()
        while (i.hasNext()) {
            val me = i.next() as Map.Entry<*, *>
            val states = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf())
            var colors: IntArray = intArrayOf(
                resources.getColor(R.color.app_blue),
                resources.getColor(R.color.app_grey)
            )
            val face = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requireContext().resources.getFont(R.font.seogee_ui)
            } else {
                TODO("VERSION.SDK_INT < O")
                // this is must please check this ashutosh purohit 2022-04025 23:38
            }

        }
    }

    private fun createFreshnessStatusView() {
        val set: Set<*> = freshnessList.entries
        val i = set.iterator()
        while (i.hasNext()) {
            val me = i.next() as Map.Entry<*, *>
            val states = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf())
            var colors: IntArray = intArrayOf(
                resources.getColor(R.color.app_blue),
                resources.getColor(R.color.app_grey)
            )
            val face = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requireContext().resources.getFont(R.font.seogee_ui)
            } else {
                TODO("VERSION.SDK_INT < O")
                // this is must please check this ashutosh purohit 2022-04025 23:38
            }
        }
    }

    private fun createLocationStatusView() {
        val set: Set<*> = locationList.entries
        // Get an iterator
        // Get an iterator
        val i = set.iterator()
        while (i.hasNext()) {
            val me = i.next() as Map.Entry<*, *>
            val states = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf())
            var colors: IntArray = intArrayOf(
                resources.getColor(R.color.app_blue),
                resources.getColor(R.color.app_grey)
            )
            val face = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requireContext().resources.getFont(R.font.seogee_ui)
            } else {
                TODO("VERSION.SDK_INT < O")
                // this is must please check this ashutosh purohit 2022-04025 23:38
            }
        }
        val jsonFileString = getJsonDataFromAsset(requireContext(), "cities.json")
        val gson = Gson()
        val listCityType = object : TypeToken<List<CityList>>() {}.type
        var persons: List<CityList> = gson.fromJson(jsonFileString, listCityType)
        exampleCityList.add("North India")
        exampleCityList.add("South India")
        exampleCityList.add("East India")
        exampleCityList.add("West India")
        exampleCityList.add("Metro")
        exampleCityList.add("Tier 2")
        persons.forEachIndexed { idx, city ->

//             someList = person.Districtname
            val CityArray: JsonArray = JsonArray()
            val dataa: String = city.CombinedName
            CityArray.add(dataa)

            for (i in 0 until CityArray.size()) {
//                val he = CityList("","",person.CombinedName,"","")
//                exampleList.add(exampleArray.toString())
                var kuch = CityArray.get(i).toString()
                var result = kuch.substring(1, kuch.length - 1)
                exampleCityList.add(result)


            }
        }

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, exampleCityList)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tiLocationForm.setAdapter(adapter)
        binding.tiLocationForm.showDropDown()
        binding.tiLocationForm.threshold=2
        binding.tiLocationForm.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        binding.tiLocationForm.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
             location_string = parent.getItemAtPosition(position).toString()
            LocalSessionManager.saveValue(Constant.FilterLocation,location_string,requireContext())
            LocalSessionManager.saveValue(Constant.FillLocation,location_string,requireContext())

        })

        var loc=LocalSessionManager.getStringValue("FillLocation","",requireContext())
        if (loc!=null) {
            binding.tiLocationForm.setText(loc)
            LocalSessionManager.saveValue(Constant.FilterLocation,binding.tiLocationForm.text!!.toString(),requireContext())
        }

        DateList.add("All Jobs")
        DateList.add("3 days")
        DateList.add("7 days")
        DateList.add("1 month")
        DateList.add("3 month")
         Dateadapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, DateList)
        Dateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tiDatePosted.setAdapter(Dateadapter)
        binding.tiDatePosted.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val item = parent.getItemAtPosition(position)
                date_posted = item.toString()
                LocalSessionManager.saveValue(
                    Constant.DatePosted,
                    date_posted,
                    requireContext()
                )
                LocalSessionManager.saveValue(
                    Constant.DatePost,
                    date_posted,
                    requireContext()
                )


//                LocationList.add(item.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })
        var date_pos_string= LocalSessionManager.getStringValue("DatePost","",requireContext())
        if (date_pos_string != null) {
            var date_pos:Int=Dateadapter.getPosition(date_pos_string)
            binding.tiDatePosted.setSelection(date_pos,true)
        }


    }

    private fun createDepartmentStatusView() {
        val set: Set<*> = departmentList.entries
        // Get an iterator
        // Get an iterator
        val i = set.iterator()
        while (i.hasNext()) {
            val me = i.next() as Map.Entry<*, *>
            val states = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf())
            var colors: IntArray = intArrayOf(
                resources.getColor(R.color.app_blue),
                resources.getColor(R.color.app_grey)
            )
            val face = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requireContext().resources.getFont(R.font.seogee_ui)
            } else {
                TODO("VERSION.SDK_INT < O")
                // this is must please check this ashutosh purohit 2022-04025 23:38
            }
            DepartmentcheckBox = CheckBox(requireContext())
            DepartmentcheckBox.id = me.key.toString().toInt()
            DepartmentcheckBox.buttonTintList = ColorStateList(states, colors)
            DepartmentcheckBox.setTextColor(resources.getColor(R.color.app_grey))
            DepartmentcheckBox.typeface = face
            DepartmentcheckBox.text = me.value.toString()
            DepartmentcheckBox.setOnClickListener(getOnClickCattleStatus(DepartmentcheckBox))
            binding.layoutDepartmentType.addView(DepartmentcheckBox)
        }
    }

    private fun getOnClickCattleType(checkBox: CheckBox): View.OnClickListener? {
        return View.OnClickListener {

            if (checkBox.isChecked) {
                typeResult.add(checkBox.id.toString())
            } else {
                typeResult.remove(checkBox.id.toString())
            }
            System.out.println(typeResult.toString())
        }


    }


    private fun getOnClickCattleStatus(checkBox: CheckBox): View.OnClickListener? {
        return View.OnClickListener {

            if (checkBox.isChecked) {
                cattleResult.add(checkBox.id.toString())
            } else {
                cattleResult.remove(checkBox.id.toString())
            }
            System.out.println("cattle result =" + cattleResult.toString())
        }


    }
}