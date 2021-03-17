package com.cmg.vaccine.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmg.vaccine.database.TestReport
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.VaccineAndTestRepositary
import com.cmg.vaccine.util.APIException
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.Exception
import java.lang.reflect.Type

class VaccineAndTestViewModel(
    private val repositary: VaccineAndTestRepositary
):ViewModel() {

    val _testReport:MutableLiveData<TestReport> = MutableLiveData()
    val testReport:LiveData<TestReport>
    get() = _testReport

    var listener:SimpleListener?=null
    var inputStream:MutableLiveData<InputStream> = MutableLiveData()

    fun loadData(value:String){
        /*val data = repositary.gettestReport(id)
        if (data != null){
            _testReport.value = data
        }*/
        val gson = Gson()
        val type: Type = object : TypeToken<TestReport>() {}.type
        var testReportData = gson.fromJson<TestReport>(value, type)
        _testReport.value = testReportData
    }

    fun download(){
        listener?.onStarted()
        //DownloadManager.Request("dasda/")
        val recordId = "LAB_A06M100004"
        Couritnes.main {
            try {
                //val response =  repositary.downloadFile(recordId)
                    val response = repositary.downloadFile("http://10.1.1.150:6001/gp-module-lab/ext/labpdf?labOrderTxn=LAB_A06M100004")
                Log.d("download_file",response.string())
                    //val response = repositary.downLoadDynamic("http://10.1.1.150:6001/gp-module-lab/ext/labpdf?labOrderTxn=LAB_A06M100004")
                //if (response.bytes().isNotEmpty())
                /*val body = response.body()?.byteStream()
                inputStream.value = body*/
                //writeFile(view,body,recordId)
                /*if (response.bytes().isNotEmpty()) {
                    listener?.onSuccess("success")
                }*/

            }catch (e:APIException){
                listener?.onFailure(e.message!!)
            }catch (e:NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e:Exception){
                listener?.onFailure(e.message!!)
            }
        }
    }

    private fun writePdf(body:InputStream,fileName: String){
        try {
            var fileOutputStream:FileOutputStream?=null
            val oriFileName = "$fileName.pdf"
           // fileOutputStream = openFileOutput(oriFileName, Context.MODE_PRIVATE)

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun writeFile(view: View,input: InputStream,fileName:String) {
        //Couritnes.main {
            val directory = File(view.context.filesDir,"files")
            if (!directory.exists())
                directory.mkdir()

            try {
                val makeFile = File(directory, "$fileName.pdf")
                val fos = FileOutputStream(makeFile)
                fos.use {output->
                    val buffer = ByteArray(4 * 1024) // or other buffer size
                    var read: Int
                    while (input.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                    }
                    output.flush()
                    fos.close()
                }
                listener?.onSuccess("success")
            }catch (e:Exception){
                listener?.onFailure(e.message!!)
            }
        //}

    }
}