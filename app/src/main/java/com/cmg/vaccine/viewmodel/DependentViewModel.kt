package com.cmg.vaccine.viewmodel

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.R
import com.cmg.vaccine.data.Gender
import com.cmg.vaccine.database.Dependent
import com.cmg.vaccine.database.User
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.request.DependentRegReq
import com.cmg.vaccine.model.request.DependentRegReqData
import com.cmg.vaccine.repositary.DependentRepositary
import com.cmg.vaccine.util.APIException
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import com.cmg.vaccine.util.selectedRelationShipPosition
import java.net.SocketTimeoutException

class DependentViewModel(
    private val repositary: DependentRepositary
):ViewModel() {


    var principalName:MutableLiveData<String> = MutableLiveData()
    var fullName:MutableLiveData<String> = MutableLiveData()
    var dob:MutableLiveData<String> = MutableLiveData()
    var address = ObservableField<String>()
    var city:MutableLiveData<String> = MutableLiveData()
    var state:MutableLiveData<String> = MutableLiveData()
    var passportNumber:MutableLiveData<String> = MutableLiveData()
    var idNo:MutableLiveData<String> = MutableLiveData()
    var contactNumber:MutableLiveData<String> = MutableLiveData()
    var email:MutableLiveData<String> = MutableLiveData()
    var masterPrivateKey:String?=null

    var relationshipItemPos = ObservableInt()
    var countryItemPos = ObservableInt()
    var nationalityItemPos = ObservableInt()
    var isChecked = ObservableBoolean()
    var isCheckedPrincipleAddress = ObservableBoolean()

    var parentAddress:String?=null
    var dependent:Dependent?=null

    var genderEnum: Gender = Gender.FEMALE
    var listener:SimpleListener?=null

    init {
        val parentUser = repositary.getUserData()
        if (parentUser != null) {
            masterPrivateKey = parentUser.privateKey
            parentAddress = parentUser.address
            principalName.value = parentUser.fullName

            /*if (isCheckedPrincipleAddress.get()){
                address.value = parentUser.address
            }else{
                address.value = ""
            }*/
        }
    }

    fun onClick(view:View) {
        if (isChecked.get()) {
            val relationShips = view.context.resources.getStringArray(R.array.relationships)
            val relationShip = relationShips.get(relationshipItemPos.get())

            val countries = view.context.resources.getStringArray(R.array.country)
            val country = countries.get(countryItemPos.get())

            val nationalities = view.context.resources.getStringArray(R.array.country)
            val nationality = nationalities.get(nationalityItemPos.get())

            val dependentRegReq = DependentRegReq()

            val dependentRegReqData = DependentRegReqData()
            dependentRegReqData.firstName = fullName.value
            dependentRegReqData.countryCode = "60"
            dependentRegReqData.mobileNumber = contactNumber.value
            dependentRegReqData.email = email.value
            dependentRegReqData.relationship = relationShip
            dependentRegReqData.nationalityCountry = "MY"
            if (genderEnum.name == "MALE") {
                dependentRegReqData.gender = "M"
            } else if (genderEnum.name == "FEMALE") {
                dependentRegReqData.gender = "F"
            } else {
                dependentRegReqData.gender = "O"
            }

            dependentRegReqData.dob = dob.value
            dependentRegReqData.residentialAddress = address.get()
            dependentRegReqData.townCity = city.value
            dependentRegReqData.provinceState = state.value
            dependentRegReqData.passportNo = passportNumber.value
            dependentRegReqData.idNo = idNo.value
            dependentRegReqData.masterPrivateKey = masterPrivateKey

            dependentRegReq.data = dependentRegReqData

            Couritnes.main {
                try {
                    val response = repositary.dependentSignUp(dependentRegReq)
                    if (response.StatusCode == 1) {
                        val dependent = Dependent(
                            country,
                            dob.value,
                            email.value!!,
                            fullName.value,
                            genderEnum.name,
                            idNo.value,
                            masterPrivateKey!!,
                            response.DependentPrivateKey,
                            contactNumber.value,
                            nationality,
                            passportNumber.value,
                            state.value,
                            relationShip,
                            address.get(),
                            city.value
                        )
                        repositary.insertDependentSignUp(dependent)
                        listener?.onSuccess(response.Message)
                    } else {
                        listener?.onFailure(response.Message)
                    }
                } catch (e: APIException) {
                    listener?.onFailure(e.message!!)
                } catch (e: NoInternetException) {
                    listener?.onFailure(e.message!!)
                } catch (e: SocketTimeoutException) {
                    listener?.onFailure(e.message!!)
                }
            }

        }else{
            listener?.onFailure("Please accept Terms and conditions")
        }
    }

    fun loadProfileData(privateKey:String){
        dependent = repositary.getDependent(privateKey)

        if (dependent != null){
            fullName.value = dependent?.firstName
            address.set(dependent?.residentialAddress)
            email.value = dependent?.email
            contactNumber.value = dependent?.mobileNumber
            city.value = dependent?.townCity
            state.value = dependent?.provinceState
            passportNumber.value = dependent?.passportNo
            idNo.value = dependent?.idNo

            dependent?.gender.run {
                genderEnum = when(this){
                    "MALE" -> Gender.MALE
                    "FEMALE" -> Gender.FEMALE
                    else -> Gender.Other
                }
            }
        }
    }

    fun updateProfile(view: View){
        if (isChecked.get()){

            val relationShipList = view.resources.getStringArray(R.array.relationships).toList()

            dependent?.residentialAddress = address.get()
            dependent?.townCity = city.value
            dependent?.provinceState = state.value
            dependent?.dob = dob.value
            dependent?.firstName = fullName.value
            dependent?.idNo = idNo.value
            dependent?.passportNo = passportNumber.value
            dependent?.mobileNumber = contactNumber.value
            relationshipItemPos.set(selectedRelationShipPosition(dependent?.relationship!!,relationShipList!!))

            repositary.updateDependent(dependent!!)
            listener?.onSuccess("Update Success")
        }else{
            listener?.onFailure("Please accept Terms and conditions")
        }
    }
}