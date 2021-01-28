package com.cmg.vaccine.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmg.vaccine.FAQTravelAdvisoryActivity
import com.cmg.vaccine.R
import com.cmg.vaccine.ViewPrivateKeyActivity
import com.cmg.vaccine.adapter.HomeListAdapter
import com.cmg.vaccine.databinding.FragmentHomeBinding
import com.cmg.vaccine.model.response.HomeResponse
import com.cmg.vaccine.viewmodel.HomeViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.HomeViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(),KodeinAware {
    override val kodein by kodein()
    private lateinit var binding:FragmentHomeBinding
    private lateinit var viewModel:HomeViewModel
    var list:List<HomeResponse>?=null

    private val factory:HomeViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        //initViews()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this,factory).get(HomeViewModel::class.java)
        binding.homeviewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.loadVaccineDetail()

        /*val home = HomeResponse()
        home.title = "Vaccine ABC"
        home.date = "19/01/2021"

        val home1 = HomeResponse()
        home1.title = "Vaccine ABC"
        home1.date = "19/01/2021"

        val home2 = HomeResponse()
        home2.title = "Vaccine ABC"
        home2.date = "19/01/2021"

        list = listOf(home,home1,home2)
        viewModel.setList(list!!)

        viewModel.list.observe(viewLifecycleOwner, Observer { list ->
            binding.recyclerView.also {
                it.layoutManager = LinearLayoutManager(context)
                it.adapter = HomeListAdapter(list)
            }
        })*/

        binding.btnViewKey.setOnClickListener {
            Intent(context,ViewPrivateKeyActivity::class.java).also {
                context?.startActivity(it)
            }
        }

        binding.btnFaq.setOnClickListener {
            Intent(context,FAQTravelAdvisoryActivity::class.java).also {
                context?.startActivity(it)
            }
        }
    }

   /* fun initViews(){
        binding.btnViewKey.setOnClickListener {
            Intent(context,ViewPrivateKeyActivity::class.java).also {
                context?.startActivity(it)
            }
        }
    }*/

}