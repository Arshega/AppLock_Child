package aldwin.tablante.com.appblock

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.tarek360.instacapture.Instacapture
import com.tarek360.instacapture.listener.SimpleScreenCapturingListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat

class ScreenshotActivity : AppCompatActivity (){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screenshot)


           instacap()   // INSTACAPTURE 2.0.0

    }


    fun instacap(){


        Instacapture.enableLogging(true);


        Instacapture.capture(this,object:SimpleScreenCapturingListener(){
            override fun onCaptureComplete(bitmap: Bitmap) {
                super.onCaptureComplete(bitmap)
                var serial = intent.getStringExtra("serial")

                val videoUri = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

                Toast.makeText(applicationContext, videoUri.toString(), Toast.LENGTH_LONG).show()

                val mdformat = SimpleDateFormat("HH:mm:ss")


                val imageName = "ChildApp.png"
                val f = File(videoUri, imageName)
                f.createNewFile()

                val bos = ByteArrayOutputStream()


                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 50, bos)

                val bitmapdata = bos.toByteArray()
                Thread.sleep(2000)
                val fos = FileOutputStream(f)

                fos.write(bitmapdata)
                fos.flush()
                fos.close()
            }

            override fun onCaptureFailed(e: Throwable) {
                super.onCaptureFailed(e)
            }

            override fun onCaptureStarted() {
                super.onCaptureStarted()
            }

        })
    }
}