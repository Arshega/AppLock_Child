package aldwin.tablante.com.appblock.Service

import aldwin.tablante.com.appblock.Activity.*
import aldwin.tablante.com.appblock.AppUsage
import aldwin.tablante.com.appblock.BlockingApps.AppDBHelper
import aldwin.tablante.com.appblock.BlockingApps.BlockApplications
import aldwin.tablante.com.appblock.Commands.*
import aldwin.tablante.com.appblock.BlockingApps.DataBaseHelper
import aldwin.tablante.com.appblock.BlockingApps.PhoneLock
import aldwin.tablante.com.appblock.Days
import aldwin.tablante.com.appblock.LockDevice
import aldwin.tablante.com.appblock.Model.*
import aldwin.tablante.com.appblock.SendInstalled
import android.app.ActivityManager
import android.app.Service
import android.content.*
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.os.*
import android.widget.Toast
import android.os.PowerManager
import org.json.JSONArray
import org.json.JSONObject
import android.app.Activity
import android.content.Intent
import android.util.Log
import java.lang.Compiler.disable
import java.lang.Compiler.enable
import android.content.ComponentName
import android.app.admin.DevicePolicyManager
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*
import android.bluetooth.BluetoothAdapter
import android.nfc.Tag
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashSet


class TrackerService : Service() {
    lateinit var db: DataBaseHelper
    val APPS_TABLE_NAME = "apps"
    val ONE_MINUTE_TABLE_NAME = "oneMin"
    val FIVE_MINUTE_TABLE_NAME = "fiveMin"
    val TEN_MINUTE_TABLE_NAME = "tenMin"
    val PAIRED_DEVICE = "pairing"
    val TWENTY_MINUTE_TABLE_NAME = "twentyMin"
    val THIRTY_MINUTE_TABLE_NAME = "thirtyMin"
    val LOCK_DEVICE = "lock_device"
    val LOCK_TABLE_NAME = "locks"
    val DAYS_TABLE_NAME = "time_days"
    val TIME_FROM_TABLE_NAME = "time_from"
    var newList = arrayListOf<String>()

    val TIME_TO_TABLE_NAME = "time_to"
    lateinit var activityManager: ActivityManager
    var deviceManger: DevicePolicyManager? = null
    var activityManagers: ActivityManager? = null
    var compName: ComponentName? = null
    val RESULT_ENABLE = 1
    lateinit var RAP: List<ActivityManager.RunningAppProcessInfo>
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag")
        wl.acquire()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            var appDBHelper = AppDBHelper(applicationContext)
            var device = android.os.Build.SERIAL
            var chkMmap: HashMap<String, Any?> = HashMap()
            var newList = arrayListOf<String>()
            var runningApps = arrayListOf<String>()
            GetCurrentLocations().requestLocationUpdates(applicationContext, device)
            var chkPair = ""


            var instalAppLabel = SendInstalled().sendingLabel(applicationContext)
            instalAppLabel = ArrayList(LinkedHashSet<String>(instalAppLabel))
            instalAppLabel.sort()

