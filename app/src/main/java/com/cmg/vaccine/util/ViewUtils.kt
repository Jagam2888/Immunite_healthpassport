package com.cmg.vaccine.util

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Point
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.akexorcist.snaptimepicker.TimeValue
import com.blongho.country_data.Country
import com.cmg.vaccine.DialogFragment.AlertDialogFragment
import com.cmg.vaccine.R
import com.cmg.vaccine.database.IdentifierType
import com.cmg.vaccine.database.User
import com.cmg.vaccine.database.WorldEntryCountries
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.niwattep.materialslidedatepicker.SlideDatePickerDialog
import immuniteeEncryption.EncryptionUtils
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import io.paperdb.Paper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.ss.formula.functions.T
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.UnsupportedEncodingException
import java.lang.reflect.Type
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.HashMap

fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToolTip(view: View,msg: String){
    SimpleTooltip.Builder(this)
        .anchorView(view)
        .text(msg)
        .transparentOverlay(false)
        .gravity(Gravity.BOTTOM)
        .animated(true)
        .build()
        .show()
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
fun Activity.navigateTo(activity: Activity, navigateClass:Class<*>){
    Intent(activity,navigateClass).also {
        startActivity(it)
    }
}

fun showAlertDialog(title: String, msg: String, status: Boolean,fragmentManager: FragmentManager){
    var alertDialog = AlertDialogFragment()
    var data= Bundle()
    data.putString(Passparams.DIALOG_TITLE, title)
    data.putString(Passparams.DIALOG_MSG, msg)
    data.putBoolean(Passparams.DIALOG_STATUS, status)
    data.putBoolean(Passparams.DIALOG_CLOSE_ACTIVITY, false)
    alertDialog.arguments = data
    try {
        alertDialog.show(fragmentManager, "TAG")
    }catch (e: Exception){
        e.printStackTrace()
    }

}
fun showAlertDialogWithClick(title: String, msg: String, status: Boolean, closeAvtivity:Boolean,fragmentManager: FragmentManager){
    var alertDialog = AlertDialogFragment()
    var data= Bundle()
    data.putString(Passparams.DIALOG_TITLE, title)
    data.putString(Passparams.DIALOG_MSG, msg)
    data.putBoolean(Passparams.DIALOG_STATUS, status)
    data.putBoolean(Passparams.DIALOG_CLOSE_ACTIVITY, closeAvtivity)
    alertDialog.arguments = data
    try {
        alertDialog.show(fragmentManager, "TAG")
    }catch (e: Exception){
        e.printStackTrace()
    }

}

fun getCountryName(isoCode:String):String{
    val locale = Locale("",isoCode)
    return locale.displayCountry
}

fun getThreeAlpha(nameCode: String):String{
    val locale = Locale("en", nameCode)
    return locale.isO3Country
}

fun getTwoAlpha(nameCode: String): String? {
    return getHashMap()[nameCode]
}

fun getHashMap():Map<String, String>{
    var hashMap = HashMap<String, String>()
    val gsonHashMap = Paper.book().read<String>(Passparams.GSON_HASHMAP_COUNTRYLIST,null)
    if (gsonHashMap.isNullOrEmpty()) {
        for (iso in Locale.getISOCountries()) {
            val l = Locale("en", iso)
            hashMap[l.isO3Country] = iso
        }
        val gson = Gson()
        Paper.book().write(Passparams.GSON_HASHMAP_COUNTRYLIST,gson.toJson(hashMap))
    }else{
        val gson = Gson()
        val type: Type = object : TypeToken<HashMap<String,String>>() {}.type
        hashMap = gson.fromJson<HashMap<String,String>>(gsonHashMap, type)
    }
    return hashMap
}

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
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }catch (e: Exception){
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

fun Context.showDatePickerDialogForPassport(editText: EditText){
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
    datePicker.datePicker.minDate = System.currentTimeMillis()

    datePicker.show()
}

fun validateTime(time: String):Boolean{
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
    val formatPattern = "^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((?:19|20)[0-9][0-9])"
    val pattern = Pattern.compile(formatPattern)
    val matcher = pattern.matcher(date)
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

        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        if ((year == currentYear) and (month.toInt() > currentMonth+1)){
            result = false
        }

        if ((year == currentYear) and (month.toInt() == currentMonth+1) and (day.toInt() > currentDay)){
            result = false
        }



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
fun isLeapYear(year: Int):Boolean {
    return ((year % 4 == 0) and ((year % 100 != 0) or (year % 400 == 0)));
}

fun validateDateFormatForPassport(date: String):Boolean{
    val formatPattern = "^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((?:19|20)[0-9][0-9])"
    val pattern = Pattern.compile(formatPattern)
    val matcher = pattern.matcher(date)
    var result = false
    if (date.isNullOrEmpty() or !matcher.matches()){
        return result
    }else{
        result = true
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val currentMonth=Calendar.getInstance().get(Calendar.MONTH)+1
        val currentDay=Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        val year = matcher.group(3).toInt()
        val month = matcher.group(2).toString()
        val day = matcher.group(1).toString()


        if (year < currentYear) {
            result = false
        }
        if(year <= currentYear && month.toInt() < currentMonth)
        {
            result = false
        }
        if(year <= currentYear && month.toInt() <= currentMonth && day.toInt()<= currentDay)
        {
            result = false
        }

        if ((month == "4" || month == "6" || month == "9" || month == "04" || month == "06" || month == "09" || month == "11") && day == "31") {
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

fun Context.showSliderDatePickerDialog(
        text: String,
        fragmentManager: FragmentManager,
        startDate: Calendar,
        endDate: Calendar){
    SlideDatePickerDialog.Builder()
        .setLocale(Locale("en"))
        .setThemeColor(ContextCompat.getColor(this, R.color.primary))
        .setHeaderDateFormat("EEE dd MMM")
        .setShowYear(true)
        .setCancelText("Cancel")
        .setConfirmText("Confirm")
        .setStartDate(startDate)
        .setEndDate(endDate)
        .build()
        .show(fragmentManager, text)

}

fun Context.showSnapTimePickerDialog(hour: Int, minute: Int): SnapTimePickerDialog {
    return SnapTimePickerDialog.Builder().apply {
        setTitle(R.string.time_picker_title)
        setTitleColor(R.color.white)
        setThemeColor(R.color.primary)
        setNegativeButtonColor(R.color.primary)
        setPositiveButtonColor(R.color.primary)
        setPreselectedTime(TimeValue(hour, minute))
        setPositiveButtonText(R.string.confirm)
        setNegativeButtonText(R.string.dialog_btn_cancel)
        setButtonTextAllCaps(true)
    }.build()
}

fun Context.onTimePicked(selectedHour: Int, selectedMinute: Int, editText: EditText) {
    val hour = selectedHour.toString().padStart(2, '0')
    val minute = selectedMinute.toString().padStart(2, '0')
    editText.setText(String.format(getString(R.string.selected_time_format, hour, minute)))
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
        if(country.equals(countries[i].alpha3, true)){
            return i
        }
    }
    return pos
}

fun selectedIdType(idType: String, idTypeList: List<IdentifierType>):Int{
    var pos:Int = 0
    for (i in idTypeList.indices!!){
        if(idType.equals(idTypeList[i].identifierCode, true)){
            return i
        }
    }
    return pos
}

fun getCurrentCountry(country: String, countries: List<Country>):Int{
    var pos:Int = 0
    for (i in countries.indices!!){
        if(country.equals(countries[i].name, true)){
            return i
        }
    }
    return pos
}



fun getCountryNameUsingCode(code: String, countries: List<Country>):String?{

    for (i in countries.indices!!){
        if(code.equals(countries.get(i).alpha3, true)){
            return countries.get(i).name
        }
    }
    return ""
}
fun getWorldEntryCountryNameUsingCode(code: String, countries: List<WorldEntryCountries>):String?{

    for (i in countries.indices!!){
        if(code.equals(countries.get(i).countryCodeAlpha, true)){
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
fun removeSeconds(time: String):String?{
    val currentDateFormat = SimpleDateFormat("HH:mm:ss")
    val simpleDateFormat = SimpleDateFormat("HH:mm")
    try {
        val date = currentDateFormat.parse(time)
        return simpleDateFormat.format(date)
    }catch (e: ParseException){
        e.printStackTrace()
    }
    return ""
}
fun changeDateFormatForVaccine(dateString: String): String {
    val resultFormat = SimpleDateFormat("dd/MM/yyyy")
    val isoFormat = SimpleDateFormat("yyyy-MM-dd")
    try {
        val date = isoFormat.parse(dateString)
        return resultFormat.format(date)
    }catch (e: ParseException){
        e.printStackTrace()
    }
    return ""
}
fun changeDateFormatForNotification(dateString: String): String {
    val resultFormat = SimpleDateFormat("dd MMM yyyy HH:mm:ss")
    val isoFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    try {
        val date = isoFormat.parse(dateString)
        return resultFormat.format(date)
    }catch (e: ParseException){
        e.printStackTrace()
    }
    return ""
}
fun changeDateFormatISO8601(dateString: String):String?{
    val currentDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
    try {
        val date = currentDateFormat.parse(dateString)
        return simpleDateFormat.format(date)
    }catch (e: ParseException){
        e.printStackTrace()
    }
    return ""
}
fun changeDateFormatOnlyDateReverse(dateString: String):String?{
    val resultFormat = SimpleDateFormat("yyyyMMdd")
    val currentDateFormat = SimpleDateFormat("dd/MM/yyyy")
    try {
        val date = currentDateFormat.parse(dateString)
        return resultFormat.format(date)
    }catch (e: ParseException){
        e.printStackTrace()
    }
    return ""
}
fun changeDateFormatNewISO8601(dateString: String):String?{
    val currentDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    try {
        val date = currentDateFormat.parse(dateString)
        return simpleDateFormat.format(date)
    }catch (e: ParseException){
        e.printStackTrace()
    }
    return ""
}
fun changeDateFormatNormal(dateString: String):String?{
    val resultFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
    val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
    try {
        val date = isoFormat.parse(dateString)
        return resultFormat.format(date)
    }catch (e: ParseException){
        e.printStackTrace()
    }
    return ""
}
fun changeDateFormatBC(dateString: String):String?{
    val resultFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
    //val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
    val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    try {
        val date = isoFormat.parse(dateString)
        return resultFormat.format(date)
    }catch (e: ParseException){
        e.printStackTrace()
    }
    return ""
}
fun changeDateFormatForPrivateKeyDecrypt(dateString: String):String?{
    val currentDateFormat = SimpleDateFormat("dd/MM/yyyy")
    val simpleDateFormat = SimpleDateFormat("yyyyMMdd")
    try {
        val date = currentDateFormat.parse(dateString)
        return simpleDateFormat.format(date)
    }catch (e: ParseException){
        e.printStackTrace()
    }
    return ""
}
fun changeDateToTimeStamp(dateString: String): Long? {
    val currentDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    var timeStamp: Long? = null
    try {
        val date = currentDateFormat.parse(dateString) as Date
        timeStamp = date.time
    }catch (e:Exception){
        e.printStackTrace()
    }

    return timeStamp
}
fun changeDateToTimeStampForWebCheckin(dateString: String): Long? {
    val currentDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
    var timeStamp: Long? = null
    try {
        val date = currentDateFormat.parse(dateString) as Date
        timeStamp = date.time
    }catch (e:Exception){
        e.printStackTrace()
    }

    return timeStamp
}
fun changeDateToTimeStampAlter(dateString: String): Long {
    val currentDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    var timeStamp:Long = 0
    try {
        val date = currentDateFormat.parse(dateString) as Date
        val time = Timestamp(date.time)
        timeStamp = time.time
    }catch (e:Exception){
        e.printStackTrace()
    }

    return timeStamp
}
fun currentDateTime():String{
    var result = ""
    val calender = Calendar.getInstance()
    val currentDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    try {
        result = currentDateFormat.format(calender.time)
    }catch (e:Exception){
        e.printStackTrace()
    }
    return result
}
fun changeDateFormatFeedback(date:String):String{
    var result = ""
    val currentDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    val resultDateFormat = SimpleDateFormat("dd MMM yyyy HH:mm:ss")
    try {
        val currentDate = currentDateFormat.parse(date)
        result = resultDateFormat.format(currentDate)
    }catch (e:Exception){
        e.printStackTrace()
    }
    return result
}
fun calculateHours(currentTimeStamp: Long, timeStamp: Long):Long{
    val differnce = currentTimeStamp - timeStamp
    val seconds = differnce / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    return hours
}
fun calculateMinutes(currentTimeStamp: Long, timeStamp: Long):Long{
    val differnce = currentTimeStamp - timeStamp
    val seconds = differnce / 1000
    val minutes = seconds / 60
    //val hours = minutes / 60
    return minutes
}
fun Context.getCurrentCountryName():String?{
    val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    var country:String = "Malaysia"
    //Couritnes.main {
        try {
            val geocoder = Geocoder(applicationContext, Locale.getDefault())
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
        }catch (e: Exception){
            toast(e.message!!)
        }catch (e: NoInternetException){
            toast(e.message!!)
        }
    //}

    return country
}

fun Context.openPdf(fileName: String, file: File){

    if(file.exists()) {
        Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(FileProvider.getUriForFile(this@openPdf, "com.cmg.vaccine.provider", file), "application/pdf")
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            try {
                startActivity(this)
            } catch (e: Exception) {
                toast(e.message!!)
            }
        }
    } else {
        toast("File does not exists")
    }
}

fun decryptQRValue(key: String, dob: String):String?{
    return EncryptionUtils.decryptBackupKey(key, dob)
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

fun MyCompareSimAlgo(s1: String, s2: String): String
{
    var simPerc = diceCoefficientOptimized(s1.toUpperCase(), s2.toUpperCase())*100
    var df= DecimalFormat("0.00")
    return df.format(simPerc)
}

private fun diceCoefficientOptimized(s: String?, t: String?): Double {
    // Verifying the input:
    if (s == null || t == null) return 0.0
    // Quick check to catch identical objects:
    if (s === t) return 1.0
    // avoid exception for single character searches
    if (s.length < 2 || t.length < 2) return 0.0

    // Create the bigrams for string s:
    val n = s.length - 1
    val sPairs = IntArray(n)
    for (i in 0..n)
        if (i == 0)
            sPairs[i] = s[i].toInt() shl 16
        else if (i == n)
            sPairs[i - 1] = sPairs[i - 1] or s[i].toInt()
        else
            sPairs[i] = s[i].let { sPairs[i - 1] = sPairs[i - 1] or it.toInt(); sPairs[i - 1] } shl 16

    // Create the bigrams for string t:
    val m = t.length - 1
    val tPairs = IntArray(m)
    for (i in 0..m)
        if (i == 0)
            tPairs[i] = t[i].toInt() shl 16
        else if (i == m)
            tPairs[i - 1] = tPairs[i - 1] or t[i].toInt()
        else
            tPairs[i] = t[i].let { tPairs[i - 1] = tPairs[i - 1] or it.toInt(); tPairs[i - 1] } shl 16

    // Sort the bigram lists:
    Arrays.sort(sPairs)
    Arrays.sort(tPairs)

    // Count the matches:
    var matches = 0
    var i = 0
    var j = 0
    while (i < n && j < m) {
        if (sPairs[i] == tPairs[j]) {
            matches += 2
            i++
            j++
        }
        else if (sPairs[i] < tPairs[j])
            i++
        else
            j++
    }
    return matches.toDouble() / (n + m)
}

fun Context.showSuccessOrFailedAlert(title: String, msg: String, status: Boolean){
    val alertDialog = AlertDialog.Builder(this)

}

fun Context.checkGoogleServices(){
    /*val googleApiAvailability = GoogleApiAvailability.getInstance()
    val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this)*/
    if (getGoogleServicesResult() != ConnectionResult.SUCCESS){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(resources.getString(R.string.app_name)).setMessage(resources.getString(R.string.immunitee_not_support_google))
            .setNegativeButton("OK"
            ) { dialog, which -> dialog.dismiss() }
        alertDialog.show()
    }
}

fun Context.getGoogleServicesResult():Int{
    val googleApiAvailability = GoogleApiAvailability.getInstance()
    return googleApiAvailability.isGooglePlayServicesAvailable(this)
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

fun getRealPathFromURI(context: Context, uri: Uri): String? {
    when {
        // DocumentProvider
        DocumentsContract.isDocumentUri(context, uri) -> {
            when {
                // ExternalStorageProvider
                isExternalStorageDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    // This is for checking Main Memory
                    return if ("primary".equals(type, ignoreCase = true)) {
                        if (split.size > 1) {
                            Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                        } else {
                            Environment.getExternalStorageDirectory().toString() + "/"
                        }
                        // This is for checking SD Card
                    } else {
                        "storage" + "/" + docId.replace(":", "/")
                    }
                }
                isDownloadsDocument(uri) -> {
                    val fileName = getFilePath(context, uri)
                    if (fileName != null) {
                        return Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName
                    }
                    var id = DocumentsContract.getDocumentId(uri)
                    if (id.startsWith("raw:")) {
                        id = id.replaceFirst("raw:".toRegex(), "")
                        val file = File(id)
                        if (file.exists()) return id
                    }
                    val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                    return getDataColumn(context, contentUri, null, null)
                }
                isMediaDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    when (type) {
                        "image" -> {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        }
                        "video" -> {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        }
                        "audio" -> {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])
                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }
            }
        }
        "content".equals(uri.scheme, ignoreCase = true) -> {
            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)
        }
        "file".equals(uri.scheme, ignoreCase = true) -> {
            return uri.path
        }
    }
    return null
}

private fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                  selectionArgs: Array<String>?): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(
        column
    )
    try {
        if (uri == null) return null
        cursor = context.contentResolver.query(uri, projection, selection, selectionArgs,
            null)
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(index)
        }
    } finally {
        cursor?.close()
    }
    return null
}


private fun getFilePath(context: Context, uri: Uri?): String? {
    var cursor: Cursor? = null
    val projection = arrayOf(
        MediaStore.MediaColumns.DISPLAY_NAME
    )
    try {
        if (uri == null) return null
        cursor = context.contentResolver.query(uri, projection, null, null,
            null)
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            return cursor.getString(index)
        }
    } finally {
        cursor?.close()
    }
    return null
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
private fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 */
private fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 */
private fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 */
private fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri.authority
}

//new way for get file path

fun Context.getFileFromUri(uri: Uri):File{
    val parcelFileDescriptor = contentResolver.openFileDescriptor(uri,"r",null)
    val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
    val file = File(cacheDir,getFileName(uri))
    val outputStream = FileOutputStream(file)
    inputStream.copyTo(outputStream)
    return file
}

fun getFIleSize(file: File): Long {
    val fileLength = file.length()
    return fileLength / (1024 * 1024)
}


fun Context.getFileName(uri:Uri):String?{
    var result:String?=null
    if (uri.scheme == "content"){
        val cursor = contentResolver.query(uri,null,null,null,null)
        cursor.use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result!!.lastIndexOf('/')
        if (cut != -1) {
            result = result!!.substring(cut + 1)
        }
    }
    return result
}
fun Context.drawResizedBitmap(src: Bitmap, dst: Bitmap) {
    val getOrient = (getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager).defaultDisplay
    var orientation = Configuration.ORIENTATION_UNDEFINED
    val point = Point()
    getOrient.getSize(point)
    val screen_width = point.x
    val screen_height = point.y
    var mScreenRotation = 0
    Log.d(
        "",
        String.format("screen size (%d,%d)", screen_width, screen_height)
    )
    if (screen_width < screen_height) {
        orientation = Configuration.ORIENTATION_PORTRAIT
        mScreenRotation = 0
    } else {
        orientation = Configuration.ORIENTATION_LANDSCAPE
        mScreenRotation = 0
    }
    //junit.framework.Assert.assertEquals(dst.width, dst.height)
    val minDim = Math.min(src.width, src.height).toFloat()
    val matrix = Matrix()

    // We only want the center square out of the original rectangle.
    val translateX = -Math.max(0f, (src.width - minDim) / 2)
    val translateY = -Math.max(0f, (src.height - minDim) / 2)
    matrix.preTranslate(translateX, translateY)
    val scaleFactor = dst.height / minDim
    matrix.postScale(scaleFactor, scaleFactor)

    // Rotate around the center if necessary.
    if (mScreenRotation != 0) {
        matrix.postTranslate(-dst.width / 2.0f, -dst.height / 2.0f)
        matrix.postRotate(mScreenRotation.toFloat())
        matrix.postTranslate(dst.width / 2.0f, dst.height / 2.0f)
    }
    val canvas = Canvas(dst)
    canvas.drawBitmap(src, matrix, null)
}


