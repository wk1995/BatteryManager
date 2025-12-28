package cn.wk.android.battery.manager

import cn.wk.android.battery.manager.charge.ChargeType

data class SystemBatteryRawData(
    val level: Int,
    val batteryScale: Int,
    val systemBatteryStatus: SystemBatteryStatus,
    val plugged: ChargeType,
)
