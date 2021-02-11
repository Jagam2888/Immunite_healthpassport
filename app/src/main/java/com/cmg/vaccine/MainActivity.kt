package com.cmg.vaccine

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
                /*R.id.notification -> {
                    loadFragment(NotificationFragment())
                    true
                }*/
                R.id.profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                R.id.world_entries -> {
                    loadFragment(WorldEntriesFragment())
                    true
                }
                R.id.setting -> {
                    loadFragment(SettingsFragment())
                    true
                }else -> false
            }
        }
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflator = menuInflater
        inflator.inflate(R.menu.home_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }*/

    private fun loadFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_container,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun showAlertForExit(){
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Are you sure you want to Exit?")
            .setTitle(resources.getString(R.string.app_name)).setCancelable(false).setPositiveButton("YES"
            ) { dialog, which ->
                finish()

            }.setNegativeButton("CANCEL"
            ) { dialog, which -> dialog?.dismiss() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onBackPressed() {
        showAlertForExit()
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.notification ->{
                loadFragment(NotificationFragment())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }*/
}