package com.cmg.vaccine.listener

interface AddSelectedRVListener {
    //fun setClick(selectedCountryName: String,selectedCountryCode: String)
    fun onClickSelectedItem(selectedCountryName: String,selectedCountryCode: String,type:String)

}