package ch.hslu.cmpproject

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import composemultiplatformproject.composeapp.generated.resources.Res
import composemultiplatformproject.composeapp.generated.resources.pinyon
import org.jetbrains.compose.resources.Font


// Eigene Schriftart
@Composable
fun customFontFamily() = FontFamily(
    Font(Res.font.pinyon, weight = FontWeight.Normal)
)

@Composable
fun customTypography() = Typography().run {
    val fontFamily = customFontFamily()
    copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        // Weitere
        titleLarge = titleLarge.copy(fontFamily = fontFamily, fontSize = 32.sp),
        bodyLarge = bodyLarge.copy(fontSize = 32.sp),
        labelLarge = labelLarge.copy(fontFamily = fontFamily)
    )
}

// Eigene Farbdefinitionen
private val LightColors = lightColorScheme(
    primary = Color(0xFF0066CC),
    secondary = Color(0xFF00BFA5),
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF66B2FF),
    secondary = Color(0xFF00E5C3),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.Black,
    onSecondary = Color.White
)

// Wrapper für das Theme
@Composable
fun CustomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = customTypography(),
        shapes = Shapes(),
        content = content
    )
}
