package com.saksham.sharma.dragonballz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.saksham.sharma.dragonballz.ui.theme.DarkNavyBlue
import com.saksham.sharma.dragonballz.ui.theme.DragonBallZTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DragonBallZTheme {
                SetStatusBarColor()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun SetStatusBarColor() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = false // white icons
    val darkNavyBlue = DarkNavyBlue // Example hex for DarkNavyBlue

    SideEffect {
        systemUiController.setStatusBarColor(
            color = darkNavyBlue,
            darkIcons = useDarkIcons
        )
    }
}


