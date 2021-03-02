package com.cmg.vaccine

import android.app.AlertDialog
import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmg.vaccine.adapter.SwitchProfileAdapter
import com.cmg.vaccine.databinding.ActivityMainBinding
import com.cmg.vaccine.fragment.*
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.RecyclerViewTouchListener
import com.cmg.vaccine.util.getDeviceUUID
import com.cmg.vaccine.viewmodel.HomeViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.HomeViewModelFactory
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        homeViewModel.setUser()

        initViews()

    }

    private fun initViews(){
        loadFragment(HomeFragment())

        val token = Paper.book().read(Passparams.FCM_TOKEN, "")
        Log.d("fcm_token", token)
        Log.d("device_id", getDeviceUUID()!!)



        bottom_navigation_view.setOnNavigationItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.home -> {
                    isHome = true
                    homeViewModel.setCurrentItem(0)
                    isShowPopWindow()
                    loadFragment(HomeFragment())
                    true
                }
                R.id.switch_profile -> {
                    if (!isHome) {
                        homeViewModel.setUser()
                        loadFragment(HomeFragment())
                    }
                    if (popupWindow != null) {
                        if (popupWindow?.isShowing == false) {
                            popUpWindow()
                        }
                    } else {
                        popUpWindow()
                    }
                    true
                }
                R.id.profile -> {
                    isHome = false
                    isShowPopWindow()
                    loadFragment(ProfileFragment())
                    true
                }
                R.id.world_entries -> {
                    isHome = false
                    isShowPopWindow()
                    loadFragment(WorldEntriesFragment())
                    true
                }
                R.id.setting -> {
                    isHome = false
                    isShowPopWindow()
                    loadFragment(SettingsFragment())
                    true
                }else -> false
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

    private fun popUpWindow(){
        val inflater:LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        // Inflate a custom view using layout inflater
        val view = inflater.inflate(R.layout.switch_profile_popupwindow, null)
        popupWindow = PopupWindow(
            view,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        popupWindow?.isOutsideTouchable = false
        //popupWindow?.dimBehind()

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width: Int = size.x

        popupWindow?.width = width - 30

        popupWindow?.elevation = 10.0F

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_switch_profile)
        val btnBack = view.findViewById<ImageView>(R.id.img_back)

        btnBack.setOnClickListener {
            popupWindow?.dismiss()
        }

        recyclerView.also {
            it.layoutManager = LinearLayoutManager(this)
            var itemDecoration = DividerItemDecoration(this, LinearLayout.VERTICAL)
            itemDecoration.setDrawable(getDrawable(R.drawable.recyclerview_item_decoration)!!)
            it.adapter = SwitchProfileAdapter(homeViewModel.users.value!!)
        }

        recyclerView.addOnItemTouchListener(
            RecyclerViewTouchListener(
                this,
                recyclerView,
                object : RecyclerViewTouchListener.ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        //binding.frameContainer.setBackgroundColor(ContextCompat.getColor(this@MainActivity,android.R.color.transparent))
                        homeViewModel.setCurrentItem(position)
                        popupWindow?.dismiss()
                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }
                })
        )

        // If API level 23 or higher then execute the code
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Create a new slide animation for popup window enter transition
            val slideIn = Slide()
            slideIn.slideEdge = Gravity.TOP
            popupWindow?.enterTransition = slideIn

            // Slide animation for popup window exit transition
            val slideOut = Slide()
            slideOut.slideEdge = Gravity.BOTTOM
            popupWindow?.exitTransition = slideOut

        }
        // Finally, show the popup window on app
        TransitionManager.beginDelayedTransition(container)
        popupWindow?.showAtLocation(
            container, // Location to display popup window
            Gravity.BOTTOM, // Exact position of layout to display popup
            0, // X offset
            binding.bottomNavigationView.height // Y offset
        )

        //binding.frameContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.dim_background))

        /*val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val windowManagerParams = view.layoutParams as WindowManager.LayoutParams
        windowManagerParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        windowManagerParams.dimAmount = 0.03f
        windowManager.updateViewLayout(view, windowManagerParams);*/
    }

    override fun onBackPressed() {
        showAlertForExit()
    }

    fun PopupWindow.dimBehind() {
        val container = contentView.rootView
        val context = contentView.context
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        //val p = container.layoutParams as WindowManager.LayoutParams
        /*val p = WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT)*/
        val p = WindowManager.LayoutParams(
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
        )
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            p.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }else{
            p.type = WindowManager.LayoutParams.TYPE_PHONE
        }*/
        p.flags = p.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.3f
        wm.addView(container, p)
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