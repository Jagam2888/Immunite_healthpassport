package com.cmg.vaccine.util

import android.graphics.*
import android.os.Handler
import android.util.Log
import android.view.TextureView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.camera.core.CameraX.LensFacing
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.shunan.circularprogressbar.CircularProgressBar


class FaceTrackingAnalyzer internal constructor(
    private val tv: TextureView,
    iv: ImageView,
    lens: LensFacing,
    private val progressBar: CircularProgressBar,
    private val layoutVS: LinearLayout,
    private val defaultLayout: LinearLayout,
    result_cap: ImageView
) :
    ImageAnalysis.Analyzer {
    private val iv: ImageView
    private var bitmap: Bitmap? = null
    private var canvas: Canvas? = null
    private var linePaint: Paint? = null
    private var widthScaleFactor = 1.0f
    private var heightScaleFactor = 1.0f
    private var fbImage: FirebaseVisionImage? = null
    private val lens: LensFacing
    private var temp:Int=0
    private val handler: Handler = Handler()
    private var progressStatus = 0
    private var capture:Boolean = false
    private var result_cap:ImageView


    override fun analyze(image: ImageProxy, rotationDegrees: Int) {
        if (image == null || image.image == null) {
            return
        }
        val rotation = degreesToFirebaseRotation(rotationDegrees)
        //ip=image
        fbImage = FirebaseVisionImage.fromMediaImage(image.image!!, rotation)
        initDrawingUtils()
        initDetector()
    }

    private fun initDetector() {
        val detectorOptions = FirebaseVisionFaceDetectorOptions.Builder()
            .enableTracking()
            .build()
        val faceDetector = FirebaseVision.getInstance().getVisionFaceDetector(detectorOptions)
        faceDetector.detectInImage(fbImage!!)
            .addOnSuccessListener { firebaseVisionFaces: List<FirebaseVisionFace> ->
                if (!firebaseVisionFaces.isEmpty()) {
                    processFaces(firebaseVisionFaces)
                } else {
                    canvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY)
                }
            }.addOnFailureListener { e: Exception ->
                Log.i(
                    TAG, e.toString()
                )
            }
    }

    private fun initDrawingUtils() {
        bitmap = Bitmap.createBitmap(tv.width, tv.height, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap!!)
        linePaint = Paint()
        linePaint!!.setColor(Color.GREEN)
        linePaint!!.setStyle(Paint.Style.STROKE)
        linePaint!!.setStrokeWidth(2f)
        linePaint!!.setTextSize(40.0f)
        widthScaleFactor = canvas?.getWidth()!! / (fbImage?.bitmap?.width!! * 1.0f)
        heightScaleFactor = canvas?.getHeight()!! / (fbImage?.bitmap?.height!! * 1.0f)
    }

    private fun processFaces(faces: List<FirebaseVisionFace>) {
        for (face in faces) {
 /*           val box = Rect(
                translateX(face.boundingBox.left.toFloat()).toInt(),
                translateY(face.boundingBox.top.toFloat()).toInt(),
                translateX(face.boundingBox.right.toFloat()).toInt(),
                translateY(face.boundingBox.bottom.toFloat()).toInt()
            )
            canvas?.drawText(
                face.trackingId.toString(),
                translateX(face.boundingBox.centerX().toFloat()),
                translateY(face.boundingBox.centerY().toFloat()),
                linePaint!!
            )
            Log.i(
                TAG, "top: " + translateY(face.boundingBox.top.toFloat()).toInt()
                        + "left: " + translateX(face.boundingBox.left.toFloat()).toInt()
                        + "bottom: " + translateY(face.boundingBox.bottom.toFloat()).toInt()
                        + "right: " + translateX(face.boundingBox.right.toFloat()).toInt()
            )
            Log.i(
                TAG, "top: " + face.boundingBox.top
                        + " left: " + face.boundingBox.left
                        + " bottom: " + face.boundingBox.bottom
                        + " right: " + face.boundingBox.right
            )
            linePaint?.let { canvas?.drawRect(box, it) }
*/

    if(!capture) {
        if (face.trackingId != temp) {
                progressStatus = 0
                temp = face.trackingId
        } else {
            Thread {
                while (progressStatus < 100) {


                    handler.post {
                        progressStatus += 1
                        progressBar.progress = progressStatus
                        if(progressBar.progress==100){
                            defaultLayout.visibility= View.GONE
                            layoutVS.visibility=View.VISIBLE
                            if(!capture) {
                                //iv.setImageBitmap(bitmap)
                                iv.visibility=View.INVISIBLE
                                //tv.visibility=View.INVISIBLE
                                //this.result_cap.setImageResource(R.drawable.msg_icon)
                                this.result_cap.setImageBitmap(fbImage?.bitmap)
                                Log.e("capture","yes")
                                capture=true
                            }

                        }

                    }
                    try {
                        Thread.sleep(2000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }.start()
        }
    }





        }


        iv.setImageBitmap(bitmap)
    }

    private fun translateY(y: Float): Float {
        return y * heightScaleFactor
    }

    private fun translateX(x: Float): Float {
        val scaledX = x * widthScaleFactor
        return if (lens == LensFacing.FRONT) {
            canvas?.getWidth()!! - scaledX
        } else {
            scaledX
        }
    }



    private fun degreesToFirebaseRotation(degrees: Int): Int {
        return when (degrees) {
            0 -> FirebaseVisionImageMetadata.ROTATION_0
            90 -> FirebaseVisionImageMetadata.ROTATION_90
            180 -> FirebaseVisionImageMetadata.ROTATION_180
            270 -> FirebaseVisionImageMetadata.ROTATION_270
            else -> throw IllegalArgumentException("Rotation must be 0, 90, 180, or 270.")
        }
    }

    companion object {
        private const val TAG = "MLKitFacesAnalyzer"
    }

    init {
        this.iv = iv
        this.lens = lens
        this.result_cap=result_cap
    }
}