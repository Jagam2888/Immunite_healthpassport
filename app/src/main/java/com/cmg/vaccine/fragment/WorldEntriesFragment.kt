package com.cmg.vaccine.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmg.vaccine.AddWorldEntryActivity
import com.cmg.vaccine.R
import com.cmg.vaccine.adapter.CountryListAdapter
import com.cmg.vaccine.adapter.WorldEntriesAdapter
import com.cmg.vaccine.database.AddWorldEntries
import com.cmg.vaccine.databinding.FragmentWorldEntriesBinding
import com.cmg.vaccine.util.RecyclerViewTouchListener
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.WorldEntryViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.WorldEntryViewModelFactory
import kotlinx.android.synthetic.main.swipe_right.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class WorldEntriesFragment : Fragment(),KodeinAware {

    override val kodein by kodein()
    private lateinit var binding:FragmentWorldEntriesBinding
    private lateinit var viewModel:WorldEntryViewModel

    private val factory:WorldEntryViewModelFactory by instance()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_world_entries,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this,factory).get(WorldEntryViewModel::class.java)
        binding.viewmodel = viewModel

        binding.lifecycleOwner = this





        viewModel.worldEntriesList.observe(viewLifecycleOwner, Observer { list ->
            binding.recyclerViewWorldEntry.also {
                it.layoutManager = LinearLayoutManager(context)
                it.adapter = WorldEntriesAdapter(list,viewModel,requireContext())
            }
        })

        binding.imgAdd.setOnClickListener {
            Intent(context,AddWorldEntryActivity::class.java).also {
                context?.startActivity(it)
            }
        }

       /* binding.recyclerViewWorldEntry.addOnItemTouchListener(RecyclerViewTouchListener(context,binding.recyclerViewWorldEntry,object :RecyclerViewTouchListener.ClickListener{
            override fun onClick(view: View?, position: Int) {

            }

            override fun onLongClick(view: View?, position: Int) {
            }
        }))*/
    }

    fun deleteEntries(countryName:String){
        viewModel.deleteWorldEntries(countryName)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadWorldEntriesData()
    }
}