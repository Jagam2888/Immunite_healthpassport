package com.cmg.vaccine

import android.app.Application
import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.prefernces.PreferenceProvider
import com.cmg.vaccine.repositary.*
import com.cmg.vaccine.viewmodel.viewmodelfactory.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class AppController : Application(),KodeinAware {

    /*override fun onCreate() {
        super.onCreate()
        Paper.init(this)
    }*/
    override val kodein = Kodein.lazy {

        import(androidXModule(this@AppController))
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { MyApi(instance(),instance()) }
        bind() from singleton { AppDatabase(instance()) }

        bind() from singleton { AuthRepositary(instance(),instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }

        bind() from singleton { SplashRepositary(instance()) }
        bind() from provider { SplashModelFactory(instance()) }

        bind() from singleton { SignUpRepositary(instance(),instance(),instance()) }
        bind() from provider { SignUpModelFactory(instance()) }

        bind() from singleton { TellUsRepositary(instance(),instance(),instance()) }
        bind() from provider { TellUsViewModelFactory(instance()) }

        bind() from singleton { ProfileRepositary(instance(),instance()) }
        bind() from provider { ProfileViewModelFactory(instance()) }

        bind() from singleton { ViewPrivateKeyRepositary(instance(),instance()) }
        bind() from provider { ViewPrivateKeyFactory(instance()) }

        bind() from singleton { HomeRepositary(instance(),instance(),instance()) }
        bind() from provider { HomeViewModelFactory(instance()) }

        bind() from singleton { ChangePasswordRepositary(instance(),instance()) }
        bind() from provider { ChangePasswordModelFactory(instance()) }

        bind() from singleton { ViewReportListRepositary(instance(),instance(),instance()) }
        bind() from provider { ViewReportModelFactory(instance()) }

    }
}