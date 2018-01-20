package com.blackbelt.githubcodechallenge.di

import android.content.Context
import com.blackbelt.githubcodechallenge.GitHubTestApp
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HelpersModuleTest {
    @Provides
    fun provideContext(app: GitHubTestApp): Context = app.applicationContext

    @Provides
    fun provideResources(context: Context) = context.resources

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()
}