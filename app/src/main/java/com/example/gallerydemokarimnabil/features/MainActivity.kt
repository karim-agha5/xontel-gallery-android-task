package com.example.gallerydemokarimnabil.features

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.gallerydemokarimnabil.NavGraphDirections
import com.example.gallerydemokarimnabil.R
import com.example.gallerydemokarimnabil.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initNavController()
        setBottomNavViewItemsClickListeners()
    }

    private fun initNavController(){
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        navController = navHostFragment.findNavController()
    }

    private fun setBottomNavViewItemsClickListeners(){
        binding.bnvGlobal.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.images -> {
                    navController.navigate(NavGraphDirections.actionGlobalImagesFragment())
                    true
                }

                R.id.videos -> {
                    navController.navigate(NavGraphDirections.actionGlobalVideosFragment())
                    true
                }
                else -> {/*Do Nothing*/ true}
            }
        }
    }
}