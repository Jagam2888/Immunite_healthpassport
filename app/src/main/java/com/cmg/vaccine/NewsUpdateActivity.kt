package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityNewsUpdateBinding

class NewsUpdateActivity : BaseActivity() {

    private lateinit var binding:ActivityNewsUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_news_update)

        binding.imageView.setOnClickListener {
            finish()
        }
    }
}