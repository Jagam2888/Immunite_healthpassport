package com.cmg.vaccine.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.blongho.country_data.World
import com.cmg.vaccine.R
import com.cmg.vaccine.WorldEntriesDetailActivity
import com.cmg.vaccine.database.AddWorldEntries
import com.cmg.vaccine.databinding.EntriesListItemBinding
import com.cmg.vaccine.databinding.SwipeHorizontalRightBinding
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.getTwoAlpha
import com.cmg.vaccine.viewmodel.WorldEntryViewModel
import com.jdev.countryutil.Country
import com.tubb.smrv.SwipeHorizontalMenuLayout
import com.tubb.smrv.SwipeMenuLayout
import com.tubb.smrv.listener.SwipeSwitchListener
import java.util.*
import kotlin.collections.ArrayList

class WorldEntriesAdapter(
    private val countryList:ArrayList<AddWorldEntries>,
    private val viewModel: WorldEntryViewModel,
    private val context: Context
):RecyclerView.Adapter<WorldEntriesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.swipe_horizontal_right,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //holder.swipeHorizontalRightBinding.worldentries = countryList.get(position)
        holder.swipeHorizontalRightBinding.smContentView.worldentries = countryList[position]

        //val flag = World.getFlagOf(countryList.get(position).countryCodeAlpha)
        val flag = Country.getCountryByISO(getTwoAlpha(countryList[position].countryCodeAlpha!!)).flag
        holder.swipeHorizontalRightBinding.smContentView.imgFlag.setImageResource(flag)


        if ((viewModel.vaccineList.value?.isEmpty() == true) and (viewModel.testReportList.value?.isEmpty() == true)){
            holder.swipeHorizontalRightBinding.smContentView.statusIndicator.setImageResource(R.drawable.red_indicator)
        }else if ((viewModel.vaccineList.value?.isNotEmpty() == true) and (viewModel.testReportList.value?.isNotEmpty() == true)){
            holder.swipeHorizontalRightBinding.smContentView.statusIndicator.setImageResource(R.drawable.green_indicator)
        }else if ((viewModel.vaccineList.value?.isNotEmpty() == true) and (viewModel.testReportList.value?.isEmpty() == true)){
            holder.swipeHorizontalRightBinding.smContentView.statusIndicator.setImageResource(R.drawable.green_indicator)
        }else if ((viewModel.vaccineList.value?.isEmpty() == true) and (viewModel.testReportList.value?.isNotEmpty() == true)){
            holder.swipeHorizontalRightBinding.smContentView.statusIndicator.setImageResource(R.drawable.yellow_indicator)
        }

        holder.swipeHorizontalRightBinding.sml.setSwipeListener(object :SwipeSwitchListener{
            override fun beginMenuClosed(swipeMenuLayout: SwipeMenuLayout?) {
            }

            override fun beginMenuOpened(swipeMenuLayout: SwipeMenuLayout?) {

            }

            override fun endMenuClosed(swipeMenuLayout: SwipeMenuLayout?) {
                holder.swipeHorizontalRightBinding.smContentView.entriesContainer.setBackgroundResource(R.drawable.gray_background_outline)
            }

            override fun endMenuOpened(swipeMenuLayout: SwipeMenuLayout?) {
                holder.swipeHorizontalRightBinding.smContentView.entriesContainer.setBackgroundResource(R.drawable.gray_background_outline_swipe)
            }
        })

        holder.swipeHorizontalRightBinding.smMenuViewRight.setOnClickListener {
            showAlertForDelete(position,holder.swipeHorizontalRightBinding.sml)
        }


        holder.swipeHorizontalRightBinding.sml.setOnClickListener {
            if (!holder.swipeHorizontalRightBinding.sml.isMenuOpen){
                Intent(context,WorldEntriesDetailActivity::class.java).also {
                    it.putExtra(Passparams.WORLD_ENTRY_SELECTED_COUNTRY_CODE, countryList[position].countryCodeAlpha)
                    context?.startActivity(it)
                }
            }
        }

    }

    override fun getItemCount() = countryList.size

    inner class MyViewHolder(
        val swipeHorizontalRightBinding: SwipeHorizontalRightBinding
    ):RecyclerView.ViewHolder(swipeHorizontalRightBinding.root)

    private fun removeItem(position: Int){
        viewModel.deleteWorldEntries(countryList.get(position).countryName!!)
        countryList.removeAt(position)
        notifyDataSetChanged()
    }

    private fun showAlertForDelete(pos:Int,view:SwipeHorizontalMenuLayout){
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setMessage("Are you sure you want to Delete?")
                .setTitle(context.resources.getString(R.string.app_name)).setCancelable(false).setPositiveButton(
                        "DELETE"
                ) { dialog, which ->
                    removeItem(pos)
                view.smoothCloseEndMenu()

                }.setNegativeButton(
                        "CANCEL"
                ) {
                    dialog, which -> dialog?.dismiss()
                    view.smoothCloseEndMenu()
                }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun onRowMoved(fromPosition: Int, toPosition: Int) {
        // var tempList=countryList
        //Collections.swap(countryList, fromPosition, toPosition )
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(countryList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(countryList, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        updateOrder()
    }

    private fun updateOrder()
    {
        var array=ArrayList<AddWorldEntries>()
        var data: AddWorldEntries? =null


        for( i in countryList.indices)
        {
            viewModel.changeOrder(countryList.get(i).countryName.toString(),i)
        }

    }
}