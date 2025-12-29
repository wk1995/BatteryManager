package cn.wk.android.battery.manager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cn.wk.android.battery.manager.ui.theme.BatteryManagerTheme

class MainActivity : ComponentActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBatteryInfoMonitor.startMonitor(this)
        enableEdgeToEdge()
        setContent {
            BatteryManagerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val systemBatteryRawData by viewModel.systemBatteryRawData.collectAsStateWithLifecycle(
                        SystemBatteryRawData()
                    )

                    val current by viewModel.current.collectAsStateWithLifecycle(0)
                    val voltage by viewModel.voltage.collectAsStateWithLifecycle(0f)

                    LaunchedEffect(Unit) {
                        viewModel.statObtainCurrentRecycle(this@MainActivity)
                    }

                    BatteryInfo(
                        Modifier,
                        systemBatteryRawData = systemBatteryRawData,
                        current = current,
                        voltage = voltage,
                        paddingValues = innerPadding
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SystemBatteryInfoMonitor.stopMonitor(this)
    }
}


@Composable
fun BatteryInfo(
    modifier: Modifier = Modifier,
    current: Int = 0,
    voltage: Float = 0f,
    systemBatteryRawData: SystemBatteryRawData = SystemBatteryRawData(),
    paddingValues: PaddingValues = PaddingValues()
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "level:${systemBatteryRawData.level}")
            Text(text = "batteryScale:${systemBatteryRawData.batteryScale}")
            Text(text = "systemBatteryStatus:${systemBatteryRawData.systemBatteryStatus}")
            Text(text = "voltage:${systemBatteryRawData.voltage}")
            Text(text = "temperature:${systemBatteryRawData.temperature}")
            Text(text = "batteryHealthType:${systemBatteryRawData.batteryHealthType}")
            Text(text = "technology:${systemBatteryRawData.technology}")
            Text(text = "plugged:${systemBatteryRawData.plugged}")
            Column(
                modifier = Modifier
                    .weight(1.0f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "current : $current")
                Text(text = "voltage : ${voltage}V")
                Text(text = "power : ${current * voltage}W")
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BatteryManagerTheme {
        BatteryInfo()
    }
}