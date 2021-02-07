package com.cmg.vaccine.viewmodel

import android.content.Context
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
import com.cmg.vaccine.model.request.UpdateProfileReq
import com.cmg.vaccine.model.request.UpdateProfileReqData
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
    var city = ObservableField<String>()
    var state = ObservableField<String>()
    var passportNumber:MutableLiveData<String> = MutableLiveData()
    var idNo:MutableLiveData<String> = MutableLiveData()
    var contactNumber:MutableLiveData<String> = MutableLiveData()
    var email:MutableLiveData<String> = MutableLiveData()
    var masterPrivateKey:String?=null
    var dependentPrivateKey:String?=null

    var relationshipItemPos = ObservableInt()
    var countryItemPos = ObservableInt()
    var nationalityItemPos = ObservableInt()
    var isChecked = ObservableBoolean()
    var isCheckedPrincipleAddress = ObservableBoolean()

    var parentAddress:String?=null
    var parentcity:String?=null
    var parentState:String?=null
    var dependent:Dependent?=null

    var genderEnum: Gender = Gender.F
    var listener:SimpleListener?=null

    init {
        val parentUser = repositary.getUserData()
        if (parentUser != null) {
            masterPrivateKey = parentUser.privateKey
            parentAddress = parentUser.address
            parentcity = parentUser.city
            parentState = parentUser.state
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
            listener?.onStarted()
            val relationShips = view.context.resources.getStringArray(R.array.relationships)
            val relationShip = relationShips.get(relationshipItemPos.get())

            val countries = view.context.resources.getStringArray(R.array.country)
            val country = countries.get(countryItemPos.get())

            val nationalities = view.context.resources.getStringArray(R.array.country)
            val nationality = nationalities.get(nationalityItemPos.get())

            val dependentRegReq = DependentRegReq()

            val dependentRegReqData = DependentRegReqData()
            dependentRegReqData.firstName = fullName.value
            dependentRegReqData.countryCode = "MY"
            dependentRegReqData.mobileNumber = contactNumber.value
            dependentRegReqData.email = email.value
            dependentRegReqData.relationship = relationShip
            dependentRegReqData.nationalityCountry = "MY"
            dependentRegReqData.gender = genderEnum.name
            dependentRegReqData.dob = dob.value
            dependentRegReqData.residentialAddress = address.get()
            dependentRegReqData.townCity = city.get()
            dependentRegReqData.provinceState = state.get()
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
                            state.get(),
                            relationShip,
                            address.get(),
                            city.get()
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

    fun loadProfileData(context: Context,privateKey:String){
        dependentPrivateKey = privateKey
        dependent = repositary.getDependent(dependentPrivateKey!!)
        val relationShipList = context.resources.getStringArray(R.array.relationships).toList()
        //val relationShip = relationShipList.get(relationshipItemPos.get())

        if (dependent != null){
            fullName.value = dependent?.firstName
            address.set(dependent?.residentialAddress)
            email.value = dependent?.email
            contactNumber.value = dependent?.mobileNumber
            city.set(dependent?.townCity)
            state.set(dependent?.provinceState)
            passportNumber.value = dependent?.passportNo
            idNo.value = dependent?.idNo
            dob.value = dependent?.dob
            relationshipItemPos.set(selectedRelationShipPosition(dependent?.relationship!!,relationShipList!!))

            dependent?.gender.run {
                genderEnum = when(this){
                    "M" -> Gender.M
                    "F" -> Gender.F
                    else -> Gender.O
                }
            }
        }
    }

    fun updateProfile(view: View){
        if (isChecked.get()){
            listener?.onStarted()
            Couritnes.main {
                try {
                    val updateProfileReq = UpdateProfileReq()
                    val updateProfileReqData = UpdateProfileReqData()

                    updateProfileReqData.firstName = fullName.value
                    updateProfileReqData.nationalityCountry = "MY"
                    updateProfileReqData.dob = dob.value
                    updateProfileReqData.privateKey = dependentPrivateKey
                    updateProfileReqData.passportNo = passportNumber.value
                    updateProfileReqData.gender = genderEnum.name
                    updateProfileReqData.idNo = idNo.value
                    updateProfileReqData.residentialAddress = address.get()
                    updateProfileReqData.townCity = city.get()
                    updateProfileReqData.provinceState = state.get()

                    updateProfileReq.data = updateProfileReqData

                    val response = repositary.updateDependentProfile(updateProfileReq)
                    if (response.StatusCode == 1){
                        var dependent = repositary.getDependent(dependentPrivateKey!!)
                        val relationShipList = view.context.resources.getStringArray(R.array.relationships)
                        val relationShip = relationShipList.get(relationshipItemPos.get())

                        dependent?.residentialAddress = address.get()
                        dependent?.townCity = city.get()
                        dependent?.provinceState = state.get()
                        dependent?.dob = dob.value
                        dependent?.firstName = fullName.value
                        dependent?.idNo = idNo.value
                        dependent?.passportNo = passportNumber.value
                        dependent?.mobileNumber = contactNumber.value
                        dependent?.relationship = relationShip
                        dependent?.gender = genderEnum.name

                        repositary.updateDependent(dependent!!)
                        listener?.onSuccess(response.Message)
                    }else{
                        listener?.onFailure(response.Message)
                    }

                }catch (e:APIException){
                    listener?.onFailure(e.message!!)
                }catch (e:NoInternetException){
                    listener?.onFailure(e.message!!)
                }catch (e:SocketTimeoutException){
                    listener?.onFailure(e.message!!)
                }
            }
        }else{
            listener?.onFailure("Please accept Terms and conditions")
        }
    }
}