package com.cmg.vaccine.util

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.blongho.country_data.Country
import com.cmg.vaccine.database.Countries
import com.cmg.vaccine.database.WorldEntryCountries
import com.cmg.vaccine.model.response.WorldEntriesCountryListData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import immuniteeEncryption.EncryptionUtils
import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec

fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun show(progressBar: ProgressBar){
    if (progressBar.visibility == View.GONE)
        progressBar.visibility = View.VISIBLE
}

fun hide(progressBar: ProgressBar){
    if (progressBar.visibility == View.VISIBLE)
        progressBar.visibility = View.GONE
}

/*fun isValidEmail(value: String):Boolean{
    return (!TextUtils.isEmpty(value) and Patterns.EMAIL_ADDRESS.matcher(value).matches())
}*/

fun isValidEmail(value: String):Boolean{
    val regx = "^[\\w-\\.+]*[\\w-\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"
    val pattern = Pattern.compile(regx)
    val matcher = pattern.matcher(value)
    return matcher.matches()
}

fun isValidPassword(value: String):Boolean{
    return value.length >= 4
}

fun Activity.hideKeyBoard(){
    val inputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}

fun Context.getDeviceUUID():String?{
    try {
        return Settings.Secure.getString(contentResolver,Settings.Secure.ANDROID_ID)
    }catch (e:Exception){
        e.printStackTrace()
    }
    return ""
}

fun Context.showDatePickerDialog(editText: EditText){
    val calender = Calendar.getInstance()
    val year = calender.get(Calendar.YEAR)
    val month = calender.get(Calendar.MONTH)
    val day = calender.get(Calendar.DAY_OF_MONTH)
    val format = SimpleDateFormat("ddMMyyyy")

    val datePicker = DatePickerDialog(
        this,
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calender.set(year, month, dayOfMonth)
            val dateDob = format.format(calender.time)
            editText.setText(dateDob)
        }, year, month, day
    )
    datePicker.datePicker.maxDate = System.currentTimeMillis()

    datePicker.show()
}

fun validateTime(time:String):Boolean{
    if (time.isNullOrEmpty())
        return false
    val timeArray = time.split(":")
    if (timeArray.size > 1){
        if (timeArray[0].isNullOrEmpty())
            return false
        if (timeArray[1].isNullOrEmpty())
            return false
    }else{
        return false
    }
    return (timeArray[0].toInt() <=23) and (timeArray[1].toInt() <= 59)
}


fun validateDateFormat(date: String):Boolean{
    //val formatPattern = "^(1[0-9]|0[1-9]|3[0-1]|2[1-9])/(0[1-9]|1[0-2])/[0-9]{4}\$"
    val formatPattern = "^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((?:19|20)[0-9][0-9])"
    val pattern = Pattern.compile(formatPattern)
    val matcher = pattern.matcher(date)
    /*if (date.isNullOrEmpty() or !matcher.matches()){
        return false
    }
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    return try {
        dateFormat.parse(date)
        true
    } catch (e: ParseException) {
        false
    }*/
    var result = false
    if (date.isNullOrEmpty() or !matcher.matches()){
        return result
    }else{
        result = true
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val year = matcher.group(3).toInt()
        if (year > currentYear){
            result = false
        }
        val month = matcher.group(2).toString()
        val day = matcher.group(1).toString()

        if ((month == "4" || month == "6" || month == "9" ||
                    month == "04" || month == "06" || month == "09" ||
                    month == "11") && day == "31"
        ) {
            result = false;
        } else if (month == "2" || month == "02") {
            if (day == "30" || day == "31") {
                result = false;
            } else if (day == "29") {  // leap year? feb 29 days.
                if (!isLeapYear(year)) {
                    result = false;
                }
            }
        }
        return result
    }
    }
fun isLeapYear(year:Int):Boolean {
    return ((year % 4 == 0) and ((year % 100 != 0) or (year % 400 == 0)));
}

fun Context.showTimepickerDialog(editText: EditText, currentTime: String){
    val sdf = SimpleDateFormat("HHmm")
    val cal = Calendar.getInstance()
    try {
        val changeFormat = currentTime.replace(":", "")
        val date = sdf.parse(changeFormat)
        cal.time = date
    }catch (e: ParseException){
        e.printStackTrace()
    }
    val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        editText.setText(SimpleDateFormat("HHmm").format(cal.time))
    }
    TimePickerDialog(
        this,
        timeSetListener,
        cal.get(Calendar.HOUR_OF_DAY),
        cal.get(Calendar.MINUTE),
        true
    ).show()
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

