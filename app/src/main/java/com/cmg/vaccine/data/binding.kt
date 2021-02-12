package com.cmg.vaccine.data

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.ObservableField

object binding {

    @BindingAdapter("clicks")
    fun listenClicks(spinner: AppCompatSpinner, result: ObservableField<String>) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                result.set(parent?.getItemAtPosition(position) as String)
            }
        }
    }

    @BindingAdapter(value = ["selectedValue", "selectedValueAttrChanged"], requireAll = false)
    fun bindSpinnerData(
            pAppCompatSpinner: Spinner,
            newSelectedValue: String,
            newTextAttrChanged: InverseBindingListener
    ) {
        pAppCompatSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
            ) {
                newTextAttrChanged.onChange()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        if (newSelectedValue != null) {
            val pos =
                    (pAppCompatSpinner.adapter as ArrayAdapter<String?>).getPosition(
                            newSelectedValue
                    )
            pAppCompatSpinner.setSelection(pos, true)
        }
    }

    @InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
    fun captureSelectedValue(pAppCompatSpinner: Spinner): String {
        return pAppCompatSpinner.selectedItem as String
    }
}