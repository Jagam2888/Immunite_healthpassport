package com.cmg.vaccine.data

import androidx.databinding.InverseMethod
import com.cmg.vaccine.R

@InverseMethod("buttonIdToGender")
fun genderToButtonId(gender: Gender) : Int?{
    var selectedItem = -1
    gender?.run {
        selectedItem = when(this){
            Gender.FEMALE -> R.id.btn_female
            Gender.MALE -> R.id.btn_male
            Gender.Other -> R.id.btn_other
        }
    }
    return selectedItem
}

fun buttonIdToGender(selectedItem : Int) : Gender?{
    return when(selectedItem){
        R.id.btn_male -> Gender.MALE
        R.id.btn_female -> Gender.FEMALE
        R.id.btn_other -> Gender.Other
        else -> null
    }
}