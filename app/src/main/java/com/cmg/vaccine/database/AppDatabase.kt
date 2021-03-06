package com.cmg.vaccine.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cmg.vaccine.model.response.*

@Database(
    entities = [User::class,LoginPin::class,Dependent::class,Countries::class,Vaccine::class,TestReport::class,AddWorldEntries::class,
               TestType::class,Virus::class,VaccineDetail::class,WorldEntryCountries::class,WorldEntryRulesByCountry::class,AirportCitiesName::class,TestCodes::class,
               IdentifierType::class,WorldPriority::class,BlockChainErrorCode::class,ObservationStatus::class,Notification::class,ScanAirportEntry::class,VaccineReport::class,
               SystemConfigResponseData::class,PackageCodeResponseData::class,GetFeedbackStatusResponseData::class,GetFeedbackStatusResponseAttachment::class,
               GetFeedbackChronology::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getDao() : PatientDao

    companion object {
        @Volatile
        private var instance:AppDatabase?=null
        private val LOCK = Any()

        /*private val migration_1_2 = object :Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `Dependent`(`id` INTEGER,`countryCode` TEXT,`dob` TEXT,`email` TEXT,`firstName` TEXT," +
                        "`gender` TEXT,`idNo` TEXT,`parentPrivateKey` TEXT,`childPrivateKey` TEXT,`mobileNumber` TEXT,`nationalityCountry` TEXT," +
                        "`passportNo` TEXT,`provinceState` TEXT,`relationship` TEXT,`residentialAddress` TEXT,`townCity` TEXT,PRIMARY KEY(`id`)))")
            }
        }*/

        operator fun invoke(context: Context) = instance?: synchronized(LOCK){
            instance?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "immunitees.db"
            ).allowMainThreadQueries().build()
    }

}