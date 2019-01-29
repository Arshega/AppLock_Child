package aldwin.tablante.com.appblock.Activity

import aldwin.tablante.com.appblock.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class RefreshLocation : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        Thread.sleep(2000)

        var intent = Intent(applicationContext,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}