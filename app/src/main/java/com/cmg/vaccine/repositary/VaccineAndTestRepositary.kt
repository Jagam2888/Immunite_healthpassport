package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.TestReport
import com.cmg.vaccine.database.Vaccine
import com.cmg.vaccine.network.BlockChainAPi
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import okhttp3.ResponseBody

class VaccineAndTestRepositary(
        private val database: AppDatabase,
        private val api:MyApi
):SafeAPIRequest() {

    fun gettestReport(id:Int):TestReport{
        return database.getDao().getTestReport(id)
    }

    /*suspend fun downloadFile(recordId:String):ResponseBody{
        return apiRequest {
            api.downLoadDynamicUrl(recordId)
        }
    }*/

    /*suspend fun downLoadDynamic(url:String):ResponseBody{
        return apiRequest {
            api.downLoadDynamicUrl(url)
        }
    }*/

    fun getApi():MyApi{
        return api
    }
}