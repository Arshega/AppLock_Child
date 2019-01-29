
package aldwin.tablante.com.appblock.BlockingApps

import aldwin.tablante.com.appblock.Activity.MainActivity
import aldwin.tablante.com.appblock.AppUsage
import aldwin.tablante.com.appblock.Service.TrackerService
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Build
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONObject

import android.R.attr.key
import android.app.usage.UsageStats

import android.preference.PreferenceManager
import android.content.SharedPreferences

import java.lang.reflect.Type
import android.support.v4.app.NotificationCompat.getExtras
import android.content.Intent.getIntent
import android.content.pm.PackageManager
import android.os.CountDownTimer
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_phone_lock.*
import android.content.Context.ACTIVITY_SERVICE
import android.content.pm.ResolveInfo
import android.content.Context.ACTIVITY_SERVICE
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.os.Handler
import org.json.JSONArray
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CheckRunningApplicationReceiver : BroadcastReceiver() {

    val TAG = "CRAR" // CheckRunningApplicationReceiver
    var id = ""
    val ONE_MINUTE_TABLE_NAME = "oneMin"
    val FIVE_MINUTE_TABLE_NAME = "fiveMin"
    val TEN_MINUTE_TABLE_NAME = "tenMin"
    val TWENTY_MINUTE_TABLE_NAME = "twentyMin"
    val APPS_TABLE_NAME = "apps"
    val APP_USAGE = "db_app_usage"
    var newList = arrayListOf<AppUsage>()
    var item = ""
    val THIRTY_MINUTE_TABLE_NAME = "thirtyMin"
    var serial = Build.SERIAL
    var context: CheckRunningApplicationReceiver = this
    var BlockList: ArrayList<String> = arrayListOf()

    override fun onReceive(aContext: Context, anIntent: Intent) {
        var appDBHelper = AppDBHelper(aContext)
        var firestore = FirebaseFirestore.getInstance()
        var noterefs = firestore.collection("Devices").document(serial)
        var newCur = ""
        val sdf = SimpleDateFormat("EEEE")
        val d = Date()
        var from: String = ""
        var to: String = ""
        val dayOfTheWeek = sdf.format(d)
        val today = Date()
        val format = SimpleDateFormat("HH:mm")
        var currentTime = format.format(today)
        val pm = aContext.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        var firestores = FirebaseFirestore.getInstance()
        try {
            val am = aContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val alltasks = am.runningAppProcesses
            var current = alltasks[0].processName
            val list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED)
            var mmap: HashMap<String, Any?> = HashMap()
            for (rInfo in list) {
                mmap.put(rInfo.activityInfo.applicationInfo.packageName.toString(), rInfo.activityInfo.applicationInfo.loadLabel(pm).toString())
            }
            var label = mmap.get(current)
            var res = appDBHelper.getLock()
            var stringBuilder = StringBuilder()
            if (res != null && res.count > 0) {
                while (res.moveToNext()) {
                    stringBuilder.append(res.getString(1))
                }
                var item = stringBuilder.toString()
                if (item == "one") {
                    var countDownTimer = object : CountDownTimer(30000, 1000) {
                        override fun onFinish() {
                            appDBHelper.deleteLock("one")
                            var mmap: HashMap<String, Any?> = HashMap()
                            mmap.put("LockScreen", "")
                            noterefs.set(mmap, SetOptions.merge()).addOnCompleteListener {
                                PhoneLock().finish()
                            }
                        }

                        override fun onTick(p0: Long) {
                            if(current!="aldwin.tablante.com.appblock.BlockingApps"){
                                var intent = Intent(aContext, PhoneLock::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                intent.putExtra("key", "on")
                                aContext.startActivity(intent)
                            }
                        }
                    }.start()
                } else if (item == "five") {
                    var countDownTimer = object : CountDownTimer(150000, 1000) {
                        override fun onFinish() {
                            appDBHelper.deleteLock("five")
                            var mmap: HashMap<String, Any?> = HashMap()
                            mmap.put("LockScreen", "")
                            noterefs.set(mmap, SetOptions.merge()).addOnCompleteListener {
                                PhoneLock().finish()
                            }
                        }

                        override fun onTick(p0: Long) {
                            if(current!="aldwin.tablante.com.appblock.BlockingApps"){
                                var intent = Intent(aContext, PhoneLock::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                intent.putExtra("key", "on")
                                aContext.startActivity(intent)
                            }
                        }
                    }.start()
                } else if (item == "ten") {
                    var countDownTimer = object : CountDownTimer(300000, 1000) {
                        override fun onFinish() {
                            appDBHelper.deleteLock("ten")
                            var mmap: HashMap<String, Any?> = HashMap()
                            mmap.put("LockScreen", "")
                            noterefs.set(mmap, SetOptions.merge()).addOnCompleteListener {
                                PhoneLock().finish()
                            }
                        }

                        override fun onTick(p0: Long) {
                            if(current!="aldwin.tablante.com.appblock.BlockingApps"){
                                var intent = Intent(aContext, PhoneLock::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                intent.putExtra("key", "on")
                                aContext.startActivity(intent)
                            }
                        }
                    }.start()
                } else if (item == "twenty") {
                    var countDownTimer = object : CountDownTimer(600000, 1000) {
                        override fun onFinish() {
                            appDBHelper.deleteLock("twenty")
                            var mmap: HashMap<String, Any?> = HashMap()
                            mmap.put("LockScreen", "")
                            noterefs.set(mmap, SetOptions.merge()).addOnCompleteListener {
                                PhoneLock().finish()
                            }
                        }

                        override fun onTick(p0: Long) {
                            if(current!="aldwin.tablante.com.appblock.BlockingApps"){
                                var intent = Intent(aContext, PhoneLock::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                intent.putExtra("key", "on")
                                aContext.startActivity(intent)
                            }
                        }
                    }.start()
                } else if (item == "thirty") {
                    var countDownTimer = object : CountDownTimer(900000, 1000) {
                        override fun onFinish() {
                            appDBHelper.deleteLock("thirty")
                            var mmap: HashMap<String, Any?> = HashMap()
                            mmap.put("LockScreen", "")
                            noterefs.set(mmap, SetOptions.merge()).addOnCompleteListener {
                                PhoneLock().finish()
                            }
                        }

                        override fun onTick(p0: Long) {
                            if(current!="aldwin.tablante.com.appblock.BlockingApps"){
                                var intent = Intent(aContext, PhoneLock::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                intent.putExtra("key", "on")
                                aContext.startActivity(intent)
                            }
                        }
                    }.start()
                }
            }
            var ressurect = appDBHelper.getAllData()
            var bobBuilder = StringBuilder()
            if (ressurect != null && ressurect.count > 0) {
                while (ressurect.moveToNext()) {
                    bobBuilder.append(ressurect.getString(1))
                }

                val jsonObj = JSONObject(bobBuilder.toString())
                var jsonArray = jsonObj.getJSONArray("Apps")

                if (jsonArray != null) {
                    for (i in 0 until jsonArray.length()) {
                        BlockList.add(jsonArray.getString(i))
                    }
                }
                for (i in 0 until BlockList.size) {
                    var app = BlockList[i]
                    if (app == label) {
                        var intent = Intent(aContext, PhoneLock::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.putExtra("key", "on")
                        aContext.startActivity(intent)
                    }
                }
            }

            var ressu = appDBHelper.getTime(ONE_MINUTE_TABLE_NAME)
            var bob = StringBuilder()
            if (ressu != null && ressu.count > 0) {
                while (ressu.moveToNext()) {
                    bob.append(ressu.getString(1))
                }
                val jsonObj = JSONObject(bob.toString())
                var jsonOne = jsonObj.getJSONArray("OneMinute")
                var oneList: ArrayList<String> = arrayListOf()
                if (jsonOne != null) {
                    for (i in 0 until jsonOne.length()) {
                        oneList.add(jsonOne.getString(i))
                    }
                }
                if (oneList.isNotEmpty()) {
                    var app = ""
                    for (e in 0 until oneList.size) {
                        app = oneList[e]
                        if (label == app) {
                            var countDownTimer = object : CountDownTimer(30000, 1000) {
                                override fun onFinish() {
                                    appDBHelper.deleteTime(ONE_MINUTE_TABLE_NAME)
                                    oneList.clear()
                                    var mmap: HashMap<String, Any?> = HashMap()
                                    mmap.put("OneMinute", FieldValue.delete())
                                    noterefs.set(mmap, SetOptions.merge()).addOnCompleteListener {
                                        PhoneLock().finish()
                                    }
                                }

                                override fun onTick(millisUntilFinished: Long) {
                                    var intent = Intent(aContext, PhoneLock::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    intent.putExtra("key", "on")
                                    aContext.startActivity(intent)
                                }
                            }.start()
                        }
                    }
                }
            }

            var ress1 = appDBHelper.getTime(FIVE_MINUTE_TABLE_NAME)
            var bob1 = StringBuilder()
            if (ress1 != null && ress1.count > 0) {
                while (ress1.moveToNext()) {
                    bob1.append(ress1.getString(1))
                }
                val json1 = JSONObject(bob1.toString())
                var jsonFive = json1.getJSONArray("FiveMinutes")
                var fiveList: ArrayList<String> = arrayListOf()
                if (jsonFive != null) {
                    for (i in 0 until jsonFive.length()) {
                        fiveList.add(jsonFive.getString(i))
                    }
                }
                if (fiveList.isNotEmpty()) {
                    var app = ""
                    for (e in 0 until fiveList.size) {
                        app = fiveList[e]
                        if (label == app) {
                            var countDownTimer = object : CountDownTimer(150000, 1000) {
                                override fun onFinish() {
                                    appDBHelper.deleteTime(FIVE_MINUTE_TABLE_NAME)
                                    fiveList.clear()
                                    var mmap: HashMap<String, Any?> = HashMap()
                                    mmap.put("FiveMinutes", FieldValue.delete())
                                    noterefs.set(mmap, SetOptions.merge()).addOnCompleteListener {
                                        PhoneLock().finish()
                                    }
                                }

                                override fun onTick(millisUntilFinished: Long) {
                                    var intent = Intent(aContext, PhoneLock::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    intent.putExtra("key", "on")
                                    aContext.startActivity(intent)
                                }
                            }.start()
                        }
                    }
                }
            }

            var ress2 = appDBHelper.getTime(TEN_MINUTE_TABLE_NAME)
            var bob2 = StringBuilder()
            if (ress2 != null && ress2.count > 0) {
                while (ress2.moveToNext()) {
                    bob2.append(ress2.getString(1))
                }
                val json2 = JSONObject(bob2.toString())
                var jsonTen = json2.getJSONArray("TenMinutes")
                var tenList: ArrayList<String> = arrayListOf()
                if (jsonTen != null) {
                    for (i in 0 until jsonTen.length()) {
                        tenList.add(jsonTen.getString(i))
                    }
                }
                if (tenList.isNotEmpty()) {
                    var app = ""
                    for (e in 0 until tenList.size) {
                        app = tenList[e]
                        if (label == app) {
                            var countDownTimer = object : CountDownTimer(300000, 1000) {
                                override fun onFinish() {
                                    appDBHelper.deleteTime(TEN_MINUTE_TABLE_NAME)
                                    tenList.clear()
                                    var mmap: HashMap<String, Any?> = HashMap()
                                    mmap.put("TenMinutes", FieldValue.delete())
                                    noterefs.set(mmap, SetOptions.merge()).addOnCompleteListener {
                                        PhoneLock().finish()
                                    }
                                }

                                override fun onTick(millisUntilFinished: Long) {
                                    var intent = Intent(aContext, PhoneLock::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    intent.putExtra("key", "on")
                                    aContext.startActivity(intent)
                                }
                            }.start()
                        }
                    }
                }
            }

            var ress3 = appDBHelper.getTime(TWENTY_MINUTE_TABLE_NAME)
            var bob3 = StringBuilder()
            if (ress3 != null && ress3.count > 0) {
                while (ress3.moveToNext()) {
                    bob3.append(ress3.getString(1))
                }
                val json3 = JSONObject(bob3.toString())
                var jsonTwenty = json3.getJSONArray("TwentyMinutes")
                var twentyList: ArrayList<String> = arrayListOf()
                if (jsonTwenty != null) {
                    for (i in 0 until jsonTwenty.length()) {
                        twentyList.add(jsonTwenty.getString(i))
                    }
                }
                if (twentyList.isNotEmpty()) {
                    var app = ""
                    for (e in 0 until twentyList.size) {
                        app = twentyList[e]
                        if (label == app) {
                            var countDownTimer = object : CountDownTimer(600000, 1000) {
                                override fun onFinish() {
                                    appDBHelper.deleteTime(TWENTY_MINUTE_TABLE_NAME)
                                    twentyList.clear()
                                    var mmap: HashMap<String, Any?> = HashMap()
                                    mmap.put("TwentyMinutes", FieldValue.delete())
                                    noterefs.set(mmap, SetOptions.merge()).addOnCompleteListener {
                                        PhoneLock().finish()
                                    }
                                }

                                override fun onTick(millisUntilFinished: Long) {
                                    var intent = Intent(aContext, PhoneLock::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    intent.putExtra("key", "on")
                                    aContext.startActivity(intent)
                                }
                            }.start()
                        }
                    }
                }
            }
            var ress4 = appDBHelper.getTime(THIRTY_MINUTE_TABLE_NAME)
            var bob4 = StringBuilder()
            if (ress4 != null && ress4.count > 0) {
                while (ress4.moveToNext()) {
                    bob4.append(ress4.getString(1))
                }
                val json4 = JSONObject(bob4.toString())
                var jsonThirty = json4.getJSONArray("ThirtyMinutes")
                var thirtyList: ArrayList<String> = arrayListOf()
                if (jsonThirty != null) {
                    for (i in 0 until jsonThirty.length()) {
                        thirtyList.add(jsonThirty.getString(i))
                    }
                }
                if (thirtyList.isNotEmpty()) {
                    var app = ""
                    for (e in 0 until thirtyList.size) {
                        app = thirtyList[e]
                        if (label == app) {
                            var countDownTimer = object : CountDownTimer(900000, 1000) {
                                override fun onFinish() {
                                    appDBHelper.deleteTime(THIRTY_MINUTE_TABLE_NAME)
                                    thirtyList.clear()
                                    var mmap: HashMap<String, Any?> = HashMap()
                                    mmap.put("ThirtyMinutes", FieldValue.delete())
                                    noterefs.set(mmap, SetOptions.merge()).addOnCompleteListener {
                                        PhoneLock().finish()
                                    }
                                }

                                override fun onTick(millisUntilFinished: Long) {
                                    var intent = Intent(aContext, PhoneLock::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    intent.putExtra("key", "on")
                                    aContext.startActivity(intent)
                                }
                            }.start()
                        }
                    }
                }
            }


            var fromRes = appDBHelper.getFromTime()
            var fromBuilder = StringBuilder()
            if (fromRes != null && fromRes.count > 0) {
                while (fromRes.moveToNext()) {
                    fromBuilder.append(fromRes.getString(1))
                }
                from = fromBuilder.toString()
            }

            var toRes = appDBHelper.getToTime()
            var toBuilder = StringBuilder()
            if (toRes != null && toRes.count > 0) {
                while (toRes.moveToNext()) {
                    toBuilder.append(toRes.getString(1))
                }
                to = toBuilder.toString()
            }

            var daysRes = appDBHelper.getAllDays()
            var daysBuilder = StringBuilder()
            var daysList: ArrayList<String> = arrayListOf()
            if (daysRes != null && daysRes.count > 0) {
                while (daysRes.moveToNext()) {
                    daysBuilder.append(daysRes.getString(1))
                }
                val jsonDays = JSONObject(daysBuilder.toString())
                var jsonDaysArray = jsonDays.getJSONArray("Days")
                if (jsonDaysArray != null) {
                    for (i in 0 until jsonDaysArray.length()) {
                        daysList.add(jsonDaysArray.getString(i))
                    }
                }
                var day = ""
                for (i in 0 until daysList.size) {
                    day = daysList[i]
                    if (day == dayOfTheWeek.toUpperCase()) {
                        var state = false
                        val inTime = SimpleDateFormat("HH:mm").parse(from)
                        val calendar1 = Calendar.getInstance()
                        calendar1.time = inTime

                        val checkTime = SimpleDateFormat("HH:mm").parse(currentTime)
                        val calendar3 = Calendar.getInstance()
                        calendar3.time = checkTime

                        val finTime = SimpleDateFormat("HH:mm").parse(to)
                        val calendar2 = Calendar.getInstance()
                        calendar2.time = finTime

                        if (to.compareTo(from) < 0) {
                            calendar2.add(Calendar.DATE, 1)
                            calendar3.add(Calendar.DATE, 1)
                        }
                        val actualTime = calendar3.time
                        if ((actualTime.after(calendar1.time) || actualTime.compareTo(calendar1.time) == 0) && actualTime.before(calendar2.time)) {
                            if(current!="aldwin.tablante.com.appblock"){
                                var intent = Intent(aContext, PhoneLock::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                intent.putExtra("key", "on")
                                aContext.startActivity(intent)
                            }
                        }
                    }
                }
            }//END OF SET SCHEDULE FOR LOCK SCREEN


            var resLock = appDBHelper.getLockDevice()
            var builderLock = StringBuilder()
            if (resLock != null && resLock.count > 0) {
                while (resLock.moveToNext()) {
                    builderLock.append(resLock.getString(1))
                }
                var lock = builderLock.toString()
                if (lock == "On"){
                    if(current!="aldwin.tablante.com.appblock"){
                        var intent = Intent(aContext, PhoneLock::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.putExtra("key", "on")
                        aContext.startActivity(intent)
                    }
                }
            }

            var cursorPair = appDBHelper.getPairingDevice()
            var builderPair = StringBuilder()
            if (cursorPair!=null && cursorPair.count > 0){
                while (cursorPair.moveToNext()){
                    builderPair.append(cursorPair.getString(1))
                }
                var chkPair = builderPair.toString()
                if (chkPair == "No"){
                    appDBHelper.close()
                    var no = firestore.collection("Devices").document(serial)
                    no.delete().addOnCompleteListener {
                        appDBHelper.close()
                        deleteCache(aContext)
                        aContext.deleteDatabase("Apps.db")
                        appDBHelper.clearDatabase(APPS_TABLE_NAME)
                        appDBHelper.close()
                        android.os.Process.killProcess(android.os.Process.myPid())
                    }
                }
            }


            //sendAppUsage(aContext)
            check(aContext)
            val packageName = "com.example.checkcurrentrunningapplication"

            if (current == "$packageName.Main") {
                Toast.makeText(aContext, "Current Example Screen.", Toast.LENGTH_LONG).show()
            }

            Log.i(TAG, "===============================")
            Log.i(TAG, "aTask.baseActivity: " + current)
            Log.i(TAG, "===============================")

        } catch (t: Throwable) {
            Log.i(TAG, "Throwable caught: " + t.message, t)
        }

    }

    fun check(context: Context){
        var ser = Build.SERIAL

    }

    fun deleteCache(context: Context) {
        try {
            val dir = context.cacheDir
            deleteDir(dir)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children!!.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            return dir.delete()
        } else return if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }

    internal inner class MyCountDownTimer(private var millisInFuture: Long, private val countDownInterval: Long) {
        fun Start() {
            val handler = Handler()
            Log.v("status", "starting")
            val counter = object : Runnable {

                override fun run() {
                    if (millisInFuture <= 0) {

                    } else {
                        val sec = millisInFuture / 2000
                        Log.v("status", java.lang.Long.toString(sec) + " seconds remain")
                        millisInFuture -= countDownInterval
                        handler.postDelayed(this, countDownInterval)
                    }
                }
            }
            handler.postDelayed(counter, countDownInterval)
        }
    }

}