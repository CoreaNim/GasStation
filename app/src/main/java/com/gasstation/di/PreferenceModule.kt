package com.gasstation.di

import android.content.Context
import android.content.SharedPreferences
import com.gasstation.domain.repository.SharePrefsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class PreferenceModule {
    @Provides
    @Singleton
    @Named("GAS_STATION_PREFERENCE")
    fun providePreferenceName() = "gas-station-preference"

    @Provides
    @Singleton
    fun provideSharePreference(
        @Named("GAS_STATION_PREFERENCE") preferenceName: String,
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSharePrefsRepo(sharePrefs: SharedPreferences): SharePrefsRepository =
        SharePrefsRepository(sharePrefs)
}