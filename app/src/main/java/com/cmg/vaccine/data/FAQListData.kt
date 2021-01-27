package com.cmg.vaccine.data

internal object FAQListData {
    val data: LinkedHashMap<String, List<String>>
        get() {
            val expandableListDetail =
                LinkedHashMap<String, List<String>>()
            val questionOne: MutableList<String> =
                ArrayList()
            questionOne.add("Sample 1")

            val questionTwo: MutableList<String> = ArrayList()
            questionTwo.add("Sample 2")

            val questionThree: MutableList<String> = ArrayList()
            questionThree.add("Sample 3")

            val questionFour: MutableList<String> = ArrayList()
            questionFour.add("Sample 4")

            val questionFive: MutableList<String> = ArrayList()
            questionFive.add("Sample 5")

            expandableListDetail["How many countries currently have travel restrictions?"] = questionOne
            expandableListDetail["Do quarantine requirements count as travel restrictions?"] = questionTwo
            expandableListDetail["Do I need to get test before travel?"] = questionThree
            expandableListDetail["Am I required to quarantine after travel?"] = questionFour
            expandableListDetail["What Should I do if I tested Positive?"] = questionFive
            return expandableListDetail
        }
}