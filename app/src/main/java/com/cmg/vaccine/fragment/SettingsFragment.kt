package com.cmg.vaccine.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.*
import com.cmg.vaccine.DialogFragment.BackupSuccessDialogFragment
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.FragmentSettingsBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.services.DriveServiceHelper
import com.cmg.vaccine.services.NotificationService
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.SettingsViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.SettingsModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.zcw.togglebutton.ToggleButton.OnToggleChanged
import immuniteeEncryption.EncryptionUtils
import io.paperdb.Paper
import kotlinx.android.synthetic.main.about.*
import kotlinx.android.synthetic.main.backup.*
import kotlinx.android.synthetic.main.change_language.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.layout_backup
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.android.synthetic.main.help.*
import kotlinx.android.synthetic.main.legacy_documents.*
import kotlinx.android.synthetic.main.notification.*
import kotlinx.android.synthetic.main.security_pin.*
import kotlinx.android.synthetic.main.sync.*
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class SettingsFragment : Fragment(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:FragmentSettingsBinding
    private lateinit var viewModel:SettingsViewModel

    private val factory:SettingsModelFactory by instance()
    private val GOOGLE_DRIVE_DB_LOCATION = "db"
    private lateinit var driveServiceHelper: DriveServiceHelper
    var isGoogleSiginSuccess:Boolean = false
    var dateOfBirth:String?=null

    companion object{
        var resultTitle: String? = null
        var resultInfo: String? = null
        var resultExtra: String? = null
        const val REQUEST_CODE_SIGN_IN = 400
        var FILE_NAME:String="/immunitees.xls"

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(SettingsViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.listener = this

        dateOfBirth = viewModel.dob.get()
        Log.d("dob",dateOfBirth!!)

        binding.layoutAbout.setOnSingleClickListener {
            hideMainLayout()
            binding.txtAppBar.text = context?.resources?.getString(R.string.about)
            binding.about.visibility = View.VISIBLE

        }

        binding.layoutSecurityPin.setOnSingleClickListener {
            hideMainLayout()
            binding.txtAppBar.text = context?.resources?.getString(R.string.privacy)
            binding.tested.visibility = View.VISIBLE
        }

        binding.layoutHelp.setOnSingleClickListener {
            hideMainLayout()
            binding.txtAppBar.text = context?.resources?.getString(R.string.help)
            binding.help.visibility = View.VISIBLE
        }

        binding.layoutAdvanced.setOnSingleClickListener {
            hideMainLayout()
            binding.txtAppBar.text = context?.resources?.getString(R.string.advanced)
            binding.advanced.visibility = View.VISIBLE
        }

        binding.layoutSync.setOnSingleClickListener {
            hideMainLayout()
            binding.txtAppBar.text = context?.resources?.getString(R.string.sync)
            binding.sync.visibility = View.VISIBLE
        }

        binding.layoutBackup.setOnSingleClickListener {
            if (context?.getGoogleServicesResult() == ConnectionResult.SUCCESS) {
                binding.txtAppBar.text = context?.resources?.getString(R.string.backup)
                requestSignIn()
            }else{
                showAlertDialog(context?.resources?.getString(R.string.failed)!!,context?.resources?.getString(R.string.not_support_google)!!,true,childFragmentManager)
            }
        }

        binding.layoutLegacyDocument.setOnSingleClickListener{
            hideMainLayout()
            binding.txtAppBar.text = "Legacy Documents"
            binding.legalDocuments.visibility = View.VISIBLE
        }

        /*binding.layoutChangePassword.setOnClickListener {
            Intent(context, ChangePasswordActivity::class.java).also {
                context?.startActivity(it)
            }
        }*/

        binding.layoutDepProfile.setOnSingleClickListener {
            Intent(context, ProfileListActivity::class.java).also {
                context?.startActivity(it)
            }
        }

        binding.layoutPrinicipleProfile.setOnSingleClickListener{
            Intent(context,ViewProfileActivity::class.java).also {
                it.putExtra(Passparams.USER, Passparams.PARENT)
                context?.startActivity(it)
            }
        }

        binding.layoutChangeLanguage.setOnSingleClickListener {
            hideMainLayout()
            binding.txtAppBar.text = context?.resources?.getString(R.string.change_language)
            binding.changeLanguage.visibility = View.VISIBLE
            /*Intent(context, ChangeLanguageActivity::class.java).also {
                context?.startActivity(it)
            }*/
        }


        binding.layoutNotification.setOnSingleClickListener {
            hideMainLayout()
            binding.txtAppBar.text = context?.resources?.getString(R.string.notification)
            binding.notification.visibility = View.VISIBLE
        }

        binding.layoutPaymentMethod.setOnSingleClickListener {
            hideMainLayout()
            binding.txtAppBar.text = context?.resources?.getString(R.string.payment_method)
            binding.paymentMethod.visibility = View.VISIBLE
        }

        binding.layoutLogout.setOnSingleClickListener {
            showAlertForLogout()
        }

        binding.imgBack.setOnSingleClickListener {
            binding.txtAppBar.text = context?.resources?.getString(R.string.setting)
            showMainLayout()
        }

        layout_version_relase.setOnSingleClickListener {
            showReleaseAppVersion()

        }

        viewModel.loginPinEnable.observe(viewLifecycleOwner, Observer { status ->
            if (status == "Y"){
                login_pin_enable.setToggleOn(true)
            }else{
                login_pin_enable.setToggleOff(true)
            }
        })

        login_pin_enable.setOnToggleChanged(OnToggleChanged {
            if (it){
                val loginPin = viewModel.enableLoginPin()
                if (loginPin == null){
                    Intent(context,LoginPinActivity::class.java).also {
                        it.putExtra(Passparams.ISCREATE,"create")
                        //it.putExtra(Passparams.ISSETTINGS,true)
                        context?.startActivity(it)
                    }
                }
            }else{
                viewModel.disableLoginPin()
            }
        })

        layout_change_pin.setOnSingleClickListener {
            Intent(context,LoginPinActivity::class.java).also {
                it.putExtra(Passparams.ISCREATE,"update")
                //it.putExtra(Passparams.ISSETTINGS,true)
                context?.startActivity(it)
            }
        }


        btn_sync.setOnSingleClickListener {
            viewModel.syncRecord()
        }

        layout_terms.setOnSingleClickListener {
            Intent(context,TermsOfUseActivity::class.java).also {
                context?.startActivity(it)
            }
        }

        layout_google_drive.setOnSingleClickListener {
            if (isGoogleSiginSuccess) {
                sqliteToExcel()
            }else{
                context?.toast("Your Google Drive Access Failed")
            }
        }

        layout_faq.setOnSingleClickListener{
            Intent(context,FAQTravelAdvisoryActivity::class.java).also {
                context?.startActivity(it)
            }
        }

        layout_feedback.setOnSingleClickListener{
            Intent(context,FeedBackActivity::class.java).also {
                context?.startActivity(it)
            }
        }

        changeLanguage()


        //Ringtone part
        /*if(viewModel.getNotificationStatus()!!)
            toggle_show_notification.setToggleOn()
        else
            toggle_show_notification.setToggleOff()*/

        if(viewModel.getNotificationSoundStatus()!!) {
            toggle_notification_sound.setToggleOn()
            //enableRingtoneSound()
        }
        else {
            toggle_notification_sound.setToggleOff()
            //disableRingtoneSound()
        }

        if (viewModel.getNotificationAdvisoryStatus()!!){
            toggle_advisory.setToggleOn()
        }else{
            toggle_advisory.setToggleOff()
        }

        if (viewModel.getNotificationNewsUpdatesStatus()!!){
            toggle_news_update.setToggleOn()
        }else{
            toggle_news_update.setToggleOff()
        }

        if (viewModel.getNotificationRegulatoryStatus()!!){
            toggle_regulatory.setToggleOn()
        }else{
            toggle_regulatory.setToggleOff()
        }



        //Notification
        /*toggle_show_notification.setOnToggleChanged(OnToggleChanged {
            Log.e("Notification Status", it.toString())
            viewModel.saveNotificationStatus(it)
        })*/

        toggle_notification_sound.setOnToggleChanged(OnToggleChanged {
            Log.e("Noti Sound Status", it.toString())
            viewModel.saveNotificationSoundStatus(it)
            /* if (it == true)
                enableRingtoneSound()
            else
                disableRingtoneSound()*/
        })

        toggle_advisory.setOnToggleChanged {
            viewModel.saveNotificationAdvisoryStatus(it)
        }

        toggle_news_update.setOnToggleChanged {
            viewModel.saveNotificationNewsUpdatesStatus(it)
        }

        toggle_regulatory.setOnToggleChanged {
            viewModel.saveNotificationRegulatoryStatus(it)
        }



       /* create_notification_btn.setOnClickListener {
            if(viewModel.getNotificationStatus()!!) {
                val noti_service= NotificationService(this.requireContext())
                noti_service?.createNotificationChannel("Hello, This is a notification from immunitee","Immunitee")
            }
        }*/

        ringtone_name.post {  ringtone_name.text=viewModel.getRingtoneName()}

        layout_change_ringtone.setOnClickListener {
            pickupRingtone()
        }


        layout_privacy_policy.setOnSingleClickListener{
            Intent(context,LegalDocumentsActivity::class.java).also {
                it.putExtra(Passparams.LEGAL_DOCUMENT,Passparams.PRIVACY_POLICY)
                startActivity(it)
            }
        }

        layout_refund_policy.setOnSingleClickListener{
            Intent(context,LegalDocumentsActivity::class.java).also {
                it.putExtra(Passparams.LEGAL_DOCUMENT,Passparams.REFUND_POLICY)
                startActivity(it)
            }
        }

        legal_terms_conditions.setOnSingleClickListener{
            Intent(context,LegalDocumentsActivity::class.java).also {
                it.putExtra(Passparams.LEGAL_DOCUMENT,Passparams.LEGAL_TERMS_CONDITIONS)
                startActivity(it)
            }
        }



    }

    private fun hideMainLayout(){
        binding.imgBack.visibility = View.VISIBLE
        binding.mainLayout.visibility = View.GONE
        binding.layoutLogout.visibility = View.GONE
    }

    private fun showMainLayout(){
        if (binding.about.visibility == View.VISIBLE) {
            binding.about.visibility = View.GONE
        }else if(binding.tested.visibility == View.VISIBLE){
            binding.tested.visibility = View.GONE
        }else if (binding.help.visibility == View.VISIBLE){
            binding.help.visibility = View.GONE
        }else if (binding.advanced.visibility == View.VISIBLE){
            binding.advanced.visibility = View.GONE
        }else if (binding.notification.visibility == View.VISIBLE){
            binding.notification.visibility = View.GONE
        }else if (binding.paymentMethod.visibility == View.VISIBLE){
            binding.paymentMethod.visibility = View.GONE
        }else if (binding.sync.visibility == View.VISIBLE){
            binding.sync.visibility = View.GONE
        }else if (binding.backup.visibility == View.VISIBLE){
            binding.backup.visibility = View.GONE
        }else if (binding.changeLanguage.visibility == View.VISIBLE){
            binding.changeLanguage.visibility = View.GONE
        } else if (binding.legalDocuments.visibility == View.VISIBLE){
            binding.legalDocuments.visibility = View.GONE
        }

        binding.mainLayout.visibility = View.VISIBLE
        //binding.layoutLogout.visibility = View.VISIBLE
        binding.imgBack.visibility = View.GONE
    }

    private fun showAlertForLogout(){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Are you sure you want to log out?")
            .setTitle("Log Out").setCancelable(false).setPositiveButton("YES"
                ) { dialog, which ->
                Intent(requireContext(), LoginPinActivity::class.java).also {
                    it.putExtra(Passparams.ISCREATE, "")
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    requireContext().startActivity(it)
                }
            }.setNegativeButton("CANCEL"
                ) { dialog, which -> dialog?.dismiss() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showReleaseAppVersion(){
        try {
            val packageInfo = context?.packageManager?.getPackageInfo(context?.packageName!!, 0)
            val versionName = packageInfo?.versionName
            //val version = "Version : $versionName \nDevelopment Server : ${Passparams.URL}"
            //val version = "Version : $versionName \nDevelopment Server"
            val version = "Version : $versionName \nStaging Server"
            //val version = "Version : $versionName \nProduction Server"
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setMessage(version).setTitle(R.string.app_name)
                    .setNegativeButton("CANCEL"
                    ) { dialog, which -> dialog?.dismiss() }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }catch (e: PackageManager.NameNotFoundException){
            e.printStackTrace()
        }


    }

    override fun onStarted() {
        show(binding.progressBar)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressBar)
        context?.toast(msg)
        //showAlertDialog(msg,"",false,childFragmentManager)
    }

    override fun onShowToast(msg: String) {
        hide(binding.progressBar)
        context?.toast(msg)
    }

    override fun onFailure(msg: String) {
        hide(binding.progressBar)
        if (msg.startsWith("2")){
            val showMsg = msg.drop(1)
            showAlertDialog(resources.getString(R.string.failed), showMsg, false, childFragmentManager)
        }else if (msg.startsWith("3")){
            val showMsg = msg.drop(1)
            showAlertDialog(showMsg, resources.getString(R.string.check_internet), false, childFragmentManager)
        }else {
            showAlertDialog(msg, "", false, childFragmentManager)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_SIGN_IN -> data?.let { handleSignInResult(it) }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun requestSignIn() {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .build()
        val client = this.context?.let { GoogleSignIn.getClient(it, signInOptions) }

        // The result of the sign-in Intent is handled in onActivityResult.ast
        if (client != null) {
            startActivityForResult(client.signInIntent, REQUEST_CODE_SIGN_IN)
        }
    }



    private fun handleSignInResult(result: Intent) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
            .addOnSuccessListener { googleAccount ->

                // Use the authenticated account to sign in to the Drive service.
                val credential = GoogleAccountCredential.usingOAuth2(
                    this.context,
                    Collections.singleton(DriveScopes.DRIVE_FILE),

                    )
                credential.selectedAccount = googleAccount.account
                val googleDriveService = Drive.Builder(
                    AndroidHttp.newCompatibleTransport(),
                    GsonFactory(),
                    credential
                ).setApplicationName("ImmuniteeMedical")
                    .build()

                //Sets the drive api service on the view model and creates our apps folder
                Log.e("Indicator", "Login In Success")
                isGoogleSiginSuccess = true
                //context?.toast("Login In Success")
                //showAlertDialog("Google Drive","Login In Success",true,childFragmentManager)
                driveServiceHelper= DriveServiceHelper(googleDriveService)
                hideMainLayout()
                binding.backup.visibility = View.VISIBLE
            }
            .addOnFailureListener { exception ->
                Log.e("Indicator", "Fail to Login")
                //context?.toast("Fail to Login")
                showAlertDialog("Google Drive","Failed to Login",false,childFragmentManager)
            }
    }

    private fun sqliteToExcel() {

        /*var progressDialog= ProgressDialog(this.context)
        progressDialog.setTitle("Converting...to Excel")
        progressDialog.setMessage("Please wait...")
        progressDialog.show()*/

        show(binding.progressBar)


        var directory_path:String = activity?.getExternalFilesDir(null).toString()
        var database_path:String = activity?.getDatabasePath("immunitees.db")?.absolutePath.toString()
        val sqliteToExcel = com.ajts.androidmads.library.SQLiteToExcel(this.context, database_path, directory_path)
        sqliteToExcel.exportAllTables("/data.xls", object : com.ajts.androidmads.library.SQLiteToExcel.ExportListener {
            override fun onStart() {

            }

            override fun onCompleted(filepath: String) {
                //progressDialog.dismiss()
                //backupLocalDatabase()
                encryptExcelFile()
                Log.e("SQLite2Excel", "Success")
            }

            override fun onError(e: java.lang.Exception) {
                hide(binding.progressBar)
                Log.e("SQLite2Excel", "Fail")
            }
        })
    }

    fun encryptExcelFile()
    {
        var excelFilePath= activity?.getExternalFilesDir(null)?.absolutePath.toString()+ FILE_NAME
        var dataPath= activity?.getExternalFilesDir(null)?.absolutePath.toString()+ "/data.xls"
        val excelFile = FileInputStream(File(dataPath))
        val workbook = HSSFWorkbook(excelFile)
        var encrypt_wb=HSSFWorkbook()

        var numberOfSheet=workbook.numberOfSheets-1

        for(current in 0..numberOfSheet) {
            val sheet = workbook.getSheetAt(current)

            val rows = sheet.iterator()
            while (rows.hasNext()) {
                val currentRow = rows.next()
                val cellsInRow = currentRow.iterator()
                while (cellsInRow.hasNext()) {
                    val currentCell = cellsInRow.next()
                    Log.e("DOB", "1988-07-28")
                    var encryptData = EncryptionUtils.encrypt(currentCell.stringCellValue, dateOfBirth)
                    currentCell.setCellValue(encryptData)
                    Log.e("Encrypted Format", currentCell.stringCellValue.toString())
                }
            }

            encrypt_wb=workbook
        }

        val encrytedExcelPath = File(excelFilePath)
        var outputStream: FileOutputStream? = null

        try {
            outputStream = FileOutputStream(encrytedExcelPath)
            encrypt_wb.write(outputStream)
            backupLocalDatabase()
            Log.e("Encrypt", "Success")

        } catch (e: IOException) {
            e.printStackTrace()
            hide(binding.progressBar)
            context?.toast("Your Backup is upload Failed cannot encrypt")
            Log.e("Encrypt", "Fail")
            try {
                outputStream?.close()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }

        workbook.close()
        excelFile.close()

    }
    private fun backupLocalDatabase()
    {
        /*var progressDialog= ProgressDialog(this.context)
        progressDialog.setTitle("Backup to Google Drive")
        progressDialog.setMessage("Please wait...")
        progressDialog.show()*/

        //var dataPath= activity?.getExternalFilesDir(null)?.absolutePath.toString()+ "/data.xls"
        var excelPath= activity?.getExternalFilesDir(null)?.absolutePath.toString()+ FILE_NAME
        driveServiceHelper.backUpFile(excelPath)?.addOnSuccessListener(
            OnSuccessListener {
                hide(binding.progressBar)
                //context?.toast("Your Backup is Successfully uploaded")
                BackupSuccessDialogFragment().show(childFragmentManager,"TAG")
                Log.e("Upload", "Success")
            }
        )
            ?.addOnFailureListener(
                OnFailureListener {
                    hide(binding.progressBar)
                    //context?.toast("Your Backup is upload Failed")
                    showAlertDialog("Backup","Your Backup is upload Failed",false,childFragmentManager)
                    Log.e("Upload", "Fail")
                }
            )
    }

    private fun changeLanguage(){
        val pos = Paper.book().read<Int>(Passparams.SELECT_LANGUAGE_POS,0)
        if (pos == 0){
            english_checkbox.isChecked = true
            malay_checkbox.isChecked = false
        }else{
            malay_checkbox.isChecked = true
            english_checkbox.isChecked = false
        }

        english_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                malay_checkbox.isChecked = false
                Paper.book().write(Passparams.SELECT_LANGUAGE_POS,0)
                Paper.book().write(Passparams.ISFROMCHANGELANGUAGE,true)

                LocaleHelper.setNewLocale(requireContext(),LocaleHelper.LANGUAGE_ENGLISH)
                Intent(context,MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(it)
                }
            }
        }

        malay_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                english_checkbox.isChecked = false
                Paper.book().write(Passparams.SELECT_LANGUAGE_POS,1)
                Paper.book().write(Passparams.ISFROMCHANGELANGUAGE,true)
                LocaleHelper.setNewLocale(requireContext(),LocaleHelper.LANGUAGE_MALAY)
                Intent(context,MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(it)
                }
            }
        }

        layout_english.setOnSingleClickListener{
            if (!english_checkbox.isChecked){
                english_checkbox.isChecked = true
                malay_checkbox.isChecked = false

                Paper.book().write(Passparams.SELECT_LANGUAGE_POS,0)
                Paper.book().write(Passparams.ISFROMCHANGELANGUAGE,true)

                LocaleHelper.setNewLocale(requireContext(),LocaleHelper.LANGUAGE_ENGLISH)
                Intent(context,MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(it)
                }
            }
        }
        layout_malay.setOnSingleClickListener{

            if (!malay_checkbox.isChecked){
                english_checkbox.isChecked = false
                malay_checkbox.isChecked = true
                Paper.book().write(Passparams.SELECT_LANGUAGE_POS,1)
                Paper.book().write(Passparams.ISFROMCHANGELANGUAGE,true)
                LocaleHelper.setNewLocale(requireContext(),LocaleHelper.LANGUAGE_MALAY)
                Intent(context,MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(it)
                }
            }

        }

    }

    //Ringtone part
    private fun pickupRingtone() {
        // if user granted access else ask for permission
        // if user granted access else ask for permission
        // am?.ringerMode  =AudioManager.RINGER_MODE_NORMAL
        val currentTone = RingtoneManager.getActualDefaultRingtoneUri(
                this.activity,
                RingtoneManager.TYPE_NOTIFICATION
        )
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone")
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
        startActivityForResult(intent, 999)
    }



}