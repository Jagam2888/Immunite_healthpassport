package com.cmg.vaccine.network

import android.content.Context


interface BlockChainAPi {

    /*@GET(Passparams.GET_TEST_REPORT_LIST_BLOCK_CHAIN)
    suspend fun getTestReportList(@Query("privateKey") key:String):Response<GetTestReportBlockChainResponse>

    @GET(Passparams.GET_VACCINE_LIST_BLOCK_CHAIN)
    suspend fun getVaccineList(@Query("privateKey") key:String):Response<GetVaccineBlockChainResponse>*/


    companion object{
        operator fun invoke(
            context: Context
        ) : BlockChainAPi{
            return RetrofitBlockChainClientInstance(context).create(BlockChainAPi::class.java)
        }
    }
}