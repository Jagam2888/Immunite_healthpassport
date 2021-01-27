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
import com.cmg.vaccine.R
import com.cmg.vaccine.ViewReportDetailActivity
import com.cmg.vaccine.adapter.ViewReportListAdapter
import com.cmg.vaccine.databinding.FragmentViewReportBinding
import com.cmg.vaccine.model.response.ViewReport
import com.cmg.vaccine.util.RecyclerViewTouchListener
import com.cmg.vaccine.viewmodel.ViewReportListViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewReportFragment : Fragment() {

    private lateinit var binding:FragmentViewReportBinding
    private lateinit var viewModel:ViewReportListViewModel
    var list:List<ViewReport>?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_view_report,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ViewReportListViewModel::class.java)

        val viewReport1 = ViewReport()
        viewReport1.title = "Covid-19 Vaccine"
        viewReport1.status = "EXPIRING"
        viewReport1.date = "20/02/2021"
        viewReport1.manufacture = "ABC"
        viewReport1.clinic = "Pantai Hospital"

        val viewReport2 = ViewReport()
        viewReport2.title = "Covid-19 Vaccine"
        viewReport2.status = "NEXT"
        viewReport2.date = "20/02/2021"
        viewReport2.manufacture = "ABC"
        viewReport2.clinic = "KPJ Hospital"

        val viewReport3 = ViewReport()
        viewReport3.title = "Covid-19 Swap Test"
        viewReport3.status = "Completed"
        viewReport3.date = "20/10/2020"
        viewReport3.manufacture = "ABC"
        viewReport3.clinic = "KPJ Hospital"

        list = listOf(viewReport1,viewReport2,viewReport3)

        viewModel.setViewReport(list!!)

        viewModel.viewReport.observe(viewLifecycleOwner, Observer {viewReportList ->

            binding.recyclerviewViewReport.also {
                it.layoutManager = LinearLayoutManager(context)
                it.adapter = ViewReportListAdapter(viewReportList,requireContext())
            }
        })

        binding.recyclerviewViewReport.addOnItemTouchListener(RecyclerViewTouchListener(requireContext(),binding.recyclerviewViewReport,object : RecyclerViewTouchListener.ClickListener{
            override fun onClick(view: View?, position: Int) {
               Intent(requireContext(),ViewReportDetailActivity::class.java).also {
                   startActivity(it)
               }
            }

            override fun onLongClick(view: View?, position: Int) {
            }
        }))

    }
}