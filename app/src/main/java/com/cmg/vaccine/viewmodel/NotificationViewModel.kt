package com.cmg.vaccine.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.Notification
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.NotificationRepositary
import kotlin.time.milliseconds

class NotificationViewModel(
    private val repositary: NotificationRepositary
):ViewModel() {

    /*val _notificationList = MutableLiveData<List<Notification>>()
    val notoficationList:LiveData<List<Notification>>
        get() = _notificationList

    fun setNotificationList(list:List<Notification>){
        _notificationList.value = list
    }*/


    var _messageList = MutableLiveData<List<Notification>>()
    val messageList:LiveData<List<Notification>>
    get() = _messageList

    var unReadCount = ObservableField<String>()

    var listener:SimpleListener?=null

    fun deleteNotificationByGroup(group: String){
        repositary.deleteNotificationByFroup(group)
        listener?.onSuccess("success")
    }

    fun loadMesaage(group:String){
        val list = repositary.getNotificationByGroup(group)
        _messageList.value = list
        if (!list.isNullOrEmpty()){
            val count = repositary.getUnReadCount(group)
            if (count != null) {
                unReadCount.set(count.toString())
            }else{
                unReadCount.set("0")
            }
        }else{
            unReadCount.set("0")
        }
    }

    fun updateNotificationReadStatus(id:Int){
        repositary.updateNotificationReadStatus(id)
    }


}