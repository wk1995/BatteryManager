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
    private val _voltage = MutableSharedFlow<Int>()
    val voltage = _voltage.asSharedFlow()


    fun statObtainCurrentRecycle(context: Context) {
        viewModelScope.launch {
            if (flag) {
                _current.emit(ChargeManager.getCurrentChargeCurrent(context))
                _voltage.emit(ChargeManager.getCurrentChargeVoltage(context))
                delay(2000)
                if (isActive) {
                    statObtainCurrentRecycle(context = context)
                }
            }
        }
    }


}