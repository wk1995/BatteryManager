package cn.wk.android.battery.manager

import cn.wk.android.battery.manager.charge.ChargeType

data class SystemBatteryRawData(
    val level: Int = 0,
    val batteryScale: Int = 0,
    val temperature: Int = 0,
    val voltage: Int = 0,
    val technology: String = "",
    val systemBatteryStatus: SystemBatteryStatus = SystemBatteryStatus.UNKNOWN,
    val batteryHealthType: BatteryHealthType = BatteryHealthType.UNKNOWN,
    val plugged: ChargeType = ChargeType.UN_KNOW,
)
