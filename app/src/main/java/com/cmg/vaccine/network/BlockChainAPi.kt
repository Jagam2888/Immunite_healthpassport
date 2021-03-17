package com.cmg.vaccine.network

import android.content.Context
import com.cmg.vaccine.util.Passparams
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface BlockChainAPi {

    /*@GET(Passparams.GET_TEST_REPORT_LIST_BLOCK_CHAIN)
    suspend fun getTestReportList(@Query("privateKey") key:String):Response<GetTestReportBlockChainResponse>

    @GET(Passparams.GET_VACCINE_LIST_BLOCK_CHAIN)
    suspend fun getVaccineList(@Query("privateKey") key:String):Response<GetVaccineBlockChainResponse>*/

    @Streaming
    @GET(Passparams.DOWNLOAD_TEST_REPORT_FILE)
    suspend fun downloadTestReportFile(@Query("labOrderTxn")recordId:String): Response<ResponseBody>

    @Streaming
    @GET
    fun downLoadDynamicUrl(@Url fileUrl:String):Call<ResponseBody>
    companion object{
        operator fun invoke(
            context: Context
        ) : BlockChainAPi{
            return RetrofitBlockChainClientInstance(context).create(BlockChainAPi::class.java)
        }
    }
}