            val pm = applicationContext.packageManager
            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            var runApp = arrayListOf<String>()
            var runPackage = arrayListOf<String>()
            val list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED)

            var apps: ArrayList<String> = ArrayList()
            activityManager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            RAP = activityManager.runningAppProcesses
            for (processInfo in RAP) {
                apps.add(processInfo.processName)
            }
            var mmaps: HashMap<String, Any?> = HashMap()
            var pams: HashMap<String, Any?> = HashMap()
            for (rInfo in list) {
                mmaps.put(rInfo.activityInfo.applicationInfo.packageName.toString(), rInfo.activityInfo.applicationInfo.loadLabel(pm).toString())
            }

            for (i in 0 until apps.size) {
                var item = mmaps.get(apps[i])
                runApp.add(item.toString())
            }
            runApp.removeAll(Collections.singleton("null"))
            runApp.removeAll(Collections.singleton(""))


            var db = FirebaseFirestore.getInstance()
            var app: ArrayList<String> = ArrayList()
            var pairRequest: ArrayList<String> = ArrayList()
            var blockApp: ArrayList<String> = arrayListOf()

            var mmap: HashMap<String, Any?> = HashMap()

            var table: SQLiteDatabase = appDBHelper.writableDatabase
            val quers = "SELECT count(*) FROM $APPS_TABLE_NAME"
            val mcursor = table.rawQuery(quers, null)
            mcursor.moveToFirst()
            val icount = mcursor.getInt(0)
            if (icount == 0) {
            } else {
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
                            blockApp.add(jsonArray.getString(i))
                        }
                    }
                }
            }

            var cursorPair = appDBHelper.getPairingDevice()
            var builderPair = StringBuilder()
            if (cursorPair != null && cursorPair.count > 0) {
                while (cursorPair.moveToNext()) {
                    builderPair.append(cursorPair.getString(1))
                }
                chkPair = builderPair.toString()
            }
            val blue: BluetoothAdapter
            blue = BluetoothAdapter.getDefaultAdapter()
            val bluetoothName = blue.name
            db.collection("Devices").document(device).addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (documentSnapshot.exists()) {
                    null
                } else {

                    mmap.put("Serial", device)
                    mmap.put("BootDevice", false)
                    mmap.put("LockScreen", "")
                    mmap.put("Applications", runApp)
                    mmap.put("BlockApplications", blockApp)
                    mmap.put("Screenshot", false)
                    mmap.put("CaptureCam", false)
                    mmap.put("TriggerAlarm", false)
                    mmap.put("InstalledAppLabel", instalAppLabel)
                    mmap.put("Paired", chkPair)
                    mmap.put("OptionMessage", "Off")
                    mmap.put("Location", false)
                    mmap.put("Messages", "")
                    mmap.put("Request", pairRequest)
                    mmap.put("AppPermit", false)

                    db.collection("Devices")
                            .document(device)
                            .set(mmap)


                }
            }

            db.collection("Devices")
                    .whereEqualTo("Serial", device)
                    .addSnapshotListener(object : EventListener<QuerySnapshot> {
                        override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                            for (doc in p0!!.documents) {
                                var devicet = doc.toObject(ConsoleCommand::class.java)

                                if (devicet.CaptureCam) {
                                    db.collection("Devices").document(doc.id).update("CaptureCam", false)
                                    //CaptureCam().openFrontCamera(doc.id,device,applicationContext)
                                    var intent = Intent(applicationContext, RequestPicture::class.java)
                                    intent.putExtra("id", "val")
                                    intent.putExtra("serial", device)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                                    startActivity(intent)
                                }

                                if (devicet.LockScreen == "one") {
                                    var appDBHelperFrom = AppDBHelper(applicationContext)
                                    var tableFrom: SQLiteDatabase = appDBHelperFrom.writableDatabase
                                    val quersFrom = "SELECT count(*) FROM $LOCK_TABLE_NAME"
                                    val mcursorFrom = tableFrom.rawQuery(quersFrom, null)
                                    mcursorFrom.moveToFirst()
                                    val icountFrom = mcursorFrom.getInt(0)
                                    if (icountFrom == 0) {
                                        appDBHelper.insertLockData("one")
                                        break
                                    } else {
                                        appDBHelperFrom.updateLockTime(1, "one")
                                    }
                                }
                                if (devicet.LockScreen == "five") {
                                    var appDBHelperFrom = AppDBHelper(applicationContext)
                                    var tableFrom: SQLiteDatabase = appDBHelperFrom.writableDatabase
                                    val quersFrom = "SELECT count(*) FROM $LOCK_TABLE_NAME"
                                    val mcursorFrom = tableFrom.rawQuery(quersFrom, null)
                                    mcursorFrom.moveToFirst()
                                    val icountFrom = mcursorFrom.getInt(0)
                                    if (icountFrom == 0) {
                                        appDBHelper.insertLockData("five")
                                        break
                                    } else {
                                        appDBHelperFrom.updateLockTime(1, "five")
                                    }
                                }
                                if (devicet.LockScreen == "ten") {
                                    var appDBHelperFrom = AppDBHelper(applicationContext)
                                    var tableFrom: SQLiteDatabase = appDBHelperFrom.writableDatabase
                                    val quersFrom = "SELECT count(*) FROM $LOCK_TABLE_NAME"
                                    val mcursorFrom = tableFrom.rawQuery(quersFrom, null)
                                    mcursorFrom.moveToFirst()
                                    val icountFrom = mcursorFrom.getInt(0)
                                    if (icountFrom == 0) {
                                        appDBHelper.insertLockData("ten")
                                        break
                                    } else {
                                        appDBHelperFrom.updateLockTime(1, "ten")
                                    }
                                }
                                if (devicet.LockScreen == "twenty") {
                                    var appDBHelperFrom = AppDBHelper(applicationContext)
                                    var tableFrom: SQLiteDatabase = appDBHelperFrom.writableDatabase
                                    val quersFrom = "SELECT count(*) FROM $LOCK_TABLE_NAME"
                                    val mcursorFrom = tableFrom.rawQuery(quersFrom, null)
                                    mcursorFrom.moveToFirst()
                                    val icountFrom = mcursorFrom.getInt(0)
                                    if (icountFrom == 0) {
                                        appDBHelper.insertLockData("twenty")
                                        break
                                    } else {
                                        appDBHelperFrom.updateLockTime(1, "twenty")
                                    }
                                }
                                if (devicet.LockScreen == "thirty") {
                                    var appDBHelperFrom = AppDBHelper(applicationContext)
                                    var tableFrom: SQLiteDatabase = appDBHelperFrom.writableDatabase
                                    val quersFrom = "SELECT count(*) FROM $LOCK_TABLE_NAME"
                                    val mcursorFrom = tableFrom.rawQuery(quersFrom, null)
                                    mcursorFrom.moveToFirst()
                                    val icountFrom = mcursorFrom.getInt(0)
                                    if (icountFrom == 0) {
                                        appDBHelper.insertLockData("thirty")
                                        break
                                    } else {
                                        appDBHelperFrom.updateLockTime(1, "thirty")
                                    }
                                }

                                if (devicet.From != "") {
                                    var appDBHelperFrom = AppDBHelper(applicationContext)
                                    var tableFrom: SQLiteDatabase = appDBHelperFrom.writableDatabase
                                    val quersFrom = "SELECT count(*) FROM $TIME_FROM_TABLE_NAME"
                                    val mcursorFrom = tableFrom.rawQuery(quersFrom, null)
                                    mcursorFrom.moveToFirst()
                                    val icountFrom = mcursorFrom.getInt(0)
                                    if (icountFrom == 0) {
                                        appDBHelperFrom.insertFromTime(devicet.From)
                                        break
                                    } else {
                                        appDBHelperFrom.updateFromTime(1, devicet.From)
                                    }
                                }

                                if (devicet.To != "") {
                                    var appDBHelperTo = AppDBHelper(applicationContext)
                                    var tableTo: SQLiteDatabase = appDBHelperTo.writableDatabase
                                    var quersTo = "SELECT count(*) FROM $TIME_TO_TABLE_NAME"
                                    val mcursorFrom = tableTo.rawQuery(quersTo, null)
                                    mcursorFrom.moveToFirst()
                                    val icountFrom = mcursorFrom.getInt(0)
                                    if (icountFrom == 0) {
                                        appDBHelperTo.insertToTime(devicet.To)
                                        break
                                    } else {
                                        appDBHelperTo.updateToTime(1, devicet.To)
                                    }

                                }

                                if (devicet.Screenshot) {
                                    db.collection("Devices").document(doc.id).update("Screenshot", false)
                                    ScreenShot().doshot(doc.id, applicationContext)
                                }

                                if (devicet.TriggerAlarm) {
                                    TriggerAlarm().playAlarm(applicationContext)
                                    db.collection("Devices").document(doc.id).update("TriggerAlarm", false)
                                }
                                if (!devicet.Messages.equals("")) {
                                    var intent = Intent(applicationContext, MessageReciever::class.java)
                                    intent.putExtra("id", doc.id)
                                    intent.putExtra("msg", devicet.Messages)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                }

                                if (devicet.Location) {
                                    db.collection("Devices").document(doc.id).update("Location", false)
                                    var intent = Intent(applicationContext, RefreshLocation::class.java)
                                    intent.putExtra("id", doc.id)
                                    intent.putExtra("msg", devicet.Messages)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                }


                            }
                        }
                    })

            var ser = Build.SERIAL
            var database = FirebaseFirestore.getInstance()
            database.collection("Devices")
                    .whereEqualTo("Serial", ser)
                    .addSnapshotListener { p0, p1 ->
                        if (p0!!.isEmpty) {
                            Toast.makeText(applicationContext, "Device is Restarting the Connector", Toast.LENGTH_SHORT).show()
                        } else {
                            for (doc in p0.documents) {
                                var result: Boolean = false
                                var app: BlockApplications = doc.toObject(BlockApplications::class.java)
                                var list = app.BlockApplications
                                if (list.isNotEmpty()) {
                                    var appDBHelper = AppDBHelper(applicationContext)
                                    var table: SQLiteDatabase = appDBHelper.writableDatabase
                                    val quers = "SELECT count(*) FROM $APPS_TABLE_NAME"
                                    val mcursor = table.rawQuery(quers, null)
                                    mcursor.moveToFirst()
                                    val icount = mcursor.getInt(0)
                                    if (icount == 0) {
                                        val json = JSONObject()
                                        json.put("Apps", JSONArray(list))
                                        val arrayList = json.toString()
                                        appDBHelper.insertAppsData(arrayList)
                                        break
                                    } else {
                                        val json = JSONObject()
                                        json.put("Apps", JSONArray(list))
                                        appDBHelper.updateAppsData(1, json)
                                    }
                                }
                            }
                        }
                    }

            var rmap: HashMap<String, Any?> = HashMap()
            rmap.put("ID", "")
            rmap.put("Name", "")

            // Request Paired
            db.collection("Requests")
                    .whereEqualTo("ID", device)
                    .addSnapshotListener(object : EventListener<QuerySnapshot> {
                        override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                            for (doc in p0!!.documents) {
                                var dev = doc.toObject(Requests::class.java)
                                var intent = Intent(this@TrackerService, RequestReciever::class.java)
                                intent.putExtra("serial", device)
                                intent.putExtra("name", dev.Name)
                                intent.putExtra("requestid", dev.RequestID)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }
                        }
                    })
            saveDays()
            saveMinutes()
            checkPairing()
            checkLockDevice()
            updateRunningApps()
            sendAppUsage()
            updateInstalledAppLabel()
        } catch (t: Throwable) {
            Log.i("Tag", "Throwable caught: " + t.message, t)
        }
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        var intent = Intent("com.android.ServiceStopped")
        sendBroadcast(intent)
        var intent3 = Intent(this@TrackerService, MainActivity::class.java)

        startActivity(intent3)

        super.onTaskRemoved(rootIntent)
    }


    override fun onDestroy() {
        super.onDestroy()
        var intent2 = Intent(this@TrackerService, MainActivity::class.java)
                .setAction("enable_capture")
        try {
            try {
                startActivity(intent2)
            } catch (e: RuntimeException) {

                e.printStackTrace()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()


        }
    }

    fun saveDays() {
        var ser = Build.SERIAL
        var database = FirebaseFirestore.getInstance()
        database.collection("Devices")
                .whereEqualTo("Serial", ser)
                .addSnapshotListener { p0, p1 ->
                    if (p0!!.isEmpty) {
                        Toast.makeText(applicationContext, "Device is Restarting the Connector", Toast.LENGTH_SHORT).show()
                    } else {
                        for (doc in p0.documents) {
                            var result: Boolean = false
                            var app: Days = doc.toObject(Days::class.java)
                            var list = app.Days
                            if (list.isNotEmpty()) {
                                var appDBHelper = AppDBHelper(applicationContext)
                                var table: SQLiteDatabase = appDBHelper.writableDatabase
                                val quers = "SELECT count(*) FROM $DAYS_TABLE_NAME"
                                val mcursor = table.rawQuery(quers, null)
                                mcursor.moveToFirst()
                                val icount = mcursor.getInt(0)
                                if (icount == 0) {
                                    val json = JSONObject()
                                    json.put("Days", JSONArray(list))
                                    val arrayList = json.toString()
                                    appDBHelper.insertDaysData(arrayList)
                                    break
                                } else {
                                    val json = JSONObject()
                                    json.put("Days", JSONArray(list))
                                    appDBHelper.updateDaysData(1, json)
                                }
                            }
                        }
                    }
                }
    }

    fun updateInstalledAppLabel() {
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                var mmap: HashMap<String, Any?> = HashMap()
                var chkMmap: HashMap<String, Any?> = HashMap()
                var serial = Build.SERIAL
                var newList = arrayListOf<String>()
                var firestore = FirebaseFirestore.getInstance()
                var noterefs = firestore.collection("Devices").document(serial)
                val pm = applicationContext.packageManager
                val intent = Intent(Intent.ACTION_MAIN, null)
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                var instalAppLabels = arrayListOf<String>()
                val list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED)
                for (rInfo in list) {
                    instalAppLabels.add(rInfo.activityInfo.applicationInfo.loadLabel(pm).toString())

                }


                instalAppLabels = ArrayList(LinkedHashSet<String>(instalAppLabels))
                instalAppLabels.sort()

                mmap.put("InstalledAppLabel", instalAppLabels)
                noterefs.update(mmap).addOnCompleteListener {
                    handler.postDelayed(this, 10000)
                    mmap.clear()
                    instalAppLabels.clear()
                    list.clear()
                }
            }
        }, 20000)
    }

    fun updateRunningApps() {
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                var mmap: HashMap<String, Any?> = HashMap()
                var serial = Build.SERIAL
                var firestore = FirebaseFirestore.getInstance()
                var noterefs = firestore.collection("Devices").document(serial)
                val pm = applicationContext.packageManager
                val intent = Intent(Intent.ACTION_MAIN, null)
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                var runApp = arrayListOf<String>()
                var runPackage = arrayListOf<String>()
                val list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED)
                for (rInfo in list) {
                }
                var apps: ArrayList<String> = ArrayList()
                activityManager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                RAP = activityManager.runningAppProcesses
                for (processInfo in RAP) {
                    apps.add(processInfo.processName)
                }
                var mmaps: HashMap<String, Any?> = HashMap()
                var pams: HashMap<String, Any?> = HashMap()
                for (rInfo in list) {
                    mmaps.put(rInfo.activityInfo.applicationInfo.packageName.toString(), rInfo.activityInfo.applicationInfo.loadLabel(pm).toString())
                }

                for (i in 0 until apps.size) {
                    var item = mmaps.get(apps[i])
                    runApp.add(item.toString())
                }
                runApp.removeAll(Collections.singleton("null"))
                runApp.removeAll(Collections.singleton(""))

                mmap.put("Applications", runApp)
                noterefs.update(mmap).addOnCompleteListener {
                    handler.postDelayed(this, 10000)
                    mmap.clear()
                    runApp.clear()
                    mmaps.clear()
                    list.clear()
                }

            }
        }, 1000)
    }

