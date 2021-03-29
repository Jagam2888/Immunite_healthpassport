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
import androidx.recyclerview.widget.LinearLayoutManager
import com.blongho.country_data.Country
import com.blongho.country_data.World
import com.cmg.vaccine.*
import com.cmg.vaccine.adapter.CountryListRecyclerViewAdapter
import com.cmg.vaccine.databinding.FragmentCountryDialogBinding
import com.cmg.vaccine.listener.AddSelectedRVListener

class CountryListDialogFragment:DialogFragment(),AddSelectedRVListener {

    private lateinit var binding:FragmentCountryDialogBinding
    var type:String?=null
    var from:String?=null

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
        type= arguments?.getString("type")
        from= arguments?.getString("from")

        var list= World.getAllCountries()
        val arrayList = arrayListOf<Country>()
        arrayList.addAll(list)

        binding.countryRv.adapter = CountryListRecyclerViewAdapter(arrayList, this, type)
        binding.countryRv.layoutManager = LinearLayoutManager(this.context)
        binding.countryRv.setIndexBarColor(R.color.transparent)
        binding.countryRv.setIndexTextSize(12)
        binding.countryRv.setIndexBarTextColor(R.color.black)
        binding.countryRv.setIndexBarHighLateTextVisibility(true)
        binding.countryRv.setIndexBarHighLateTextVisibility(true)


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
}