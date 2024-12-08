package com.yandex.yaweather.Theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
  primary = StatusBarLight,
  onPrimary = SettingsTextBackLight,
  primaryContainer = BottomSheetBackLight,
  onPrimaryContainer = SettingsTextSelectedLight,
  inversePrimary = SettingsTextLight,
  secondary = SettingsAnotherBackLight,
  onSecondary = SettingsSwitchBackLight,
  secondaryContainer = SettingsSwitchTrackLight,
  onSecondaryContainer = SupportLightOverlay,
  tertiary = BackLightElevated,
  onTertiary = ColorLightGray,
  tertiaryContainer = InfoContactsLight,
//  onTertiaryContainer =,
//  background =,
//  onBackground =,
//  surface =,
//  onSurface =
//  surfaceVariant =,
//  onSurfaceVariant =,
//  surfaceTint =,
//  inverseSurface =,
//  inverseOnSurface =,
//  error =,
//  onError =,
//  errorContainer =,
//  onErrorContainer =,
//  outline =,
//  outlineVariant =,
//  scrim =,
//  surfaceBright =,
//  surfaceContainer =,
//  surfaceContainerHigh =,
//  surfaceContainerHighest =,
//  surfaceContainerLow =,
//  surfaceContainerLowest =,
//  surfaceDim =,
)

private val DarkColorScheme = darkColorScheme(
  primary = StatusBarDark,
  onPrimary = SettingsTextBackDark,
  primaryContainer = BottomSheetBackDark,
  onPrimaryContainer = SettingsTextSelectedDark,
  inversePrimary = SettingsTextDark,
  secondary = SettingsAnotherBackDark,
  onSecondary = SettingsSwitchBackDark,
  secondaryContainer = SettingsSwitchTrackDark,
  onSecondaryContainer = SupportDarkOverlay,
  tertiary = BackDarkElevated,
  onTertiary = ColorDarkGrey,
  tertiaryContainer = InfoContactsDark,
//  onTertiaryContainer =,
//  background =,
//  onBackground =,
//  surface =,
//  onSurface =
//  surfaceVariant =,
//  onSurfaceVariant =,
//  surfaceTint =,
//  inverseSurface =,
//  inverseOnSurface =,
//  error =,
//  onError =,
//  errorContainer =,
//  onErrorContainer =,
//  outline =,
//  outlineVariant =,
//  scrim =,
//  surfaceBright =,
//  surfaceContainer =,
//  surfaceContainerHigh =,
//  surfaceContainerHighest =,
//  surfaceContainerLow =,
//  surfaceContainerLowest =,
//  surfaceDim =,
)

@Composable
fun YaWeatherTheme(
  darkTheme: Boolean = isSystemInDarkTheme(), dynamicColor: Boolean = false, content: @Composable () -> Unit
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }

    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme, typography = Typography, content = content
  )
}