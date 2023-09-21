package com.example.gallerydemokarimnabil.features.main.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivityViewModel : ViewModel() {
    private val _appStartDestination = MutableStateFlow<Fragment?>(null)
    val appStartDestination = _appStartDestination.asStateFlow()

    fun setAppStartDestination(navHostFragment: NavHostFragment){
        _appStartDestination.value = navHostFragment.childFragmentManager.fragments[0]
    }
}