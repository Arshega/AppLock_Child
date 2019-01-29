package aldwin.tablante.com.appblock.Commands

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class TriggerAlarm {

     fun playAlarm(context: Context) {
        var r : Ringtone
        var alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        if(alarmUri == null){
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)


        }

        r = RingtoneManager.getRingtone(context.applicationContext,alarmUri)
        r.play()


         if(r.isPlaying){


           var v =  context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 v.vibrate(VibrationEffect.createOneShot(7000, VibrationEffect.DEFAULT_AMPLITUDE));
             } else {
                 //deprecated in API 26
                 v.vibrate(7000);
             }

         }

         Thread.sleep(8000)
         r.stop()
    }
}