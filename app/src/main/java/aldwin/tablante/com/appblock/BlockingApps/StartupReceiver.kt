package aldwin.tablante.com.appblock.BlockingApps

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log

class StartupReceiver : BroadcastReceiver() {

    internal val startupID = 1111111


    override fun onReceive(context: Context, intent: Intent) {

        val alarmManager = context
                .getSystemService(Context.ALARM_SERVICE) as AlarmManager
        try {
            val i7 = Intent(context, CheckRunningApplicationReceiver::class.java)
            val ServiceManagementIntent = PendingIntent.getBroadcast(context,
                    startupID, i7, 0)
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(),
                    5000, ServiceManagementIntent)


        } catch (e: Exception) {
            Log.i(TAG, "Exception : $e")
        }

    }

    companion object {

        internal val TAG = "SR"
    }

}
