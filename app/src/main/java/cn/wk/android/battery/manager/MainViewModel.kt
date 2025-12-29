package cn.wk.android.battery.manager

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.wk.android.battery.manager.charge.ChargeManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {


    val systemBatteryRawData = SystemBatteryInfoMonitor.systemBatteryRawData

    private val flag: Boolean = true

    private val _current = MutableSharedFlow<Int>()
    val current = _current.asSharedFlow()
    private val _voltage = MutableSharedFlow<Float>()
    val voltage = _voltage.asSharedFlow()


    fun statObtainCurrentRecycle(context: Context) {
        viewModelScope.launch {
            if (flag) {
                val current = ChargeManager.getCurrentChargeCurrent(context)
                _current.emit(current)
                val voltage = ChargeManager.getCurrentChargeVoltage(context).let {
                    if (it > 1000) {
                        it.toFloat() / 1000
                    } else {
                        it.toFloat()
                    }
                }
                _voltage.emit(voltage)
                delay(2000)
                if (isActive) {
                    statObtainCurrentRecycle(context = context)
                }
            }
        }
    }


}