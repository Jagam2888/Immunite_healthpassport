package com.cmg.vaccine

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityRestoredBackupOptionListBinding
import com.cmg.vaccine.fragment.SettingsFragment
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.services.DriveServiceHelper
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.RestoreBackupOptionListViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.RestoreBackupOptionListViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import immuniteeEncryption.EncryptionUtils
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.*

class RestoredBackupOptionList : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityRestoredBackupOptionListBinding
    private lateinit var viewModel:RestoreBackupOptionListViewModel
    private lateinit var driveServiceHelper: DriveServiceHelper
    var isGoogleSiginSuccess:Boolean = false
    var dob:String?=null
    var progress_status:Float=0f

    private val factory:RestoreBackupOptionListViewModelFactory by instance()

    companion object{
        const val REQUEST_CODE_SIGN_IN = 400
        var FILE_NAME:String="/immunitees.xls"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_restored_backup_option_list)
        viewModel = ViewModelProvider(this,factory).get(RestoreBackupOptionListViewModel::class.java)
        binding.viewmodel = viewModel

        binding.lifecycleOwner = this

        viewModel.listener = this
        binding.progressPerc.text="${progress_status.toInt()}%"

        dob = intent.extras?.getString(Passparams.USER_DOB,"")


        binding.layoutGoogleDrive.setOnSingleClickListener {
            try {
                requestSignIn()
            }catch (e:NoInternetException){
                toast(e.message!!)
            }
        }

        binding.imgBack.setOnClickListener {
            finish()
        }
    }

    private fun requestSignIn() {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Scope(DriveScopes.DRIVE_FILE))
                .build()
        val client = this.let { GoogleSignIn.getClient(it, signInOptions) }

        // The result of the sign-in Intent is handled in onActivityResult.ast
        if (client != null) {
            startActivityForResult(client.signInIntent, SettingsFragment.REQUEST_CODE_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            SettingsFragment.REQUEST_CODE_SIGN_IN -> data?.let { handleSignInResult(it) }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleSignInResult(result: Intent) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener { googleAccount ->

                    // Use the authenticated account to sign in to the Drive service.
                    val credential = GoogleAccountCredential.usingOAuth2(
                            this,
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
                    toast("Login In Success")
                    binding.layoutGoogleDrive.visibility= View.GONE
                    binding.progressBarView.visibility= View.VISIBLE
                    driveServiceHelper= DriveServiceHelper(googleDriveService)
                    downLoadExcelFromDrive()
                }
                .addOnFailureListener { exception ->
                    Log.e("Indicator", "Fail to Login")
                    toast("Fail to Login")
                }
    }

    private fun downLoadExcelFromDrive(){
        //show(binding.progressBar)

        driveServiceHelper.downloadFile(File(getExternalFilesDir(null)?.absoluteFile,FILE_NAME))?.addOnSuccessListener {
            //hide(binding.progressBar)
            toast("Download success")
            progress_status+=40;
            //binding.circularProgressBar.progress = progress_status
            binding.circularProgressBar.apply {
                progress = progress_status
                setProgressWithAnimation(65f, 1000)
            }
            binding.progressPerc.text = "${progress_status.toInt()}%"
            decryptExcelFile()
        }?.addOnFailureListener {
            hide(binding.progressBar)
            toast("Download Failed")
        }
    }

    private fun decryptExcelFile()
    {
        var dataPath= getExternalFilesDir(null)?.absolutePath.toString()+ "/data.xls"
        var excelFilePath= this.getExternalFilesDir(null)?.absolutePath.toString()+ FILE_NAME
        Log.e("Path", excelFilePath)
        val excelFile = FileInputStream(File(excelFilePath))
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
                    //Log.e("DOB", viewModel.getUserDOB().toString())
                    var decryptData = EncryptionUtils.decrypt(currentCell.stringCellValue, dob)
                    currentCell.setCellValue(decryptData)

                    Log.e("Encrypted Format", currentCell.stringCellValue.toString())
                }
            }

            encrypt_wb=workbook
        }

        val encrytedExcelPath = File(dataPath)
        var outputStream: FileOutputStream? = null

        try {
            outputStream = FileOutputStream(encrytedExcelPath)
            encrypt_wb.write(outputStream)
            toast("Decrypt Success")
            insertDataIntoLocalDatabase()
            progress_status+=40;
            //binding.circularProgressBar.progress=progress_status
            binding.circularProgressBar.apply {
                progress = progress_status
                setProgressWithAnimation(65f, 1000)
            }
            binding.progressPerc.text="${progress_status.toInt()}%"
            Log.e("Decrypt", "Success")

        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("Decrypt", "Fail")
            try {
                outputStream?.close()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }

        workbook.close()
        excelFile.close()
    }

    private fun insertDataIntoLocalDatabase() {

        //viewModel.deleteAllTables()
        var database = SQLiteDatabase.openOrCreateDatabase(this.getDatabasePath("immunitees.db")?.getAbsolutePath().toString(), null)
        var dataPath = getExternalFilesDir(null)?.absolutePath.toString() + "/data.xls"
        val excelFile = FileInputStream(File(dataPath))
        val workbook = HSSFWorkbook(excelFile)

        var numberOfSheet = workbook.numberOfSheets - 1

        for (current in 0..numberOfSheet) {
            val sheet = workbook.getSheetAt(current)
            //because worldentrycountries already loaded from api
            if (sheet.sheetName != "room_master_table" && sheet.sheetName != "sqlite_sequence" && sheet.sheetName != "WorldEntryCountries") {
                val firstrow = sheet.iterator()

                var q1: StringBuilder = java.lang.StringBuilder("INSERT INTO " + sheet.sheetName)
                var data_headers: String = ""

                val currentRow = firstrow.next()
                val cellsInRow = currentRow.iterator()
                while (cellsInRow.hasNext()) {
                    val currentCell = cellsInRow.next()
                    if (data_headers == "")
                        data_headers += currentCell.stringCellValue
                    else
                        data_headers += "," + currentCell.stringCellValue
                }

                var data_headers_wc = " ($data_headers)"
                var q2 = q1.append(data_headers_wc)


                var data_value: String = ""
                val rows = sheet.iterator()
                rows.next()//skip header
                while (rows.hasNext()) {
                    val currentRow = rows.next()
                    val cellsInRow = currentRow.iterator()
                    while (cellsInRow.hasNext()) {
                        val currentCell = cellsInRow.next()
                        if (data_value == "")
                            data_value += " '" + currentCell.stringCellValue + "'"
                        else
                            data_value += ", '" + currentCell.stringCellValue + "'"

                    }
                    var data_value_wc = " ($data_value)"
                    var full_query = "$q2 VALUES$data_value_wc"

                    if (data_value != "") {
                        Log.e("FUll_Query", full_query.toString())
                        try {
                            database.execSQL(full_query.toString())
                        }catch (e:SQLiteException){
                            //toast("Invalid Private Key")
                            Log.e("sqlite_error",e.printStackTrace().toString())
                        }
                        data_value=""
                    }
                }
            }
        }
        //hide(binding.progressBar)
        toast("Database insert done")
        progress_status+=20;
        //binding.circularProgressBar.progress=progress_status
        binding.circularProgressBar.apply {
            progress = progress_status
            setProgressWithAnimation(65f, 1000)
        }
        binding.progressPerc.text="${progress_status.toInt()}%"
        viewModel.getUser(this)

        viewModel.userData.observe(this, androidx.lifecycle.Observer {
            if (it.virifyStatus.equals("Y",true)){
                if (viewModel.loginPin.value != null){
                    Intent(this, LoginPinActivity::class.java).also {intentValue->
                        intentValue.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intentValue.putExtra(Passparams.ISCREATE,"")
                        startActivity(intentValue)
                    }
                }else{
                    Intent(this, MainActivity::class.java).also {intentValue->
                        intentValue.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intentValue)
                    }
                }
            }else{
                toast("sorry Your account not active")
            }
        })
        //viewModel.setUserSubId()
    }

    override fun onStarted() {
    }

    override fun onSuccess(msg: String) {
        toast(msg)
        hide(binding.progressBar)
        if (viewModel.userData.value?.virifyStatus.equals("Y",true)){
            if (viewModel.loginPin.value != null){
                Intent(this, LoginPinActivity::class.java).also {intentValue->
                    intentValue.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intentValue.putExtra(Passparams.ISCREATE,"")
                    startActivity(intentValue)
                }
            }else{
                Intent(this, MainActivity::class.java).also {intentValue->
                    intentValue.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intentValue)
                }
            }
        }else{
            //toast("sorry Your account not active")
            showAlertDialog(msg,"",false,supportFragmentManager)
        }
    }

    override fun onShowToast(msg: String) {
        hide(binding.progressBar)
        toast(msg)
    }

    override fun onFailure(msg: String) {
        hide(binding.progressBar)
        if (msg.startsWith("3")){
            val showMsg = msg.drop(1)
            showAlertDialog(showMsg, resources.getString(R.string.check_internet), false, supportFragmentManager)
        }else {
            showAlertDialog(msg, "", false, supportFragmentManager)
        }
    }
}