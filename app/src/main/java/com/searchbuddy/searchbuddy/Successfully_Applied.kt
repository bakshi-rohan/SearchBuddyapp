package com.searchbuddy.searchbuddy

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.searchbuddy.R
import com.bumptech.searchbuddy.databinding.FragmentSuccessfullyAppliedBinding

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.searchbuddy.searchbuddy.Dashboard.Dashboard

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Successfully_Applied.newInstance] factory method to
 * create an instance of this fragment.
 */
class Successfully_Applied : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var binding: FragmentSuccessfullyAppliedBinding
    lateinit var bottomNavView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomNavView=  (activity as Dashboard?)!!.findViewById(R.id.nav_view)
        bottomNavView.visibility=View.GONE
        val nav: Menu = bottomNavView.menu
        var home=nav.findItem(R.id.navigation_home)
        home.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {

                activity?.onBackPressed()

                return true
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSuccessfullyAppliedBinding.inflate(inflater, container, false)

binding.btnLoginSuccess.setOnClickListener {
//    Navigation.findNavController(it).navigate(R.id.action_successfully_applied_to_nav_sales)
    activity?.onBackPressed()


}
        return binding.root
    }


}