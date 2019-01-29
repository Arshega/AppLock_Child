package aldwin.tablante.com.appblock.Model

import aldwin.tablante.com.appblock.Service.TrackerService
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import java.util.*

class Timer {

    fun setTimer(context: Context,hour:Int,min:Int){
        var myIntent= Intent(context.applicationContext, TrackerService::class.java)
        var alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var calendar: Calendar = Calendar.getInstance()
        var pi : PendingIntent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, min)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
           // calendar.set(Calendar.AM_PM,Calendar.AM)



        } else {


            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, min)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            // sec = System.currentTimeMillis().toInt()



        }

        pi = PendingIntent.getBroadcast(context.applicationContext, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarm.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)



    }



}