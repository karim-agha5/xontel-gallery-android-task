package com.example.gallerydemokarimnabil.features.main

import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.gallerydemokarimnabil.NavGraphDirections
import com.example.gallerydemokarimnabil.R
import com.example.gallerydemokarimnabil.core.Utils
import com.example.gallerydemokarimnabil.core.helpers.MediaPermissionsHandler
import com.example.gallerydemokarimnabil.core.interfaces.GalleryStartDestination
import com.example.gallerydemokarimnabil.databinding.ActivityMainBinding
import com.example.gallerydemokarimnabil.features.main.viewmodel.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private val mainActivityViewModel by viewModels<MainActivityViewModel>()
    private lateinit var galleryStartDestination: GalleryStartDestination
    private lateinit var permissionsHandler: MediaPermissionsHandler

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        initStrictPolicy()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setScreenViewsToGone()
        initNavComponents()
        setAppStartDestination()
        setBottomNavViewItemsClickListeners()

        // Register the permission result callback
        val requestPermissionLauncher = onPermissionResult()


        // Checking permissions based on the android versions
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P){
            buildPermissionHandlerForSdkLessThanP(requestPermissionLauncher)
        }
        else{
            buildPermissionHandlerForSdkMoreThanP(requestPermissionLauncher)
        }

        if(permissionsHandler.arePermissionsGranted()){
            setScreenViewsToVisible()
        }else{
            permissionsHandler.requestPermissions()
        }


    }

    // TODO remove before delivery
    private fun initStrictPolicy(){
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
            .detectDiskReads()
            .detectDiskWrites()
            .penaltyLog()
            .penaltyDeath()
            .build())

        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
            .detectLeakedSqlLiteObjects()
            .detectLeakedClosableObjects()
            .penaltyLog()
            .penaltyDeath()
            .build())
    }

    private fun buildPermissionHandlerForSdkLessThanP(requestPermissionLauncher: ActivityResultLauncher<Array<String>>){
        permissionsHandler =
            MediaPermissionsHandler
                .Builder(application)
                .readExternalStorage()
                //.onPermissionsGranted(galleryStartDestination::invokeWhenPermissionsGranted)
                .onPermissionsGranted(
                    arrayOf(
                        this::galleryStartDestinationOnPermissionGranted,
                        this::setScreenViewsToVisible
                    )
                )
                .activityResultLauncher(requestPermissionLauncher)
                .build()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun buildPermissionHandlerForSdkMoreThanP(requestPermissionLauncher: ActivityResultLauncher<Array<String>>){
        permissionsHandler =
            MediaPermissionsHandler
                .Builder(application)
                .readExternalStorage()
                .writeExternalStorage()
                .readMediaImages()
                .readMediaVideos()
                //.onPermissionsGranted(galleryStartDestination::invokeWhenPermissionsGranted)
                .onPermissionsGranted(
                    arrayOf(
                        this::galleryStartDestinationOnPermissionGranted,
                        this::setScreenViewsToVisible
                    )
                )
                .activityResultLauncher(requestPermissionLauncher)
                .build()
    }

    private fun galleryStartDestinationOnPermissionGranted(){
        if(this::galleryStartDestination.isInitialized){
            galleryStartDestination.invokeWhenPermissionsGranted()
        }
    }

    private fun initNavComponents(){
         navHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        navController = navHostFragment.findNavController()
    }

    private fun setAppStartDestination(){
        val attachedFragment = navHostFragment.childFragmentManager.fragments[0]
        if(attachedFragment is GalleryStartDestination){
            galleryStartDestination = attachedFragment
        }
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

    private fun permissionRationaleAction(){
        showPermanentSnackbar()
    }

    private fun noPermissionRationaleAction(){
        showPermanentSnackbarForDeviceSettings()
    }

    private fun showPermanentSnackbar(){
        Snackbar
            .make(binding.root,R.string.permanent_snackbar_message,Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.permanent_snackbar_button_label){
                permissionsHandler.requestPermissions()
            }
            .show()
    }

    private fun showPermanentSnackbarForDeviceSettings(){
        Snackbar
            .make(binding.root,R.string.permanent_snackbar_message_no_rationale,Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.permanent_snackbar_button_label_no_rationale){
                Utils.openDeviceSettings(this)
            }
            .show()
    }

    private fun handlePermissionResultsWhenSdkLessThanP() {
        if(permissionsHandler.isReadExternalStoragePermissionGranted()){
            permissionsHandler.invokeMultipleOnPermissionsGrantedIfProvided()
        }
        else{
            if(permissionsHandler.shouldShowRationaleForReadExternalStorage(this)){
                setScreenViewsToGone()
                permissionRationaleAction()
            }
            else{
                noPermissionRationaleAction()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun handlePermissionResultsWhenSdkMoreThanP() {
        if(
            permissionsHandler.isReadMediaImagesPermissionGranted()
            &&
            permissionsHandler.isReadMediaVideosPermissionGranted()
        ){
            permissionsHandler.invokeMultipleOnPermissionsGrantedIfProvided()
        }
        if(
            !permissionsHandler.isReadMediaImagesPermissionGranted()
            &&
            !permissionsHandler.isReadMediaVideosPermissionGranted()
        ){
            if(permissionsHandler.shouldShowRationaleForReadImagesAndVideos(this)){
                setScreenViewsToGone()
                permissionRationaleAction()
            }
            else{
                noPermissionRationaleAction()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun onPermissionResult() : ActivityResultLauncher<Array<String>>{
        return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P){
                handlePermissionResultsWhenSdkLessThanP()
            }
            else{
                handlePermissionResultsWhenSdkMoreThanP()
            }
        }
    }
}