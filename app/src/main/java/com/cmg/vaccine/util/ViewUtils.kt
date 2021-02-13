package com.cmg.vaccine.util

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.cmg.vaccine.R
import com.cmg.vaccine.database.Countries
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.custom_alert_dialog.view.*
import java.io.UnsupportedEncodingException
import java.lang.StringBuilder
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec

fun Context.toast(message:String){
    Toast.makeText(this,message, Toast.LENGTH_LONG).show()
}

fun show(progressBar: ProgressBar){
    //progress_bar.visibility = View.VISIBLE
    progressBar.visibility = View.VISIBLE
}

fun hide(progressBar: ProgressBar){
    //progress_bar.visibility = View.INVISIBLE
    progressBar.visibility = View.INVISIBLE
}

fun isValidEmail(value:String):Boolean{
    return (!TextUtils.isEmpty(value) and Patterns.EMAIL_ADDRESS.matcher(value).matches())
}

fun isValidPassword(value:String):Boolean{
    return value.length >= 4
}

fun Context.showDatePickerDialog(editText: EditText){
    val calender = Calendar.getInstance()
    val year = calender.get(Calendar.YEAR)
    val month = calender.get(Calendar.MONTH)
    val day = calender.get(Calendar.DAY_OF_MONTH)
    val format = SimpleDateFormat("dd/MM/yyyy")

    val datePicker = DatePickerDialog(this,
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calender.set(year, month, dayOfMonth)
            val dateDob = format.format(calender.time)
            editText.setText(dateDob)
        }, year, month, day
    )
    datePicker.datePicker.maxDate = System.currentTimeMillis()

    datePicker.show()
}

fun Context.showTimepickerDialog(editText: EditText){
    val cal = Calendar.getInstance()
    val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        editText.setText(SimpleDateFormat("HH:mm:ss").format(cal.time))
    }
    TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
}


fun Context.alertDialog(){
    val builder = AlertDialog.Builder(this)

    val dialogView = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog,null)
    builder.setView(dialogView).setCancelable(false).setTitle("Please Enter Your URL")

    dialogView.btn_submit.setOnClickListener {
        var url = StringBuilder()
        if(dialogView.edt_ip_address.text.isNullOrEmpty()){
            return@setOnClickListener
        }
        url.append(dialogView.edt_ip_address.text.toString())

        if(!dialogView.edt_port.text.isNullOrEmpty()){
            url.append(":")
            url.append(dialogView.edt_port.text)
        }
        Paper.book().write("url",url.toString())

    }
}

fun selectedRelationShipPosition(state: String, relationShipList: List<String>):Int{
    var pos:Int = 0
    for (i in relationShipList.indices!!){
        if(state.equals(relationShipList.get(i))){
            return i
        }
    }
    return pos
}

fun selectedCurrentCountry(country: String, countries: List<Countries>):Int{
    var pos:Int = 0
    for (i in countries.indices!!){
        if(country.equals(countries.get(i).countryName,false)){
            return i
        }
    }
    return pos
}

fun changeDateFormatEmail(timeMills:Long):String?{
    val simpleDateFormat = SimpleDateFormat("DD MMMM YYYY 'at' HH:mm aaa")
    val calender = Calendar.getInstance()
    calender.timeInMillis = timeMills
    return simpleDateFormat.format(calender.time)
}
fun genearteKey(secretKey:String): SecretKeySpec?{
    try {
        var keyBytes = secretKey.toByteArray(charset("UTF-8"))
        val messageDigest = MessageDigest.getInstance("SHA-1")
        keyBytes = messageDigest.digest(keyBytes)
        keyBytes = Arrays.copyOf(keyBytes,16)
        return SecretKeySpec(keyBytes,"AES")
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace();
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace();
    }
    return null
}
fun decrypt(secretKey: String, jsonValues: String):String?{
    try {
        //val keyBytes = secretKey.toByteArray(charset("UTF-8"))
        //val skey = SecretKeySpec(keyBytes, "AES")
        Log.d("secretkey",genearteKey(secretKey).toString())
        synchronized(Cipher::class.java) {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING")
            cipher.init(Cipher.DECRYPT_MODE, genearteKey(secretKey))
            var cipherFinal = cipher.doFinal(Base64.decode(jsonValues, Base64.DEFAULT))
            val result = String(cipherFinal)
            return result
        }
    } catch (uee: UnsupportedEncodingException) {
        uee.printStackTrace()
    } catch (ibse: IllegalBlockSizeException) {
        ibse.printStackTrace()
    } catch (bpe: BadPaddingException) {
        bpe.printStackTrace()
    } catch (ike: InvalidKeyException) {
        ike.printStackTrace()
    } catch (nspe: NoSuchPaddingException) {
        nspe.printStackTrace()
    } catch (nsae: NoSuchAlgorithmException) {
        nsae.printStackTrace()
    } catch (e: ShortBufferException) {
        e.printStackTrace()
    }
    return null
}
fun encryptToString(secretKey: String, jsonValues: String): String? {
    try {
        //val keyBytes = secretKey.toByteArray(charset("UTF-8"))
        //val skey = SecretKeySpec(keyBytes, "AES")
        synchronized(Cipher::class.java) {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, genearteKey(secretKey))
            var cipherFinal = Base64.encodeToString(cipher.doFinal(jsonValues.toByteArray(charset("UTF-8"))), Base64.DEFAULT)
            return cipherFinal
        }
    }catch (uee: UnsupportedEncodingException) {
        uee.printStackTrace()
    } catch (ibse: IllegalBlockSizeException) {
        ibse.printStackTrace()
    } catch (bpe: BadPaddingException) {
        bpe.printStackTrace()
    } catch (ike: InvalidKeyException) {
        ike.printStackTrace()
    } catch (nspe: NoSuchPaddingException) {
        nspe.printStackTrace()
    } catch (nsae: NoSuchAlgorithmException) {
        nsae.printStackTrace()
    } catch (e: ShortBufferException) {
        e.printStackTrace()
    }
    return null
}
