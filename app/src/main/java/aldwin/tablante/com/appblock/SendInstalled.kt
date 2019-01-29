package aldwin.tablante.com.appblock

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.HashMap

class SendInstalled {
    fun sendingLabel(context: Context): ArrayList<String> {
        var serial = Build.SERIAL
        var firestore = FirebaseFirestore.getInstance()
        var noterefs = firestore.collection("Devices").document(serial)
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        var instalAppLabels = arrayListOf<String>()
        var instalApps = arrayListOf<String>()
        val list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED)
        for (rInfo in list) {
            instalAppLabels.add(rInfo.activityInfo.applicationInfo.loadLabel(pm).toString())
        }
        return instalAppLabels
    }
    fun sendingPackages(context: Context): ArrayList<String> {
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        var instalAppLabels = arrayListOf<String>()
        var instalApps = arrayListOf<String>()
        val list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED)
        for (rInfo in list) {
            instalApps.add(rInfo.activityInfo.applicationInfo.packageName.toString())
        }
        return instalApps
    }
}