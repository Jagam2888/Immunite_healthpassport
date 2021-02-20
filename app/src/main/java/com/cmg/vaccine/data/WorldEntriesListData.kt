package com.cmg.vaccine.data

internal object WorldEntriesListData {

    val WorldCountryData: LinkedHashMap<String, List<String>>
        get() {
            val expandableListDetail =
                LinkedHashMap<String, List<String>>()
            val questionOne: MutableList<String> =
                ArrayList()
            questionOne.add("United State has restricted the entry of all foreign nationals except those with prior approval from the government. Travelers with approval from low-risk countries will be issued with a 7-day SHN (Stay-Home-Notice), or no SHN from September 1.")

            val questionTwo: MutableList<String> = ArrayList()
            questionTwo.add("Sample 2")

            val questionThree: MutableList<String> = ArrayList()
            questionThree.add("Covid-19 Vaccine1 (Pfizer)")
            questionThree.add("Covid-19 Vaccine2 (Pfizer)")


            val questionFour: MutableList<String> = ArrayList()
            questionFour.add("Sample 4")

            val questionFive: MutableList<String> = ArrayList()
            questionFive.add("Sample 5")

            expandableListDetail["Entry Requirements"] = questionOne
            expandableListDetail["Test"] = questionTwo
            expandableListDetail["Vaccine"] = questionThree
            expandableListDetail["Personal Profile Details"] = questionFour

            return expandableListDetail
        }
}
