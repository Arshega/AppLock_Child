package aldwin.tablante.com.appblock.Activity

import aldwin.tablante.com.appblock.R
import aldwin.tablante.com.appblock.Service.TrackerService
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.storage.StorageMetadata
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat


class MainActivity : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ,android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.SYSTEM_ALERT_WINDOW,android.Manifest.permission.CAMERA,android.Manifest.permission.WAKE_LOCK
        ,android.Manifest.permission.BLUETOOTH,android.Manifest.permission.INTERNET,android.Manifest.permission.SYSTEM_ALERT_WINDOW,android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
        ,android.Manifest.permission.LOCATION_HARDWARE
        )
        ActivityCompat.requestPermissions(this, permissions,0)


        var intent = Intent(this@MainActivity, TrackerService::class.java)
                .setAction("enable_capture")


        startService(intent)


       //this.moveTaskToBack(true)
        initialize()
               finish()
    }

    private fun initialize() {
        baseContext.applicationContext.sendBroadcast(
                Intent("StartupReceiver_Manual_Start"))
    }

}
