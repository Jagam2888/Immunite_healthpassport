package com.cmg.vaccine.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.LoginPin
import com.cmg.vaccine.database.User
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.RestoreBackupOptionListRepositary
import com.cmg.vaccine.util.*
import io.paperdb.Paper
import org.json.JSONException
import java.lang.Exception
import java.net.SocketTimeoutException

class RestoreBackupOptionListViewModel(
        private val repositary: RestoreBackupOptionListRepositary
):ViewModel() {

    var _userData:MutableLiveData<User> = MutableLiveData()

    val userData:LiveData<User>
    get() = _userData

    var _loginPin:MutableLiveData<LoginPin> = MutableLiveData()

    val loginPin:LiveData<LoginPin>
    get() = _loginPin

    var listener:SimpleListener?=null

    fun getUser(context: Context){
        val user = repositary.getUserData()
        if (user != null){
            _userData.value = user
            repositary.saveSubId(user.parentSubscriberId!!)
            updateUUID(context)
        }

        val getLogin = repositary.getLogin()
        if (getLogin != null){
            _loginPin.value = getLogin
        }
    }

    private fun updateFCMToken(){
        val token = Paper.book().read<String>(Passparams.FCM_TOKEN,"")
        Couritnes.main {
            try {
                val response = repositary.updateFCMToken(repositary.getPatientSubId()!!,token)
                if (response.StatusCode == 1){
                    //listener?.onSuccess(response.Message)
                    listener?.onSuccess("Setup Manually Success")
                }else{
                    listener?.onFailure(response.Message)
                }
            }catch (e:APIException){
                listener?.onFailure(e.message!!)
            }catch (e:NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e:SocketTimeoutException){
                listener?.onFailure(e.message!!)
            }catch (e:JSONException){
                listener?.onFailure("invalid")
            }catch (e: Exception){
                listener?.onFailure(e.message!!)
            }

        }
    }

    private fun updateUUID(context: Context){
        Couritnes.main {
            try {
                val response = repositary.updateUUID(repositary.getPatientSubId()!!,context.getDeviceUUID()!!)
                if (response.StatusCode == 1){
                    //listener?.onSuccess(response.Message)
                    updateFCMToken()
                }else{
                    listener?.onFailure(response.Message)
                }
            }catch (e: APIException){
                listener?.onFailure(e.message!!)
            }catch (e: NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e: SocketTimeoutException){
                listener?.onFailure(e.message!!)
            }catch (e: JSONException){
                listener?.onFailure("invalid")
            }
        }
    }
}