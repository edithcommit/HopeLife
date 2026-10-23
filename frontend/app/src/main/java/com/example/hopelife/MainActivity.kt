
package com.example.hopelife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.hopelife.navigation.SetupNavigation
import com.example.hopelife.ui.theme.HopeLifeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HopeLifeTheme {
                SetupNavigation()
            }
        }
    }
}