//    fun check(){
//        var ser = Build.SERIAL
//        var data = FirebaseFirestore.getInstance()
//        data.collection("Devices").document(ser).collection("Parents").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
//            for (doc in querySnapshot.documents){
//                if (!doc.exists()){
//                    Toast.makeText(applicationContext, "HI", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }


    fun checkPairing() {
        var ser = Build.SERIAL
        var database = FirebaseFirestore.getInstance()
        database.collection("Devices")
                .whereEqualTo("Serial", ser)
                .addSnapshotListener { p0, p1 ->
                        for (doc in p0.documents) {
                            var pairs: Paired = doc.toObject(Paired::class.java)
                            var chkPairs = pairs.Paired
                            if (chkPairs == "Yes") {
                                var appDBHelper = AppDBHelper(applicationContext)
                                var table: SQLiteDatabase = appDBHelper.writableDatabase
                                val quers = "SELECT count(*) FROM $PAIRED_DEVICE"
                                val mcursor = table.rawQuery(quers, null)
                                mcursor.moveToFirst()
                                val icount = mcursor.getInt(0)
                                if (icount == 0) {
                                    appDBHelper.insertPairingDevice("Yes")
                                    break
                                } else {
                                    appDBHelper.updatePairingDevice(1, "Yes")
                                }
                            } else if (chkPairs == "No") {
                                var data = FirebaseFirestore.getInstance()
                                data.collection("Devices").document(ser).collection("Parents").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                                    if (querySnapshot.isEmpty == true){
                                        var appDBHelper = AppDBHelper(applicationContext)
                                        var table: SQLiteDatabase = appDBHelper.writableDatabase
                                        val quers = "SELECT count(*) FROM $PAIRED_DEVICE"
                                        val mcursor = table.rawQuery(quers, null)
                                        mcursor.moveToFirst()
                                        val icount = mcursor.getInt(0)
                                        if (icount == 0) {
                                            appDBHelper.insertPairingDevice("No")
                                        } else {
                                            appDBHelper.updatePairingDevice(1, "No")
                                        }
                                    }
                                }
                            }
                        }
                }
    }

