package cn.wk.android.battery.manager.charge

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

object ChargeManager {
    private var batteryManager: BatteryManager? = null


    private fun initBatteryManager(context: Context) {
        if (batteryManager == null) {
            batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as? BatteryManager
        }
    }

    /**
     * 获取当前充电电流
     * */
    fun getCurrentChargeCurrent(context: Context): Int {
        initBatteryManager(context = context)
        val current =
            batteryManager?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW) ?: 0
        return current
    }


    fun getCurrentChargeVoltage(context: Context): Int {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val intent = context.registerReceiver(null, intentFilter)
        return intent?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) ?: 0
    }

}