package com.androidactivity.datastore_android_github

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "settings")

data class UserPreferences(
    var toggle: Boolean
)

class MyPreferencesDatastore @Inject constructor(@ApplicationContext context: Context) {
    private val dataStore = context.datastore

    private object PreferencesKeys {
        val TOGGLE_BUTTON_KEY = booleanPreferencesKey("toggle_button_key")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    suspend fun updateMyToggleButton(toggle: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TOGGLE_BUTTON_KEY] = toggle
        }
    }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        // Get our show completed value, defaulting to false if not set:
        val toggle = preferences[PreferencesKeys.TOGGLE_BUTTON_KEY] ?: false
        return UserPreferences(toggle)
    }
}