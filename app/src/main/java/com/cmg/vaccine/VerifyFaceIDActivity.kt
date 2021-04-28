package com.cmg.vaccine

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityVerifyFaceIDBinding
import com.cmg.vaccine.prefernces.PreferenceProvider
import com.cmg.vaccine.util.toast

class VerifyFaceIDActivity : BaseActivity() {

    private lateinit var binding:ActivityVerifyFaceIDBinding

    companion object{
        const val CAMERA_PERMISSION_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_verify_face_i_d)


        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.faceIdBtn.setOnSingleClickListener{
            binding.layoutVerifyMenu.visibility = View.GONE
            binding.layoutFaceId.visibility = View.VISIBLE
        }

        binding.imgBackFaceId.setOnSingleClickListener{
            binding.layoutVerifyMenu.visibility = View.VISIBLE
            binding.layoutFaceId.visibility = View.GONE
        }

        binding.btnGetStart.setOnSingleClickListener{
            Intent(this, FaceRecognitionActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.takePhotoBtn.setOnSingleClickListener{
            //if (checkPermission()) {
                cropImage()
           /* }else{
                requestPermission()
            }*/
        }
    }

    private fun checkPermission():Boolean{
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            CAMERA_PERMISSION_CODE
        )
    }

    private fun cropImage() {
        CropImage.activity( )
            .setGuidelines(CropImageView.Guidelines.ON)
            .setActivityTitle("Edit Photo")
            .setCropShape(CropImageView.CropShape.OVAL)
            .setFixAspectRatio(true)
            .setCropMenuCropButtonTitle("Done")
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === RESULT_OK) {
                val resultUri = result?.uri
                var pref= PreferenceProvider(this)
                pref.saveProfileImage(resultUri.toString())
                //viewModel.profileImageUri.set(resultUri.toString())
                toast("You profile picture was successfully changed")
                Intent(this, FaceRecognitionActivity::class.java).also {
                    it.putExtra("recent_capture",resultUri.toString())
                    startActivity(it)
                }
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result?.error
                error?.message?.let { toast(it) }
            }
        }
    }
}