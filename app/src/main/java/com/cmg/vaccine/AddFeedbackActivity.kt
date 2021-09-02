package com.cmg.vaccine

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.DocumentsContract.*
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.cmg.vaccine.adapter.CustomUploadFileAdapter
import com.cmg.vaccine.data.MultipleFilesData
import com.cmg.vaccine.data.UserProfileList
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityAddFeedbackBinding
import com.cmg.vaccine.listener.AdapterListener
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.FeedBackViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.FeedBackViewModelFactory
import id.zelory.compressor.Compressor
import immuniteeEncryption.EncryptionUtils
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.text.DecimalFormat

class AddFeedbackActivity : BaseActivity(),KodeinAware,SimpleListener,AdapterListener {

    override val kodein by kodein()
    private lateinit var binding:ActivityAddFeedbackBinding
    private lateinit var viewModel:FeedBackViewModel

    private val factory:FeedBackViewModelFactory by instance()

    private var listOfFiles:ArrayList<MultipleFilesData>?=null
    var clickPosition:Int = 0

    private lateinit var customAdapter:CustomUploadFileAdapter

    companion object{
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this,factory).get(FeedBackViewModel::class.java)

        binding.viewmodel = viewModel

        viewModel.listener = this

        viewModel.ratings.set(binding.ratingBar.count)

        binding.ratingBar.setOnRatingChangeListener { ratingBar, i, i2 ->
            viewModel.ratings.set(ratingBar.count)
        }

        viewModel.userProfileList.observe(this, Observer {list->
            val adapter = ArrayAdapter<UserProfileList>(this,android.R.layout.simple_list_item_1,android.R.id.text1,list)
            binding.spinnerProfile.adapter = adapter
            setSpinnerValue(0)
        })


        binding.spinnerProfile.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                setSpinnerValue(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }





        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->

