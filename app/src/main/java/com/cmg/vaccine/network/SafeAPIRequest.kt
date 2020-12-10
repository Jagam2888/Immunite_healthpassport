package com.cmg.vaccine.network

import com.cmg.vaccine.util.APIException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeAPIRequest{
    suspend fun <T:Any>apiRequest(call : suspend () -> Response<T>): T {
        val response = call.invoke()
        if(response.isSuccessful){
            return response.body()!!
        }else{
            val error = response.errorBody()?.string()
            val msg = StringBuilder()
            error?.let {
                try {
                    msg.append(JSONObject(it).getString("message"))
                }catch (e : JSONException){

                }
                //msg.append("\n")
            }
            //msg.append("Error code : ${response.code()}")

            throw APIException(msg.toString())
        }
    }
}