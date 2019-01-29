package aldwin.tablante.com.appblock.Activity

import aldwin.tablante.com.appblock.R
import aldwin.tablante.com.appblock.Service.TrackerService
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_request.*

class RequestReciever : Activity() {
    var parentList = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)


        var serial = intent.getStringExtra("serial")
        var requestid = intent.getStringExtra("requestid")
        var name = intent.getStringExtra("name")
        textView4.setText(name.toString())
        var firestore = FirebaseFirestore.getInstance()
        var noterefs = firestore.collection("Devices").document(serial)
        var mmap: HashMap<String, Any?> = HashMap()
        mmap.put("Paired", "Yes")
        accept.setOnClickListener {
            noterefs.set(mmap, SetOptions.merge())
            var db = FirebaseFirestore.getInstance()
            db.collection("Requests")
                    .document(requestid + "+" + serial)
                    .delete()
            addDevice(requestid,serial,name)

        }
        decline.setOnClickListener {
            var db = FirebaseFirestore.getInstance()
            db.collection("Requests")
                    .document(requestid + "+" + serial)
                    .delete()
            finish()
        }

    }

    fun addDevice(accID: String, serial: String, name: String) {
        val blue = BluetoothAdapter.getDefaultAdapter()
        val bluetoothName = blue.name
        var data = FirebaseFirestore.getInstance()
        var refd = data.collection("Devices").document(serial).collection("Parents")

        var firestore = FirebaseFirestore.getInstance()
        var noterefs = firestore.collection("Accounts").document(accID)


        var mmap: HashMap<String, Any?> = HashMap()
        mmap.put("ParentID",accID)
        refd.document(accID).set(mmap)
        mmap.clear()
        mmap.put("id", serial)
        mmap.put("name", bluetoothName)




        noterefs.collection("Devices").document(serial).set(mmap).addOnSuccessListener {

            android.os.Process.killProcess(android.os.Process.myPid())

            this.moveTaskToBack(true)
            root.visibility = View.GONE
            var ser = Intent(applicationContext, TrackerService::class.java)
            startService(ser)
        }



    }
}