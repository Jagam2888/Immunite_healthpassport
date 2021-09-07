package com.tzutalin.dlib;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by darrenl on 2016/4/22.
 * Modified by Gaurav on Feb 23, 2018
 */

public final class Constants {
    private Constants() {
        // Constants should be prive
    }

    public static String getDLibDirectoryPath(Context context) {
        //File sdcard = Environment.getExternalStorageDirectory();
        //String targetPath = sdcard.getAbsolutePath() + File.separator + "dlib_rec_example";
        String path = context.getCacheDir().getPath();
        String targetPath = path + File.separator + "dlib_rec_example";
        return targetPath;
    }

    public static String getDLibImageDirectoryPath(Context context) {
        String targetPath = getDLibDirectoryPath(context)+ File.separator + "images";
        return targetPath;
    }

    public static String getFaceShapeModelPath(Context context) {
        String targetPath = getDLibDirectoryPath(context) + File.separator + "shape_predictor_5_face_landmarks.dat";
        return targetPath;
    }

    public static String getFaceDescriptorModelPath(Context context) {
        String targetPath = getDLibDirectoryPath(context) + File.separator + "dlib_face_recognition_resnet_model_v1.dat";
        return targetPath;
    }

    static void cachPath(){

    }
}
