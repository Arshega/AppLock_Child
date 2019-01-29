package aldwin.tablante.com.appblock.Model

import aldwin.tablante.com.appblock.Service.RingtoneService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmBroadRcv:BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {

        var getResult:String = p1!!.getStringExtra("extra")
        var service: Intent = Intent(p0, RingtoneService::class.java)
        service.putExtra("extra",getResult)
        p0!!.startService(service)
    }
}