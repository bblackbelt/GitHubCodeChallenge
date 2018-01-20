package com.blackbelt.githubcodechallenge.di

import com.blackbelt.githubcodechallenge.GitHubTestApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AndroidSupportInjectionModule::class,
        BindsModuleTest::class,
        BuildersModule::class,
        NetworkModule::class, HelpersModuleTest::class))
interface GitHubTestComponent : GitHubComponent {

    fun inject(app: GitHubTestApp)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: GitHubTestApp): Builder

        fun build(): GitHubTestComponent
    }
}