package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityTermsOfUseBinding
import com.cmg.vaccine.util.Passparams

class TermsOfUseActivity : BaseActivity() {

    private lateinit var binding:ActivityTermsOfUseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_terms_of_use)

        binding.webView.loadUrl(Passparams.TERMS_CONDITION_URL)
        val webViewSettings = binding.webView.settings

        webViewSettings.javaScriptEnabled = true

    }
}