package com.example.gallerydemokarimnabil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.gallerydemokarimnabil.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.bnvGlobal.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.images -> {
                    true
                }

                R.id.videos -> {
                    true
                }
                else -> {/*Do Nothing*/ true}
            }
        }
    }
}