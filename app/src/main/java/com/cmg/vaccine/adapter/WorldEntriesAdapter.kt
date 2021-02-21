package com.cmg.vaccine.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmg.vaccine.R
import com.cmg.vaccine.WorldEntriesDetailActivity
import com.cmg.vaccine.database.AddWorldEntries
import com.cmg.vaccine.databinding.EntriesListItemBinding
import com.cmg.vaccine.databinding.SwipeHorizontalRightBinding
import com.cmg.vaccine.viewmodel.WorldEntryViewModel
import com.tubb.smrv.SwipeHorizontalMenuLayout
import com.tubb.smrv.SwipeMenuLayout
import com.tubb.smrv.listener.SwipeSwitchListener

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
        holder.swipeHorizontalRightBinding.smContentView.worldentries = countryList.get(position)

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

                }.setNegativeButton(
                        "CANCEL"
                ) {
                    dialog, which -> dialog?.dismiss()
                    view.smoothCloseEndMenu()
                }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}