//    fun logRunningApps(){
//        var installedMap: HashMap<String, Any?> = HashMap()
//        var runningMap: HashMap<String, Any?> = HashMap()
//        val pm = applicationContext.packageManager
//        val intent = Intent(Intent.ACTION_MAIN, null)
//        intent.addCategory(Intent.CATEGORY_LAUNCHER)
//        var runApp = arrayListOf<String>()
//        var runPackage = arrayListOf<String>()
//        val list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED)
//        val activityManagers = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        val services = activityManagers.getRunningServices(Integer.MAX_VALUE)
//        val currentMillis = Calendar.getInstance().timeInMillis
//        val cal = Calendar.getInstance()
//        for (rInfo in list) {
//            installedMap.put(rInfo.activityInfo.applicationInfo.packageName.toString(),rInfo.activityInfo.applicationInfo.loadLabel(pm).toString())
//        }
//        for (info in services) {
//            cal.timeInMillis = currentMillis - info.activeSince
//            runningMap.put(String.format("%s",info.process),String.format("%d ms", info.activeSince))
//        }
//
//        for (entry in runningMap.entries){
//            var run = runningMap.get(entry.key)
//            Toast.makeText(applicationContext, "${entry.key}", Toast.LENGTH_SHORT).show()
//        }
//
//    }

    fun checkLockDevice() {
        var ser = Build.SERIAL
        var database = FirebaseFirestore.getInstance()
        val am = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val alltasks = am.runningAppProcesses
        var current = alltasks[0].processName
        database.collection("Devices")
                .whereEqualTo("Serial", ser)
                .addSnapshotListener { p0, p1 ->
                    if (p0!!.isEmpty) {
                        Toast.makeText(applicationContext, "Device is Restarting the Connector", Toast.LENGTH_SHORT).show()
                    } else {
                        for (doc in p0.documents) {
                            var result: Boolean = false
                            var pairs: LockDevice = doc.toObject(LockDevice::class.java)
                            var chkPairs = pairs.LockDevice
                            if (chkPairs == "On") {
                                var appDBHelper = AppDBHelper(applicationContext)
                                var table: SQLiteDatabase = appDBHelper.writableDatabase
                                val quers = "SELECT count(*) FROM $LOCK_DEVICE"
                                val mcursor = table.rawQuery(quers, null)
                                mcursor.moveToFirst()
                                val icount = mcursor.getInt(0)
                                if (icount == 0) {
                                    appDBHelper.insertLockDevice("On")
                                    break
                                } else {
                                    appDBHelper.updateLockDevice(1, "On")
                                }
                            } else if (chkPairs == "Off") {
                                var appDBHelper = AppDBHelper(applicationContext)
                                var table: SQLiteDatabase = appDBHelper.writableDatabase
                                val quers = "SELECT count(*) FROM $LOCK_DEVICE"
                                val mcursor = table.rawQuery(quers, null)
                                mcursor.moveToFirst()
                                val icount = mcursor.getInt(0)
                                if (icount == 0) {
                                    appDBHelper.insertLockDevice("Off")
                                    break
                                } else {
                                    appDBHelper.updateLockDevice(1, "Off")
                                }
                            }
                        }
                    }
                }
    }

    fun sendAppUsage() {
        var newCur = ""
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                val pm = applicationContext.packageManager
                val intent = Intent(Intent.ACTION_MAIN, null)
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                var serial = Build.SERIAL
                var firestores = FirebaseFirestore.getInstance()
                var noteref = firestores.collection("Devices").document(serial)
                var maps: HashMap<String, Any?> = HashMap()
                val am = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val alltasks = am.runningAppProcesses
                var current = alltasks[0].processName
                val list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED)
                var mmap: HashMap<String, Any?> = HashMap()
                for (rInfo in list) {
                    mmap.put(rInfo.activityInfo.applicationInfo.packageName.toString(), rInfo.activityInfo.applicationInfo.loadLabel(pm).toString())
                }
                var label = mmap.get(current)
                val today = Date()
                val format = SimpleDateFormat("HH:mm")
                var currentTime = format.format(today)
                var newString = label.toString()
                if (label != newCur && newString != "null" && newString != "") {
                    newCur = label.toString()
                    var newString = "The Application $newCur was accessed at $currentTime"
                    newList.add(newString)
                    newList = ArrayList(LinkedHashSet<String>(newList))
                    maps.put("AppUsage", newList)
                    noteref.set(maps, SetOptions.merge())
                }

                handler.postDelayed(this, 10000)
            }
        }, 20000)
    }

    fun saveMinutes() {
        var messaging = arrayListOf<String>("Messaging")
        var appDBHelper = AppDBHelper(applicationContext)
        var ser = Build.SERIAL
        var database = FirebaseFirestore.getInstance()
        database.collection("Devices")
                .whereEqualTo("Serial", ser)
                .addSnapshotListener { p0, p1 ->
                    if (p0!!.isEmpty) {
                        Toast.makeText(applicationContext, "Device is Restarting the Connector", Toast.LENGTH_SHORT).show()
                    } else {
                        for (doc in p0.documents) {
                            var one: OneMinute = doc.toObject(OneMinute::class.java)
                            var oneList = one.OneMinute
                            var five: FiveMinutes = doc.toObject(FiveMinutes::class.java)
                            var fiveList = five.FiveMinutes
                            var ten: TenMinutes = doc.toObject(TenMinutes::class.java)
                            var tenList = ten.TenMinutes
                            var twenty: TwentyMinutes = doc.toObject(TwentyMinutes::class.java)
                            var twentyList = twenty.TwentyMinutes
                            var thirtyMinutes: ThirtyMinutes = doc.toObject(ThirtyMinutes::class.java)
                            var thirtyList = thirtyMinutes.ThirtyMinutes

                            if (oneList.isNotEmpty()) {
                                val json = JSONObject()
                                json.put("OneMinute", JSONArray(oneList))
                                val arrayList = json.toString()
                                appDBHelper.insertOneMin(arrayList)
                                break
                            } else if (fiveList.isNotEmpty()) {
                                val json = JSONObject()
                                json.put("FiveMinutes", JSONArray(fiveList))
                                val arrayList = json.toString()
                                appDBHelper.insertFiveMin(arrayList)
                                break
                            } else if (tenList.isNotEmpty()) {
                                val json = JSONObject()
                                json.put("TenMinute", JSONArray(tenList))
                                val arrayList = json.toString()
                                appDBHelper.insertTenMin(arrayList)
                                break
                            } else if (twentyList.isNotEmpty()) {
                                val json = JSONObject()
                                json.put("TwentyMinutes", JSONArray(twentyList))
                                val arrayList = json.toString()
                                appDBHelper.insertTwentyMin(arrayList)
                                break
                            } else if (thirtyList.isNotEmpty()) {
                                val json = JSONObject()
                                json.put("ThirtyMinutes", JSONArray(thirtyList))
                                val arrayList = json.toString()
                                appDBHelper.insertThirtyMin(arrayList)
                                break
                            }

                        }
                    }
                }

    }


}


