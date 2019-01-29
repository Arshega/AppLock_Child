package aldwin.tablante.com.appblock.BlockingApps

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    val DATABASE_NAME = "Apps.db"
    val TABLE_NAME = "Apps_table"
    var context = context

    val COL_1 = "ID"
    val COL_2 = "APPNAME"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_NAME ( ID INTEGER PRIMARY KEY AUTOINCREMENT ,APPNAME TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }

    fun insertData(name:String):Boolean {
        var createSuccessful = false

        val db:SQLiteDatabase = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put(COL_2, name)
        createSuccessful = db.insert(TABLE_NAME, null, contentValues) > 0
        db.close()

        return createSuccessful
    }

    fun getAllData(): Cursor{
        val db:SQLiteDatabase = this.writableDatabase
        var res: Cursor = db.rawQuery("Select * from $TABLE_NAME", null)
        return res
    }

    fun deleteDuplicates() {
        writableDatabase.execSQL("delete from $TABLE_NAME where _id not in (SELECT MIN(_id) FROM $TABLE_NAME GROUP BY ID)")
    }

}