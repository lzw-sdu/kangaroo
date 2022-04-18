import 'package:flutter/material.dart';

class Themes {
  static const _seed = Color(0xFF8D9976);
  static const _lightColorScheme = ColorScheme(
    brightness: Brightness.light,
    primary: Color(0xFF49680E),
    onPrimary: Color(0xFFFFFFFF),
    primaryContainer: Color(0xFFC9F088),
    onPrimaryContainer: Color(0xFF121F00),
    secondary: Color(0xFF596248),
    onSecondary: Color(0xFFFFFFFF),
    secondaryContainer: Color(0xFFDDE7C6),
    onSecondaryContainer: Color(0xFF171E0A),
    tertiary: Color(0xFF386661),
    onTertiary: Color(0xFFFFFFFF),
    tertiaryContainer: Color(0xFFBBECE5),
    onTertiaryContainer: Color(0xFF00201D),
    error: Color(0xFFBA1B1B),
    errorContainer: Color(0xFFFFDAD4),
    onError: Color(0xFFFFFFFF),
    onErrorContainer: Color(0xFF410001),
    background: Color(0xFFFDFCF4),
    onBackground: Color(0xFF1B1C18),
    surface: Color(0xFFFDFCF4),
    onSurface: Color(0xFF1B1C18),
    surfaceVariant: Color(0xFFE2E4D5),
    onSurfaceVariant: Color(0xFF45483D),
    outline: Color(0xFF75786C),
    onInverseSurface: Color(0xFFF2F1E9),
    inverseSurface: Color(0xFF30312C),
    inversePrimary: Color(0xFFADD36F),
    shadow: Color(0xFF000000),
  );

  static const _darkColorScheme = ColorScheme(
    brightness: Brightness.dark,
    primary: Color(0xFFADD36F),
    onPrimary: Color(0xFF213600),
    primaryContainer: Color(0xFF324F00),
    onPrimaryContainer: Color(0xFFC9F088),
    secondary: Color(0xFFC1CBAB),
    onSecondary: Color(0xFF2B331D),
    secondaryContainer: Color(0xFF414A32),
    onSecondaryContainer: Color(0xFFDDE7C6),
    tertiary: Color(0xFFA0D0C9),
    onTertiary: Color(0xFF003733),
    tertiaryContainer: Color(0xFF204E4A),
    onTertiaryContainer: Color(0xFFBBECE5),
    error: Color(0xFFFFB4A9),
    errorContainer: Color(0xFF930006),
    onError: Color(0xFF680003),
    onErrorContainer: Color(0xFFFFDAD4),
    background: Color(0xFF1B1C18),
    onBackground: Color(0xFFE4E3DB),
    surface: Color(0xFF1B1C18),
    onSurface: Color(0xFFE4E3DB),
    surfaceVariant: Color(0xFF45483D),
    onSurfaceVariant: Color(0xFFC5C8B9),
    outline: Color(0xFF8F9284),
    onInverseSurface: Color(0xFF1B1C18),
    inverseSurface: Color(0xFFE4E3DB),
    inversePrimary: Color(0xFF49680E),
    shadow: Color(0xFF000000),
  );

  static ThemeData get lightTheme => ThemeData(colorScheme: _lightColorScheme, useMaterial3: true);

  static ThemeData get darkTheme => ThemeData(colorScheme: _darkColorScheme, useMaterial3: true);
}
