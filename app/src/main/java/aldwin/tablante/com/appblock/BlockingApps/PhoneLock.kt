package aldwin.tablante.com.appblock.BlockingApps

import aldwin.tablante.com.appblock.Activity.MainActivity
import aldwin.tablante.com.appblock.Model.ConsoleCommand
import aldwin.tablante.com.appblock.Model.OptionMessage
import aldwin.tablante.com.appblock.R
import android.content.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_phone_lock.*
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import com.google.firebase.firestore.*
import java.util.HashMap


class PhoneLock : AppCompatActivity() {
    var name = ""
    var serial = Build.SERIAL
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_lock)




        var db = FirebaseFirestore.getInstance()
        db.collection("Devices")
                .whereEqualTo("Serial", serial)
                .addSnapshotListener(object : EventListener<QuerySnapshot> {
                    override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                        for (doc in p0!!.documents) {
                            var option: OptionMessage = doc.toObject(OptionMessage::class.java)
                            var options = option.OptionMessage
                            if (options == "On"){
                                imageView3.visibility = View.VISIBLE
                                textView9.visibility = View.VISIBLE
                                button3.visibility = View.VISIBLE
                                editText2.visibility = View.VISIBLE
                                textView8.visibility = View.VISIBLE
                                cuteAndroid.visibility = View.INVISIBLE
                                warning.visibility = View.INVISIBLE
                                warning_message.visibility = View.INVISIBLE
                            }else if (options == "Off"){
                                imageView3.visibility = View.INVISIBLE
                                textView9.visibility = View.INVISIBLE
                                button3.visibility = View.INVISIBLE
                                editText2.visibility = View.INVISIBLE
                                textView8.visibility = View.INVISIBLE
                                cuteAndroid.visibility = View.VISIBLE
                                warning.visibility = View.VISIBLE
                                warning_message.visibility = View.VISIBLE
                            }
                        }
                    }
                })

        var firestore = FirebaseFirestore.getInstance()
        var noterefs = firestore.collection("Devices").document(serial)
        var mmap: HashMap<String, Any?> = HashMap()
        var list = arrayListOf<String>()
        button3.setOnClickListener {
            if(editText2.text.toString().isNotBlank()) {
                list.add(editText2.text.toString())
                mmap.put("ChildMessage", list)
                noterefs.set(mmap, SetOptions.merge()).addOnCompleteListener {
                    editText2.text.clear()
                }
            }
            else{
                editText2.error = "Non-Blank Message"
            }
        }
    }

    override fun onUserLeaveHint() {
        finish()
        super.onUserLeaveHint()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    fun theEnd(){
        finish()
    }

    //    fun sendMsg(text:String){
//
//        list.add(text)
//        mmap.put("ChildMessage", list)
//        noterefs.set(mmap, SetOptions.merge()).addOnCompleteListener {
//            editText2.text.clear()
//        }
//
//    }
}
