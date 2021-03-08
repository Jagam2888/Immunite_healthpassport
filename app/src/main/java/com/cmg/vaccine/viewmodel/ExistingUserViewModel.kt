package com.cmg.vaccine.viewmodel

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.User
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.ExistingUserRepositary
import com.cmg.vaccine.util.APIException
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import com.cmg.vaccine.util.changeDateFormatNormal
import org.json.JSONException
import org.json.JSONObject
import java.net.SocketTimeoutException

class ExistingUserViewModel(
    private val repositary: ExistingUserRepositary
):ViewModel() {

    var privateKey = ObservableField<String>()
    var listener:SimpleListener?=null

    var passportNo:String?=null
    var patientIdNo:String?=null
    var dob:String?=null
    var dobTime:String?=null

    fun onSubmit(){
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

                if (jsonBodySecond.has("passportNo")) {
                    passportNo = jsonBodySecond.optString("passportNo")
                }

                if (jsonBodySecond.has("idNo")) {
                    patientIdNo = jsonBodySecond.optString("idNo")
                }

                if (jsonBodySecond.has("dob")){
                    if (!jsonBodySecond.getString("dob").isNullOrEmpty()) {
                        val isoFormat = changeDateFormatNormal(jsonBodySecond.getString("dob"))
                        var dobFormatArray = isoFormat?.split(" ")
                        dob = dobFormatArray?.get(0)
                        dobTime = dobFormatArray?.get(1)
                    }
                }

                val user = User(
                    jsonBodySecond.getString("firstName"),
                    jsonBodySecond.getString("email"),
                    jsonBodySecond.getString("email"),
                    jsonBodySecond.getString("mobileNumber"),
                    passportNo,
                    jsonBodySecond.getString("idType"),
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
                }
                listener?.onSuccess("Restored Success")


                Log.d("response_body",responseBody)

            }catch (e:APIException){
                listener?.onFailure(e.message!!)
            }catch (e:NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e:SocketTimeoutException){
                listener?.onFailure(e.message!!)
            }catch (e:JSONException){
                listener?.onFailure("invalid")
            }
        }
    }
}