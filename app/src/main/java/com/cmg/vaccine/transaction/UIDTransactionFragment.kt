package com.cmg.vaccine.transaction

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cmg.vaccine.R

class UIDTransactionFragment : Fragment() {

    companion object {
        fun newInstance() = UIDTransactionFragment()
    }

    private lateinit var viewModel: UIDTransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.uid_transaction_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UIDTransactionViewModel::class.java)
        // TODO: Use the ViewModel
    }

}