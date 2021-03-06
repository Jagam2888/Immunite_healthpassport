package com.cmg.vaccine

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmg.vaccine.DialogFragment.PopUpDialogFragment
import com.cmg.vaccine.adapter.SwitchProfileAdapter
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityMainBinding
import com.cmg.vaccine.fragment.*
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.HomeViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.HomeViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import immuniteeEncryption.EncryptionUtils
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class MainActivity : BaseActivity(),KodeinAware {
    override val kodein by kodein()
    private lateinit var homeViewModel:HomeViewModel
    private lateinit var binding:ActivityMainBinding

    private val factory:HomeViewModelFactory by instance()
    var isHome:Boolean = true
    var popupWindow:PopupWindow?=null
    var switchProfilePos:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        homeViewModel.setUser()


        initViews()

    }



    private fun initViews(){

        val isFromChangeLanguage = Paper.book().read<Boolean>(Passparams.ISFROMCHANGELANGUAGE,false)
        if (!isFromChangeLanguage) {
            loadFragment(HomeFragment())
            PopUpDialogFragment().show(supportFragmentManager,"Popup")
        }else{
            Paper.book().write(Passparams.ISFROMCHANGELANGUAGE,false)
            loadFragment(SettingsFragment())
            showAlertDialog(resources.getString(R.string.language),resources.getString(R.string.change_language_success),true,supportFragmentManager)
        }

        val token = Paper.book().read(Passparams.FCM_TOKEN, "")
        Log.d("fcm_token", token)
        Log.d("device_id", getDeviceUUID()!!)





        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.home -> {
                    isHome = true
                    switchProfilePos = 0
                    homeViewModel.setCurrentItem(switchProfilePos)
                    //isShowPopWindow()
                    loadFragment(HomeFragment())
                    true
                }
                R.id.switch_profile -> {
                    if (!isHome) {
                        homeViewModel.setUser()
                        //homeViewModel.setCurrentItem(0)
                        loadFragment(HomeFragment())
                    }
                    /*if (popupWindow != null) {
                        if (popupWindow?.isShowing == false) {
                            popUpWindow()
                        }
                    } else {
                        popUpWindow()
                    }*/
                    /*val bottomSheet = SwitchProfileFragment()
                    bottomSheet.show(supportFragmentManager,"switch")*/
                    homeViewModel.setCurrentItem(switchProfilePos)
                    bottomDialog()
                    true
                }
                /*R.id.profile -> {
                    isHome = false
                    //isShowPopWindow()
                    loadFragment(ProfileFragment())
                    true
                }*/
                R.id.world_entries -> {
                    isHome = false
                    //isShowPopWindow()
                    loadFragment(WorldEntriesFragment())
                    true
                }
                R.id.setting -> {
                    isHome = false
                    //isShowPopWindow()
                    loadFragment(SettingsFragment())
                    true
                }else -> false
            }
        }

        /*binding.qrFab.setOnClickListener {
            Paper.book().write(Passparams.QR_CODE_VALUE, "")
            Intent(this,ScanQRActivity::class.java).also {
                it.putExtra(Passparams.NAVIGATE_TO,Passparams.DEPARTURE_VERIFICATION)
                startActivity(it)
            }
        }*/

        binding.qrFab.setOnSingleClickListener {
            Paper.book().write(Passparams.QR_CODE_VALUE, "")
            Intent(this,ScanQRActivity::class.java).also {
                it.putExtra(Passparams.NAVIGATE_TO,Passparams.DEPARTURE_VERIFICATION)
                startActivity(it)
            }
        }


    }

    private fun isShowPopWindow(){
        if (popupWindow != null){
            if (popupWindow?.isShowing == true){
                popupWindow?.dismiss()
            }
        }
    }


    private fun loadFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun showAlertForExit(){
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Are you sure you want to Exit?")
            .setTitle(resources.getString(R.string.app_name)).setCancelable(false).setPositiveButton(
                        "YES"
                ) { dialog, which ->
                finish()

            }.setNegativeButton(
                        "CANCEL"
                ) { dialog, which -> dialog?.dismiss() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun bottomDialog(){
        var switchAdapter:SwitchProfileAdapter?=null
        val bottomSheet = layoutInflater.inflate(R.layout.switch_profile_popupwindow, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheet)

        val recyclerView = bottomSheet.findViewById<RecyclerView>(R.id.recycler_view_switch_profile)
        val btnBack = bottomSheet.findViewById<ImageView>(R.id.img_close)

        recyclerView.also {
            it.layoutManager = LinearLayoutManager(this)
            var itemDecoration = DividerItemDecoration(this, LinearLayout.VERTICAL)
            itemDecoration.setDrawable(getDrawable(R.drawable.recyclerview_item_decoration)!!)
            switchAdapter = SwitchProfileAdapter(homeViewModel.users.value!!)
            it.adapter = switchAdapter
        }

        recyclerView.addOnItemTouchListener(
                RecyclerViewTouchListener(
                        this,
                        recyclerView,
                        object : RecyclerViewTouchListener.ClickListener {
                            override fun onClick(view: View?, position: Int) {
                                switchProfilePos = position
                                homeViewModel.setCurrentItem(switchProfilePos)
                                bottomSheetDialog.dismiss()
                            }

                            override fun onLongClick(view: View?, position: Int) {

                            }
                        })
        )

        btnBack.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()

        homeViewModel.currentPagerPosition.observe(this, Observer {
            switchAdapter?.changeItem(it)
            Log.d("current_pos", it.toString())
        })
    }

    override fun onBackPressed() {
        showAlertForExit()
    }

}