            val radioButton = findViewById<RadioButton>(checkedId)
            viewModel.feedbackCategory.set(radioButton.text.toString())
            when(checkedId){
                R.id.radio_btn_account ->{
                    if (binding.layoutRadioGroup.visibility == View.VISIBLE)
                        binding.layoutRadioGroup.visibility = View.GONE

                    if (binding.layoutExperience.visibility == View.GONE)
                        binding.layoutExperience.visibility = View.VISIBLE
                }
                R.id.radio_btn_health ->{
                    if (binding.layoutRadioGroup.visibility == View.VISIBLE)
                        binding.layoutRadioGroup.visibility = View.GONE

                    if (binding.layoutExperience.visibility == View.GONE)
                        binding.layoutExperience.visibility = View.VISIBLE
                }
                R.id.radio_btn_others ->{
                    if (binding.layoutRadioGroup.visibility == View.VISIBLE)
                        binding.layoutRadioGroup.visibility = View.GONE

                    if (binding.layoutExperience.visibility == View.GONE)
                        binding.layoutExperience.visibility = View.VISIBLE
                }
                R.id.radio_btn_profile ->{
                    if (binding.layoutRadioGroup.visibility == View.VISIBLE)
                        binding.layoutRadioGroup.visibility = View.GONE

                    if (binding.layoutExperience.visibility == View.GONE)
                        binding.layoutExperience.visibility = View.VISIBLE
                }
                R.id.radio_btn_world ->{
                    if (binding.layoutRadioGroup.visibility == View.VISIBLE)
                        binding.layoutRadioGroup.visibility = View.GONE

                    if (binding.layoutExperience.visibility == View.GONE)
                        binding.layoutExperience.visibility = View.VISIBLE
                }
                else ->{
                    if (binding.layoutRadioGroup.visibility == View.GONE)
                        binding.layoutRadioGroup.visibility = View.VISIBLE

                    if (binding.layoutExperience.visibility == View.VISIBLE)
                        binding.layoutExperience.visibility = View.GONE
                }
            }
        }

        binding.imgBack.setOnClickListener {
            if (binding.layoutExperience.visibility == View.VISIBLE){
                binding.layoutExperience.visibility = View.GONE
                binding.layoutRadioGroup.visibility = View.VISIBLE
            }else{
                finish()
            }
        }

        listOfFiles = ArrayList<MultipleFilesData>()

        listOfFiles!!.add(MultipleFilesData(
            "",""
        ))




        customAdapter = CustomUploadFileAdapter(this,listOfFiles!!)
        binding.listview.adapter = customAdapter
        customAdapter.listener = this


        binding.addDocument.setOnSingleClickListener{
            if (listOfFiles!!.size < 5) {

                listOfFiles!!.add(MultipleFilesData(
                    "",""
                ))
                Log.d("list_size_after",listOfFiles!!.size.toString())
                customAdapter.notifyDataSetChanged()
            }else{
                toast("Sorry, Already you have reached your attachment limit")
            }
        }
    }

    private fun setSpinnerValue(position:Int){
        viewModel.caseDob.set(viewModel.userProfileList.value?.get(position)?.dob)
        viewModel.caseEncryptPk.set("")
        if (!viewModel.userProfileList.value?.get(position)?.privateKey.isNullOrEmpty()) {
            val encryptpK = EncryptionUtils.encryptForBackup(viewModel.userProfileList.value?.get(position)?.privateKey,changeDateFormatOnlyDateReverse(viewModel.caseDob.get()!!))
            val trimValue = encryptpK.replace("\n","")
            viewModel.caseEncryptPk.set(trimValue)
        }else{
            toast("Your Key is InProgress, Please check MyKey")
        }

        viewModel.caseSubId.set(viewModel.userProfileList.value?.get(position)?.subId)

    }

    private fun openFile() {
        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type = "image/*|application/pdf"
            val mimeTYpe = arrayOf("image/*","application/pdf")
            it.putExtra(Intent.EXTRA_MIME_TYPES,mimeTYpe)
            startActivityForResult(it, IMAGE_PICK_CODE)
        }
    }

    private fun checkPermission():Boolean{
        return ((ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED))
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            PERMISSION_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == IMAGE_PICK_CODE) and (resultCode == Activity.RESULT_OK)) {
            if (data == null)
                return

            try {
                val uri = data.data
                val filePath = getFileFromUri(uri!!)

                Log.d("file_path",filePath.absolutePath)
                Log.d("file_path_name",filePath.name)
                if (getFIleSize(filePath) <= 5) {
                    if (filePath.name.contains(".pdf", true)) {
                        viewModel.feedbackFile.set(filePath.name)
                        viewModel.filePath.value = filePath.absolutePath

                        val multipleFilesData = MultipleFilesData(
                            filePath.name,filePath.absolutePath
                        )
                        listOfFiles!![clickPosition] = multipleFilesData
                        Log.d("list_size",listOfFiles!!.size.toString())
                        customAdapter.notifyDataSetChanged()

                        //viewModel._filePathList.value?.clear()
                        viewModel._filePathList.value = listOfFiles

                    } else {
                        compressImage(filePath)
                    }
                }else{
                    toast("Sorry, Your File size exceeded 5MB")
                }
            }catch (e:Exception){
                e.printStackTrace()
                toast("Sorry!,this folder file can't access")
            }

        }
    }

    private fun compressImage(actualImage:File) {
        actualImage?.let { imageFile ->
            lifecycleScope.launch {
                // Default compression
                val compressImage = Compressor.compress(this@AddFeedbackActivity, imageFile)
                viewModel.feedbackFile.set(compressImage.name)
                viewModel.filePath.value = compressImage.absolutePath
                //viewModel._filePathList.value?.add(compressImage.absolutePath)
                Log.d("file_com",compressImage.absolutePath)
                Log.d("file_com_name",compressImage.name)
                val multipleFilesData = MultipleFilesData(
                    compressImage.name,compressImage.absolutePath
                )
                listOfFiles!![clickPosition] = multipleFilesData

                customAdapter.notifyDataSetChanged()
                Log.d("list_size",listOfFiles!!.size.toString())
                //viewModel._filePathList.value?.clear()
                viewModel._filePathList.value = listOfFiles

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE ->{
                if (grantResults.isNotEmpty()) {
                    val locationAccepted: Boolean =
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (locationAccepted) {
                        openFile()
                    } else {
                        toast("Permission Denied, You cannot get Location")
                    }
                }
            }
        }
    }




    override fun onStarted() {
        show(binding.progressCircular)
    }

    override fun onSuccess(msg: String) {
        //toast(msg)
        hide(binding.progressCircular)
        Intent(this,FeedbackSuccessActivity::class.java).also {
            startActivity(it)
        }
        finish()
        //showAlertDialogWithClick(resources.getString(R.string.success),msg,true,true,supportFragmentManager)
    }

    override fun onFailure(msg: String) {
        toast(msg)
        hide(binding.progressCircular)
    }

    override fun onShowToast(msg: String) {
        toast(msg)
        hide(binding.progressCircular)
    }

    override fun onClick(position: Int) {
        if (checkPermission()){
            clickPosition = position
            openFile()
        }else{
            requestPermission()
        }
        //toast(position.toString())
    }

    override fun onBackPressed() {
        if (binding.layoutExperience.visibility == View.VISIBLE){
            binding.layoutExperience.visibility = View.GONE
            binding.layoutRadioGroup.visibility = View.VISIBLE
        }else{
            finish()
        }
    }
}