fun selectedCountryName(country: String, countries: List<Country>):Int{
    var pos:Int = 0
    for (i in countries.indices!!){
        if(country.equals(countries.get(i).alpha3, false)){
            return i
        }
    }
    return pos
}
fun getCurrentCountry(country: String, countries: List<Country>):Int{
    var pos:Int = 0
    for (i in countries.indices!!){
        if(country.equals(countries[i].name, false)){
            return i
        }
    }
    return pos
}

fun getCountryNameUsingCode(code: String, countries: List<Country>):String?{

    for (i in countries.indices!!){
        if(code.equals(countries.get(i).alpha3, false)){
            return countries.get(i).name
        }
    }
    return ""
}
fun getWorldEntryCountryNameUsingCode(code: String, countries: List<WorldEntryCountries>):String?{

    for (i in countries.indices!!){
        if(code.equals(countries.get(i).countryCodeAlpha, false)){
            return countries.get(i).countryName
        }
    }
    return ""
}

fun changeDateFormatEmail(timeMills: Long):String?{
    val simpleDateFormat = SimpleDateFormat("DD MMMM YYYY 'at' HH:mm aaa")
    val calender = Calendar.getInstance()
    calender.timeInMillis = timeMills
    return simpleDateFormat.format(calender.time)
}
fun changeDateFormatForViewProfile(dateString: String):String?{
    val currentDateFormat = SimpleDateFormat("dd/MM/yyyy")
    val simpleDateFormat = SimpleDateFormat("dd MMM yyyy")
    try {
        val date = currentDateFormat.parse(dateString)
        return simpleDateFormat.format(date)
    }catch (e: ParseException){
        e.printStackTrace()
    }
    return ""
}
fun changeDateFormatISO8601(dateString: String):String?{
    val currentDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    try {
        val date = currentDateFormat.parse(dateString)
        return simpleDateFormat.format(date)
    }catch (e: ParseException){
        e.printStackTrace()
    }
    return ""
}
fun changeDateFormatForPrivateKeyDecrypt(dateString:String):String?{
    val currentDateFormat = SimpleDateFormat("dd/MM/yyyy")
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    try {
        val date = currentDateFormat.parse(dateString)
        return simpleDateFormat.format(date)
    }catch (e: ParseException){
        e.printStackTrace()
    }
    return ""
}
fun Context.getCurrentCountryName():String?{
    val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    var country:String = ""
    //Couritnes.main {
        try {
            val geocoder = Geocoder(applicationContext,Locale.getDefault())
            for (provider in locationManager.allProviders){
                @SuppressWarnings("ResourceType")
                val location = locationManager.getLastKnownLocation(provider)
                if (location != null){
                    val address:List<Address> = geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                    )
                    if (!address.isNullOrEmpty()){
                        country = address.get(0).countryName
                    }
                }
            }
        }catch (e:Exception){
            toast(e.message!!)
        }catch (e:NoInternetException){
            toast(e.message!!)
        }
    //}

    return country
}

fun Context.getLastLocation():Location? {
    var lastLocation: Location? = null
    var fusedLocationProviderClient: FusedLocationProviderClient =LocationServices.getFusedLocationProviderClient(
        this
    )
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
        return null
    }
    fusedLocationProviderClient?.lastLocation!!.addOnCompleteListener { task ->
        if (task.isSuccessful && task != null){
            lastLocation =  task.result
        }
    }
    return lastLocation
}

fun decryptBackupKey(encryptedKey: String): String? {
    var encryptedKey = encryptedKey
    return if (encryptedKey.isEmpty()) {
        "invalid"
    } else {
        if (encryptedKey.contains(" ")) {
            encryptedKey = encryptedKey.replace(' ', '+')
        }
        encryptedKey = encryptedKey.replace("IMMUNITEE|", "")
        val dobHint = encryptedKey.substring(0, 8)
        val finalToDecryptKey = encryptedKey.substring(8)
        val finalDecryptedPrivateKey = EncryptionUtils.decrypt(finalToDecryptKey, dobHint)
        finalDecryptedPrivateKey ?: "invalid"
    }
}

fun genearteKey(secretKey: String): SecretKeySpec?{
    try {
        var keyBytes = secretKey.toByteArray(charset("UTF-8"))
        val messageDigest = MessageDigest.getInstance("SHA-1")
        keyBytes = messageDigest.digest(keyBytes)
        keyBytes = Arrays.copyOf(keyBytes, 16)
        return SecretKeySpec(keyBytes, "AES")
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
        Log.d("secretkey", genearteKey(secretKey).toString())
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
            var cipherFinal = Base64.encodeToString(
                cipher.doFinal(jsonValues.toByteArray(charset("UTF-8"))),
                Base64.DEFAULT
            )
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
