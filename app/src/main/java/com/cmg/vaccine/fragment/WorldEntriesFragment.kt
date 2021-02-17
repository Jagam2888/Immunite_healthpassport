package com.cmg.vaccine.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.AddWorldEntryActivity
import com.cmg.vaccine.R
import com.cmg.vaccine.databinding.FragmentWorldEntriesBinding


class WorldEntriesFragment : Fragment() {

    private lateinit var binding:FragmentWorldEntriesBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_world_entries,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.imgAdd.setOnClickListener {
            Intent(context,AddWorldEntryActivity::class.java).also {
                context?.startActivity(it)
            }
        }
    }

}