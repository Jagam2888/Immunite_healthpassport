package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.FaceDetectRepositary
import com.cmg.vaccine.viewmodel.FaceDetectViewModel


/**
 * Created by jagad on 9/6/2021
 */
class FaceDetectViewModelFactory(
    private val faceDetectViewModelRepositary: FaceDetectRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FaceDetectViewModel(faceDetectViewModelRepositary) as T
    }
}