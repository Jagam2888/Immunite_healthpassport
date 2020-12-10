package com.cmg.vaccine.tracker

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cmg.vaccine.R

class UIDTrackerFragment : Fragment() {

    companion object {
        fun newInstance() = UIDTrackerFragment()
    }

    private lateinit var viewModel: UIDTrackerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.uid_tracker_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UIDTrackerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}