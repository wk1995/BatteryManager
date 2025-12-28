package cn.wk.android.battery.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.core.content.ContextCompat
import cn.wk.android.battery.manager.charge.ChargeType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SystemBatteryInfoMonitor {
    private val filter = IntentFilter().apply {
        addAction(Intent.ACTION_BATTERY_CHANGED)
        addAction(Intent.ACTION_POWER_CONNECTED)
        addAction(Intent.ACTION_POWER_DISCONNECTED)
        addAction(Intent.ACTION_BATTERY_LOW)
        addAction(Intent.ACTION_BATTERY_OKAY)
    }
    private var batteryReceiver: BroadcastReceiver? = null

    private val _systemBatteryRawData = MutableSharedFlow<SystemBatteryRawData>()
    val systemBatteryRawData = _systemBatteryRawData.asSharedFlow()

    private fun initReceiver(initSuccess: () -> Unit) {
        if (batteryReceiver != null) {
            return
        }
        batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    Intent.ACTION_BATTERY_CHANGED -> {
                        //当前电量刻度（不是百分比）
                        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                        //电量刻度的最大值
                        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                        val batteryPct = level * 100 / scale.toFloat()
                        //当前电池状态（不是充电类型） status 描述的是 电池当前是否在接收能量.插不插电由另一个字段决定BatteryManager.EXTRA_PLUGGED
                        val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                        val systemBatteryStatue = when (status) {
                            BatteryManager.BATTERY_STATUS_CHARGING -> {
                                SystemBatteryStatus.CHARGING
                            }

                            BatteryManager.BATTERY_STATUS_FULL -> {
                                SystemBatteryStatus.FULL
                            }

                            BatteryManager.BATTERY_STATUS_DISCHARGING -> {
                                SystemBatteryStatus.DISCHARGING
                            }

                            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> {
                                SystemBatteryStatus.NOT_CHARGING
                            }

                            else -> {
                                SystemBatteryStatus.UNKNOWN
                            }
                        }

                        val plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
                        val chargeType = when (plugged) {
                            //数据口供电
                            BatteryManager.BATTERY_PLUGGED_USB -> ChargeType.UN_KNOW
                            //充电器供电
                            BatteryManager.BATTERY_PLUGGED_AC -> ChargeType.UN_KNOW
                            BatteryManager.BATTERY_PLUGGED_WIRELESS -> ChargeType.UN_KNOW
                            //.设备通过“底座（Dock）”方式供电或充电。
                            BatteryManager.BATTERY_PLUGGED_DOCK -> ChargeType.UN_KNOW
                            else -> ChargeType.UN_KNOW
                        }
                        _systemBatteryRawData.tryEmit(
                            SystemBatteryRawData(
                                level = level,
                                batteryScale = scale,
                                systemBatteryStatus = systemBatteryStatue,
                                plugged = chargeType
                            )
                        )
                    }

                    Intent.ACTION_POWER_CONNECTED -> {
                        // 电源连接

                    }

                    Intent.ACTION_POWER_DISCONNECTED -> {
                        // 电源断开

                    }

                    Intent.ACTION_BATTERY_LOW -> {
                        // 电量低

                    }

                    Intent.ACTION_BATTERY_OKAY -> {
                        // 电量恢复

                    }
                }
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