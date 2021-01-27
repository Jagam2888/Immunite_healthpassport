package com.cmg.vaccine.data

internal object TravelListData {

    val travelData: LinkedHashMap<String, List<String>>
        get() {
            val expandableListDetail =
                LinkedHashMap<String, List<String>>()
            val questionOne: MutableList<String> =
                ArrayList()
            questionOne.add("Starting on January 26, all travelers (including US citizens and transit passenger) must present a negative COVID-19 viral or antigen test result issued 72 hours prior to departure or have an evidence of having contracted and recovered from COVID-19 prior to boarding the plane.\\n\\nUS citizens and permanent residents arriving from the UK must present a medical certificate with a negative COVID-19 PCR or antigen test result issued within 72 hours prior to departure.")

            val questionTwo: MutableList<String> = ArrayList()
            questionTwo.add("Sample 2")

            val questionThree: MutableList<String> = ArrayList()
            questionThree.add("Sample 3")

            val questionFour: MutableList<String> = ArrayList()
            questionFour.add("Sample 4")

            val questionFive: MutableList<String> = ArrayList()
            questionFive.add("Sample 5")

            expandableListDetail["Entry Requirements"] = questionOne
            expandableListDetail["Certified Vaccine"] = questionTwo
            expandableListDetail["Quarantine Requirements"] = questionThree
            expandableListDetail["Entry Restrictions"] = questionFour
            expandableListDetail["Other Information"] = questionFive
            return expandableListDetail
        }
}