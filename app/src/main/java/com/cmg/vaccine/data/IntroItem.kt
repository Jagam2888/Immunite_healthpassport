package com.cmg.vaccine.data

import android.icu.text.CaseMap.Title




class IntroItem {
    constructor(introTitle: String?, desc: String?, introImg: Int) {
        this.introTitle = introTitle
        this.desc = desc
        this.introImg = introImg
    }

    var introTitle:String?=null
    var desc:String?=null
    var introImg:Int=0


    fun setTitle(introTitle: String) {
        this.introTitle = introTitle
    }

    fun setDescription(desc: String) {
        this.desc = desc
    }

    fun setScreenImg(introImg: Int) {
        this.introImg = introImg
    }

    fun getTitle(): String? {
        return introTitle
    }

    fun getDescription(): String? {
        return desc
    }

    fun getScreenImg(): Int {
        return introImg
    }
}