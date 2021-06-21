package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityLegalDocumentsBinding
import com.cmg.vaccine.util.Passparams

class LegalDocumentsActivity : BaseActivity() {

    private lateinit var binding:ActivityLegalDocumentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_legal_documents)

        val url = intent.extras?.getString(Passparams.LEGAL_DOCUMENT,Passparams.PRIVACY_POLICY)

        if (!url.isNullOrEmpty()) {
            binding.webView.loadUrl(url)
            binding.webView.setInitialScale(1)
            binding.webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            val webViewSettings = binding.webView.settings

            webViewSettings.javaScriptEnabled = true
            webViewSettings.loadWithOverviewMode = true
            webViewSettings.useWideViewPort = true
        }
    }
}