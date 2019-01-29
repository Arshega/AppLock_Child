package aldwin.tablante.com.appblock.BlockingApps

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast

class MyAdmin: DeviceAdminReceiver() {
    internal fun showToast(context: Context, msg:CharSequence) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
    override fun onEnabled(context:Context, intent: Intent) {
        showToast(context, "Sample Device Admin: enabled")
    }
    override fun onDisableRequested(context:Context, intent:Intent):CharSequence {
        return "This is an optional message to warn the user about disabling."
    }
    override fun onDisabled(context:Context, intent:Intent) {
        showToast(context, "Sample Device Admin: disabled")
    }
    override fun onPasswordChanged(context:Context, intent:Intent) {
        showToast(context, "Sample Device Admin: pw changed")
    }
    override fun onPasswordFailed(context:Context, intent:Intent) {
        showToast(context, "Sample Device Admin: pw failed")
    }
    override fun onPasswordSucceeded(context:Context, intent:Intent) {
        showToast(context, "Sample Device Admin: pw succeeded")
    }
    companion object {
        internal fun getSamplePreferences(context:Context): SharedPreferences {
            return context.getSharedPreferences(
                    DeviceAdminReceiver::class.java!!.getName(), 0)
        }
        internal var PREF_PASSWORD_QUALITY = "password_quality"
        internal var PREF_PASSWORD_LENGTH = "password_length"
        internal var PREF_MAX_FAILED_PW = "max_failed_pw"
    }
}