package aldwin.tablante.com.appblock.Commands

import aldwin.tablante.com.appblock.Activity.RequestCamera
import aldwin.tablante.com.appblock.ScreenshotActivity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.net.Uri
import android.os.Environment
import android.view.View
import android.view.Window
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

class ScreenShot {


    fun doshot(serial: String, context: Context) {

        var intent = Intent(context.applicationContext, RequestCamera::class.java)
        intent.putExtra("serial", serial)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.applicationContext.startActivity(intent)
    }


}