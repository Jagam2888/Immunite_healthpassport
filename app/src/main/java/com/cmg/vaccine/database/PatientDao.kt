package com.cmg.vaccine.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cmg.vaccine.model.request.SignUpReqData

@Dao
interface PatientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSignUp(user:User):Long

    @Query("SELECT * FROM User where email = :email AND password = :password")
    fun login(email:String,password:String):User

    @Query("SELECT * FROM User where email = :email")
    fun getUserData(email:String):User

    @Query("SELECT privateKey FROM user where email =:email")
    fun getPrivateKey(email: String):String

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePassword(user: User):Int

}