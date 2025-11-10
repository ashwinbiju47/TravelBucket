package com.example.travelbucket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.travelbucket.ui.AppRoot
import com.example.travelbucket.ui.theme.TravelBucketTheme   // ✅ import your custom theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelBucketTheme(    // ✅ apply your green eco theme here
                darkTheme = false,   // or true / isSystemInDarkTheme()
                dynamicColor = false // disable wallpaper colors
            ) {
                AppRoot()
            }
        }
    }
}
