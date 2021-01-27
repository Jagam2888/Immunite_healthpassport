package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivityTellUsMoreBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.hide
import com.cmg.vaccine.util.show
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.TellUsMoreViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.TellUsViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class TellUsMoreActivity : AppCompatActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityTellUsMoreBinding
    private lateinit var viewModel:TellUsMoreViewModel
    private val factory:TellUsViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_tell_us_more)
        viewModel = ViewModelProvider(this,factory).get(TellUsMoreViewModel::class.java)
        binding.tellusviewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.listener = this

        /*binding.btnMale.setOnClickListener {
            binding.btnMale.setBackgroundResource(R.drawable.male_selected)
            binding.btnFemale.setBackgroundResource(R.drawable.female_unselected)
            binding.btnOther.setBackgroundResource(R.drawable.other_unselected)
        }

        binding.btnFemale.setOnClickListener {
            binding.btnFemale.setBackgroundResource(R.drawable.female_selected)
            binding.btnMale.setBackgroundResource(R.drawable.male_unselected)
            binding.btnOther.setBackgroundResource(R.drawable.other_unselected)
        }

        binding.btnOther.setOnClickListener {
            binding.btnFemale.setBackgroundResource(R.drawable.female_unselected)
            binding.btnMale.setBackgroundResource(R.drawable.male_unselected)
            binding.btnOther.setBackgroundResource(R.drawable.other_selected)
        }*/

        /*binding.btnSignup.setOnClickListener {
            Intent(this,SignupCompleteActivity::class.java).also {
                startActivity(it)
            }
        }*/
    }

    override fun onStarted() {
        show(binding.progressTellus)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressTellus)
        toast(msg)
        Intent(this,SignupCompleteActivity::class.java).also {
            startActivity(it)
        }
    }

    override fun onFailure(msg: String) {
        hide(binding.progressTellus)
        toast(msg)
    }
}