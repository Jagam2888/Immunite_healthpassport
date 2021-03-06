package com.cmg.vaccine

import android.app.Application
import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.network.BlockChainAPi
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
        bind() from singleton { MyApi(instance()) }
        bind() from singleton { BlockChainAPi(instance()) }
        bind() from singleton { AppDatabase(instance()) }

        bind() from singleton { AuthRepositary(instance(),instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }

        bind() from singleton { SplashRepositary(instance(),instance()) }
        bind() from provider { SplashModelFactory(instance()) }

        bind() from singleton { SignUpRepositary(instance(),instance(),instance()) }
        bind() from provider { SignUpModelFactory(instance()) }

        bind() from singleton { TellUsRepositary(instance(),instance(),instance()) }
        bind() from provider { TellUsViewModelFactory(instance()) }

        bind() from singleton { ProfileRepositary(instance(),instance(),instance()) }
        bind() from provider { ProfileViewModelFactory(instance()) }

        bind() from singleton { ViewPrivateKeyRepositary(instance(),instance(),instance()) }
        bind() from provider { ViewPrivateKeyFactory(instance()) }

        bind() from singleton { HomeRepositary(instance(),instance(),instance()) }
        bind() from provider { HomeViewModelFactory(instance()) }

        bind() from singleton { ChangePasswordRepositary(instance(),instance()) }
        bind() from provider { ChangePasswordModelFactory(instance()) }

        bind() from singleton { ViewReportListRepositary(instance(),instance(),instance()) }
        bind() from provider { ViewReportModelFactory(instance()) }

        bind() from singleton { LoginPinRepositary(instance(),instance()) }
        bind() from provider { LoginPinViewFactory(instance()) }

        bind() from singleton { OTPVerifyRepositary(instance(),instance(),instance()) }
        bind() from provider { OTPVerifyModelFactory(instance()) }

        bind() from singleton { DependentRepositary(instance(),instance(),instance()) }
        bind() from provider { DependentViewModelFactory(instance()) }

        bind() from singleton { SettingsRepositary(instance(),instance(),instance()) }
        bind() from provider { SettingsModelFactory(instance()) }

        bind() from singleton { WorldEntryRepositary(instance(),instance(),instance()) }
        bind() from provider { WorldEntryViewModelFactory(instance()) }

        bind() from singleton { VaccineAndTestRepositary(instance(),instance()) }
        bind() from provider { VaccineAndTestModelFactory(instance()) }

        bind() from singleton { ImmunizationHistoryRepositary(instance()) }
        bind() from provider { ImmunizationHistoryViewModelFactory(instance()) }

        bind() from singleton { ExistingUserRepositary(instance(),instance(),instance()) }
        bind() from provider { ExistingUserViewModelFactory(instance()) }

        bind() from singleton { RestoreBackupOptionListRepositary(instance(),instance(),instance()) }
        bind() from provider { RestoreBackupOptionListViewModelFactory(instance()) }

        bind() from singleton { DepartureVerificationRepositary(instance(),instance(),instance()) }
        bind() from provider { DepartureVerificationModelFactory(instance()) }

        bind() from singleton { CountryListFragmentRepositary(instance(),instance()) }
        bind() from provider { CountryListFragmentViewModelFactory(instance()) }

        bind() from singleton { NotificationRepositary(instance(),instance(),instance()) }
        bind() from provider { NotificationViewModelFactory(instance()) }

        bind() from singleton { ImmunizationDetailRepositary(instance()) }
        bind() from provider { ImmunizationDetailViewModelFactory(instance()) }

        bind() from singleton { WelcomeViewModelRepositary(instance(),instance(),instance()) }
        bind() from provider { WelcomeViewModelFactory(instance()) }

        bind() from singleton { ClinicListRepositary(instance()) }
        bind() from provider { ClinicListViewModelFactory(instance()) }

        bind() from singleton { AirasiaRepositary(instance()) }
        bind() from provider { AirasiaViewModelFactory(instance()) }

        bind() from  singleton { ECodeValidationRepositary(instance()) }
        bind() from provider { ECodeValidationViewModelFactory(instance()) }

        bind() from singleton { CheckOutRepositary(instance(),instance()) }
        bind() from provider { CheckOutViewModelFactory(instance()) }

        bind() from singleton { FeedBackViewRepositary(instance(),instance(),instance()) }
        bind() from provider { FeedBackViewModelFactory(instance()) }

        bind() from singleton { SubscriptionRepositary(instance()) }
        bind() from provider { SubscriptionViewModelFactory(instance()) }

        bind() from singleton { FaceDetectRepositary(instance(),instance()) }
        bind() from provider { FaceDetectViewModelFactory(instance()) }

        bind() from singleton { FaceRecognitionRepositary(instance()) }
        bind() from provider { FaceRecognitionViewModelFactory(instance()) }

        //bind() from provider { MyFirebaseInstanceIDService(instance()) }

    }
}