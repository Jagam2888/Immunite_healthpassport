package com.cmg.vaccine.viewmodel

import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.User
import com.cmg.vaccine.database.WorldEntryCountries
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.ExistingUserRepositary
import com.cmg.vaccine.util.*
import io.paperdb.Paper
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.net.SocketTimeoutException

class ExistingUserViewModel(
    private val repositary: ExistingUserRepositary
):ViewModel() {

    var privateKey = ObservableField<String>()
    var listener:SimpleListener?=null

    var passportNo:String?=null
    var passportExpType:String?=null
    var patientIdNo:String?=null
    var patientIdType:String?=null
    var dob:String?=null
    var dobTime:String?=null

    var dobTxt = ObservableField<String>()
    var edttxtQR = ObservableField<String>()

    var isRestoreForSync = ObservableBoolean()

    fun onSubmit(view:View){
        if (dobTxt.get().isNullOrEmpty()){
            listener?.onFailure("Please Check your Date of Birth, Maybe Wrong or Empty")
            return
        }
        if (!isRestoreForSync.get()){
            if (!privateKey.get().isNullOrEmpty()) {
                getSyncManually(view)
            }else if((!edttxtQR.get().isNullOrEmpty()) and privateKey.get().isNullOrEmpty()){
                val qrCodeValue = edttxtQR.get()?.replace("\\n","\n")
                privateKey.set(decryptQRValue(qrCodeValue!!,changeDateFormatForPrivateKeyDecrypt(dobTxt.get()!!)!!))
                getSyncManually(view)
            }else{
                listener?.onFailure("Please Enter or Scan Your Private key")
            }
        }else{
                listener?.onSuccess("restore")

        }
    }

    private fun updateUUID(view: View){
        Couritnes.main {
            try {
                val response = repositary.updateUUID(repositary.getPatientSubId()!!,view.context.getDeviceUUID()!!)
                if (response.StatusCode == 1){
                    updateFCMToken()
                    //listener?.onSuccess(response.Message)

                }else{
                    listener?.onFailure(response.Message)
                }
            }catch (e:APIException){
                listener?.onFailure(e.message!!)
            }catch (e:NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e:SocketTimeoutException){
                listener?.onFailure(e.message!!)
            }catch (e:JSONException){
                listener?.onFailure("invalid")
            }catch (e:Exception){
                listener?.onFailure(e.message!!)
            }
        }
    }

    private fun updateFCMToken(){
        val token = Paper.book().read<String>(Passparams.FCM_TOKEN,"")
        Couritnes.main {
            try {
                val response = repositary.updateFCMToken(repositary.getPatientSubId()!!,token)
                //if (response.StatusCode == 1){
                    //listener?.onSuccess(response.Message)
                    listener?.onSuccess("Setup Manually Success")
                /*}else{
                    listener?.onFailure(response.Message)
                }*/
            }catch (e:APIException){
                listener?.onFailure(e.message!!)
            }catch (e:NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e:SocketTimeoutException){
                listener?.onFailure(e.message!!)
            }catch (e:JSONException){
                listener?.onFailure("invalid")
            }catch (e:Exception){
                listener?.onFailure(e.message!!)
            }

        }
    }

    private fun getSyncManually(view: View){
        Log.d("enter","yes")
        Log.d("private_key",privateKey.get()!!)
        Couritnes.main {
            try {
                listener?.onStarted()
                val response = repositary.getExistingUser(privateKey.get()!!)

                val responseBody = response.string()
                val jsonBody = JSONObject(responseBody)
                val jsonBodyFirst = jsonBody.getJSONObject("data")
                val jsonBodySecond = jsonBodyFirst.getJSONObject("data")

                /*if (jsonBodySecond.has("passportExpiryDate")){


                }*/

                if (jsonBodySecond.has("IDTypes")){
                    val jsonIdTypeArray = jsonBodySecond.getJSONArray("IDTypes")
                    for (i in 0 until jsonIdTypeArray.length()){
                        val item = jsonIdTypeArray.getJSONObject(i)
                        if (item.has("IdType")) {
                            if (item.getString("IdType").equals("PPN",false)) {
                                if (item.has("idNo")) {
                                    passportNo = item.getString("idNo")
                                }
                            }else{
                                patientIdType = item.getString("IdType")

                                if (item.has("idNo")) {
                                    patientIdNo = item.getString("idNo")
                                }
                            }
                        }

                    }
                }

                /*if (jsonBodySecond.has("passportNo")) {
                    passportNo = jsonBodySecond.optString("passportNo")
                }

                if (jsonBodySecond.has("idNo")) {
                    patientIdNo = jsonBodySecond.optString("idNo")
                }*/

                if (jsonBodySecond.has("dob")){
                    if (!jsonBodySecond.getString("dob").isNullOrEmpty()) {
                        val isoFormat = changeDateFormatBC(jsonBodySecond.getString("dob"))
                        var dobFormatArray = isoFormat?.split(" ")
                        dob = dobFormatArray?.get(0)
                        dobTime = dobFormatArray?.get(1)
                    }
                }

                val user = User(
                        jsonBodySecond.getString("fullName"),
                        jsonBodySecond.getString("email"),
                        jsonBodySecond.getString("email"),
                        jsonBodySecond.getString("mobileNumber"),
                        passportNo,
                    "",
                        patientIdType,
                        patientIdNo,
                        jsonBodySecond.getString("countryCode"),
                        jsonBodySecond.getString("placeOfBirth"),
                        jsonBodySecond.getString("gender"),
                        jsonBodySecond.getString("nationalityCountry"),
                        dob,
                        dobTime,
                        "",
                        "",
                        "",
                        privateKey.get(),
                        "",
                        jsonBodySecond.getString("subsId"),
                        "Y"
                )

                if (repositary.getUserCount() > 0){
                    repositary.deleteOldUser()
                }

                repositary.insertUserLocalDb(user)
                if (!jsonBodySecond.getString("subsId").isNullOrEmpty()) {
                    repositary.savePatientSubId(jsonBodySecond.getString("subsId"))
                    updateUUID(view)
                }else{
                    listener?.onFailure("Sub id missing")
                }
                //listener?.onSuccess("Setup Manually Success")


                Log.d("response_body",responseBody)

            }catch (e:APIException){
                listener?.onFailure(e.message!!)
            }catch (e:NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e:SocketTimeoutException){
                listener?.onFailure(e.message!!)
            }catch (e:JSONException){
                listener?.onFailure("invalid")
            }catch (e:Exception){
                listener?.onFailure(e.message!!)
            }
        }
    }

    //this is the purpose of create database structure
    fun loadWorldEntries(){
        val getWorldEntries = repositary.getWorldEntryCountries()
        if (getWorldEntries.isNullOrEmpty()) {
            listener?.onStarted()
            Couritnes.main {
                try {
                    val response = repositary.getWorldEntriesCountryList()
                    if (response.data.isNotEmpty()){
                        response.data.forEach {
                            val worldEntryCountries = WorldEntryCountries(
                                    it.countryName,
                                    it.countryCodeAlpha,
                                    it.countryMstrSeqno
                            )
                            repositary.insertWorldEntryCountries(worldEntryCountries)
                        }
                    }
                    listener?.onSuccess("")
                }catch (e:APIException){
                    listener?.onFailure(e.message!!)
                }catch (e:NoInternetException){
                    listener?.onFailure(e.message!!)
                }catch (e:SocketTimeoutException){
                    listener?.onFailure(e.message!!)
                }catch (e:JSONException){
                    listener?.onFailure("invalid")
                }catch (e:Exception){
                    listener?.onFailure(e.message!!)
                }
            }
        }
    }
}