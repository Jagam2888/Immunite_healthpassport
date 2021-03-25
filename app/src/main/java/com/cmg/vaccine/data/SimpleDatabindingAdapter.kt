package com.cmg.vaccine.data

import android.view.View
import android.widget.ImageView
import androidx.core.os.HandlerCompat.postDelayed
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseMethod
import com.cmg.vaccine.R
import java.util.concurrent.atomic.AtomicBoolean

@InverseMethod("buttonIdToGender")
fun genderToButtonId(gender: Gender) : Int?{
    var selectedItem = -1
    gender?.run {
        selectedItem = when(this){
            Gender.F -> R.id.btn_female
            Gender.M -> R.id.btn_male
            Gender.O -> R.id.btn_other
        }
    }
    return selectedItem
}

@BindingAdapter("android:src")
fun serImage(imageView: ImageView,resource:Int){
    imageView.setImageResource(resource)
}

fun buttonIdToGender(selectedItem : Int) : Gender?{
    return when(selectedItem){
        R.id.btn_male -> Gender.M
        R.id.btn_female -> Gender.F
        R.id.btn_other -> Gender.O
        else -> null
    }
}

@BindingAdapter("onSingleClick")
fun View.setOnSingleClickListener(clickListener: View.OnClickListener?) {
    clickListener?.also {
        setOnClickListener(OnSingleClickListener(it))
    } ?: setOnClickListener(null)
}

class OnSingleClickListener(
        private val clickListener: View.OnClickListener,
        private val intervalMs: Long = 1000L
) : View.OnClickListener {
    private var canClick = AtomicBoolean(true)

    override fun onClick(v: View?) {
        if (canClick.getAndSet(false)) {
            v?.run {
                postDelayed({
                    canClick.set(true)
                }, intervalMs)
                clickListener.onClick(v)
            }
        }
    }
}
