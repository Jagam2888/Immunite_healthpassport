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
import com.cmg.vaccine.util.Passparams
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

    var recorId:MutableLiveData<String> = MutableLiveData()

    fun loadData(value:String){
        /*val data = repositary.gettestReport(id)
        if (data != null){
            _testReport.value = data
        }*/
        val gson = Gson()
        val type: Type = object : TypeToken<TestReport>() {}.type
        var testReportData = gson.fromJson<TestReport>(value, type)
        _testReport.value = testReportData

        recorId.value = testReportData.recordId
    }

    fun download(view: View){
        listener?.onStarted()
        if(!recorId.value.isNullOrEmpty()) {
            Couritnes.main {
                try {
                    //val url = "http://stg.i-care.com:6001/gp-module-lab/ext/labpdf?labOrderTxn=LAB_A07R100051"
                    val url = Passparams.DOWNLOAD_TEST_REPORT+recorId.value
                    val downloadCall = repositary.getApi()
                        .downLoadDynamicUrl(url)
                    downloadCall.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful) {
                                Log.d("download_file", response.body()?.byteStream().toString())

                                saveFile(response.body(), view, recorId.value!!)
                            } else {
                                Log.d("download_file_errorbody", response.body().toString())
                                listener?.onFailure(response.body().toString())
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.d("download_file_error", t.toString())
                            listener?.onFailure(t.toString())
                        }
                    })

                } catch (e: APIException) {
                    listener?.onFailure(e.message!!)
                } catch (e: NoInternetException) {
                    listener?.onFailure(e.message!!)
                } catch (e: Exception) {
                    listener?.onFailure(e.message!!)
                }
            }
        }
    }

    fun saveFile(body: ResponseBody?,view: View,fileName:String):String{
        if (body==null)
            return ""
        var input: InputStream? = null
        try {
            var path = view.context.filesDir.absolutePath.toString()+"/TestReport"
            val directory = File(path)
            if (!directory.exists())
                directory.mkdir()

            var savePath= "$path/$fileName.pdf"
            //download and save file to internal
            input = body.byteStream()
            //val file = File(getCacheDir(), "cacheFileAppeal.srl")
            val fos = FileOutputStream(savePath)
            fos.use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            listener?.onSuccess("success")
            return savePath
        }catch (e:Exception){
            Log.e("saveFile",e.toString())
        }
        finally {
            input?.close()
        }
        return ""
    }

}