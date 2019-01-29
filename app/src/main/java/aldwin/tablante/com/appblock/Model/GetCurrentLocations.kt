package aldwin.tablante.com.appblock.Model

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class GetCurrentLocations {


    fun requestLocationUpdates(context: Context, id: String) {
        val request = LocationRequest()
        request.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        val blue: BluetoothAdapter
        blue = BluetoothAdapter.getDefaultAdapter()
        val client = LocationServices.getFusedLocationProviderClient(context)
        val model = android.os.Build.MODEL
        val serial = android.os.Build.SERIAL


   // var device = childDevice(serial, bluetoothName)
        var bool = false



        var dataFstore = FirebaseFirestore.getInstance()
        var refStore = dataFstore.collection("Devices").document(serial)






        val permission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {

            request.setFastestInterval(5000)
                    .setInterval(10000)

            client.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    val location = locationResult!!.lastLocation

                    if (location != null) {
                        var mmap: HashMap<String, Any?> = HashMap()

                        mmap.put("longitude", location.longitude)
                        mmap.put("latitude", location.latitude)
                        mmap.put("altitude", location.altitude)
                        mmap.put("bearing", location.bearing)
                        mmap.put("time", location.time)
                        mmap.put("accuracy", location.accuracy)
                        mmap.put("provider", location.provider)
                        mmap.put("speed", location.speed)



                        refStore.collection("Locations").document("Current").set(mmap)


                        /* if(bool) {

                             dataref.child("longitude").setValue(location.longitude)

                         }

                         dataref.updateChildren(mmap)*/
                    }

                }
            }

                    , null)
        } else {
            val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.SYSTEM_ALERT_WINDOW, android.Manifest.permission.CAMERA, android.Manifest.permission.WAKE_LOCK
                    , android.Manifest.permission.BLUETOOTH, android.Manifest.permission.INTERNET, android.Manifest.permission.SYSTEM_ALERT_WINDOW, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
                    , android.Manifest.permission.LOCATION_HARDWARE
            )


        }

    }
}