package com.androidactivity.datastore_android_github

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MyViewModel @Inject constructor(
    private val myPreferencesDatastore: MyPreferencesDatastore
) : ViewModel() {
    val userPreferencesFlow = myPreferencesDatastore.userPreferencesFlow
        .map { it.toggle }

    fun updateThemeState(newValue: Boolean) {
        viewModelScope.launch {
            myPreferencesDatastore.updateMyToggleButton(newValue)
        }
    }
}