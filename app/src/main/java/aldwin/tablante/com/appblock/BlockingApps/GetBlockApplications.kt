package aldwin.tablante.com.appblock.BlockingApps

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class GetBlockApplications : Service(){
    lateinit var db: DataBaseHelper
    val DATABASE_NAME = "Apps.db"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var blockList: ArrayList<String> = arrayListOf()

        db = DataBaseHelper(this, DATABASE_NAME, null, 1)

        var ser = Build.SERIAL
        var database = FirebaseFirestore.getInstance()
        database.collection("Devices")
                .whereEqualTo("Serial", ser)
                .addSnapshotListener { p0, p1 ->
                    if (p0!!.isEmpty) {
                        Toast.makeText(applicationContext, "Device is Restarting the Connector", Toast.LENGTH_SHORT).show()
                    } else {
                        for (doc in p0!!.documents) {
                            var app: BlockApplications = doc.toObject(BlockApplications::class.java)
                            var list = app.BlockApplications
                            for (i in 0 until list.size) {
                                blockList.add(list[i])
                                var item = list[i]
                                var result = db.insertData(item)
                                db.close()
                                if (result == true){
                                    Toast.makeText(this, "Data Inserted", Toast.LENGTH_LONG).show()
                                }else{
                                    Toast.makeText(this, "Insertion Failed", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                }
        return START_STICKY
        }
    }