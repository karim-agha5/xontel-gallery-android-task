package com.example.gallerydemokarimnabil.features.main

import android.content.ContentUris
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.gallerydemokarimnabil.NavGraphDirections
import com.example.gallerydemokarimnabil.R
import com.example.gallerydemokarimnabil.core.interfaces.GalleryStartDestination
import com.example.gallerydemokarimnabil.databinding.ActivityMainBinding
import com.example.gallerydemokarimnabil.features.main.viewmodel.MainActivityViewModel
import com.example.gallerydemokarimnabil.features.main.viewmodel.MainActivityViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private val mainActivityViewModel by viewModels<MainActivityViewModel>()
    private lateinit var galleryStartDestination: GalleryStartDestination
    private lateinit var imagesLiveData: MutableLiveData<List<Drawable?>>
    private lateinit var permissionsHandler: MediaPermissionsHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setScreenViewsToGone()
       // imagesLiveData = MutableLiveData()

        initNavComponents()
        setAppStartDestination()
        setBottomNavViewItemsClickListeners()




        /*imagesLiveData.observe(this){
            Log.i("MainActivity", "list's size -> ${it.size}")
        }*/

        // Register the permission result callback
        val requestPermissionLauncher = onPermissionResult()

        permissionsHandler =
            MediaPermissionsHandler
                .Builder(application)
                .readExternalStorage()
                .writeExternalStorage()
                .onPermissionsGranted(galleryStartDestination::invokeWhenPermissionsGranted)
                .activityResultLauncher(requestPermissionLauncher)
                .build()

        if(permissionsHandler.isReadExternalStoragePermissionGranted()){
            setScreenViewsToVisible()
            //loadImages()
        }
        else{
            permissionsHandler.requestPermissions()
        }
    }

    private fun initNavComponents(){
         navHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        navController = navHostFragment.findNavController()
    }

    private fun setAppStartDestination(){
        if(mainActivityViewModel.appStartDestination.value != null){
            galleryStartDestination =
                mainActivityViewModel.appStartDestination.value as GalleryStartDestination
        }
        else{
            mainActivityViewModel.setAppStartDestination(navHostFragment)
            galleryStartDestination =
                mainActivityViewModel.appStartDestination.value as GalleryStartDestination
        }
    }

    /*
    * The current visible fragment has to be the start destination.
    * */
    private fun findStartDestination() : Fragment?{
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        return navHostFragment.childFragmentManager.getFragments().get(0)
    }

    private fun setBottomNavViewItemsClickListeners(){
        binding.bnvGlobal.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.images -> {
                    // If it's currently selected. Don't navigate again.
                    if(binding.bnvGlobal.selectedItemId != it.itemId){
                        navigateToImages()
                    }
                    true
                }

                R.id.videos -> {
                    // If it's currently selected. Don't navigate again.
                    if(binding.bnvGlobal.selectedItemId != it.itemId){
                        navigateToVideos()
                    }
                    true
                }
                else -> {/*Do Nothing*/ true}
            }
        }
    }

    private fun navigateToImages(){
        navController.navigate(
            NavGraphDirections.actionGlobalImagesFragment(),
            navOptions {
                popUpTo(R.id.nav_graph){
                    inclusive = true
                }
            }
        )
    }

    private fun navigateToVideos(){
        navController.navigate(
            NavGraphDirections.actionGlobalVideosFragment(),
            navOptions {
                popUpTo(R.id.images){
                    inclusive = false
                }
            }
        )
    }

    private fun setScreenViewsToGone(){
        binding.fragmentContainerView.visibility = View.GONE
        binding.bnvGlobal.visibility = View.GONE
    }

    private fun setScreenViewsToVisible(){
        binding.fragmentContainerView.visibility = View.VISIBLE
        binding.bnvGlobal.visibility = View.VISIBLE
    }

    private fun setBottomNavViewVisibilityToGone(){
        binding.bnvGlobal.visibility = View.GONE
    }

    private fun showPermissionRationale(){
        showPermanentSnackbar()
    }

    private fun showPermanentSnackbar(){
        Snackbar
            .make(binding.root,R.string.permanent_snackbar_message,Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.permanent_snackbar_button_label){
                permissionsHandler.requestPermissions()
            }
            .show()
    }

    private fun onPermissionResult() : ActivityResultLauncher<Array<String>>{
        return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            if(it[MediaPermissionsHandler.READ_EXTERNAL_STORAGE] == true){
                setScreenViewsToVisible()
                permissionsHandler.invokeOnPermissionsGrantedIfProvided()
                //loadImages()
            }
            if(it[MediaPermissionsHandler.READ_EXTERNAL_STORAGE] == false){
                if(permissionsHandler.shouldShowRationaleForReadExternalStorage(this)){
                    setScreenViewsToGone()
                    showPermissionRationale()
                }
            }
        }
    }

    private suspend fun loadImagesFromInternalStorage(){
        val images = mutableListOf<Drawable?>()
        var inputStream: InputStream? = null

        withContext(Dispatchers.IO){
            val projections = arrayOf(MediaStore.Images.Media._ID)
            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
            application.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projections,
                null,
                null,
                sortOrder
            )?.use {
                val columnId = it.getColumnIndex(MediaStore.Images.Media._ID)
                while (it.moveToNext()){
                    val id = it.getLong(columnId)
                    val contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id)
                    inputStream = contentResolver.openInputStream(contentUri)
                    images.add(Drawable.createFromStream(inputStream,contentUri.toString()))
                    if(!isActive){
                        Log.i("MainActivity", "The current coroutine is inactive.")
                    }
                }
            }
            inputStream?.close()
        }

        imagesLiveData.postValue(images)
    }

    /*
    * This function is main-safe
    * */
    private fun loadImages(){
        lifecycleScope.launch { loadImagesFromInternalStorage() }
    }
}