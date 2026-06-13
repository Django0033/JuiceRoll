package com.juiceroll.ui.screens.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juiceroll.R
import com.juiceroll.ui.theme.Background
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Parchment
import kotlinx.coroutines.delay

/**
 * Splash screen shown on app launch for 1.5 seconds.
 * Displays the JuiceRoll branding with a fade-in animation
 * then invokes [onSplashComplete] to navigate to the home screen.
 */
@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "splashAlpha",
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(1500L)
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.alpha(alphaAnim),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // App icon
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "JuiceRoll Icon",
                modifier = Modifier.size(96.dp),
                tint = Parchment,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // App name
            Text(
                text = "JuiceRoll",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                color = Parchment,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = "Solo RPG Companion",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Gold,
            )
        }
    }
}
