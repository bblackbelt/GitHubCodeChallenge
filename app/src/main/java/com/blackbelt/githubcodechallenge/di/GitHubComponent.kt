package com.blackbelt.githubcodechallenge.di

import com.blackbelt.githubcodechallenge.GitHubApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AndroidSupportInjectionModule::class,
        BindsModule::class,
        BuildersModule::class,
        NetworkModule::class, HelpersModule::class))
interface GitHubComponent {

    fun inject(app: GitHubApp)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: GitHubApp): Builder

        fun build(): GitHubComponent
    }
}