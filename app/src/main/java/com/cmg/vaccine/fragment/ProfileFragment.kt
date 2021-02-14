package com.cmg.vaccine.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spanned

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmg.vaccine.*
import com.cmg.vaccine.adapter.ChildListAdapter
import com.cmg.vaccine.database.Dependent
import com.cmg.vaccine.databinding.FragmentProfileBinding
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.RecyclerViewTouchListener
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.ProfileViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.ProfileViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ProfileFragment : Fragment(),KodeinAware {

    override val kodein by kodein()
    private lateinit var binding:FragmentProfileBinding
    private lateinit var viewModel:ProfileViewModel

    private val factory:ProfileViewModelFactory by instance()

    var childAdapter:ChildListAdapter?=null
    var listChild:List<Dependent>?=null



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
        viewModel.loadParentData()

        val email = viewModel.email1.value
        val privateKey = viewModel.privateKey.value



        viewModel.dependentList.observe(viewLifecycleOwner, Observer {list ->
            binding.recyclerViewChild.also {
                it.layoutManager = LinearLayoutManager(context)
                it.adapter = ChildListAdapter(list)
            }
        })



        binding.layoutParent.setOnClickListener {
            /*Intent(context,EditProfileActivity::class.java).also {
                context?.startActivity(it)
            }*/
            Intent(context,ViewProfileActivity::class.java).also {
                it.putExtra("child_private_key","")
                it.putExtra(Passparams.USER,Passparams.PARENT)
                context?.startActivity(it)
            }
        }

        binding.btnAddDependent.setOnClickListener {
            Intent(context,AddDependentActivity::class.java).also {
                context?.startActivity(it)
            }
        }

        binding.recyclerViewChild.addOnItemTouchListener(RecyclerViewTouchListener(context,binding.recyclerViewChild,object :RecyclerViewTouchListener.ClickListener{
            override fun onClick(view: View?, position: Int) {
               /* Intent(context,EditDependentProfileActivity::class.java).also {
                    it.putExtra("child_private_key",viewModel.dependentList.value?.get(position)?.childPrivateKey)
                    startActivity(it)
                }*/
                Intent(context,ViewProfileActivity::class.java).also {
                    //it.putExtra(Passparams.PRIVATEKEY,viewModel.dependentList.value?.get(position)?.childPrivateKey)
                    it.putExtra(Passparams.PRIVATEKEY,"")
                    it.putExtra(Passparams.USER,Passparams.DEPENDENT)
                    context?.startActivity(it)
                }
            }

            override fun onLongClick(view: View?, position: Int) {
            }
        }))


        /*binding.btnPastVaccine.setOnClickListener {
            Intent(context,ChoosePastVaccinationActivity::class.java).also {
                context?.startActivity(it)
            }
        }

        binding.txtEditProfile.setOnClickListener {
            Intent(context,EditProfileActivity::class.java).also {
                context?.startActivity(it)
            }
        }

        binding.btnEmailPrivateKey.setOnClickListener {
            var htmlMsgNody:Spanned?=null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                htmlMsgNody = Html.fromHtml("<h1>This email contains a backup of your private key</h1></br><h1>Send Your Self this email to keep your backup in your safe place</h1></br></br><font color=#ff0000><h3>Your Private Key</h3><h3>$privateKey</h3></font></br></br><h1>Backup Date: ${changeDateFormatEmail(System.currentTimeMillis())}</h1></br></br><h2>Sent via Immunitee App</h2>", Html.FROM_HTML_MODE_LEGACY)
            }else{
                htmlMsgNody = Html.fromHtml("<h1>This email contains a backup of your private key</h1></br><h1>Send Your Self this email to keep your backup in your safe place</h1></br></br><font color=#ff0000><h3>Your Private Key</h3><h3>$privateKey</h3></font></br></br><h1>Backup Date: ${changeDateFormatEmail(System.currentTimeMillis())}</h1></br></br><h2>Sent via Immunitee App</h2>")
            }
            sendEmail(email!!,"Immunitee Private Key Backup",htmlMsgNody)
        }*/
    }

    private fun sendEmail(receipent:String,subject:String,msgBody:Spanned){
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "message/rfc822"
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(receipent))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mIntent.putExtra(Intent.EXTRA_TEXT, msgBody)
        //mIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail")

        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Send mail..."))
        }
        catch (e: Exception){
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
            context?.toast(e.message!!)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadChildList()
    }

}