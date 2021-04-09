package com.cmg.vaccine.DialogFragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.doOnLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.blongho.country_data.Country
import com.blongho.country_data.World
import com.cmg.vaccine.*
import com.cmg.vaccine.adapter.CountryListRecyclerViewAdapter
import com.cmg.vaccine.database.Countries
import com.cmg.vaccine.databinding.FragmentCountryDialogBinding
import com.cmg.vaccine.listener.AddSelectedRVListener
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.hide
import com.cmg.vaccine.util.show
import com.cmg.vaccine.util.showAlertDialog
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.CountryListFragmentViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.CountryListFragmentViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class CountryListDialogFragment:DialogFragment(),AddSelectedRVListener,KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:FragmentCountryDialogBinding
    private lateinit var viewModel:CountryListFragmentViewModel
    var type:String?=null
    var from:String?=null

    private val factory:CountryListFragmentViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.white_frame);
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_country_dialog,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //activity?.hideKeyBoard()
        viewModel = ViewModelProvider(this,factory).get(CountryListFragmentViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.listener = this

        type= arguments?.getString("type")
        from= arguments?.getString("from")

        /*var list= World.getAllCountries()
        val arrayList = arrayListOf<Country>()*/

        viewModel.countryList.observe(viewLifecycleOwner, Observer {
            var list= it
            val arrayList = arrayListOf<Countries>()
            arrayList.addAll(list)

            binding.countryRv.adapter = CountryListRecyclerViewAdapter(arrayList, this, type)
            binding.countryRv.layoutManager = LinearLayoutManager(this.context)
            binding.countryRv.setIndexBarColor(R.color.transparent)
            binding.countryRv.setIndexTextSize(12)
            binding.countryRv.setIndexBarTextColor(R.color.black)
            binding.countryRv.setIndexBarHighLateTextVisibility(true)
            binding.countryRv.setIndexBarHighLateTextVisibility(true)
        })



        binding.countrySv.doOnLayout {
            it.clearFocus()
        }

        binding.countrySv.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // worldEntryCountryListAdapter?.filter?.filter(newText)
                var adapter: CountryListRecyclerViewAdapter =
                    binding.countryRv.adapter as CountryListRecyclerViewAdapter
                adapter?.filter?.filter(newText)
                return false
            }
        })

        binding.closeBtn.setOnClickListener {
            dismiss()
        }

    }

    /*override fun setClick(selectedCountryName: String, selectedCountryCode: String) {
    }*/

    override fun onClickSelectedItem(
        selectedCountryName: String,
        selectedCountryCode: String,
        type: String
    ) {
        if(from=="add_dep")
        {
            val activity = context as AddDependentActivity
            if(type=="nation")
            {
                activity.setNation(selectedCountryCode)
            }
            if(type=="pob")
            {
                activity.setPOB(selectedCountryCode)
            }
        }else if(from == "edit_profile"){
            val activity = context as EditProfileActivity
            if(type=="nation")
            {
                activity.setNation(selectedCountryCode)
            }
            if(type=="pob")
            {
                activity.setPOB(selectedCountryCode)
            }
        }else if (from == "edit_dep"){
            val activity = context as EditDependentProfileActivity
            if(type=="nation")
            {
                activity.setNation(selectedCountryCode)
            }
            if(type=="pob")
            {
                activity.setPOB(selectedCountryCode)
            }

        }else if (from == "sign_up"){
            val activity = context as SignUpActivity
            activity.setPOB(selectedCountryCode)

        }else if (from == "tell_us"){
            val activity = context as TellUsMoreActivity
            activity.setNation(selectedCountryCode)

        }

        dismiss()
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.90).toInt()
        dialog!!.window?.setLayout(width, height)
    }

    override fun onStarted() {
        show(binding.progressBar)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressBar)
    }

    override fun onShowToast(msg: String) {
        hide(binding.progressBar)
        context?.toast(msg)
    }

    override fun onFailure(msg: String) {
        hide(binding.progressBar)
        showAlertDialog(msg, resources.getString(R.string.check_internet), false, childFragmentManager)
    }
}