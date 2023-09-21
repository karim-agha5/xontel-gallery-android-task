package com.example.gallerydemokarimnabil.features.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment

class MainActivityViewModelFactory(
    private val navHostFragment: NavHostFragment
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel() as T
    }

}