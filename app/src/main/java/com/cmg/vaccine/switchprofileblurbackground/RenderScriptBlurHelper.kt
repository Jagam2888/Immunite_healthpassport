package com.cmg.vaccine.switchprofileblurbackground

import android.R.attr
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.renderscript.*
import android.util.Log


class RenderScriptBlurHelper {

    fun doBlur(sentBitmap: Bitmap, radius: Int, canReuseInBitmap: Boolean, context: Context):Bitmap?{
        var bitmap:Bitmap?=null
        bitmap = if (canReuseInBitmap) {
            sentBitmap;
        } else {
            sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (bitmap?.config == Bitmap.Config.RGB_565) {
            // RenderScript hates RGB_565 so we convert it to ARGB_8888
            // (see http://stackoverflow.com/questions/21563299/
            // defect-of-image-with-scriptintrinsicblur-from-support-library)
            bitmap = convertRGB565toARGB888(bitmap);
        }
        try {
            val rs = RenderScript.create(context)
            val input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT)
            val output = Allocation.createTyped(rs, input.type)
            val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            script.setRadius(attr.radius.toFloat())
            script.setInput(input)
            script.forEach(output)
            output.copyTo(bitmap)
            return bitmap!!
        } catch (e: RSRuntimeException) {
            Log.e(TAG, "RenderScript known error : https://code.google.com/p/android/issues/detail?id=71347 "
                    + "continue with the FastBlur approach.")
        }

        return bitmap
    }

    private fun convertRGB565toARGB888(bitmap: Bitmap): Bitmap? {
        return bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }
}