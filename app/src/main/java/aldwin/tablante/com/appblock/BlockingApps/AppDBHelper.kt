package aldwin.tablante.com.appblock.BlockingApps

import aldwin.tablante.com.appblock.LockDevice
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.json.JSONObject

class   AppDBHelper(context: Context?) : SQLiteOpenHelper(context, "Apps.db", null, 8) {
    val APPS_TABLE_NAME = "apps"
    val ONE_MINUTE_TABLE_NAME = "oneMin"
    val FIVE_MINUTE_TABLE_NAME = "fiveMin"
    val TEN_MINUTE_TABLE_NAME = "tenMin"
    val TWENTY_MINUTE_TABLE_NAME = "twentyMin"
    val THIRTY_MINUTE_TABLE_NAME = "thirtyMin"
    val LOCK_TABLE_NAME = "locks"
    val DAYS_TABLE_NAME = "time_days"
    val TIME_FROM_TABLE_NAME = "time_from"
    val TIME_TO_TABLE_NAME = "time_to"
    val PAIRED_DEVICE = "pairing"
    val LOCK_DEVICE = "lock_device"
    val APP_USAGE = "db_app_usage"

    val COL_2 = "APPNAME"
    val COL_3 = "MINUTE"
    val COL_4 = "NAME"
    val COL_5 = "APPS"
    val MINUTE1 = "OneMinute"
    val MINUTE2 = "FiveMinutes"
    val MINUTE3 = "TenMinutes"
    val MINUTE4 = "TwentyMinutes"
    val MINUTE5 = "ThirtyMinutes"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $APPS_TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, APPNAME TEXT)")
        db?.execSQL("CREATE TABLE $ONE_MINUTE_TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT)")
        db?.execSQL("CREATE TABLE $FIVE_MINUTE_TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT)")
        db?.execSQL("CREATE TABLE $TEN_MINUTE_TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT)")
        db?.execSQL("CREATE TABLE $TWENTY_MINUTE_TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT)")
        db?.execSQL("CREATE TABLE $THIRTY_MINUTE_TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT)")
        db?.execSQL("CREATE TABLE $LOCK_TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, MINUTE TEXT)")
        db?.execSQL("CREATE TABLE $DAYS_TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, TIME_DAYS TEXT)")
        db?.execSQL("CREATE TABLE $TIME_FROM_TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, TIME_FROM TEXT)")
        db?.execSQL("CREATE TABLE $TIME_TO_TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, TIME_TO TEXT)")
        db?.execSQL("CREATE TABLE $LOCK_DEVICE (_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT)")
        db?.execSQL("CREATE TABLE $PAIRED_DEVICE (_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT)")

        db?.execSQL("CREATE TABLE $APP_USAGE (_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        db?.execSQL("DROP TABLE IF EXISTS $APPS_TABLE_NAME")
//        db?.execSQL("DROP TABLE IF EXISTS $TIMES_TABLE_NAME")
//        db?.execSQL("DROP TABLE IF EXISTS $LOCK_TABLE_NAME")
    }

    fun clearDatabase(TABLE_NAME: String) {
        val db:SQLiteDatabase = this.writableDatabase
        val clearDBQuery = "DELETE FROM $TABLE_NAME"
        db.execSQL(clearDBQuery)
    }


    fun insertAppUsage(name:String): Boolean{
        var createSuccessful = false
        var contentValues = ContentValues()
        contentValues.put("NAME", name)
        val db:SQLiteDatabase = this.writableDatabase
        createSuccessful = db.insert(APP_USAGE, null, contentValues) > 0
        db.close()
        return createSuccessful
    }

    fun updatAppUsage(id: Int, jsonObject: JSONObject):Boolean {
        val db:SQLiteDatabase = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put("NAME", jsonObject.toString())
        var result = db.update(APP_USAGE, contentValues, "_id =?", arrayOf<String>(id.toString()))
        if(result>0){
            return true
        }else{
            return false
        }
    }

    fun getAppUsage(): Cursor {
        val db:SQLiteDatabase = this.writableDatabase
        var res: Cursor = db.rawQuery("SELECT * FROM $APP_USAGE", null)
        return res
    }

    fun insertPairingDevice(name:String): Boolean{
        var createSuccessful = false
        var contentValues = ContentValues()
        contentValues.put("NAME", name)
        val db:SQLiteDatabase = this.writableDatabase
        createSuccessful = db.insert(PAIRED_DEVICE, null, contentValues) > 0
        db.close()
        return createSuccessful
    }

    fun updatePairingDevice(id: Int, name: String):Boolean {
        val db:SQLiteDatabase = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put("NAME", name)
        var result = db.update(PAIRED_DEVICE, contentValues, "_id =?", arrayOf<String>(id.toString()))
        if(result>0){
            return true
        }else{
            return false
        }
    }

    fun getPairingDevice(): Cursor {
        val db:SQLiteDatabase = this.writableDatabase
        var res: Cursor = db.rawQuery("SELECT * FROM $PAIRED_DEVICE", null)
        return res
    }

    fun insertLockDevice(name:String):Boolean {
        var createSuccessful = false
        var contentValues = ContentValues()
        contentValues.put("NAME", name)
        val db:SQLiteDatabase = this.writableDatabase
        createSuccessful = db.insert(LOCK_DEVICE, null, contentValues) > 0
        db.close()
        return createSuccessful
    }

    fun updateLockDevice(id: Int, name: String):Boolean {
        val db:SQLiteDatabase = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put("NAME", name)
        var result = db.update(LOCK_DEVICE, contentValues, "_id =?", arrayOf<String>(id.toString()))
        if(result>0){
            return true
        }else{
            return false
        }
    }

    fun getLockDevice(): Cursor {
        val db:SQLiteDatabase = this.writableDatabase
        var res: Cursor = db.rawQuery("SELECT * FROM $LOCK_DEVICE", null)
        return res
    }

    fun insertAppsData(name:String):Boolean {
        var createSuccessful = false

        var contentValues = ContentValues()
        contentValues.put("APPNAME", name)

        val db:SQLiteDatabase = this.writableDatabase
        createSuccessful = db.insert(APPS_TABLE_NAME, null, contentValues) > 0

        db.close()

        return createSuccessful
    }

    fun updateAppsData(id: Int, jsonObject: JSONObject):Boolean {
        val db:SQLiteDatabase = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put("APPNAME", jsonObject.toString())
        var result = db.update(APPS_TABLE_NAME, contentValues, "_id =?", arrayOf<String>(id.toString()))
        if(result>0){
            return true
        }else{
            return false
        }
    }






    fun insertOneMin(apps:String):Boolean {
        var createSuccessful = false

        var contentValues = ContentValues()
        contentValues.put("NAME", apps)

        val db:SQLiteDatabase = this.writableDatabase
        createSuccessful = db.insert(ONE_MINUTE_TABLE_NAME, null, contentValues) > 0

        db.close()

        return createSuccessful
    }

    fun insertFiveMin(apps:String):Boolean {
        var createSuccessful = false

        var contentValues = ContentValues()
        contentValues.put("NAME", apps)

        val db:SQLiteDatabase = this.writableDatabase
        createSuccessful = db.insert(FIVE_MINUTE_TABLE_NAME, null, contentValues) > 0

        db.close()

        return createSuccessful
    }

    fun insertTenMin(apps:String):Boolean {
        var createSuccessful = false

        var contentValues = ContentValues()
        contentValues.put("NAME", apps)

        val db:SQLiteDatabase = this.writableDatabase
        createSuccessful = db.insert(TEN_MINUTE_TABLE_NAME, null, contentValues) > 0

        db.close()

        return createSuccessful
    }

    fun insertTwentyMin(apps:String):Boolean {
        var createSuccessful = false

        var contentValues = ContentValues()
        contentValues.put("NAME", apps)

        val db:SQLiteDatabase = this.writableDatabase
        createSuccessful = db.insert(TWENTY_MINUTE_TABLE_NAME, null, contentValues) > 0

        db.close()

        return createSuccessful
    }

    fun insertThirtyMin(apps:String):Boolean {
        var createSuccessful = false

        var contentValues = ContentValues()
        contentValues.put("NAME", apps)

        val db:SQLiteDatabase = this.writableDatabase
        createSuccessful = db.insert(THIRTY_MINUTE_TABLE_NAME, null, contentValues) > 0

        db.close()

        return createSuccessful
    }

    fun insertLockData(name:String):Boolean {
        var createSuccessful = false

        var contentValues = ContentValues()
        contentValues.put("MINUTE", name)

        val db:SQLiteDatabase = this.writableDatabase
        createSuccessful = db.insert(LOCK_TABLE_NAME, null, contentValues) > 0

        db.close()

        return createSuccessful
    }

    fun updateLockTime(id: Int, time: String):Boolean {
        val db:SQLiteDatabase = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put("MINUTE", time)
        var result = db.update(LOCK_TABLE_NAME, contentValues, "_id =?", arrayOf<String>(id.toString()))
        if(result>0){
            return true
        }else{
            return false
        }
    }

    fun insertDaysData(name:String):Boolean {
        var createSuccessful = false
        var contentValues = ContentValues()
        contentValues.put("TIME_DAYS", name)
        val db:SQLiteDatabase = this.writableDatabase
        createSuccessful = db.insert(DAYS_TABLE_NAME, null, contentValues) > 0
        db.close()
        return createSuccessful
    }

    fun updateDaysData(id: Int, jsonObject: JSONObject):Boolean {
        val db:SQLiteDatabase = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put("TIME_DAYS", jsonObject.toString())
        var result = db.update(DAYS_TABLE_NAME, contentValues, "_id =?", arrayOf<String>(id.toString()))
        if(result>0){
            return true
        }else{
            return false
        }
    }

    fun getAllDays(): Cursor {
        val db:SQLiteDatabase = this.writableDatabase
        var res: Cursor = db.rawQuery("SELECT * FROM $DAYS_TABLE_NAME", null)
        return res
    }

    fun insertFromTime(name:String):Boolean {
        var createSuccessful = false
        var contentValues = ContentValues()
        contentValues.put("TIME_FROM", name)
        val db:SQLiteDatabase = this.writableDatabase
        createSuccessful = db.insert(TIME_FROM_TABLE_NAME, null, contentValues) > 0
        db.close()

        return createSuccessful
    }

    fun updateFromTime(id: Int, time: String):Boolean {
        val db:SQLiteDatabase = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put("TIME_FROM", time)
        var result = db.update(TIME_FROM_TABLE_NAME, contentValues, "_id =?", arrayOf<String>(id.toString()))
        if(result>0){
            return true
        }else{
            return false
        }
    }

    fun getFromTime(): Cursor {
        val db:SQLiteDatabase = this.writableDatabase
        var res: Cursor = db.rawQuery("SELECT * FROM $TIME_FROM_TABLE_NAME", null)
        return res
    }

    fun insertToTime(name:String):Boolean {
        var createSuccessful = false
        var contentValues = ContentValues()
        contentValues.put("TIME_TO", name)
        val db:SQLiteDatabase = this.writableDatabase
        createSuccessful = db.insert(TIME_TO_TABLE_NAME, null, contentValues) > 0
        db.close()
        return createSuccessful
    }


    fun updateToTime(id: Int, time: String):Boolean {
        val db:SQLiteDatabase = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put("TIME_TO", time)
        var result = db.update(TIME_TO_TABLE_NAME, contentValues, "_id =?", arrayOf<String>(id.toString()))
        if(result>0){
            return true
        }else{
            return false
        }
    }

    fun getToTime(): Cursor {
        val db:SQLiteDatabase = this.writableDatabase
        var res: Cursor = db.rawQuery("SELECT * FROM $TIME_TO_TABLE_NAME", null)
        return res
    }

    fun getAllData(): Cursor {
        val db:SQLiteDatabase = this.writableDatabase
        var res: Cursor = db.rawQuery("SELECT * FROM $APPS_TABLE_NAME", null)
        return res
    }

    fun getTime(name: String): Cursor{
        val db:SQLiteDatabase = this.writableDatabase
        val res: Cursor = db.rawQuery( "SELECT * FROM $name ", null)
        return res
    }

    fun getLock(): Cursor{
        val db:SQLiteDatabase = this.writableDatabase
        val res: Cursor = db.rawQuery( "SELECT * FROM $LOCK_TABLE_NAME", null)
        return res
    }

    fun  deleteData(){
        val db:SQLiteDatabase = this.writableDatabase
        var query = "DELETE FROM $APPS_TABLE_NAME"
        db.execSQL(query)
    }

    fun  deleteTime(name: String){
        val db:SQLiteDatabase = this.writableDatabase
        var query = "DELETE FROM $name"
        db.execSQL(query)
    }

    fun  deleteLock(name: String){
        val db:SQLiteDatabase = this.writableDatabase
        var query = "DELETE FROM $LOCK_TABLE_NAME WHERE $COL_3 = '$name'"
        db.execSQL(query)
    }

    fun checkTable(TABLE_NAME: String): Int{
        val db:SQLiteDatabase = this.writableDatabase
        val res: Cursor = db.rawQuery( "SELECT * FROM $TABLE_NAME", null)
        res.moveToFirst()
        var count = res.getInt(0)
        if(count > 0 ) {
            return 0
        }
        return 0
    }

}