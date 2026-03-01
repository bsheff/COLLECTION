package com.watchclock.tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.watchclock.tracker.ui.navigation.NavGraph
import com.watchclock.tracker.ui.theme.WatchClockTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WatchClockTheme {
                NavGraph()
            }
        }
    }
}
