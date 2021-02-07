package com.cmg.vaccine.data

import androidx.databinding.InverseMethod
import com.cmg.vaccine.R

@InverseMethod("buttonIdToGender")
fun genderToButtonId(gender: Gender) : Int?{
    var selectedItem = -1
    gender?.run {
        selectedItem = when(this){
            Gender.F -> R.id.btn_female
            Gender.M -> R.id.btn_male
            Gender.O -> R.id.btn_other
        }
    }
    return selectedItem
}

fun buttonIdToGender(selectedItem : Int) : Gender?{
    return when(selectedItem){
        R.id.btn_male -> Gender.M
        R.id.btn_female -> Gender.F
        R.id.btn_other -> Gender.O
        else -> null
    }
}