package com.maurozegarra.app.screentimeout

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    val WRITE_SETTING_PERMISSION = 1

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val hasPermission = Settings.System.canWrite(this)

        if (!hasPermission) {
            findViewById<TextView>(R.id.tv_grant_permission).visibility = View.VISIBLE
            findViewById<Button>(R.id.button_grant_permission).visibility = View.VISIBLE

            findViewById<Button>(R.id.button_grant_permission).setOnClickListener {
                requestWriteSettingPermission(it)
            }
        } else {
            /* Hide views related to grant permission */
            findViewById<TextView>(R.id.tv_grant_permission).visibility = View.GONE
            findViewById<Button>(R.id.button_grant_permission).visibility = View.GONE

            /* Show views related to GRANTED permission */
            findViewById<TextView>(R.id.tv_all_ready).visibility = View.VISIBLE
            findViewById<View>(R.id.line_spacing).visibility = View.VISIBLE


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            else {
                findViewById<TextView>(R.id.tv_hide_launcher).visibility = View.VISIBLE
                findViewById<Button>(R.id.button_hide_launcher).visibility = View.VISIBLE
                findViewById<Button>(R.id.button_hide_launcher).setOnClickListener {
                    hideLauncher(it)
                }
            }
        }
    }

    /* doesn't work on Android 10 (API 29) */
    private fun hideLauncher(view: View) {
        val myPackageManager = packageManager
        val componentName = ComponentName(
            this,
            // activity which is first time open in manifest file which is declare
            // as <category android:name="android.intent.category.LAUNCHER" />
            MainActivity::class.java
        )

        myPackageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestWriteSettingPermission(view: View) {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.data = Uri.parse("package:" + this.packageName)
        startActivityForResult(intent, WRITE_SETTING_PERMISSION)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == WRITE_SETTING_PERMISSION && Settings.System.canWrite(this)) {
            toast("Permission Success")

            /* Hide views related to grant permission */
            findViewById<TextView>(R.id.tv_grant_permission).visibility = View.GONE
            findViewById<Button>(R.id.button_grant_permission).visibility = View.GONE

            /* Show views related to GRANTED permission */
            findViewById<TextView>(R.id.tv_all_ready).visibility = View.VISIBLE
            findViewById<View>(R.id.line_spacing).visibility = View.VISIBLE


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            else {
                findViewById<TextView>(R.id.tv_hide_launcher).visibility = View.VISIBLE
                findViewById<Button>(R.id.button_hide_launcher).visibility = View.VISIBLE
                findViewById<Button>(R.id.button_hide_launcher).setOnClickListener {
                    hideLauncher(it)
                }
            }
        }
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
