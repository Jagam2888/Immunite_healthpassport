package com.cmg.vaccine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.model.response.Notification

class NotificationViewModel:ViewModel() {

    val _notificationList = MutableLiveData<List<Notification>>()
    val notoficationList:LiveData<List<Notification>>
        get() = _notificationList

    fun setNotificationList(list:List<Notification>){
        _notificationList.value = list
    }
}