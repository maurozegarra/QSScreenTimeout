package com.maurozegarra.app.screentimeout

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.provider.Settings.System.SCREEN_OFF_TIMEOUT
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.N)
class ScreenTile : TileService() {
    private val thirtyMinutes = TimeUnit.MINUTES.toMillis(30)

    override fun onTileAdded() {
        Log.e(TAG, "Called: onTileAdded")
        super.onTileAdded()

        val hasPermission = Settings.System.canWrite(this)

        if (hasPermission) {
            val currentTimeout = Settings.System.getLong(contentResolver, SCREEN_OFF_TIMEOUT)

            if (currentTimeout == thirtyMinutes) {
                qsTile.state = Tile.STATE_ACTIVE
                qsTile.label = getString(R.string.label)
                qsTile.subtitle = getString(R.string.screen_timeout_30_minutes)
            } else {
                qsTile.state = Tile.STATE_INACTIVE
                qsTile.label = getString(R.string.label)
                // show current screen timeout
                qsTile.subtitle = formatTime(currentTimeout)
            }
            // Permission Required
        } else {
            qsTile.state = Tile.STATE_INACTIVE
            qsTile.label = "Permission Required"
        }

        qsTile.updateTile()
    }

    override fun onStartListening() {
        Log.e(TAG, "Called: onStartListening")
        super.onStartListening()

        val hasPermission = Settings.System.canWrite(this)

        if (hasPermission) {
            val currentTimeout = Settings.System.getLong(contentResolver, SCREEN_OFF_TIMEOUT)

            if (currentTimeout == thirtyMinutes) {
                qsTile.state = Tile.STATE_ACTIVE
                qsTile.label = getString(R.string.label)
                qsTile.subtitle = getString(R.string.screen_timeout_30_minutes)
            } else {
                qsTile.state = Tile.STATE_INACTIVE
                qsTile.label = getString(R.string.label)
                // show current screen timeout
                qsTile.subtitle = formatTime(currentTimeout)
            }
            // Permission Required
        } else {
            qsTile.state = Tile.STATE_INACTIVE
            qsTile.label = "Permission Required"
        }

        qsTile.updateTile()
    }

    private fun formatTime(timeout: Long): String {
        val timeInSeconds = TimeUnit.MILLISECONDS.toSeconds(timeout)
        val timeInMinutes = TimeUnit.MILLISECONDS.toMinutes(timeout)

        return when (timeInSeconds) {
            in 1..59 -> "$timeInSeconds seconds"
            60L -> "$timeInMinutes minute"
            else -> "$timeInMinutes minutes"
        }
    }

    override fun onClick() {
        Log.e(TAG, "Called: onClick")
        super.onClick()

        val hasPermission = Settings.System.canWrite(this)

        if (hasPermission) {
            val currentTimeout = Settings.System.getLong(contentResolver, SCREEN_OFF_TIMEOUT)

            // toggle display timeout
            if (currentTimeout != thirtyMinutes) {
                Settings.System.putLong(
                    contentResolver,
                    SCREEN_OFF_TIMEOUT,
                    TimeUnit.MINUTES.toMillis(30)
                )

                qsTile.state = Tile.STATE_ACTIVE
                qsTile.label = getString(R.string.label)
                qsTile.subtitle = getString(R.string.screen_timeout_30_minutes)

                toast(getString(R.string.screen_timeout_30_minutes))
            } else {
                Settings.System.putLong(
                    contentResolver,
                    SCREEN_OFF_TIMEOUT,
                    TimeUnit.SECONDS.toMillis(30)
                )

                qsTile.state = Tile.STATE_INACTIVE
                qsTile.label = getString(R.string.label)
                qsTile.subtitle = getString(R.string.screen_timeout_30_seconds)

                toast(getString(R.string.screen_timeout_30_seconds))
            }

            qsTile.updateTile()
            // Ask for permission
        } else {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:" + this.packageName)
            // FLAG needed because we are calling an Activity out of an Activity
            intent.flags = FLAG_ACTIVITY_NEW_TASK
            startActivityAndCollapse(intent)
        }
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
