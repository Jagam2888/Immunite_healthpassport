package com.cmg.vaccine.data

data class UserProfileList(
    val userName:String,
    val profile:String,
    val subId:String,
    val privateKey:String,
    val dob:String
){
    override fun toString(): String {
        return "$userName($profile)"
    }
}
