package com.cmg.vaccine.fragment

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmg.vaccine.AddWorldEntryActivity
import com.cmg.vaccine.R
import com.cmg.vaccine.adapter.WorldEntriesAdapter
import com.cmg.vaccine.database.AddWorldEntries
import com.cmg.vaccine.databinding.FragmentWorldEntriesBinding
import com.cmg.vaccine.viewmodel.WorldEntryViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.WorldEntryViewModelFactory
import me.rishabhkhanna.recyclerswipedrag.RecyclerHelper
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class WorldEntriesFragment : Fragment(),KodeinAware {

    override val kodein by kodein()
    private lateinit var binding:FragmentWorldEntriesBinding
    private lateinit var viewModel:WorldEntryViewModel
    var worldEntriesAdapter:WorldEntriesAdapter?=null

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


        viewModel.getVaccineAndTestReportList()
        var lastClickTime:Long = 0


        viewModel.worldEntriesList.observe(viewLifecycleOwner, Observer { list ->
            worldEntriesAdapter = WorldEntriesAdapter(list,viewModel,requireContext())
            binding.recyclerViewWorldEntry.also {
                it.layoutManager = LinearLayoutManager(context)
                it.adapter = worldEntriesAdapter
            }
            val touchHelper: RecyclerHelper<AddWorldEntries> = RecyclerHelper<AddWorldEntries>(
                    list,
                    binding.recyclerViewWorldEntry.adapter!!
            )
        })

        val simpleCallback: ItemTouchHelper.SimpleCallback =
                object : ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.UP or ItemTouchHelper.DOWN ,
                        0
                ) {
                    override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
                    ): Boolean {
                        worldEntriesAdapter?.onRowMoved(viewHolder.adapterPosition,target.adapterPosition)
                        return false
                    }

                    override fun clearView(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder
                    ) {
                        worldEntriesAdapter?.notifyDataSetChanged()
                        super.clearView(recyclerView, viewHolder)
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    }

                }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewWorldEntry)

        worldEntriesAdapter?.notifyDataSetChanged()

        binding.imgAdd.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickTime<1000){
                return@setOnClickListener
            }
            Log.d("onclick","come here")
            lastClickTime = SystemClock.elapsedRealtime()
            Intent(context,AddWorldEntryActivity::class.java).also {
                context?.startActivity(it)
            }
        }
    }

    fun deleteEntries(countryName:String){
        viewModel.deleteWorldEntries(countryName)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadWorldEntriesData()
    }
}