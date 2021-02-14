package com.cmg.vaccine.database

import androidx.room.*

@Dao
interface PatientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountries(countries: Countries):Long

    @Query("SELECT * FROM Countries")
    fun getAllCountries():List<Countries>

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

    @Query("SELECT * FROM User where parentSubscriberId = :subsId AND virifyStatus = :verifyStatus")
    fun getUserData(subsId:String,verifyStatus:String):User

    @Query("SELECT privateKey FROM user where email =:email")
    fun getPrivateKey(email: String):String

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

    @Query("SELECT * FROM Dependent WHERE masterSubsId = :privateKey")
    fun getDependentList(privateKey: String):List<Dependent>

    @Query("SELECT * FROM Dependent WHERE subsId =:childPrivateKey")
    fun getDependent(childPrivateKey:String):Dependent

}