package com.cmg.vaccine.viewmodel

import android.content.Context
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.R
import com.cmg.vaccine.data.Gender
import com.cmg.vaccine.database.Countries
import com.cmg.vaccine.database.Dependent
import com.cmg.vaccine.database.User
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.request.DependentRegReq
import com.cmg.vaccine.model.request.DependentRegReqData
import com.cmg.vaccine.model.request.UpdateProfileReq
import com.cmg.vaccine.model.request.UpdateProfileReqData
import com.cmg.vaccine.repositary.DependentRepositary
import com.cmg.vaccine.util.*
import java.net.SocketTimeoutException

class DependentViewModel(
    private val repositary: DependentRepositary
):ViewModel() {


    var principalName:MutableLiveData<String> = MutableLiveData()
    var fullName:MutableLiveData<String> = MutableLiveData()
    var dob:MutableLiveData<String> = MutableLiveData()
    var dobTime:MutableLiveData<String> = MutableLiveData()
    var address = ObservableField<String>()
    var city = ObservableField<String>()
    var state = ObservableField<String>()
    var passportNumber:MutableLiveData<String> = MutableLiveData()
    var country:MutableLiveData<String> = MutableLiveData()
    var gender:MutableLiveData<String> = MutableLiveData()
    var idNo:MutableLiveData<String> = MutableLiveData()
    var idType:MutableLiveData<String> = MutableLiveData()
    var contactNumber:MutableLiveData<String> = MutableLiveData()
    var email:MutableLiveData<String> = MutableLiveData()
    var reTypeEmail:MutableLiveData<String> = MutableLiveData()
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

    var _countries:MutableLiveData<List<Countries>> = MutableLiveData()

    val countries: LiveData<List<Countries>>
        get() = _countries

    var countryList:List<Countries>?=null
    var selectedItemNationalityCode = ObservableInt()
    var selectedItemBirthPlaceCode = ObservableInt()
    var selectedItemContactCode = ObservableField<String>()
    var selectedItemIdTYpe = ObservableInt()
    var countryCode:MutableLiveData<Int> = MutableLiveData()

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
        countryList = repositary.getAllCountriesDB()
        _countries.value = countryList
    }

    fun setCurrentCountry(country:String){
        countryList = repositary.getAllCountriesDB()
        if (!countryList.isNullOrEmpty()){
            val pos = selectedCurrentCountry(country,countryList!!)
            selectedItemBirthPlaceCode.set(pos)
            selectedItemNationalityCode.set(pos)
            //selectedItemNationalityCode.set(5)
        }
    }

    fun onClick(view:View) {
        if (isChecked.get()) {
            listener?.onStarted()
            val relationShips = view.context.resources.getStringArray(R.array.relationships)
            val relationShip = relationShips.get(relationshipItemPos.get())

            var placeBirth = ""
            if (!countryList.isNullOrEmpty()){
                placeBirth = countryList?.get(selectedItemBirthPlaceCode.get())?.countryCodeAlpha!!
            }

            var nationality = ""
            if (!countryList.isNullOrEmpty()){
                nationality = countryList?.get(selectedItemNationalityCode.get())?.countryCodeAlpha!!
            }

            val idTypeList = view.context.resources.getStringArray(R.array.id_type)
            idType.value = idTypeList[selectedItemIdTYpe.get()]



            val dependentRegReq = DependentRegReq()

            val dependentRegReqData = DependentRegReqData()
            dependentRegReqData.firstName = fullName.value
            dependentRegReqData.countryCode = selectedItemContactCode.get()
            dependentRegReqData.mobileNumber = contactNumber.value
            dependentRegReqData.email = email.value
            dependentRegReqData.relationship = relationShip
            dependentRegReqData.nationalityCountry = nationality
            dependentRegReqData.gender = genderEnum.name
            dependentRegReqData.dob = dob.value+" "+dobTime.value
            dependentRegReqData.placeOfBirth = placeBirth
            dependentRegReqData.idType = idType.value
            /*dependentRegReqData.residentialAddress = address.get()
            dependentRegReqData.townCity = city.get()
            dependentRegReqData.provinceState = state.get()*/
            dependentRegReqData.passportNo = passportNumber.value
            dependentRegReqData.idNo = idNo.value
            dependentRegReqData.masterSubsId = repositary.getParentSubId()

            dependentRegReq.data = dependentRegReqData

            Couritnes.main {
                try {
                    val response = repositary.dependentSignUp(dependentRegReq)
                    if (response.StatusCode == 1) {
                        /*val dependent = Dependent(
                            selectedItemContactCode.get(),
                            dob.value,
                            email.value!!,
                            fullName.value,
                            genderEnum.name,
                            idNo.value,
                            repositary.getParentSubId()!!,
                            response.DependentPrivateKey,
                            contactNumber.value,
                            nationality,
                            passportNumber.value,
                            state.get(),
                            relationShip,
                            address.get(),
                            city.get()
                        )*/
                            val dependent = Dependent(
                                    selectedItemContactCode.get(),
                                    dob.value,
                                    dobTime.value,
                                    email.value,
                                    fullName.value,
                                    genderEnum.name,
                                    idNo.value,
                                    idType.value,
                                    repositary.getParentSubId(),
                                    response.SubsId,
                                    contactNumber.value,
                                    nationality,
                                    passportNumber.value,
                                    placeBirth,
                                    relationShip
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

    fun loadProfileData(context: Context,subsId:String){
        dependentPrivateKey = subsId
        dependent = repositary.getDependent(subsId!!)
        val relationShipList = context.resources.getStringArray(R.array.relationships).toList()
        //val relationShip = relationShipList.get(relationshipItemPos.get())

        if (dependent != null){
            fullName.value = dependent?.firstName
            //address.set(dependent?.residentialAddress)
            email.value = dependent?.email
            contactNumber.value = dependent?.mobileNumber
            //city.set(dependent?.townCity)
            //state.set(dependent?.provinceState)
            passportNumber.value = dependent?.passportNo
            idNo.value = dependent?.idNo
            dob.value = dependent?.dob
            dobTime.value = dependent?.dobTime
            country.value = dependent?.nationalityCountry
            relationshipItemPos.set(selectedRelationShipPosition(dependent?.relationship!!,relationShipList!!))


            if (!dependent?.countryCode.isNullOrEmpty())
                countryCode.value = dependent?.countryCode?.toInt()

            selectedItemNationalityCode.set(selectedCurrentCountry(dependent?.nationalityCountry!!,countryList!!))
            selectedItemBirthPlaceCode.set(selectedCurrentCountry(dependent?.placeOfBirth!!,countryList!!))

            dependent?.gender.run {
                genderEnum = when(this){
                    "M" -> Gender.M
                    "F" -> Gender.F
                    else -> Gender.O
                }
            }

            if (dependent?.gender == "M"){
                gender.value = "Male"
            }else if (dependent?.gender == "F"){
                gender.value = "Female"
            }else{
                gender.value = "Other"
            }
        }
    }

    fun updateProfile(view: View){
        if (isChecked.get()){
            listener?.onStarted()
            Couritnes.main {
                try {

                    val relationShips = view.context.resources.getStringArray(R.array.relationships)
                    val relationShip = relationShips.get(relationshipItemPos.get())

                    var placeBirth = ""
                    if (!countryList.isNullOrEmpty()){
                        placeBirth = countryList?.get(selectedItemBirthPlaceCode.get())?.countryCodeAlpha!!
                    }

                    var nationality = ""
                    if (!countryList.isNullOrEmpty()){
                        nationality = countryList?.get(selectedItemNationalityCode.get())?.countryCodeAlpha!!
                    }

                    val idTypeList = view.context.resources.getStringArray(R.array.id_type)
                    idType.value = idTypeList[selectedItemIdTYpe.get()]


                    val updateProfileReq = UpdateProfileReq()
                    val updateProfileReqData = UpdateProfileReqData()

                    updateProfileReqData.firstName = fullName.value
                    updateProfileReqData.nationalityCountry = nationality
                    updateProfileReqData.dob = dob.value+" "+dobTime.value
                    updateProfileReqData.placeOfBirth = placeBirth
                    updateProfileReqData.countryCode = selectedItemContactCode.get()
                    updateProfileReqData.passportNo = passportNumber.value
                    updateProfileReqData.gender = genderEnum.name
                    updateProfileReqData.idNo = idNo.value
                    updateProfileReqData.subsId = dependent?.subsId
                    updateProfileReqData.masterSubsId = dependent?.masterSubsId
                    updateProfileReqData.relationship = relationShip
                    updateProfileReqData.email = email.value
                    updateProfileReqData.mobileNumber = contactNumber.value
                    updateProfileReqData.idType = idType.value
                    /*updateProfileReqData.residentialAddress = address.get()
                    updateProfileReqData.townCity = city.get()
                    updateProfileReqData.provinceState = state.get()*/

                    updateProfileReq.data = updateProfileReqData

                    val response = repositary.updateDependentProfile(updateProfileReq)
                    if (response.StatusCode == 1){
                        var dependent = repositary.getDependent(dependentPrivateKey!!)
                        val relationShipList = view.context.resources.getStringArray(R.array.relationships)
                        val relationShip = relationShipList.get(relationshipItemPos.get())

                        //dependent?.residentialAddress = address.get()
                        //dependent?.townCity = city.get()
                        //dependent?.provinceState = state.get()
                        dependent?.dob = dob.value
                        dependent?.dobTime = dobTime.value
                        dependent?.firstName = fullName.value
                        dependent?.idNo = idNo.value
                        dependent?.idType = idType.value
                        dependent?.passportNo = passportNumber.value
                        dependent?.mobileNumber = contactNumber.value
                        dependent?.relationship = relationShip
                        dependent?.gender = genderEnum.name
                        dependent?.placeOfBirth = placeBirth
                        dependent?.nationalityCountry = nationality

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