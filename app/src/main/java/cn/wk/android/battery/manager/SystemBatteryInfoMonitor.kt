package cn.wk.android.battery.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.core.content.ContextCompat

object SystemBatteryInfoMonitor {
    private val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    private var batteryReceiver: BroadcastReceiver? = null




    private fun initReceiver(initSuccess: () -> Unit) {
        if (batteryReceiver != null) {
            return
        }
        batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                val batteryPct = level * 100 / scale.toFloat()

                val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                val isCharging =
                    status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

                val plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
                val chargeType = when (plugged) {
                    BatteryManager.BATTERY_PLUGGED_USB -> "USB"
                    BatteryManager.BATTERY_PLUGGED_AC -> "AC"
                    BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Wireless"
                    else -> "Unplugged"
                }

                // 这里你就可以更新 UI 或发通知了
            }
        }
        initSuccess()
    }

    fun startMonitor(context: Context) {
        initReceiver {
            ContextCompat.registerReceiver(
                context, batteryReceiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
    }

    fun stopMonitor(context: Context) {
        batteryReceiver?.let {
            context.unregisterReceiver(it)
        }

    }
}