package com.cmg.vaccine.repositary

import android.content.Context
import android.graphics.Bitmap
import com.cmg.vaccine.R
import com.cmg.vaccine.util.copyFileFromRawToOthers
import com.cmg.vaccine.util.drawResizedBitmap
import com.tzutalin.dlib.Constants
import com.tzutalin.dlib.FaceRec
import java.io.File

/**
 * Created by jagad on 9/6/2021
 */
class FaceRecognitionRepositary(
    private val context: Context
) {

    var mFaceRec:FaceRec?=null

    fun initializeFace():String{
        val folder = File(Constants.getDLibDirectoryPath(context))
        var success = false
        /*if (!folder.exists()){
            success = folder.mkdirs()
        }*/
        var msg:String = ""
        if (folder.exists()){
            val imageFolder = File(Constants.getDLibImageDirectoryPath(context))
            imageFolder.mkdirs()
            if (!File(Constants.getFaceShapeModelPath(context)).exists()) {
                copyFileFromRawToOthers(
                    context,
                    R.raw.shape_predictor_5_face_landmarks,
                    Constants.getFaceShapeModelPath(context)
                )
            }
            if (!File(Constants.getFaceDescriptorModelPath(context)).exists()) {
                copyFileFromRawToOthers(
                    context,
                    R.raw.dlib_face_recognition_resnet_model_v1,
                    Constants.getFaceDescriptorModelPath(context)
                )
            }
            //mFaceRec = FaceRec(Constants.getDLibDirectoryPath(context))
            //mFaceRec?.train()
            msg = "1success"
        }else{
            msg = "1Failed"
        }
        return msg
    }

    fun recogizeFace(bitmap: Bitmap):ArrayList<String>{
        val mCroppedBitMap = Bitmap.createBitmap(500,500,Bitmap.Config.ARGB_8888)
        context.drawResizedBitmap(bitmap,mCroppedBitMap)
        mFaceRec = FaceRec(Constants.getDLibDirectoryPath(context))
        mFaceRec?.train()
        val results = mFaceRec?.recognize(mCroppedBitMap)

        val names = ArrayList<String>()
        for (n in results!!) {
            names.add(n.label)
        }
        return names
    }
}