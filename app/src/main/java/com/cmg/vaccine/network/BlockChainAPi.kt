package com.cmg.vaccine.network

import android.content.Context
import com.cmg.vaccine.model.response.GetTestReportBlockChainResponse
import com.cmg.vaccine.model.response.GetVaccineBlockChainResponse
import com.cmg.vaccine.util.Passparams
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BlockChainAPi {

    @GET(Passparams.GET_TEST_REPORT_LIST_BLOCK_CHAIN)
    suspend fun getTestReportList(@Query("privateKey") key:String):Response<GetTestReportBlockChainResponse>

    @GET(Passparams.GET_VACCINE_LIST_BLOCK_CHAIN)
    suspend fun getVaccineList(@Query("privateKey") key:String):Response<GetVaccineBlockChainResponse>


    companion object{
        operator fun invoke(
            context: Context
        ) : BlockChainAPi{
            return RetrofitBlockChainClientInstance(context).create(BlockChainAPi::class.java)
        }
    }
}