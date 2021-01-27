package com.cmg.vaccine.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.ChoosePastVaccinationActivity
import com.cmg.vaccine.EditProfileActivity
import com.cmg.vaccine.R
import com.cmg.vaccine.databinding.FragmentProfileBinding
import com.cmg.vaccine.util.isValidEmail
import com.cmg.vaccine.viewmodel.ProfileViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.ProfileViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment(),KodeinAware {

    override val kodein by kodein()
    private lateinit var binding:FragmentProfileBinding
    private lateinit var viewModel:ProfileViewModel

    private val factory:ProfileViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this,factory).get(ProfileViewModel::class.java)
        binding.profileviewmodel = viewModel
        binding.lifecycleOwner = this
        viewModel.loadData()

        binding.btnPastVaccine.setOnClickListener {
            Intent(context,ChoosePastVaccinationActivity::class.java).also {
                context?.startActivity(it)
            }
        }

        binding.txtEditProfile.setOnClickListener {
            Intent(context,EditProfileActivity::class.java).also {
                context?.startActivity(it)
            }
        }
    }

}