package com.cmg.vaccine

import android.app.Application
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.prefernces.PreferenceProvider
import com.cmg.vaccine.repositary.AuthRepositary
import com.cmg.vaccine.repositary.SplashRepositary
import com.cmg.vaccine.viewmodel.AuthViewModelFactory
import com.cmg.vaccine.viewmodel.SplashModelFactory
import io.paperdb.Paper
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.bindings.Singleton
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import kotlin.math.sin

class AppController : Application(),KodeinAware {

    /*override fun onCreate() {
        super.onCreate()
        Paper.init(this)
    }*/
    override val kodein = Kodein.lazy {

        import(androidXModule(this@AppController))
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { MyApi(instance(),instance()) }
        bind() from singleton { AuthRepositary(instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from singleton { SplashRepositary(instance()) }
        bind() from provider { SplashModelFactory(instance()) }

    }
}