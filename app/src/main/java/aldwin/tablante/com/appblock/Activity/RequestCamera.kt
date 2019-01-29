package aldwin.tablante.com.appblock.Activity

import aldwin.tablante.com.appblock.R

import android.app.Activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

import android.util.DisplayMetrics
import android.util.SparseIntArray
import android.view.Surface
import android.view.View
import android.widget.Toast
import android.widget.ToggleButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import kotlinx.android.synthetic.main.activity_screenshot.*
import java.io.File
import java.io.IOException

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class RequestCamera : Activity() {
private var serial = ""

    private var dateform = ""
   private var  bool = false
    private var screenDensity = 0
    private var projectionManager: MediaProjectionManager? = null
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var mediaProjectionCallBack: MediaProjectionCallback? = null
    private var mediaRecorder: MediaRecorder? = null


    var videoURI = ""

    companion object {
        private val REQUEST_CODE = 1000
        private val REQUEST_PERMISSION = 1001
        private var DISPLAY_WIDTH = 700
        private var DISPLAY_HEIGHT = 1280
        private val ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)


        }


    }
    @RequiresApi()
    inner class MediaProjectionCallback : MediaProjection.Callback() {

        override fun onStop() {

            mediaProjection = null
            stopScreenRecord()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screenshot)
       serial = intent.getStringExtra("serial")
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        screenDensity = metrics.densityDpi

        mediaRecorder = MediaRecorder()
         projectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

        DISPLAY_HEIGHT = metrics.heightPixels
        DISPLAY_WIDTH = metrics.widthPixels

     //   TB.setOnClickListener { v ->

            if (ContextCompat.checkSelfPermission(this@RequestCamera,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    + ContextCompat.checkSelfPermission(this@RequestCamera
                            , android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this@RequestCamera,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this@RequestCamera,
                                android.Manifest.permission.RECORD_AUDIO)
                ) {


                    Snackbar.make(rootLayout, "Permissions", Snackbar.LENGTH_INDEFINITE)
                            .setAction("ENABLE", {

                                ActivityCompat.requestPermissions(this@RequestCamera, arrayOf(
                                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        android.Manifest.permission.RECORD_AUDIO), REQUEST_PERMISSION)
                            }).show()
                } else {
                    ActivityCompat.requestPermissions(this@RequestCamera, arrayOf(
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.RECORD_AUDIO), REQUEST_PERMISSION)
                }

            } else {

                startRecording()
            }
        //}


    }

    private fun startRecording() {

       /* if ((v as ToggleButton).isChecked) {
            initRecorder()
            shareScreen()

        }*/

        if (true) {
            initRecorder()
            shareScreen()

        }
        else {
            mediaRecorder!!.stop()
            mediaRecorder!!.reset()
            stopScreenRecord()

        }
    }

    private fun initRecorder() {


        try {
            dateform = SimpleDateFormat("dd-MM-yyyy-hh_mm_ss").format(Date())
            mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.SURFACE)
            mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            videoURI = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + StringBuilder("/")
                    .append("Appblock")
                    .append(dateform)
                    .append(".mp4")
                    .toString()

            mediaRecorder!!.setOutputFile(videoURI)
            mediaRecorder!!.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT)
            mediaRecorder!!.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mediaRecorder!!.setVideoEncodingBitRate(512 * 1000)
            mediaRecorder!!.setVideoFrameRate(30)

            val rotation = windowManager.defaultDisplay.rotation
            val orientation = ORIENTATIONS.get(rotation + 90)
            mediaRecorder!!.setOrientationHint(orientation)
            mediaRecorder!!.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun shareScreen() {
        if (mediaProjection == null) {

            startActivityForResult(projectionManager!!.createScreenCaptureIntent(), REQUEST_CODE)
            return
        }
        virtualDisplay = createVirtualDisplay()
        mediaRecorder!!.stop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != REQUEST_CODE) {
            return
        }

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "Screen cast permission denied", Toast.LENGTH_LONG).show()
            return
        }
        mediaProjectionCallBack = MediaProjectionCallback()
        mediaProjection = projectionManager!!.getMediaProjection(resultCode, data)
        mediaProjection!!.registerCallback(mediaProjectionCallBack, null)
        virtualDisplay = createVirtualDisplay()
        mediaRecorder!!.start()

        asy().execute()
        this.moveTaskToBack(true)


    }

    private fun createVirtualDisplay(): VirtualDisplay? {
        return mediaProjection!!.createVirtualDisplay("MainActivity", DISPLAY_WIDTH, DISPLAY_HEIGHT, screenDensity
                , DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder!!.surface, null, null)

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {

            REQUEST_PERMISSION -> {

                if (grantResults.size > 0 && grantResults[0] + grantResults[1]
                        == PackageManager.PERMISSION_GRANTED) {
                    startRecording()
                } else {

                    Snackbar.make(rootLayout, "Permissions", Snackbar.LENGTH_INDEFINITE)
                            .setAction("ENABLE", {

                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_SETTINGS
                                intent.addCategory(Intent.CATEGORY_DEFAULT)
                                intent.data = Uri.parse("package:$packageName")
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                                startActivity(intent)
                            }).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    private fun stopScreenRecord() {


        if (virtualDisplay == null) {
            return

        }
        var fbase = FirebaseStorage.getInstance()
        var rbase = fbase.getReference("Videos")
        var dbase = FirebaseFirestore.getInstance()
        var rdbase = dbase.collection("Devices").document(serial).collection("Videos")
        var file = Uri.fromFile(File(videoURI))
        //Toast.makeText(applicationContext, videoURI, Toast.LENGTH_LONG).show()
        rbase.child(serial).child(serial+dateform).putFile(file)
                .addOnSuccessListener { bool = true

                    virtualDisplay!!.release()
                    destroyMediaProjection()
                    var hash : HashMap<String,Any?> = HashMap()
                    hash.put("serial",serial)
                    hash.put("url",videoURI)
                    hash.put("file",serial+dateform)
                    rdbase.document("screenshot").set(hash)

                }


    }


    private fun destroyMediaProjection() {
        if (mediaProjection != null) {


            mediaProjection!!.unregisterCallback(mediaProjectionCallBack)
            mediaProjection!!.stop()
            mediaProjection = null
            finish()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyMediaProjection()
    }

    inner class asy : AsyncTask<Void, Void, Void>() {

        override fun onPreExecute() {
            super.onPreExecute()

        }
        override fun doInBackground(vararg p0: Void?): Void? {
            Thread.sleep(2000)
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)


            mediaRecorder!!.stop()
            mediaRecorder!!.reset()
            stopScreenRecord()
        }
    }

    override fun onPause() {
        super.onPause()

    }

}