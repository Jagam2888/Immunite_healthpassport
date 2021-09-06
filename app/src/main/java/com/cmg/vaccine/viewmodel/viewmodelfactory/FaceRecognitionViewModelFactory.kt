package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.FaceRecognitionRepositary
import com.cmg.vaccine.viewmodel.FaceRecognitionViewModel

/**
 * Created by jagad on 9/6/2021
 */
class FaceRecognitionViewModelFactory(
    private val faceRecognitionRepositary: FaceRecognitionRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FaceRecognitionViewModel(faceRecognitionRepositary) as T
    }
}