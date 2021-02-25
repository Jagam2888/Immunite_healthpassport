package com.cmg.vaccine.database

import androidx.room.*

@Dao
interface PatientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountries(countries: Countries):Long

    @Query("SELECT * FROM Countries")
    fun getAllCountries():List<Countries>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWorldCountries(worldEntryCountries: WorldEntryCountries):Long

    @Query("SELECT * FROM WorldEntryCountries")
    fun getAllWorldCountries():List<WorldEntryCountries>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLoginPin(loginPin: LoginPin):Long

    @Query("SELECT * FROM LoginPin")
    fun getLoginPin():LoginPin

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateLoginPin(loginPin: LoginPin):Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSignUp(user:User):Long

    /*@Query("SELECT * FROM User where email = :email AND password = :password")
    fun login(email:String,password:String):User*/

    @Query("SELECT * FROM User where parentSubscriberId = :subsId")
    fun getUserData(subsId:String):User

    /*@Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePrivateKey(user: User):Int*/

    @Query("SELECT privateKey FROM User where parentSubscriberId =:subsId")
    fun getPrivateKey(subsId: String):String

    @Query("SELECT privateKey FROM Dependent where subsId =:subsId")
    fun getDependentPrivateKey(subsId: String):String

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(user: User):Int

    /*@Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePassword(user: User):Int*/

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateVerifyStatus(user: User):Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDependent(dependent: Dependent):Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateDependent(dependent: Dependent):Int

    @Query("SELECT * FROM Dependent WHERE masterSubsId = :masterSubId")
    fun getDependentList(masterSubId: String):List<Dependent>

    @Query("SELECT * FROM Dependent WHERE subsId =:subId")
    fun getDependent(subId:String):Dependent

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVaccineData(vaccine: Vaccine):Long

    @Query("SELECT * FROM Vaccine")
    fun getVaccineList():List<Vaccine>

    @Query("SELECT * FROM Vaccine WHERE id =:id")
    fun getVaccine(id:Int):Vaccine

    @Query("DELETE FROM Vaccine")
    fun deleteAllVaccine():Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTestReport(testReport: TestReport):Long

    @Query("SELECT * FROM TestReport")
    fun getTestReportList():List<TestReport>

    @Query("SELECT * FROM TestReport WHERE id =:id")
    fun getTestReport(id:Int):TestReport

    @Query("DELETE FROM TestReport")
    fun deleteAllTestReport():Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAddWorldEntry(addWorldEntries: AddWorldEntries):Long

    @Query("SELECT * FROM AddWorldEntries")
    fun getWorldEntries():List<AddWorldEntries>

    @Query("SELECT * FROM AddWorldEntries WHERE countryName =:countryName")
    fun getCountryExists(countryName:String):Int

    @Query("DELETE FROM AddWorldEntries WHERE countryName =:countryName")
    fun deleteAddWorldEntries(countryName: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTestType(testType: TestType):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVirus(virus: Virus):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVaccineDetail(vaccineDetail: VaccineDetail):Long

}