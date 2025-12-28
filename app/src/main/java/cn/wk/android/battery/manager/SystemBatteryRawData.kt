package cn.wk.android.battery.manager

import cn.wk.android.battery.manager.charge.ChargeType

data class SystemBatteryRawData(
    val level: Int = 0,
    val batteryScale: Int = 0,
    val systemBatteryStatus: SystemBatteryStatus = SystemBatteryStatus.UNKNOWN,
    val plugged: ChargeType = ChargeType.UN_KNOW,
)
