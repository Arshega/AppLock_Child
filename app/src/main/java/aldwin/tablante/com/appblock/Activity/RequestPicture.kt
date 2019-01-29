package aldwin.tablante.com.appblock.Activity



import aldwin.tablante.com.appblock.R

import aldwin.tablante.com.appblock.Service.TrackerService

import android.content.Context

import android.content.Intent

import android.graphics.Bitmap

import android.graphics.drawable.AnimationDrawable

import android.net.Uri

import android.os.AsyncTask

import android.os.Bundle

import android.os.Environment

import android.support.v4.app.ActivityCompat

import android.support.v7.app.AppCompatActivity

import android.view.View

import android.view.animation.AnimationUtils

import android.widget.Toast

import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.firestore.EventListener

import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.firestore.FirebaseFirestoreException

import com.google.firebase.firestore.QuerySnapshot

import com.google.firebase.storage.FirebaseStorage

import com.google.firebase.storage.StorageReference

import com.wonderkiln.camerakit.*

import kotlinx.android.synthetic.main.activity_camera.*

import java.io.ByteArrayOutputStream

import java.io.File

import java.io.FileOutputStream

import java.text.SimpleDateFormat

import java.util.*



class RequestPicture : AppCompatActivity() {

    var aID = ""

    var sID = ""

    var anim: AnimationDrawable? = null



    var bmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_camera)

        val permissions = arrayOf(android.Manifest.permission.CAMERA

        )

        ActivityCompat.requestPermissions(this, permissions, 0)

        try {

            cameraView.facing = CameraKit.Constants.FACING_FRONT



            cameraView.start()



            logoSplash.setBackgroundResource(R.drawable.loading)





            var serial = intent.getStringExtra("serial")

            var db = FirebaseFirestore.getInstance()

            var accID = ""

            anim = logoSplash.background as AnimationDrawable

            anim!!.start()



            capture.setOnClickListener {

                cameraView.captureImage()



                //capture.visibility = View.GONE



                //loader().execute()

                //this.moveTaskToBack(true)





            }





            cameraView.addCameraKitListener(object : CameraKitEventListener {

                override fun onError(p0: CameraKitError?) {

                    null

                }



                override fun onEvent(p0: CameraKitEvent?) {



                    null

                }



                override fun onImage(p0: CameraKitImage?) {

                    val videoUri = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

                    val imageName = "ChildApp.png"

                    val f = File(videoUri, imageName)

                    f.createNewFile()

                    val bos = ByteArrayOutputStream()



                    bmap = p0!!.bitmap

                    p0!!.bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos)



                    val bitmapdata = bos.toByteArray()

                    Thread.sleep(2000)

                    val fos = FileOutputStream(f)



                    fos.write(bitmapdata)

                    fos.flush()

                    fos.close()



                    // aID = accID

                    //   sID = serial

                    //  loader().execute(videoUri)



                    uploadImage(accID, f, serial, applicationContext)





                }



                override fun onVideo(p0: CameraKitVideo?) {

                    null

                }

            })

            takepicture().execute()

        } catch (e: Exception) {

            e.printStackTrace()

            recursiveIntent()

        }

    }



    fun getCurrentTime(): Date {



        val calendar = Calendar.getInstance()

        val mdformat = SimpleDateFormat("HH:mm:ss")

        val strDate = calendar.time



        return strDate





    }



    fun uploadImage(accID: String, file: File, serial: String, context: Context) {





        storageFire(file, accID, serial)





    }





    fun storageFire(file: File, accID: String, serial: String) {

        var timer = serial + getCurrentTime().toGMTString().toString()

        var storage = FirebaseStorage.getInstance()

        var ref: StorageReference = storage.getReference("Images")

        ref.child(serial).child(timer).putFile(Uri.fromFile(file))

                .addOnSuccessListener {

                    ref.child(serial).child(timer).downloadUrl.addOnSuccessListener { v ->



                        //   var fbase = FirebaseDatabase.getInstance()

                        //  var refbase = fbase.getReference()

                        var map: HashMap<String, Any?> = HashMap()

                        map.put("Serial", serial)

                        map.put("image", timer.toString())

                        map.put("url", v.toString())

                        // refbase.child("Images").child(serial).child("ChildImage").setValue(map)



                        var refFire = FirebaseFirestore.getInstance()

                        var rbase = refFire.collection("Devices")

                                .document(serial)

                                .collection("Images")

                                .document("FrontCamera")

                                .set(map)







                        anim!!.stop()



                        cameraView.stop()

                        this@RequestPicture.finish()

                        System.exit(0);

                    }





                            .addOnFailureListener {

                                Toast.makeText(applicationContext, "fail", Toast.LENGTH_LONG).show()

                                anim!!.stop()

                                cameraView.stop()

                                val intent = Intent(applicationContext, MainActivity::class.java)

                                startActivity(intent)

                            }





                }



    }





    override fun onResume() {

        cameraView.start()

        super.onResume()

    }



    override fun onDestroy() {

        cameraView.stop()

        super.onDestroy()



    }





    inner class takepicture : AsyncTask<Void, Void, Void>() {

        override fun onPreExecute() {

            super.onPreExecute()

            splashScreen.visibility = View.VISIBLE

        }



        override fun onProgressUpdate(vararg values: Void?) {

            super.onProgressUpdate(*values)





        }



        override fun doInBackground(vararg p0: Void?): Void? {



            Thread.sleep(5000)



            onProgressUpdate()







            return null

        }



        override fun onPostExecute(result: Void?) {

            super.onPostExecute(result)

            if (cameraView.isStarted && cameraView.isFacingFront) {



                try {





                    capture.performClick()

                } catch (e:RuntimeException){

                    capture.performClick()

                    e.printStackTrace()

                }

            }



        }

    }





    inner class refresh : AsyncTask<Void, Void, Void>() {



        override fun doInBackground(vararg p0: Void?): Void? {

            Thread.sleep(20000)

            recursiveIntent()

            return null

        }

    }



    override fun onBackPressed() {

        super.onBackPressed()

        recursiveIntent()

    }



    fun recursiveIntent() {



        var intent2 = Intent(applicationContext, RequestPicture::class.java)

        intent2.putExtra("id", this.intent.getStringExtra("id"))

        intent2.putExtra("serial", this.intent.getStringExtra("serial"))

        startActivity(intent)



    }

}