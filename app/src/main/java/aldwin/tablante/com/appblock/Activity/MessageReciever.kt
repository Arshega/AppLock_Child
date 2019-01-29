package aldwin.tablante.com.appblock.Activity

import aldwin.tablante.com.appblock.R
import aldwin.tablante.com.appblock.Service.TrackerService
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_message.*

class MessageReciever: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        var msgs = intent.getStringExtra("msg")
        var id = intent.getStringExtra("id")



        msg.setText(msgs)
        var db = FirebaseFirestore.getInstance()
        db.collection("Devices").document(id).update("Messages", "")

        close.setOnClickListener {
        finish()
        }
    }
}