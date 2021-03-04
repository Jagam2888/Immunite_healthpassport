package com.cmg.vaccine.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmg.vaccine.R
import com.cmg.vaccine.adapter.SwitchProfileAdapter
import com.cmg.vaccine.databinding.SwitchProfilePopupwindowBinding
import com.cmg.vaccine.util.RecyclerViewTouchListener
import com.cmg.vaccine.viewmodel.HomeViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.HomeViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

open class SwitchProfileFragment:BottomSheetDialogFragment(),KodeinAware {
    override val kodein by kodein()
    private lateinit var binding:SwitchProfilePopupwindowBinding
    private lateinit var viewModel:HomeViewModel

    private val factory:HomeViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setStyle(DialogFragment.STYLE_NORMAL,R.style.ModalBottomSheetDialog);
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.switch_profile_popupwindow,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View).setBackgroundColor(Color.TRANSPARENT)
        val resources = resources

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            assert(view != null)
            val parent = view?.parent as View
            val layoutParams = parent.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.setMargins(
                resources.getDimensionPixelSize(R.dimen.screen_margin), // LEFT 16dp
                0,
                resources.getDimensionPixelSize(R.dimen.screen_margin), // RIGHT 16dp
                0
            )
            parent.layoutParams = layoutParams
        }
        viewModel = ViewModelProvider(this,factory).get(HomeViewModel::class.java)

        viewModel.setUser()

        binding.imgClose.setOnClickListener {
            dismiss()
        }

        viewModel.users.observe(viewLifecycleOwner, Observer {profileList->
            binding.recyclerViewSwitchProfile.also {
                it.layoutManager = LinearLayoutManager(context)
                var itemDecoration = DividerItemDecoration(context, LinearLayout.VERTICAL)
                itemDecoration.setDrawable(context?.getDrawable(R.drawable.recyclerview_item_decoration)!!)
                it.adapter = SwitchProfileAdapter(profileList)
            }
        })

        binding.recyclerViewSwitchProfile.addOnItemTouchListener(RecyclerViewTouchListener(context,binding.recyclerViewSwitchProfile,object :RecyclerViewTouchListener.ClickListener{
            override fun onClick(view: View?, position: Int) {
                viewModel.setCurrentItem(position)
                dismiss()
            }

            override fun onLongClick(view: View?, position: Int) {
            }
        }))
    }

    /*override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)*/

}