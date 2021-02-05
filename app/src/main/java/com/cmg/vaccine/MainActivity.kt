package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.cmg.vaccine.fragment.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

    }

    private fun initViews(){

        loadFragment(HomeFragment())

        bottom_navigation_view.setOnNavigationItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.home ->{
                    loadFragment(HomeFragment())
                    true
                }
                R.id.notification -> {
                    loadFragment(NotificationFragment())
                    true
                }
                R.id.profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                R.id.view_report -> {
                    loadFragment(ViewReportFragment())
                    true
                }
                R.id.setting -> {
                    loadFragment(SettingsFragment())
                    true
                }else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_container,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        finish()
    }
}