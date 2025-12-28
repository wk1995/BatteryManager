package cn.wk.android.battery.manager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Column() {
                            Text(text = "level:${systemBatteryRawData.level}")
                            Text(text = "batteryScale:${systemBatteryRawData.batteryScale}")
                            Text(text = "systemBatteryStatus:${systemBatteryRawData.systemBatteryStatus}")
                            Text(text = "plugged:${systemBatteryRawData.plugged}")
                        }

                    }
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
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BatteryManagerTheme {
        Greeting("Android")
    }
}