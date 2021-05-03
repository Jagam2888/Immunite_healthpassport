package com.cmg.vaccine.database

import androidx.room.*
import com.cmg.vaccine.model.JoinWorldEntryRuleAndPriority
import com.cmg.vaccine.model.TestCodeFilterByReport
import com.cmg.vaccine.model.response.SystemConfigResponseData

@Dao
interface PatientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountries(countries: Countries):Long

    @Query("SELECT * FROM Countries")
    fun getAllCountries():List<Countries>

    @Query("DELETE FROM Countries")
    fun deleteAllCountries()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWorldCountries(worldEntryCountries: WorldEntryCountries):Long

    @Query("SELECT * FROM WorldEntryCountries ORDER BY countryName")
    fun getAllWorldCountries():List<WorldEntryCountries>

    @Query("DELETE FROM WorldEntryCountries")
    fun deleteAllWorldCountries()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLoginPin(loginPin: LoginPin):Long

    @Query("SELECT * FROM LoginPin")
    fun getLoginPin():LoginPin

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateLoginPin(loginPin: LoginPin):Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSignUp(user:User):Long

    @Query("SELECT * From User")
    fun getUserCount():Int

    @Query("DELETE FROM User")
    fun deleteOldUser():Int

    /*@Query("SELECT * FROM User where email = :email AND password = :password")
    fun login(email:String,password:String):User*/

    @Query("SELECT * FROM User where parentSubscriberId = :subsId")
    fun getUserData(subsId:String):User

    @Query("SELECT * FROM User")
    fun getExistingUserData():User

    @Query("SELECT profileImage FROM User WHERE parentSubscriberId =:subsId")
    fun getParentProfileImage(subsId: String):String

    /*@Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePrivateKey(user: User):Int*/

    @Query("SELECT privateKey FROM User where parentSubscriberId =:subsId")
    fun getPrivateKey(subsId: String):String

    @Query("SELECT privateKey FROM Dependent where subsId =:subsId")
    fun getDependentPrivateKey(subsId: String):String

    /*@Query("SELECT dob FROM User WHERE parentSubscriberId =:subsId")
    fun getPrinicipalDOB(subsId:String)

    @Query("SELECT dob FROM Dependent WHERE subsId =:subsId")
    fun getDependentDOB(subsId: String)*/

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

    @Query("SELECT * FROM Dependent WHERE privateKey =:privateKey")
    fun getDependentUsingPrivateKey(privateKey: String):Dependent

    @Query("SELECT profileImage FROM Dependent WHERE subsId =:subsId")
    fun getDependentProfileImage(subsId: String):String

    @Query("DELETE FROM Dependent WHERE subsId =:subsId")
    fun deleteDependent(subsId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVaccineData(vaccine: Vaccine):Long

    @Query("SELECT * FROM Vaccine ORDER BY id DESC")
    fun getVaccineList():List<Vaccine>

    @Query("SELECT * FROM Vaccine WHERE id =:id")
    fun getVaccine(id:Int):Vaccine

    @Query("DELETE FROM Vaccine")
    fun deleteAllVaccine():Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTestReport(testReport: TestReport):Long

    @Query("SELECT * FROM TestReport WHERE privateKey = :privateKey AND statusFinalized in ('final','corrected') ORDER BY id DESC")
    fun getTestReportList(privateKey:String):List<TestReport>

    @Query("SELECT * FROM TestReport WHERE id =:id")
    fun getTestReport(id:Int):TestReport

    @Query("SELECT * FROM TestReport WHERE testCode =:testCode")
    fun getTestReportByTestCode(testCode:String):TestReport

    @Query("DELETE FROM TestReport")
    fun deleteAllTestReport():Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVaccineReport(vaccineReport: VaccineReport):Long

    @Query("SELECT * FROM VaccineReport WHERE privateKey = :privateKey ORDER BY id DESC")
    fun getVaccineReportList(privateKey:String):List<VaccineReport>

    @Query("SELECT * FROM VaccineReport WHERE id = :id")
    fun getVaccineReport(id: Int):VaccineReport

    @Query("DELETE FROM VaccineReport")
    fun deleteAllVaccineReport():Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAddWorldEntry(addWorldEntries: AddWorldEntries):Long

    /*@Query("SELECT * FROM AddWorldEntries")
    fun getWorldEntries():List<AddWorldEntries>*/

    @Query("SELECT * FROM AddWorldEntries WHERE countryName =:countryName")
    fun getCountryExists(countryName:String):Int

    @Query("DELETE FROM AddWorldEntries WHERE countryName =:countryName")
    fun deleteAddWorldEntries(countryName: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTestType(testType: TestType):Long

    @Query("SELECT * FROM VaccineDetail")
    fun getVaccineDetailList():List<VaccineDetail>

    @Query("SELECT * FROM TestType")
    fun getTestTypeList():List<TestType>

    @Query("SELECT * FROM VaccineDetail WHERE vaccine_code =:vaccineCode")
    fun getVaccineDetail(vaccineCode:String):VaccineDetail

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVirus(virus: Virus):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVaccineDetail(vaccineDetail: VaccineDetail):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWorldEntryRulesByCountry(worldEntryRulesByCountry: WorldEntryRulesByCountry)

    @Query("SELECT * FROM WorldEntryRulesByCountry WHERE woen_country_code =:countryCode")
    fun getWorldEntryRuleByCountryByCode(countryCode:String):List<WorldEntryRulesByCountry>

    @Query("SELECT * FROM WorldEntryRulesByCountry WHERE woen_country_code =:countryCode AND woen_rule_match_criteria =:criteria AND woen_status='A'")
    fun getWorldEntryRuleForMAS(countryCode:String,criteria:String):List<WorldEntryRulesByCountry>

    @Query("SELECT max(woen_duration_hours) FROM WorldEntryRulesByCountry WHERE woen_country_code =:countryCode AND woen_rule_match_criteria =:criteria AND woen_status='A' AND woen_rule_seq_no =:ruleSeqNo")
    fun getMaxHoursWorldEntryRuleForMAS(countryCode:String,criteria:String,ruleSeqNo:String):String

    @Query("SELECT * FROM WorldEntryRulesByCountry")
    fun getAllWorldEntryRuleByCountry():List<WorldEntryRulesByCountry>

    @Query("DELETE FROM WorldEntryRulesByCountry")
    fun deleteAllWorldEntryRuleByCountry()

    @Query("UPDATE AddWorldEntries SET `order`=:order WHERE countryName=:countryName")
    fun updateWEOrder(countryName:String,order:Int)

    //These two used for world entries save the order
    @Query("SELECT COUNT(id) FROM AddWorldEntries")
    fun getCurrentCount():Int

    @Query("SELECT * FROM AddWorldEntries ORDER BY `order`")
    fun getWorldEntries():List<AddWorldEntries>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAirportCitiesName(airportCitiesName: AirportCitiesName):Long

    @Query("SELECT * FROM AirportCitiesName")
    fun getAllAirportCities():List<AirportCitiesName>

    @Query("SELECT * FROM AirportCitiesName WHERE cityCode =:cityCode")
    fun getAirportCityByCode(cityCode:String):AirportCitiesName

    @Query("DELETE FROM AirportCitiesName")
    fun deleteAllAirportCities()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTestCodes(testCodes: TestCodes):Long

    @Query("SELECT * FROM TestCodes")
    fun getAllTestCodes():List<TestCodes>

    @Query("SELECT * FROM TestCodes WHERE wetstTestcategory IN (:category) AND wetstCountryCode =:countryCode")
    fun getAllTestCodesByCategory(category:ArrayList<String>,countryCode: String):List<TestCodes>

    @Query("DELETE FROM TestCodes")
    fun deleteAllTestCodes()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWorldPriority(worldPriority: WorldPriority):Long

    @Query("DELETE FROM WorldPriority")
    fun deleteAllWorldPriority()

    @Query("SELECT * FROM WorldPriority")
    fun getAllWorldPriority():List<WorldPriority>

    @Query("SELECT * FROM WorldPriority WHERE prioRuleNo =:ruleNo AND prioRuleCountry =:countryCode")
    fun getAllWorldPriorityByRuleNo(ruleNo:String,countryCode: String):WorldPriority

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIdentifierType(identifierType: IdentifierType):Long

    @Query("SELECT * FROM IdentifierType")
    fun getAllIdentifierType():List<IdentifierType>

    @Query("DELETE FROM IdentifierType")
    fun deleteAllIdentifierType()

    @Query("SELECT a.woen_rule_match_criteria,a.woen_country_code,b.prioRuleCountry, a.woen_rule_seq_no,a.woen_test_code,a.woen_duration_hours,b.prioRuleNo,b.prioRuleCriteria,b.prioRulePair FROM WorldEntryRulesByCountry a,WorldPriority b WHERE a.woen_country_code =:countryCode and a.woen_rule_match_criteria in ('T','P','V') and a.woen_rule_seq_no = b.prioRuleNo and a.woen_country_code = b.prioRuleCountry")
    fun getJoinWorldEntryRuleAndPriority(countryCode: String):List<JoinWorldEntryRuleAndPriority>

    @Query("SELECT a.testCode,a.observationCode,a.dateSampleCollected,a.timeSampleCollected,b.wetstObservationStatusCode FROM TestReport a,TestCodes b WHERE a.privateKey =:privateKey AND b.wetstCountryCode=:countryCode AND b.wetstTestCode=a.testCode")
    fun getFilterTestCodeByReport(privateKey: String,countryCode: String):List<TestCodeFilterByReport>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBlockChainErrorCode(blockChainErrorCode: BlockChainErrorCode):Long

    @Query("DELETE FROM BlockChainErrorCode")
    fun deleteAllBlockChainErrorCode()

    @Query("SELECT prioRuleCriteria FROM BlockChainErrorCode WHERE prioRuleCountry =:errorCode")
    fun getErrorMessage(errorCode:String):String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertObservationStatus(observationStatus: ObservationStatus):Long

    @Query("SELECT oscDisplayName FROM ObservationStatus WHERE oscSnomedCode =:code")
    fun getObservationStatus(code:String):String?

    @Query("DELETE FROM ObservationStatus")
    fun deleteAllObservationStatus()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotificationMessage(notification: Notification):Long

    @Query("SELECT * FROM Notification WHERE notificationGroup =:group")
    fun getNotificationMsgByGroup(group:String):List<Notification>

    @Query("DELETE FROM Notification WHERE notificationGroup =:group")
    fun deleteNotificationByGroup(group: String)

    @Query("SELECT COUNT(isRead) FROM Notification WHERE isRead =0 AND notificationGroup =:group")
    fun getUnreadNotificationCount(group: String):Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertScanAirportEntry(scanAirportEntry: ScanAirportEntry):Long

    @Query("UPDATE Notification SET isRead=1 WHERE id =:id")
    fun updateNotificationReadStatus(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSystemConfig(systemConfigResponseData: SystemConfigResponseData)

    @Query("DELETE FROM SystemConfigResponseData")
    fun deleteAllSystemConfig()

    @Query("SELECT sysMappingValue FROM SystemConfigResponseData WHERE sysMappingKeyName ='No_of_Dependent'")
    fun getNoOfDependentCount():String

    @Query("SELECT sysMappingValue FROM SystemConfigResponseData WHERE sysMappingKeyName ='MAS_Counter_Check_IN'")
    fun getCounterCheckinDecryptKey():String
}